package com.dykj.rpg.db.dao;

import org.springframework.jdbc.core.RowMapper;

import java.util.Collection;

/**
 * @Author: jianbo.Gong
 * @Date: 2018/3/17 16:28
 * @Description:
 */
public interface IConfigDao<K,T> {
    /**
     * 通过主键拿某个对�?
     *
     * @param
     * @return
     */
    T getConfigByKey(K key);

    /**
     * 拿到�?以的配置
     *
     * @return
     */
    Collection<T> getConfigs();

    /**
     * 清空数据
     */
    void clear();

    /**
     * model映射
     *
     * @return
     */
    RowMapper<T> rowMapper();

    /**
     * 加载配置
     */
    void load();
}
