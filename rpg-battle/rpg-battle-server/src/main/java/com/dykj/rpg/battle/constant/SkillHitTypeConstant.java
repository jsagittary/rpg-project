package com.dykj.rpg.battle.constant;

/**
 * 技能施法作用域类型
 */
public class SkillHitTypeConstant {
    /**
     * 全局
     */
    public final static byte SKILL_HIT_TYPE_GLOBAL = 1;

    /**
     * 矩形
     */
    public final static byte SKILL_HIT_TYPE_RECTANGLE = 2;

    /**
     * 扇形
     */
    public final static byte SKILL_HIT_TYPE_SECTOR = 3;

    /**
     * 圆形
     */
    public final static byte SKILL_HIT_TYPE_CIRCLE = 4;

    /**
     * 环形
     */
    public final static byte SKILL_HIT_TYPE_RING = 5;

    /**
     * 直线子弹
     */
    public final static byte SKILL_HIT_TYPE_BULLET_LINE = 6;

    /**
     * 跟踪子弹（必中）
     */
    public final static byte SKILL_HIT_TYPE_BULLET_TRACK = 7;

    /**
     * 自身
     */
    public final static byte SKILL_HIT_TYPE_SELF = 9;

}
