package com.dykj.rpg.common.cache;

import java.util.List;

/**
 * @Author: jyb
 * @Date: 2020/9/23 17:09
 * @Description:
 */
public interface ICache2Manager<V,T> {
    /***
     * 添加cache
     */
    void addCache(T t);

    /**
     * 更新cache
     */
    void updateValue(V v);

    /**
     * 删除cache
     */
    void removeValue(V v);

    /**
     * 移除缓存
     *
     * @param t
     */
    void removeCache(T t);

    /**
     * 通过key移除缓存
     *
     * @param key
     */
    void removeCache(Long key);

    /**
     * 玩家数据
     *
     * @param key
     * @return
     */
    T getCache(Long key);

    /**
     * 拿到所有的cache
     *
     * @return
     */
    List<T> values();

    /**
     * 双主键查询value
     * @param key1
     * @param key2
     * @return
     */
    V getValue(Long key1, Long key2);

}
