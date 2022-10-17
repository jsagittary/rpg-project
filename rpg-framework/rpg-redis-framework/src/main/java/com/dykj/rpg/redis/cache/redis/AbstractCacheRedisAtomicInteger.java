package com.dykj.rpg.redis.cache.redis;

import com.dykj.rpg.redis.cache.base.CachePathUtil;
import com.dykj.rpg.redis.cache.core.IAtomicInteger;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;

import javax.annotation.PostConstruct;

/**
 * @author jyb
 * @date 2018/12/14 17:36
 */
public abstract class AbstractCacheRedisAtomicInteger extends AbstractCacheRedis implements IAtomicInteger {

    private RedisAtomicInteger atomicInteger;

    public int initValue() {
        return 1;
    }

    public void reset() {
        atomicInteger.set(initValue());
    }

    @PostConstruct
    public void init() {
        String i = String.valueOf(initValue());
        getRedisTemplate().boundValueOps(path()).set(String.valueOf(initValue()));
        atomicInteger = new RedisAtomicInteger(path(), getRedisTemplate().getConnectionFactory());
    }

    @Override
    public int addAndGet(int delta) {
        return atomicInteger.addAndGet(delta);
    }

    @Override
    public boolean compareAndSet(int expect, int update) {
        return atomicInteger.compareAndSet(expect, update);
    }

    @Override
    public int decrementAndGet() {
        return atomicInteger.decrementAndGet();
    }

    @Override
    public int get() {
        return atomicInteger.get();
    }

    @Override
    public int getAndAdd(int delta) {
        return atomicInteger.getAndAdd(delta);
    }

    @Override
    public int getAndDecrement() {
        return atomicInteger.getAndDecrement();
    }

    @Override
    public int getAndIncrement() {
        return atomicInteger.getAndIncrement();
    }

    @Override
    public int getAndSet(int newValue) {
        return atomicInteger.getAndSet(newValue);
    }

    @Override
    public int incrementAndGet() {
        return atomicInteger.incrementAndGet();
    }

    @Override
    public String path() {
        return CachePathUtil.cachePath2String(super.path(), "atomicInteger");
    }
}
