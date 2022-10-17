package com.dykj.rpg.redis.cache.core;

import java.util.Iterator;

/**
 * User: jyb
 * Date: 2018-11-26 22:05
 */
public interface ICacheCollection<E> {

    int size();

    boolean isEmpty();

    void add(E e);

    /** 使用这个必须确保内存数据和缓存的一致 */
    boolean remove(E e);

    void clear();

    Iterator<E> iterator();
}
