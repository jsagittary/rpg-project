package com.dykj.rpg.redis.cache.core.prefix;

import java.util.Iterator;

/**
 * @author jyb
 * @date 2018/11/29 16:45
 */
public interface ICacheCollectionPrefix<E> {

    int size(Object prefixKey);

    boolean isEmpty(Object prefixKey);

    void add(Object prefixKey, E e);

    /** 使用这个必须确保内存数据和缓存的一致 */
    boolean remove(Object prefixKey, E e);

    void clear(Object prefixKey);

    Iterator<E> iterator(Object prefixKey);
}
