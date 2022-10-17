package com.dykj.rpg.redis.cache.core;

import com.dykj.rpg.redis.cache.redis.data.HScanResult;
import com.dykj.rpg.redis.cache.base.ICacheMapPrimaryKey;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * User: jyb
 * Date: 2018-11-26 21:57
 */
public interface ICacheMap<V extends ICacheMapPrimaryKey> {

    long size();

    boolean isEmpty();

    boolean containsKey(Object key);

    V get(Object key);

    List<V> multiGet(Collection<?> keys);

    void put(V value);

    long remove(Object key);

    void clear();

    Set<String> keySet();

    List<V> values();

    HScanResult<V> scan(int index, int count);
}
