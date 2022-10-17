package com.dykj.rpg.net.thread;

/**
 * @author gongjianbo
 */
public class ThreadUtil {

    private static final ThreadUtil DEFAULT_INSTANCE = new ThreadUtil();

    public static ThreadUtil getDefault() {
        return DEFAULT_INSTANCE;
    }
    private ThreadUtil(){
        threadPool = new ThreadPool();
    }
    private ThreadPool threadPool;

    public ThreadPool getThreadPool() {
        return threadPool;
    }


}
