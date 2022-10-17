package com.dykj.rpg.game.module.event.core;

/**
 * @Author: jyb
 * @Date: 2020/9/5 14:53
 * @Description:
 */
public interface EventManager<T> {

    /**
     * 添加事件
     * @param t
     */
    void addEvent(T t);

    /**
     * 执行事件
     */
    void doEvents(Object ... prams);
}
