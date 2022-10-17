package com.dykj.rpg.battle.config;

public class TileRoadDetail {

    private int index;

    private float x;

    private float z;

    public TileRoadDetail(int index,float x, float z) {
        this.index = index;
        this.x = x;
        this.z = z;
    }

    public TileRoadDetail copy(){
        return new TileRoadDetail(this.index,this.x,this.z);
    }

    public int getIndex() {
        return index;
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }
}
