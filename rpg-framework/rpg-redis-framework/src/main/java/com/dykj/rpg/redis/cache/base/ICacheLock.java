package com.dykj.rpg.redis.cache.base;

import java.util.concurrent.TimeUnit;

/**
 * User: jyb
 * Date: 2018-11-26 21:52
 */
public interface ICacheLock {

    void lock(int childKey);

    void lock(String childKey);

    void lock(int childKey, long time, TimeUnit unit);

    void lock(String childKey, long time, TimeUnit unit);

    void unlock(int childKey);

    void unlock(String childKey);

    boolean tryLock(int key);

    boolean tryLock(String key);

    /**
     * @param key
     * @param time 单位：毫秒
     * @return
     */
    boolean tryLock(String key, int time);

    boolean tryLock(int key, int time, TimeUnit unit);

    boolean tryLock(String key, int time, TimeUnit unit);
}
