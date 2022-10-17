package com.dykj.rpg.redis.cache.redis;

import com.dykj.rpg.redis.cache.base.CachePathUtil;
import com.dykj.rpg.redis.cache.core.ICacheValue;
import org.springframework.data.redis.core.BoundValueOperations;

import java.util.concurrent.TimeUnit;

/**
 * @author jyb
 * @date 2019/1/2 15:28
 */
public abstract class AbstractCacheRedisValue<V> extends AbstractCacheRedis<V> implements ICacheValue<V> {

    @Override
    public void delete(Object key) {
        getRedisTemplate().delete(CachePathUtil.cachePath2String(path(), key));
    }

    @Override
    public V get(Object key) {
        String s = boundValueOps(key).get();
        if (null != s) {
            return decode(s);
        }
        return null;
    }

    @Override
    public V getAndSet(Object key, V value) {
        String s = boundValueOps(key).getAndSet(encode(value));
        if (null != s) {
            return decode(s);
        }
        return null;
    }

    @Override
    public void set(Object key, V value) {
        boundValueOps(key).set(encode(value));
    }

    @Override
    public void set(Object key, V value, long timeout, TimeUnit unit) {
        boundValueOps(key).set(encode(value), timeout, unit);
    }

    @Override
    public boolean setIfAbsent(Object key, V value) {
        return boundValueOps(key).setIfAbsent(encode(value));
    }

    @Override
    public String path() {
        return CachePathUtil.cachePath2String(super.path(), "value");
    }

    private final BoundValueOperations<String, String> boundValueOps(Object key) {
        return getRedisTemplate().boundValueOps(CachePathUtil.cachePath2String(path(), key));
    }
}
