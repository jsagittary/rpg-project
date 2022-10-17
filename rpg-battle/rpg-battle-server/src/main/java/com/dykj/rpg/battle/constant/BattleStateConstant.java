package com.dykj.rpg.battle.constant;

public class BattleStateConstant {

    public final static byte BATTLE_STATE_NULL = 0;

    public final static byte BATTLE_STATE_INIT = 1;

    public final static byte BATTLE_STATE_RUNNING = 2;

    public final static byte BATTLE_STATE_PAUSE = 3;

    /**
     * 战斗结束
     */
    public final static byte BATTLE_STATE_FINISH = 4;
    /**
     * 战斗结束，从跑帧队列中删除
     */
    public final static byte BATTLE_STATE_REMOVE = 5;
    /**
     * 清空数据，从同步数据队列中删除
     */
    public final static byte BATTLE_STATE_CLEAR = 6;
    /**
     * 销毁对象，从战斗管理器中删除
     */
    public final static byte BATTLE_STATE_DESTROY = 7;

}
