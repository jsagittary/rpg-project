package com.dykj.rpg.battle.constant;

/**
 * 角色生命状态静态参数
 */
public class RoleStateConstant {

    /**
     * 自由 [此时角色可以拥有主动AI]
     */
    public final static byte ROLE_STATE_FREE = 0;
    /**
     * 出生
     */
    public final static byte ROLE_STATE_BORN = 1;

    /**
     * 死亡
     */
    public final static byte ROLE_STATE_DEATH = 2;

    /**
     * 定身[静止]
     */
    public final static byte ROLE_STATE_STILL = 3;

    /**
     * 隐藏
     */
    public final static byte ROLE_STATE_HIDE = 4;

}
