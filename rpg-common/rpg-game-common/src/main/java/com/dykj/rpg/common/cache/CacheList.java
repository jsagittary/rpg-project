package com.dykj.rpg.common.cache;


import com.dykj.rpg.util.JsonUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author: jyb
 * @Date: 2020/9/23 17:18
 * @Description:
 */
public abstract class CacheList<T extends Db2Model> implements ICache<T> {

    private long key;

    private List<T> list = new ArrayList<>();


    public CacheList(int key) {
        this.key = key;
    }

    public CacheList() {
    }

    /**
     * 更新某一条数据
     *
     * @param o
     */
    @Override
    public void update(T o) {
        for (T t : list) {
            if (t.primary2Key().equals(o.primary2Key())) {
                t = o;
                return;
            }
        }
        list.add(o);
    }

    /**
     * 获取某一条数据
     *
     * @param key2
     * @return
     */
    @Override
    public T selectByKey2(Long key2) {
        for (T t : list) {
            if (t.primary2Key().equals(key2)) {
                return t;
            }
        }
        return null;
    }

    /**
     * 移除某一条数据
     *
     * @param t
     */
    @Override
    public void remove(T t) {
        Iterator<T> iterable = list.iterator();
        while (iterable.hasNext()) {
            T t1 = iterable.next();
            if (t1.primary2Key() == t.primary2Key()) {
                iterable.remove();
                return;
            }
        }
    }

    /**
     * 拿到所有
     *
     * @return
     */
    @Override
    public List<T> all() {
        return list;
    }

    /**
     * 批量添加
     *
     * @param list
     */
    @Override
    public void addAll(List<T> list) {
        for (T t : list) {
            update(t);
        }
    }

    @Override
    public long cachePrimaryKey() {
        return key;
    }

    @Override
    public String toString() {
        return JsonUtil.toJsonString(list);
    }

    public long getKey() {
        return key;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
