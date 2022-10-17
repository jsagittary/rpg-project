package com.dykj.rpg.redis.cache.redis;

import com.dykj.rpg.redis.cache.service.RedisLockService;
import com.dykj.rpg.redis.cache.base.CachePathUtil;
import com.dykj.rpg.redis.cache.base.ICacheLock;
import com.dykj.rpg.redis.cache.base.ICachePath;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * User: struct
 * Date: 2018-11-26 23:37
 */
public abstract class AbstractCacheRedisLock implements ICacheLock, ICachePath {

    /**
     * 重试等待时间 单位毫秒
     */
    private int RETRY_AWAIT = 300;

    @Autowired
    private RedisLockService redisLockService;

    private String lockPath(String childKey) {
        if (null == childKey || "".equals(childKey)) {
            return path();
        }
        return CachePathUtil.cachePath2String(path(), String.valueOf(childKey));
    }

    @Override
    public void lock(int childKey) {
        lock(String.valueOf(childKey));
    }

    @Override
    public void lock(String childKey) {
        lock(childKey, RETRY_AWAIT, TimeUnit.MILLISECONDS);
    }

    @Override
    public void lock(int childKey, long time, TimeUnit unit) {
        lock(String.valueOf(childKey), time, unit);
    }

    @Override
    public void lock(String childKey, long time, TimeUnit unit) {
        redisLockService.lock(lockPath(childKey), time, unit);
    }

    @Override
    public void unlock(int childKey) {
        unlock(String.valueOf(childKey));
    }

    @Override
    public void unlock(String childKey) {
        redisLockService.unlock(CachePathUtil.cachePath2String(path(), String.valueOf(childKey)));
    }

    @Override
    public boolean tryLock(int key) {
        return tryLock(String.valueOf(key));
    }

    @Override
    public boolean tryLock(String key) {
        return tryLock(key, RETRY_AWAIT, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean tryLock(String key, int time) {
        return tryLock(key, time, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean tryLock(int key, int time, TimeUnit unit) {
        return tryLock(String.valueOf(key), time, unit);
    }

    @Override
    public boolean tryLock(String key, int time, TimeUnit unit) {
        return redisLockService.tryLock(lockPath(key), (int) unit.toMillis(time));
    }
}
