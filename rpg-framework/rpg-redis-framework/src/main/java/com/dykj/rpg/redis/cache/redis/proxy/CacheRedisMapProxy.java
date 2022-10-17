package com.dykj.rpg.redis.cache.redis.proxy;

import com.dykj.rpg.redis.cache.redis.data.HScanResult;
import com.dykj.rpg.redis.cache.base.ICacheSerialize;
import com.dykj.rpg.util.JsonUtil;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.*;

/**
 * @author jyb
 * @date 2018/11/28 20:05
 */
public class CacheRedisMapProxy<E> extends AbstractRedisProxy<E> {

    public CacheRedisMapProxy(Class<E> clazz, ICacheSerialize<E> serialize) {
        super(clazz, serialize);
    }

    public long size(BoundHashOperations operations) {
        Long size = operations.size();
        if (null == size) return 0;
        return size;
    }

    public boolean containsKey(BoundHashOperations operations, Object key) {
        return operations.hasKey(key2String(key));
    }

    public E get(BoundHashOperations operations, Object key) {
        Object object = operations.get(key2String(key));
        if (null == object) {
            return null;
        }
        return decode((String) object);
    }

    public List<E> multiGet(BoundHashOperations operations, Collection<?> keys) {
        List<String> list = operations.multiGet(key2String(keys));
        if (null != list && 0 < list.size()) {
            return string2Entity(list);
        }
        return null;
    }

    public void put(BoundHashOperations operations, Object primaryKey, E value) {
        operations.put(key2String(primaryKey), encode(value));
    }

    public long remove(BoundHashOperations operations, Object key) {
        return operations.delete(key2String(key));
    }

    public Set<String> keySet(BoundHashOperations operations) {
        return operations.keys();
    }

    public List<E> values(BoundHashOperations operations) {
        return string2Entity(operations.values());
    }

    public HScanResult<E> scan(RedisTemplate<String, String> redisTemplate, RedisScript<String> hashScan, String path, int index, int count) {
        List<String> keys = Arrays.asList(path);
        Object o = redisTemplate.execute(hashScan, keys, String.valueOf(index), String.valueOf(count));
        if (null != o && o instanceof List) {
            return toHScanResult((List) o);
        }
        return null;
    }

    private HScanResult<E> toHScanResult(List list) {
        HScanResult scanResult = null;
        try {
            if (null != list) {
                int nextIndex = -1;
                Map<String, E> map = null;
                if (0 < list.size()) {
                    nextIndex = Integer.parseInt((String) list.get(0));
                }
                if (1 < list.size()) {
                    List items = (List) list.get(1);
                    if (0 == items.size() % 2) {
                        map = new HashMap<>(items.size() / 2);
                        for (int i=0; i<items.size();) {
                            map.put((String) items.get(i++), JsonUtil.toInstance((String) items.get(i++), clazz));
                        }
                    }
                }
                scanResult = new HScanResult(nextIndex, map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scanResult;
    }
}
