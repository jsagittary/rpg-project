package com.dykj.rpg.common.cache;

import java.util.List;

/**
 * @Author: jyb
 * @Date: 2020/9/25 11:29
 * @Description:
 */
public interface ICache<T> extends ICachePrimaryKey {

    /**
     * 更新某一条数据
     *
     * @param o
     */
    public void update(T o);

    /**
     * 获取某一条数据
     *
     * @param key2
     * @return
     */
    public T selectByKey2(Long key2);

    /**
     * 移除某一条数据
     *
     * @param t
     */
    public void remove(T t);

    /**
     * 拿到所有
     *
     * @return
     */
    public List<T> all();

    /**
     * 批量添加
     *
     * @param list
     */
    public void addAll(List<T> list);

}
