package com.dykj.rpg.battle.data;

public class WaitUsePotionData {

    public int potionId;

    public boolean ignoreCdTime;

    public WaitUsePotionData(){}

    public WaitUsePotionData(int potionId,boolean ignoreCdTime){
        this.potionId = potionId;
        this.ignoreCdTime = ignoreCdTime;
    }

}
