package com.dykj.rpg.redis.cache.core.prefix;

import java.util.List;

/**
 * @author jyb
 * @date 2018/11/29 16:44
 */
public interface ICacheListPrefix<E> extends ICacheCollectionPrefix<E> {

    E get(Object prefixKey, int index);

    void set(Object prefixKey, int index, E element);

    E removeByIndex(Object prefixKey, int index);

    E first(Object prefixKey);

    E getAndRemoveFirst(Object prefixKey);

    /**
     * 移除前面几个
     * @param prefixKey
     * @param num
     */
    void removeFirstFew(Object prefixKey, int num);

    E last(Object prefixKey);

    E getAndRemoveLast(Object prefixKey);

    List<E> subList(Object prefixKey, int fromIndex, int toIndex);

    List<E> all(Object prefixKey);
}
