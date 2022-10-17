package com.dykj.rpg.battle.data;

public class WaitEventData {
    /**
     * 寻找目标事件
     */
    public final static int EVENT_DATA_TYPE_FIND_TARGET = 1;
    /**
     * 设置目标地点
     */
    public final static int EVENT_DATA_TYPE_SET_TARGET_POSITION = 2;
    /**
     * 设置目标角色
     */
    public final static int EVENT_DATA_TYPE_SET_TARGET_ROLE = 3;
    /**
     * 设置角色到指定地点
     */
    public final static int EVENT_DATA_TYPE_SET_ROLE_POSITION = 4;


    public final static int EVENT_DATA_TYPE_RELEASE_SKILL = 101;

    public final static int EVENT_DATA_TYPE_SOUL_RELEASE_SKILL = 102;

    /**
     * 设置角色走出地图(优先级最高)
     */
    public final static int EVENT_DATA_TYPE_SET_ROLE_EXIT = 1000;

    public int eventType;

    public int[] eventDataParams;

    public float[] eventPosParams;

    public WaitEventData(){
        eventDataParams = new int[10];
        eventPosParams = new float[3];
    }

    public WaitEventData(int eventType, int[] eventDataParams, float[] eventPosParams) {
        this.eventType = eventType;
        this.eventDataParams = eventDataParams;
        this.eventPosParams = eventPosParams;
    }

    public void release(){
        eventType = 0;
        int length = eventDataParams.length;
        for(int i=0;i<length;i++){
            eventDataParams[i] = 0;
        }
        length = eventPosParams.length;
        for(int i=0;i<length;i++){
            eventPosParams[i] = 0;
        }
    }
}
