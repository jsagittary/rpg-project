package com.dykj.rpg.battle.config;

public class TileNpcDetail {

    public int npcId;

    public float active_distance;

    public float x;

    public float z;

    public int minDelayTime;

    public int maxDelayTime;

    public int curDelayTime;

    /**
     * 怪物的出生信息
     */
    public int groupId;
    //public TileEffectConfig effectConfig;

    public TileNpcDetail(int groupId,int npcId, float active_distance, float x, float z,int minDelayTime,int maxDelayTime) {
        this.npcId = npcId;
        this.active_distance = active_distance;
        this.x = x;
        this.z = z;

        this.minDelayTime = minDelayTime;
        this.maxDelayTime = maxDelayTime;

        this.groupId = groupId;
    }

    public TileNpcDetail copy(){
        return new TileNpcDetail(this.groupId,this.npcId,this.active_distance,this.x,this.z,this.minDelayTime,this.maxDelayTime);
    }

    public float getActive_distance() {
        return active_distance;
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public int getMinDelayTime() {
        return minDelayTime;
    }

    public int getMaxDelayTime() {
        return maxDelayTime;
    }

    public int getCurDelayTime() {
        return curDelayTime;
    }

    public void setCurDelayTime(int curDelayTime) {
        this.curDelayTime = curDelayTime;
    }

    @Override
    public String toString() {
        return "TileNpcDetail{" +
                "npcId=" + npcId +
                ", active_distance=" + active_distance +
                ", x=" + x +
                ", z=" + z +
                ", minDelayTime=" + minDelayTime +
                ", maxDelayTime=" + maxDelayTime +
                ", curDelayTime=" + curDelayTime +
                '}';
    }
}
