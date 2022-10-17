package com.dykj.rpg.battle.config;


import java.util.ArrayList;
import java.util.List;

public class TileGroupConfig {

    public int groupId;
    public int npcId;
    public String type;
    public float x;
    public float z;
    public float activeDistance;
    public int deadGroupId;
    public List<TileNpcDetail> npcDetails;

    public TileGroupConfig(int groupId, int npcId, String type, float x, float z, float activeDistance, int deadGroupId) {
        this.groupId = groupId;
        this.npcId = npcId;
        this.type = type;
        this.x = x;
        this.z = z;
        this.activeDistance = activeDistance;
        this.deadGroupId = deadGroupId;
        this.npcDetails = new ArrayList<>();
    }

    public void addNpcDetail(TileNpcDetail detail){
        npcDetails.add(detail);
    }

    public TileGroupConfig copy(){
        TileGroupConfig groupConfig = new TileGroupConfig(groupId,npcId,type,x,z,activeDistance,deadGroupId);
        for(int i=0;i<npcDetails.size();i++){
            groupConfig.addNpcDetail(npcDetails.get(i).copy());
        }
        return groupConfig;
    }

    public void removeNpcDetail(TileNpcDetail detail){
        npcDetails.remove(detail);
    }
}

