package com.dykj.rpg.battle.config;

public class MapGrowthConfig {
    private int id;
    private String type;
    private float x;

    private float z;
    private int birthInterval;
    private int dir;

    public MapGrowthConfig(int id, String type, float x, float z, int birthInterval, int dir) {
        this.id = id;
        this.type = type;
        this.x = x;
        this.z = z;
        this.birthInterval = birthInterval;
        this.dir = dir;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public int getBirthInterval() {
        return birthInterval;
    }

    public int getDir() {
        return dir;
    }
}
