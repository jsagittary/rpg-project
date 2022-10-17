package com.dykj.rpg.battle.config;

public class TileListenerCondition {

    public final static int CONDITION_TYPE_TIME = 1;

    public final static int CONDITION_TYPE_DISTANCE = 2;

    public final static int CONDITION_TYPE_KILL = 3;

    public String typeStr;

    public int type;

    public int time;

    public float x;

    public float z;

    public float[] pos;

    public float distance;

    public int kill;

    public TileListenerCondition(String type,int time,float x,float z,float distance,int kill){
        this.typeStr = type;
        this.time = time;
        this.x = x;
        this.z = z;
        this.distance = distance;
        this.kill = kill;
        if(type.equals("time")){
            this.type = CONDITION_TYPE_TIME;
        }
        if(type.equals("distance")){
            this.type = CONDITION_TYPE_DISTANCE;
        }
        if(type.equals("kill")){
            this.type = CONDITION_TYPE_KILL;
        }
    }

    public TileListenerCondition copy(){
        TileListenerCondition listenerCondition = new TileListenerCondition(typeStr,time,x,z,distance,kill);
        return listenerCondition;
    }

}
