package com.dykj.rpg.redis.cache.core;

import java.util.List;

/**
 * User: jyb
 * Date: 2018-11-26 22:05
 */
public interface ICacheList<E> extends ICacheCollection<E> {

    E get(int index);

    void set(int index, E element);

    E removeByIndex(int index);

    E first();

    E getAndRemoveFirst();

    /**
     * 移除前面几个
     * @param num
     */
    void removeFirstFew(int num);

    E last();

    E getAndRemoveLast();

    List<E> subList(int fromIndex, int toIndex);

    List<E> all();
}
