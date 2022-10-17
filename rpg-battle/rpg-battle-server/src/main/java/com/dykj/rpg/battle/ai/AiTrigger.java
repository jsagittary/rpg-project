package com.dykj.rpg.battle.ai;

public class AiTrigger {

    /**
     * 全时触发
     */
    public static final int AI_TRIGGER_ALL_TIME = 1;

    /**
     * 行走时触发
     */
    public static final int AI_TRIGGER_RUN = 11;

    /**
     * 释放技能时触发
     */
    public static final int AI_TRIGGER_RELEASE_SKILL = 21;
    /**
     * 释放技能后触发
     */
    public static final int AI_TRIGGER_AFTER_SKILL = 22;
    /**
     * 灵魂之影释放技能时触发
     */
    public static final int AI_TRIGGER_SOUL_RELEASE_SKILL = 23;

    /**
     * 自身受击时触发
     */
    public static final int AI_TRIGGER_HIT_SELF = 31;
    /**
     * 主角锁定的敌人受击时触发
     */
    public static final int AI_TRIGGER_HIT_TARGET = 32;


}
