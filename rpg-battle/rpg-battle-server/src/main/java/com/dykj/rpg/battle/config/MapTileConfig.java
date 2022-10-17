package com.dykj.rpg.battle.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapTileConfig {

    public int tileId;

    public byte enter;

    public byte exit;

    public List<TileGroupConfig> groupConfigList;

    public List<TileRoadDetail> roadDetailList;

    public List<TileListenerConfig> listenerConfigList;

    public List<TileEffectConfig> effectConfigList;

    public float startPosX;

    public float startPosZ;

    /**
     * 怪物统计记录
     */
    public Map<Integer,Integer> npcRecordInfoMap;

    public MapTileConfig(int tileId,byte enter,byte exit){
        groupConfigList = new ArrayList<>();
        roadDetailList = new ArrayList<>();
        listenerConfigList = new ArrayList<>();
        effectConfigList = new ArrayList<>();

        this.tileId = tileId;
        this.enter = enter;
        this.exit = exit;
        this.startPosX = 0;
        this.startPosZ = 0;

        npcRecordInfoMap = new HashMap<>();
    }

    public MapTileConfig(int tileId,byte enter,byte exit,float startPosX,float startPosZ){
        groupConfigList = new ArrayList<>();
        roadDetailList = new ArrayList<>();
        listenerConfigList = new ArrayList<>();
        effectConfigList = new ArrayList<>();

        this.tileId = tileId;
        this.enter = enter;
        this.exit = exit;
        this.startPosX = startPosX;
        this.startPosZ = startPosZ;

        npcRecordInfoMap = new HashMap<>();
    }

    public MapTileConfig copy(){
        MapTileConfig config = new MapTileConfig(this.tileId,this.enter,this.exit,this.startPosX,this.startPosZ);
        for(TileGroupConfig groupConfig : groupConfigList){
            config.groupConfigList.add(groupConfig.copy());
        }
        for(TileRoadDetail roadDetail : roadDetailList){
            config.roadDetailList.add(roadDetail.copy());
        }
        for(TileListenerConfig triggerConfig : listenerConfigList){
            config.listenerConfigList.add(triggerConfig.copy());
        }
        for(TileEffectConfig effectConfig : effectConfigList){
            config.effectConfigList.add(effectConfig.copy());
        }
        for(Integer key : npcRecordInfoMap.keySet()){
            config.npcRecordInfoMap.put(key,npcRecordInfoMap.get(key));
        }
        return config;
    }

    public void removeTileNpcDetail(TileNpcDetail tileNpcDetail){
        for(TileGroupConfig groupConfig : groupConfigList){
            if(groupConfig.groupId == tileNpcDetail.groupId){
                groupConfig.npcDetails.remove(tileNpcDetail);
                if(groupConfig.npcDetails.size() == 0){
                   // System.out.println("remove group from groupConfigList ! group id = "+groupConfig.groupId);
                    groupConfigList.remove(groupConfig);
                }
                break;
            }
        }
    }

    public boolean containsGroupConfig(int groupId){
        for(TileGroupConfig groupConfig : groupConfigList){
            if(groupConfig.groupId == groupId){
                return true;
            }
        }
        return false;
    }
}
