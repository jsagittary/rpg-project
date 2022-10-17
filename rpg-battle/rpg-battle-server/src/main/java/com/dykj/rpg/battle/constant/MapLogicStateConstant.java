package com.dykj.rpg.battle.constant;

/**
 * 地块运行状态
 */
public class MapLogicStateConstant {

    /**
     * 地块停止逻辑（寻路）运行
     */
    public final static byte MAPLOGIC_STATE_STOP = 0;

    /**
     * 地块正常逻辑（寻路）运行
     */
    public final static byte MAPLOGIC_STATE_RUN = 1;

    /**
     * 地块任务完成
     */
    public final static byte MAPLOGIC_STATE_FINISH = 2;
}
