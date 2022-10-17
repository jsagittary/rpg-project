package com.dykj.rpg.redis.cache.base;

import java.util.Collection;
import java.util.List;

/**
 * User: jyb
 * Date: 2018-11-26 21:55
 */
public interface ICacheSerialize<V> {

    List<V> string2Entity(Collection<String> ss);

    String encode(V v);

    V decode(String s);
}
