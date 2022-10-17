package com.dykj.rpg.common.cache;

import java.util.List;

/**
 * @author jyb
 * @date 2020/11/9 17:42
 * @Description
 * 没有主键的对象缓存
 */
public interface ICache0Manager<T> {
    /***
     * 添加cache
     */
    void addCache(T t);
    /**
     * 删除cache
     */
    void removeCache( T  t);

    /**
     * 更新缓存
     * @param t
     */
    void updateCache(T t);
    /**
     * 拿到所有的cache
     * @return
     */
    List<T> values();

    /**
     * 添加所有
     */
    void addAll(List<T> ts);
}