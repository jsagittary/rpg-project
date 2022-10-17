package com.dykj.rpg.common.cache;

import java.util.List;

/**
 * @Author: jyb
 * @Date: 2020/9/23 17:09
 * @Description:
 */
public interface ICache1Manager<T> {
    /***
     * 添加cache
     */
    void addCache(T t);
    /**
     * 删除cache
     */
    void removeCache( T  t);
    /**
     * 删除cache
     */
    void removeCache(long key);
    /**
     * 通过key拿数据
     *
     * @param key
     * @return
     */
    T getCache(long key);
    /**
     * 拿到所有的cache
     * @return
     */
    List<T> values();

}
