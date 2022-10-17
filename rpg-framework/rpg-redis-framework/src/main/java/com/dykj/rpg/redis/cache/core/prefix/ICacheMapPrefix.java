package com.dykj.rpg.redis.cache.core.prefix;

import com.dykj.rpg.redis.cache.redis.data.HScanResult;
import com.dykj.rpg.redis.cache.base.ICacheMapPrimaryKey;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author jyb
 * @date 2018/11/28 19:49
 */
public interface ICacheMapPrefix<V extends ICacheMapPrimaryKey> {

    long size(Object prefixKey);

    boolean isEmpty(Object prefixKey);

    boolean containsKey(Object prefixKey, Object key);

    V get(Object prefixKey, Object key);

    List<V> multiGet(Object prefixKey, Collection<?> keys);

    void put(Object prefixKey, V value);

    long remove(Object prefixKey, Object key);

    void clear(Object prefixKey);

    Set<String> keySet(Object prefixKey);

    List<V> values(Object prefixKey);

    HScanResult<V> scan(Object prefixKey, int index, int count);
}
