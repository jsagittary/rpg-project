package com.dykj.rpg.redis.cache.core;

import java.util.concurrent.TimeUnit;

/**
 * @author jyb
 * @date 2019/1/2 15:22
 */
public interface ICacheValue<V> {

    void delete(Object key);

    V get(Object key);

    V getAndSet(Object key, V value);

    void set(Object key, V value);

    void set(Object key, V value, long timeout, TimeUnit unit);

    /**
     * 如果绑定键不存在，则将绑定键设置为保留字符串值
     * @param value
     * @return
     */
    boolean setIfAbsent(Object key, V value);
}
