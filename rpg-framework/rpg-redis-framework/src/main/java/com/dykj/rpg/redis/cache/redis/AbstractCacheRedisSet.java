package com.dykj.rpg.redis.cache.redis;

import com.dykj.rpg.redis.cache.redis.proxy.CacheRedisSetProxy;
import com.dykj.rpg.redis.cache.base.CachePathUtil;
import com.dykj.rpg.redis.cache.core.ICacheSet;
import org.springframework.data.redis.core.BoundSetOperations;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author jyb
 * @date 2018/11/27 16:38
 */
public abstract class AbstractCacheRedisSet<E> extends AbstractCacheRedis<E> implements ICacheSet<E> {

    private CacheRedisSetProxy<E> proxy = new CacheRedisSetProxy<>(clazz, this);

    @Override
    public Collection<E> all() {
        return proxy.all(setOperations());
    }

    @Override
    public E randomMember() {
        return proxy.randomMember(setOperations());
    }

    @Override
    public Collection<E> randomMembers(int num) {
        return proxy.randomMembers(setOperations(), num);
    }

    @Override
    public boolean contains(E e) {
        return proxy.contains(setOperations(), e);
    }

    @Override
    public int size() {
        return proxy.size(setOperations());
    }

    @Override
    public boolean isEmpty() {
        return 0 >= size();
    }

    @Override
    public void add(E e) {
        proxy.add(setOperations(), e);
    }

    @Override
    public void clear() {
        proxy.clear(getRedisTemplate(), path());
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Redis Set can't get iterator");
    }

    @Override
    public boolean remove(E e) {
        return proxy.remove(setOperations(), e);
    }

    private final BoundSetOperations<String, String> setOperations() {
        return getRedisTemplate().boundSetOps(path());
    }

    @Override
    public String path() {
        return CachePathUtil.cachePath2String(super.path(), "set");
    }
}
