package com.dykj.rpg.redis.cache.redis;

import com.dykj.rpg.redis.cache.redis.proxy.CacheRedisListProxy;
import com.dykj.rpg.redis.cache.base.CachePathUtil;
import com.dykj.rpg.redis.cache.core.ICacheList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * User: jyb
 * Date: 2018-11-27 0:13
 */
public abstract class AbstractCacheRedisList<E> extends AbstractCacheRedis<E> implements ICacheList<E> {

    private CacheRedisListProxy<E> proxy = new CacheRedisListProxy<>(clazz, this);

    @Override
    public E get(int index) {
        return proxy.get(boundListOps(), index);
    }

    @Override
    public void set(int index, E element) {
        proxy.set(boundListOps(), index, element);
    }

    @Override
    public E removeByIndex(int index) {
        E e = get(index);
        if (null != e) {
            remove(e);
        }
        return e;
    }

    @Override
    public boolean remove(E e) {
        return proxy.remove(boundListOps(), e);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return proxy.subList(boundListOps(), fromIndex, toIndex);
    }

    @Override
    public List<E> all() {
        return subList(0, -1);
    }

    @Override
    public int size() {
        return proxy.size(boundListOps());
    }

    @Override
    public boolean isEmpty() {
        return 0 >= size();
    }

    @Override
    public void add(E e) {
        proxy.add(boundListOps(), e);
    }

    @Override
    public E first() {
        return proxy.first(boundListOps());
    }

    @Override
    public E getAndRemoveFirst() {
        return proxy.getAndRemoveFirst(boundListOps());
    }

    @Autowired
    private RedisScript<Boolean> listPop;

    @Override
    public void removeFirstFew(int num) {
        getRedisTemplate().execute(listPop, Arrays.asList(this.path()), String.valueOf(num));
    }

    @Override
    public E last() {
        return proxy.last(boundListOps());
    }

    @Override
    public E getAndRemoveLast() {
        return proxy.getAndRemoveLast(boundListOps());
    }

    @Override
    public void clear() {
        proxy.clear(getRedisTemplate(), path());
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Redis List can't get iterator");
    }

    private final BoundListOperations<String, String> boundListOps() {
        return getRedisTemplate().boundListOps(path());
    }

    @Override
    public String path() {
        return CachePathUtil.cachePath2String(super.path(), "list");
    }


}
