package com.dykj.rpg.redis.cache.redis.proxy;

import com.dykj.rpg.redis.cache.base.ICacheSerialize;
import org.springframework.data.redis.core.BoundSetOperations;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author jyb
 * @date 2018/12/14 11:21
 */
public class CacheRedisSetProxy<E> extends AbstractRedisProxy<E> {

    public CacheRedisSetProxy(Class<E> clazz, ICacheSerialize<E> serialize) {
        super(clazz, serialize);
    }

    public Collection<E> all(BoundSetOperations<String, String> operations) {
        Set<String> ss = operations.members();
        if (null == ss || 0 >= ss.size()) return new HashSet<>(0);
        return string2Entity(ss);
    }

    public E randomMember(BoundSetOperations<String, String> operations) {
        return decode(operations.randomMember());
    }

    public Collection<E> randomMembers(BoundSetOperations<String, String> operations, int num) {
        // 如果大于 size，则返回真个 set，否则随机 num 个
        long size = size(operations);
        Set<String> ss = null;
        if (num >= size) {
            ss = operations.members();
        } else {
            List<String> list = operations.randomMembers(num);
            if (null != list && 0 < list.size()) {
                ss = new HashSet<>(list);
            }
        }
        if (null == ss || 0 >= ss.size()) return null;
        return string2Entity(ss);
    }

    public boolean contains(BoundSetOperations<String, String> operations, E e) {
        return operations.isMember(encode(e));
    }

    public void add(BoundSetOperations<String, String> operations, E e) {
        operations.add(encode(e));
    }

    public int size(BoundSetOperations<String, String> operations) {
        Long size = operations.size();
        return null != size ? (int) (long) size : 0;
    }

    public boolean remove(BoundSetOperations<String, String> operations, E e) {
        return 0 < operations.remove(encode(e));
    }
}
