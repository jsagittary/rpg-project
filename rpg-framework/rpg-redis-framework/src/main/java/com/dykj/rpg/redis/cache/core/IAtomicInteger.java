package com.dykj.rpg.redis.cache.core;

/**
 * @author jyb
 * @date 2018/12/14 17:33
 */
public interface IAtomicInteger {
    int addAndGet(int delta);
    boolean compareAndSet(int expect, int update);
    int decrementAndGet();
    int get();
    int getAndAdd(int delta);
    int getAndDecrement();
    int getAndIncrement();
    int getAndSet(int newValue);
    int incrementAndGet();
}
