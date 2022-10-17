package com.dykj.rpg.battle.constant;

/**
 * 角色动画静态参数
 */
public class RoleAnimConstant {

    /**
     * 延续上一帧动作
     */
    public final static byte ROLE_ANIM_CONTINUE = -1;

    public final static byte ROLE_ANIM_WAIT = 0;

    public final static byte ROLE_ANIM_BORN = 1;

    public final static byte ROLE_ANIM_MOVE = 2;

    public final static byte ROLE_ANIM_DEAD = 3;

    public final static byte ROLE_ANIM_SKILL = 4;

    public final static byte ROLE_ANIM_HIT = 5;

    /**
     * 硬直
     */
    public final static byte ROLE_ANIM_STIFF = 6;

    /**
     * 施法被打断
     */
    public final static byte ROLE_ANIM_BREAK_SKILL = 7;

    /**
     * 跳跃
     */
    public final static byte ROLE_ANIM_JUMP = 8;


}
