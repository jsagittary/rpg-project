package com.dykj.rpg.redis.cache.core.prefix;

import java.util.Collection;

/**
 * @author jyb
 * @date 2018/12/14 11:06
 */
public interface ICacheSetPrefix<E> extends ICacheCollectionPrefix<E> {

    boolean contains(Object prefixKey, E e);

    void add(Object prefixKey, E e);

    boolean remove(Object prefixKey, E e);

    E randomMember(Object prefixKey);

    Collection<E> randomMembers(Object prefixKey, int num);

    Collection<E> all(Object prefixKey);
}
