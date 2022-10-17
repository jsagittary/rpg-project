package com.dykj.rpg.common.cache;

import com.dykj.rpg.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: jyb
 * @Date: 2020/9/23 17:37
 * @Description:
 */
public abstract class CacheMap<T extends Db2Model> implements ICache<T> {

    Map<Long, T> map = new ConcurrentHashMap<>();

    private long key;

    public CacheMap() {
    }

    public void remove(Number key) {
        map.remove(key);
    }

    public Map<Long, T> getMap() {
        return map;
    }

    @Override
    public void update(T t) {
        map.put(t.primary2Key(), t);
    }

    @Override
    public T selectByKey2(Long key2) {
        return map.get(key2);
    }

    @Override
    public void remove(T t) {
        map.remove(t.primary2Key());
    }

    @Override
    public List<T> all() {
        return new ArrayList<>(map.values());
    }

    @Override
    public void addAll(List<T> list) {
        for (T t : list) {
            update(t);
        }
    }

    public void setMap(Map<Long, T> map) {
        this.map = map;
    }

    @Override
    public long cachePrimaryKey() {
        return key;
    }

    @Override
    public String toString() {
         return JsonUtil.toJsonString(map);
    }

    public long getKey() {
        return key;
    }

    @Override
    public void primaryKey(long key) {
        this.key =key;
    }
}
