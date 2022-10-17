package com.dykj.rpg.game.module.event.core;

/**
 * 事件监听
 *
 * @param
 */
public interface Event {

    /**
     * 执行事件
     *
     * @param prams
     */
    void doEvent(Object... prams) throws Exception;
}
