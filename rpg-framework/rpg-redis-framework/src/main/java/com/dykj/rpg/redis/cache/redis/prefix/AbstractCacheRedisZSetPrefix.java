package com.dykj.rpg.redis.cache.redis.prefix;

import com.dykj.rpg.redis.cache.base.CachePathUtil;
import com.dykj.rpg.redis.cache.core.prefix.ICacheZSetPrefix;
import com.dykj.rpg.redis.cache.redis.AbstractCacheRedis;
import com.dykj.rpg.redis.cache.redis.proxy.CacheRedisZSetProxy;
import org.springframework.data.redis.core.BoundZSetOperations;

import java.util.Iterator;
import java.util.Set;

/**
 * @author jyb
 * @date 2018/12/15 12:04
 */
public abstract class AbstractCacheRedisZSetPrefix extends AbstractCacheRedis implements ICacheZSetPrefix {

    private CacheRedisZSetProxy proxy;

    public AbstractCacheRedisZSetPrefix(int logicFirstIndex) {
        super();
        proxy = new CacheRedisZSetProxy(clazz, this, logicFirstIndex);
    }

    @Override
    public void add(Object prefixKey, Object key, double score) {
        proxy.add(zSetOperations(prefixKey), key, score);
    }

    @Override
    public long count(Object prefixKey, double min, double max) {
        return proxy.count(zSetOperations(prefixKey), min, max);
    }

    @Override
    public double incrementScore(Object prefixKey, Object key, double delta) {
        return proxy.incrementScore(zSetOperations(prefixKey), key, delta);
    }

    @Override
    public String range(Object prefixKey, long rank) {
        return proxy.range(zSetOperations(prefixKey), rank);
    }

    @Override
    public Set<String> range(Object prefixKey, long start, long end) {
        return proxy.range(zSetOperations(prefixKey), start, end);
    }

    @Override
    public String reverseRange(Object prefixKey, long rank) {
        return proxy.reverseRange(zSetOperations(prefixKey), rank);
    }

    @Override
    public Set<String> reverseRange(Object prefixKey, long start, long end) {
        return proxy.reverseRange(zSetOperations(prefixKey), start, end);
    }

    @Override
    public Set<String> all(Object prefixKey) {
        return proxy.all(zSetOperations(prefixKey));
    }

    @Override
    public Set<String> rangeByScore(Object prefixKey, double min, double max) {
        return proxy.rangeByScore(zSetOperations(prefixKey), min, max);
    }

    @Override
    public Set<String> reverseRangeByScore(Object prefixKey, double min, double max) {
        return proxy.reverseRangeByScore(zSetOperations(prefixKey), min, max);
    }

    @Override
    public long rank(Object prefixKey, Object key) {
        return proxy.rank(zSetOperations(prefixKey), key);
    }

    @Override
    public long reverseRank(Object prefixKey, Object key) {
        return proxy.reverseRank(zSetOperations(prefixKey), key);
    }

    @Override
    public void removeRange(Object prefixKey, long start, long end) {
        proxy.removeRange(zSetOperations(prefixKey), start, end);
    }

    @Override
    public void removeRangeByScore(Object prefixKey, double min, double max) {
        proxy.removeRangeByScore(zSetOperations(prefixKey), min, max);
    }

    @Override
    public double score(Object prefixKey, Object key) {
        return proxy.score(zSetOperations(prefixKey), key);
    }

    @Override
    public int size(Object prefixKey) {
        return (int) proxy.size(zSetOperations(prefixKey));
    }

    @Override
    public boolean isEmpty(Object prefixKey) {
        return 0 >= size(prefixKey);
    }

    @Override
    public void add(Object prefixKey, Object o) {
        throw new UnsupportedOperationException("Redis ZSet can't add");
    }

    @Override
    public boolean remove(Object prefixKey, Object o) {
        return 0 < proxy.remove(zSetOperations(prefixKey), o);
    }

    @Override
    public void clear(Object prefixKey) {
        proxy.clear(getRedisTemplate(), CachePathUtil.cachePath2String(path(), prefixKey));
    }

    @Override
    public Iterator iterator(Object prefixKey) {
        throw new UnsupportedOperationException("Redis List can't get iterator");
    }

    private final BoundZSetOperations<String, String> zSetOperations(Object prefixKey) {
        return getRedisTemplate().boundZSetOps(CachePathUtil.cachePath2String(path(), prefixKey));
    }

    @Override
    public String path() {
        return CachePathUtil.cachePath2String(super.path(), "zSetPrefix");
    }
}
