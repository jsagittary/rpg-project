package com.dykj.rpg.battle.constant;

public class DetourTypeConstant {

    public static final byte DETOUR_TYPE_NULL = 0;
    /**
     * 寻路方式一
     * 对目标坐标点做路径寻路
     */
    public static final byte DETOUR_TYPE_PATH_POSITION = 1;

    /**
     * 寻路方式二
     * 对目标角色做路径寻路
     */
    public static final byte DETOUR_TYPE_PATH_ROLE = 2;

    /**
     * 寻路方式三
     * 对目标坐标点做群体寻路
     */
    public static final byte DETOUR_TYPE_CROWD_POSITION = 3;

    /**
     * 寻路方式四
     * 对目标角色做群体寻路
     */
    public static final byte DETOUR_TYPE_CROWD_ROLE = 4;
}
