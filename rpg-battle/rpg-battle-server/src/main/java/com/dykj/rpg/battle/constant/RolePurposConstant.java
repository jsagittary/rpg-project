package com.dykj.rpg.battle.constant;

/**
 * 角色行为目的静态参数
 */
public class RolePurposConstant {
    /**
     * 漫无目的
     */
    public final static byte ROLE_PURPOS_NULL = 1;
    /**
     * 原地等待 （休息）
     */
    public final static byte ROLE_PURPOS_WAIT = 2;
    /**
     * 寻找对象（同伴，敌人等）
     */
    public final static byte ROLE_PURPOS_FIND_OBJECT = 3;
    /**
     * 寻找地图块内的位置点
     */
    public final static byte ROLE_PURPOS_FIND_POSITION = 4;
    /**
     * 寻找地块出口，一般在完成地块任务后切换为此状态
     */
    public final static byte ROLE_PURPOS_FIND_EXIT = 5;
    /**
     * 战斗中
     */
    public final static byte ROLE_PURPOS_FIGHT = 6;

}
