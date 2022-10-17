package com.dykj.rpg.battle.config;

public class TileEffectConfig {

    public final static byte EFFECT_TYPE_NPC = 1;
    public final static byte EFFECT_TYPE_BUFF = 2;
    public final static byte EFFECT_TYPE_ROAD = 3;

    public final static byte EFFECT_TYPE_STATE_UNUSE = 0;
    public final static byte EFFECT_TYPE_STATE_USED = 1;

    public int id;
    public String typeStr;
    public int type;
    public int npcGroupId;
    public int delayTime;
    public int destroyEffectId;
    public int skillId;
    public int removeRoadPoint;

    public byte state;

    public TileEffectConfig(int id,String type,int npcGroupId,int delayTime,int destroyEffectId,int skillId,int removeRoadPoint){
        this.id = id;
        this.typeStr = type;
        this.npcGroupId = npcGroupId;
        this.delayTime = delayTime;
        this.destroyEffectId = destroyEffectId;
        this.skillId = skillId;
        this.removeRoadPoint = removeRoadPoint;
        this.state = EFFECT_TYPE_STATE_UNUSE;

        if(type.equals("GenerateNpc")){
            this.type = EFFECT_TYPE_NPC;
        }
        if(type.equals("GenerateBuff")){
            this.type = EFFECT_TYPE_BUFF;
        }
        if(type.equals("DestroyRoadPoint")){
            this.type = EFFECT_TYPE_ROAD;
        }
    }

    public TileEffectConfig copy(){

        TileEffectConfig effectConfig = new TileEffectConfig(id,typeStr,npcGroupId,delayTime,destroyEffectId,skillId,removeRoadPoint);

        return effectConfig;
    }
}
