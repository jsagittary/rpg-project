package com.dykj.rpg.redis.cache.redis.prefix;

import com.dykj.rpg.redis.cache.base.CachePathUtil;
import com.dykj.rpg.redis.cache.base.ICacheMapPrimaryKey;
import com.dykj.rpg.redis.cache.core.prefix.ICacheMapPrefix;
import com.dykj.rpg.redis.cache.redis.AbstractCacheRedis;
import com.dykj.rpg.redis.cache.redis.data.HScanResult;
import com.dykj.rpg.redis.cache.redis.proxy.CacheRedisMapProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author jyb
 * @date 2018/11/28 20:02
 */
public abstract class AbstractCacheRedisMapPrefix<V extends ICacheMapPrimaryKey> extends AbstractCacheRedis<V> implements ICacheMapPrefix<V> {

    private CacheRedisMapProxy<V> proxy = new CacheRedisMapProxy<>(clazz,this);

    @Override
    public long size(Object prefixKey) {
        return proxy.size(hashOperations(prefixKey));
    }

    @Override
    public boolean isEmpty(Object prefixKey) {
        return 0 >= size(prefixKey);
    }

    @Override
    public boolean containsKey(Object prefixKey, Object key) {
        return proxy.containsKey(hashOperations(prefixKey), key);
    }

    @Override
    public V get(Object prefixKey, Object key) {
        return proxy.get(hashOperations(prefixKey), key);
    }

    @Override
    public List<V> multiGet(Object prefixKey, Collection<?> keys) {
        return proxy.multiGet(hashOperations(prefixKey), keys);
    }

    @Override
    public void put(Object prefixKey, V value) {
        proxy.put(hashOperations(prefixKey), value.primaryKey(), value);
    }

    @Override
    public long remove(Object prefixKey, Object key) {
        return proxy.remove(hashOperations(prefixKey), key);
    }

    @Override
    public void clear(Object prefixKey) {
        proxy.clear(getRedisTemplate(), CachePathUtil.cachePath2String(path(), prefixKey));
    }

    @Override
    public Set<String> keySet(Object prefixKey) {
        return proxy.keySet(hashOperations(prefixKey));
    }

    @Override
    public List<V> values(Object prefixKey) {
        return proxy.values(hashOperations(prefixKey));
    }

    @Override
    public String path() {
        return CachePathUtil.cachePath2String(super.path(), "mapPrefix");
    }

    private final BoundHashOperations hashOperations(Object prefixKey) {
        return getRedisTemplate().boundHashOps(CachePathUtil.cachePath2String(path(), prefixKey));
    }

    @Autowired
    private RedisScript<String> hashScan;

    public HScanResult<V> scaan(Object prefixKey, int index, int count) {
        return proxy.scan(getRedisTemplate(), hashScan, CachePathUtil.cachePath2String(path(), prefixKey), index, count);
    }
}
