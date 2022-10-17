package com.dykj.rpg.game.module.battle.logic;

public interface IBattle {
    /**
     * 获得副本id
     *
     * @return
     */
    int getBattleId();
    /**
     * 开始战斗
     */
    void startBattle();
    /**
     * 结束战斗
     */
    void endBattle();
    /**
     * 销毁战斗
     */
    boolean destroy();
}
