package com.dykj.rpg.battle.constant;

/**
 * 角色目标类型静态配置
 */
public class RoleTargetTypeConstant {

    /**
     * 没有选中的目标
     */
    public final static byte ROLE_TARGET_TYPE_NULL = 0;

    /**
     * 以地图上的角色为目标（目标可移动）
     */
    public final static byte ROLE_TARGET_TYPE_ROLE = 1;

    /**
     * 以地图上的坐标点为目标（目标不可移动）
     */
    public final static byte ROLE_TARGET_TYPE_POSITION = 2;

}
