package com.dykj.rpg.db.dao;

import java.io.Serializable;

/**
 * @Author: jianbo.Gong
 * @Date: 2018/3/17 17:01
 * @Description:
 */
public abstract class BaseConfig<K> implements Serializable {
    /**
     * 拿到唯一key
     * @return
     */
    public abstract K getKey();
}
