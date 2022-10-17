package com.dykj.rpg.db.annotation;

public enum PrimaryKey {
    /**
     * 非主键
     */
    NO_KEY,

    /**
     * 自增主键
     */
    INCREMENT,

    /**
     * 普通主键
     */
    GENERAL;
}
