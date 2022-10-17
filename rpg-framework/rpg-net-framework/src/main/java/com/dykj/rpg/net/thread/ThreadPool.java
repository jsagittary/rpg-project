package com.dykj.rpg.net.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

public class ThreadPool {

    /**
     * 逻辑线程数量池
     */
    private static final int MAX_LOGIC_THREAD = Runtime.getRuntime().availableProcessors() * 2;

    /**
     * 登录线程线程数量
     */
    private static final int MAX_LOGIN_THREAD = Runtime.getRuntime().availableProcessors();
    /**
     * 逻辑线程
     */
    private final ExecutorService[] LOGIC_THREAD_POOL;
    /**
     * 登录线程
     */
    private final ExecutorService[] LOGIN_THREAD_POOL;

    /**
     * 定时任务线程
     */
    private final ScheduledThreadPoolExecutor TIMEER_THREAD;

    /**
     * kafka消息发送线程
     */
    private final  ExecutorService  MSG_SEND_THREAD;


    public ThreadPool() {

        //逻辑线程池
        LOGIC_THREAD_POOL = new ExecutorService[MAX_LOGIC_THREAD];
        for (int i = 0; i < MAX_LOGIC_THREAD; i++) {
            LOGIC_THREAD_POOL[i] = Executors
                    .newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat(CmdThreadEnum.LOGIC.getCode() + "_" + i).build());
        }

        // 启动定时任务线程
        TIMEER_THREAD = new ScheduledThreadPoolExecutor(3, new ThreadFactory() {
            private int count = 0;

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, CmdThreadEnum.TIMER_TASK.getCode() + "-" + (++count));
            }
        });

        /** 登录线程 */
        //逻辑线程池
        LOGIN_THREAD_POOL = new ExecutorService[MAX_LOGIN_THREAD];
        for (int i = 0; i < MAX_LOGIN_THREAD; i++) {
            LOGIN_THREAD_POOL[i] = Executors
                    .newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat(CmdThreadEnum.LOGIN.getCode() + "_" + i).build());
        }

        MSG_SEND_THREAD = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat(CmdThreadEnum.LOGIC.getCode()).build());
    }

    /**
     * 执行延时任务(执行一次)
     *
     * @param runnable
     * @param delay
     * @param timeUnit
     */
    public ScheduledFuture<?> schedule(final Runnable runnable, long delay, TimeUnit timeUnit) {
        return TIMEER_THREAD.schedule(runnable, delay, timeUnit);
    }

    public ExecutorService getLoginThreadPool(int id) {
        return LOGIN_THREAD_POOL[id % MAX_LOGIN_THREAD];
    }


    public ExecutorService getLogicThreadPool(int id) {
        return LOGIC_THREAD_POOL[id % MAX_LOGIC_THREAD];
    }



    public static int getMaxLogicThread() {
        return MAX_LOGIC_THREAD;
    }


    public ExecutorService getMSG_SEND_THREAD() {
        return MSG_SEND_THREAD;
    }
}
