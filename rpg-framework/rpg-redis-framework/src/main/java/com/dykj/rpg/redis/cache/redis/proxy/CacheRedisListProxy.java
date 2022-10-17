package com.dykj.rpg.redis.cache.redis.proxy;

import com.dykj.rpg.redis.cache.base.ICacheSerialize;
import org.springframework.data.redis.core.BoundListOperations;

import java.util.List;

/**
 * @author jyb
 * @date 2018/11/29 16:50
 */
public class CacheRedisListProxy<E> extends AbstractRedisProxy<E> {

    public CacheRedisListProxy(Class<E> clazz, ICacheSerialize<E> serialize) {
        super(clazz, serialize);
    }

    public E get(BoundListOperations operations, int index) {
        return decode((String) operations.index(index));
    }

    public void set(BoundListOperations operations, int index, E element) {
        operations.set(index, encode(element));
    }

    public boolean remove(BoundListOperations operations, E e) {
        return 0 < operations.remove(1, encode(e));
    }

    public List<E> subList(BoundListOperations operations, int fromIndex, int toIndex) {
        return string2Entity(operations.range(fromIndex, toIndex));
    }

    public int size(BoundListOperations operations) {
        Long size = operations.size();
        return null != size ? (int) (long) size : 0;
    }

    public void add(BoundListOperations operations, E e) {
        operations.rightPush(encode(e));
    }

    public E first(BoundListOperations operations) {
        return decode((String) operations.index(0));
    }

    public E getAndRemoveFirst(BoundListOperations operations) {
        return decode((String) operations.leftPop());
    }

    public E last(BoundListOperations operations) {
        return decode((String) operations.index(-1));
    }

    public E getAndRemoveLast(BoundListOperations operations) {
        return decode((String) operations.rightPop());
    }
}
