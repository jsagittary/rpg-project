package com.dykj.rpg.common.kafka.core;

import java.util.concurrent.TimeUnit;

import javax.management.RuntimeErrorException;

/**
 *
 * @param <T>
 */
public class DefaultFuture<T> {

	private static final Throwable CANCELLED = new Throwable();
	private final boolean cancellable;
	private boolean done;
	private Throwable cause;
	private int waiters;
	private static final long DEFAULT_SECONDS = 5000l;

	private T response;

	public T getResponse() {
		return response;
	}

	public DefaultFuture(boolean cancellable) {  
        this.cancellable = cancellable;  
    }

	public synchronized boolean isDone() {
		return done;
	}

	public synchronized boolean isSuccess() {
		return done && cause == null;
	}

	public synchronized Throwable getCause() {
		if (cause != CANCELLED) {
			return cause;
		} else {
			return null;
		}
	}

	public synchronized boolean isCancelled() {
		return cause == CANCELLED;
	}
	
	public T get(long timeout, TimeUnit unit) throws Exception {  
	    if (timeout <= 0) {  
	        timeout = DEFAULT_SECONDS; 
	        unit = TimeUnit.SECONDS;
	    }  
	    if (! isDone()) {  
	    	await(timeout, unit);  
	    }  
	    return this.getResponse();
	} 

	public DefaultFuture<T> sync() throws InterruptedException {
		await();
		rethrowIfFailed0();
		return this;
	}

	public DefaultFuture<T> syncUninterruptibly() {
		awaitUninterruptibly();
		rethrowIfFailed0();
		return this;
	}

	private void rethrowIfFailed0() {
		Throwable cause = getCause();
		if (cause == null) {
			return;
		}

		if (cause instanceof RuntimeException) {
			throw (RuntimeException) cause;
		}

		if (cause instanceof Error) {
			throw (Error) cause;
		}

		throw new RuntimeErrorException((Error) cause);
	}

	public DefaultFuture<T> await() throws InterruptedException {
		if (Thread.interrupted()) {
			throw new InterruptedException();
		}

		synchronized (this) {
			while (!done) {
				waiters++;
				try {
					wait();
				} finally {
					waiters--;
				}
			}
		}
		return this;
	}

	public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
		return await0(unit.toNanos(timeout), true);
	}

	public boolean await(long timeoutMillis) throws InterruptedException {
		return await0(TimeUnit.MILLISECONDS.toNanos(timeoutMillis), true);
	}

	public DefaultFuture<T> awaitUninterruptibly() {
		boolean interrupted = false;
		synchronized (this) {
			while (!done) {
				waiters++;
				try {
					wait();
				} catch (InterruptedException e) {
					interrupted = true;
				} finally {
					waiters--;
				}
			}
		}

		if (interrupted) {
			Thread.currentThread().interrupt();
		}

		return this;
	}

	public boolean awaitUninterruptibly(long timeout, TimeUnit unit) {
		try {
			return await0(unit.toNanos(timeout), false);
		} catch (InterruptedException e) {
			throw new InternalError();
		}
	}

	public boolean awaitUninterruptibly(long timeoutMillis) {
		try {
			return await0(TimeUnit.MILLISECONDS.toNanos(timeoutMillis), false);
		} catch (InterruptedException e) {
			throw new InternalError();
		}
	}

	private boolean await0(long timeoutNanos, boolean interruptable) throws InterruptedException {
		if (interruptable && Thread.interrupted()) {
			throw new InterruptedException();
		}

		long startTime = timeoutNanos <= 0 ? 0 : System.nanoTime();
		long waitTime = timeoutNanos;
		boolean interrupted = false;

		try {
			synchronized (this) {
				if (done || waitTime <= 0) {
					return done;
				}

				waiters++;
				try {
					for (;;) {
						try {
							wait(waitTime / 1000000, (int) (waitTime % 1000000));
						} catch (InterruptedException e) {
							if (interruptable) {
								throw e;
							} else {
								interrupted = true;
							}
						}

						if (done) {
							return true;
						} else {
							waitTime = timeoutNanos - (System.nanoTime() - startTime);
							if (waitTime <= 0) {
								return done;
							}
						}
					}
				} finally {
					waiters--;
				}
			}
		} finally {
			if (interrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}
	
	public boolean onReceive(T response) {
		synchronized (this) {
			// Allow only once.
			if (done) {
				return false;
			}

			done = true;
			this.response = response;
			if (waiters > 0) {
				notifyAll();
			}
		}

		return true;
	}

	public boolean cancel() {
		if (!cancellable) {
			return false;
		}

		synchronized (this) {
			// Allow only once.
			if (done) {
				return false;
			}

			cause = CANCELLED;
			done = true;
			if (waiters > 0) {
				notifyAll();
			}
		}

		return true;
	}

}
