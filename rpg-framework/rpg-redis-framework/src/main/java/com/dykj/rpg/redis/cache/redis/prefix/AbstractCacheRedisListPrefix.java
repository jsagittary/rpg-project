package com.dykj.rpg.redis.cache.redis.prefix;

import com.dykj.rpg.redis.cache.base.CachePathUtil;
import com.dykj.rpg.redis.cache.core.prefix.ICacheListPrefix;
import com.dykj.rpg.redis.cache.redis.AbstractCacheRedis;
import com.dykj.rpg.redis.cache.redis.proxy.CacheRedisListProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author jyb
 * @date 2018/11/29 16:47
 */
public abstract class AbstractCacheRedisListPrefix<E> extends AbstractCacheRedis<E> implements ICacheListPrefix<E> {

    private CacheRedisListProxy<E> proxy = new CacheRedisListProxy<>(clazz, this);

    @Override
    public E get(Object prefixKey, int index) {
        return proxy.get(boundListOps(prefixKey), index);
    }

    @Override
    public void set(Object prefixKey, int index, E element) {
        proxy.set(boundListOps(prefixKey), index, element);
    }

    @Override
    public E removeByIndex(Object prefixKey, int index) {
        E e = get(prefixKey, index);
        if (null != e) {
            remove(prefixKey, e);
        }
        return e;
    }

    @Override
    public boolean remove(Object prefixKey, E e) {
        return proxy.remove(boundListOps(prefixKey), e);
    }

    @Override
    public List<E> subList(Object prefixKey, int fromIndex, int toIndex) {
        return proxy.subList(boundListOps(prefixKey), fromIndex, toIndex);
    }

    @Override
    public List<E> all(Object prefixKey) {
        return subList(prefixKey, 0, -1);
    }

    @Override
    public int size(Object prefixKey) {
        return proxy.size(boundListOps(prefixKey));
    }

    @Override
    public boolean isEmpty(Object prefixKey) {
        return 0 >= size(prefixKey);
    }

    @Override
    public void add(Object prefixKey, E e) {
        proxy.add(boundListOps(prefixKey), e);
    }

    @Override
    public E first(Object prefixKey) {
        return proxy.first(boundListOps(prefixKey));
    }

    @Override
    public E getAndRemoveFirst(Object prefixKey) {
        return proxy.getAndRemoveFirst(boundListOps(prefixKey));
    }

    @Autowired
    private RedisScript<Boolean> listPop;

    @Override
    public void removeFirstFew(Object prefixKey, int num) {
        getRedisTemplate().execute(listPop, Arrays.asList(CachePathUtil.cachePath2String(path(), prefixKey)), String.valueOf(num));
    }

    @Override
    public E last(Object prefixKey) {
        return proxy.last(boundListOps(prefixKey));
    }

    @Override
    public E getAndRemoveLast(Object prefixKey) {
        return proxy.getAndRemoveLast(boundListOps(prefixKey));
    }

    @Override
    public void clear(Object prefixKey) {
        proxy.clear(getRedisTemplate(), CachePathUtil.cachePath2String(path(), prefixKey));
    }

    @Override
    public Iterator<E> iterator(Object prefixKey) {
        throw new UnsupportedOperationException("Redis List can't get iterator");
    }

    private final BoundListOperations boundListOps(Object prefix) {
        return getRedisTemplate().boundListOps(CachePathUtil.cachePath2String(path(), prefix));
    }

    @Override
    public String path() {
        return CachePathUtil.cachePath2String(super.path(), "listPrefix");
    }
}
