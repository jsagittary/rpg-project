package com.dykj.rpg.battle.attribute;

public class AttributeConfig {

    public int id;

    public int type1;

    public int type2;

    public int num;

    public AttributeConfig(String attrStr){
        String[] strs = attrStr.split(":");
        if(strs.length == 4){
            id = Integer.parseInt(strs[0]);
            type1 = Integer.parseInt(strs[1]);
            type2 = Integer.parseInt(strs[2]);
            num = Integer.parseInt(strs[3]);
        }
        if(strs.length == 3){
            id = Integer.parseInt(strs[0]);
            type1 = Integer.parseInt(strs[1]);
            num = Integer.parseInt(strs[2]);
        }
    }

    public AttributeConfig(int id,int type1,int num){
        this.id = id;
        this.type1 = type1;
        this.num = num;
    }

    public AttributeConfig(int id,int type1,int type2,int num){
        this.id = id;
        this.type1 = type1;
        this.type2 = type2;
        this.num = num;
    }

}
