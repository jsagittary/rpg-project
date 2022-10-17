package com.dykj.rpg.redis.cache.redis.proxy;

import com.dykj.rpg.redis.cache.base.ICacheSerialize;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author jyb
 * @date 2018/12/14 11:36
 */
public abstract class AbstractRedisProxy<E> implements ICacheSerialize<E> {

    protected Class<E> clazz;
    protected ICacheSerialize<E> serialize;

    public AbstractRedisProxy(Class<E> clazz, ICacheSerialize<E> serialize) {
        this.clazz = clazz;
        this.serialize = serialize;
    }

    public void clear(RedisTemplate redisTemplate, String path) {
        redisTemplate.delete(path);
    }

    @Override
    public List<E> string2Entity(Collection<String> ss) {
        return serialize.string2Entity(ss);
    }

    @Override
    public String encode(E e) {
        return serialize.encode(e);
    }

    @Override
    public E decode(String s) {
        return serialize.decode(s);
    }

    /**
     * Object key 转 String
     * @return
     */
    protected String key2String(Object k) {
        if (String.class == k.getClass()) {
            return (String) k;
        }
        return String.valueOf(k);
    }

    /**
     * Object key 列表转 String 列表
     * @param ks
     * @return
     */
    protected Collection<String> key2String(Collection<?> ks) {
        if (null != ks && 0 < ks.size()) {
            if (String.class == ks.iterator().next().getClass()) {
                return (Collection<String>) ks;
            }
            List<String> list = new ArrayList<>(ks.size());
            for (Object o : ks) {
                list.add(key2String(o));
            }
            return list;
        }
        return null;
    }
}