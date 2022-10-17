package com.dykj.rpg.battle.config;

import com.dykj.rpg.battle.constant.BattleTypeConstant;
import com.dykj.rpg.battle.detour.MapLoader;
import com.dykj.rpg.battle.logic.MapTileData;
import com.dykj.rpg.protocol.battle.BattleMapInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LevelMapConfig {

    Logger logger = LoggerFactory.getLogger("LevelMapConfig");

    public String mapXmlPath;

    public byte battleType = 1;

    public int npcRounds = 0;

    public int totalNpcNums = 0;

    public List<MapTileConfig> tileConfigs;


    public LevelMapConfig(int id,String mapXmlPath){
        this.mapXmlPath = mapXmlPath;
        this.tileConfigs = new ArrayList<>();
        loadConfig();
    }

    private void loadConfig(){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream is = LevelMapConfig.class.getClassLoader().getResourceAsStream(mapXmlPath);
            //InputStream is = LevelMapConfig.class.getClassLoader().getResourceAsStream("level/new_sample.xml");
            Document doc = builder.parse(is);
            NodeList type = doc.getElementsByTagName("type");
            if(type != null && type.getLength() != 0){
                String typeStr = type.item(0).getTextContent();
                if("normal".equals(typeStr)){
                    battleType = BattleTypeConstant.BATTLE_TYPE_GENERAL;
                }
                if("protect".equals(typeStr)){
                    battleType = BattleTypeConstant.BATTLE_TYPE_BASE_GUARD;
                }
            }

            NodeList tile = doc.getElementsByTagName("tile");
            if(tile != null){
                for(int i=0;i<tile.getLength();i++){
                    MapTileConfig tileConfig = loadTile(tile.item(i));
                    tileConfigs.add(tileConfig);
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            logger.error("the level config["+mapXmlPath+"] file error !!!");
        } catch (SAXException e) {
            e.printStackTrace();
            logger.error("the level config["+mapXmlPath+"] file error !!!");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("the level config["+mapXmlPath+"] file not found !!!");
        }

    }

    private MapTileConfig loadTile(Node tile){
        NodeList children = tile.getChildNodes();

        NamedNodeMap attrs = tile.getAttributes();
        String path = attrs.getNamedItem("path").getNodeValue();
        int tileId = MapLoader.getInstance().getTileIdByPath(path);
        if(tileId == 0){
            return null;
        }
        byte enter = Byte.parseByte(attrs.getNamedItem("entry").getNodeValue());
        byte exit = Byte.parseByte(attrs.getNamedItem("exit").getNodeValue());

        MapTileConfig tileConfig = new MapTileConfig(tileId,enter,exit);

        for(int i=0;i<children.getLength();i++){
            String nodeName = children.item(i).getNodeName();
            if("player".equals(nodeName)){
                Node player = children.item(i);
                NamedNodeMap attrs2 = player.getAttributes();
                float x = Float.parseFloat(attrs2.getNamedItem("x").getNodeValue());
                float z = Float.parseFloat(attrs2.getNamedItem("z").getNodeValue());
                tileConfig.startPosX = x;
                tileConfig.startPosZ = z;
            }
            if("npcs".equals(nodeName)){
                loadNpcs(tileConfig,children.item(i));
            }
            if("roads".equals(nodeName)){
                loadRoads(tileConfig,children.item(i));
            }
            if("listeners".equals(nodeName)){
                loadListeners(tileConfig,children.item(i));
            }
            if("effects".equals(nodeName)){
                loadEffects(tileConfig,children.item(i));
            }

        }

        return tileConfig;
    }

    private void loadNpcs(MapTileConfig tileConfig,Node npcs){
        NodeList children = npcs.getChildNodes();
        for(int i=0;i<children.getLength();i++){
            Node group = children.item(i);
            if("npc_group".equals(group.getNodeName())){
                NamedNodeMap attrs = group.getAttributes();
                int group_id = Integer.parseInt(attrs.getNamedItem("group_id").getNodeValue());
                int npc_id = 0;
                if(attrs.getNamedItem("npc_id") != null){
                    npc_id = Integer.parseInt(attrs.getNamedItem("npc_id").getNodeValue());
                }
                String type = "";
                if(attrs.getNamedItem("type") != null){
                    type = attrs.getNamedItem("type").getNodeValue();
                }
                float x = 0;
                if(attrs.getNamedItem("x") != null){
                    x = Float.parseFloat(attrs.getNamedItem("x").getNodeValue());
                }
                float z = 0;
                if(attrs.getNamedItem("z") != null){
                    z = Float.parseFloat(attrs.getNamedItem("z").getNodeValue());
                }
                float active_distance = 0;
                if(attrs.getNamedItem("active_distance") != null){
                    active_distance = Float.parseFloat(attrs.getNamedItem("active_distance").getNodeValue());
                }

                int dead_group_id = 0;
                if(attrs.getNamedItem("dead_group_id") != null){
                    dead_group_id = Integer.parseInt(attrs.getNamedItem("dead_group_id").getNodeValue());
                }

                int minDelayTime = 0;
                int maxDelayTime = 0;
                try {
                    minDelayTime = Integer.parseInt(attrs.getNamedItem("born_randmin").getNodeValue());
                }catch(Exception e){ }
                try {
                    maxDelayTime = Integer.parseInt(attrs.getNamedItem("born_randmax").getNodeValue());
                }catch(Exception e){ }

                TileGroupConfig groupConfig = new TileGroupConfig(group_id,npc_id,type,x,z,active_distance,dead_group_id);

                NodeList groupNpcs = group.getChildNodes();

                for(int j=0;j<groupNpcs.getLength();j++){
                    Node npc = groupNpcs.item(j);
                    if("npc".equals(npc.getNodeName())){
                        NamedNodeMap attrs2 = npc.getAttributes();
                        float x2 = Float.parseFloat(attrs2.getNamedItem("x").getNodeValue());
                        float z2 = Float.parseFloat(attrs2.getNamedItem("z").getNodeValue());
                        int npcId = 0;
                        if(attrs2.getNamedItem("npc_id") != null){
                            npcId = Integer.parseInt(attrs2.getNamedItem("npc_id").getNodeValue());
                        }else{
                            if(npc_id != 0){
                                npcId = npc_id;
                            }
                        }

                        TileNpcDetail npcDetail = new TileNpcDetail(group_id,npcId,0,x2,z2,minDelayTime,maxDelayTime);
                        groupConfig.addNpcDetail(npcDetail);
                        totalNpcNums ++;

                        Integer npcNum = tileConfig.npcRecordInfoMap.get(npcId);
                        if(npcNum != null){
                            tileConfig.npcRecordInfoMap.put(npcId,npcNum+1);
                        }else{
                            tileConfig.npcRecordInfoMap.put(npcId,1);
                        }
                    }
                }

                tileConfig.groupConfigList.add(groupConfig);

                npcRounds ++ ;
            }

        }
    }

    private void loadRoads(MapTileConfig tileConfig,Node roads){
        NodeList children = roads.getChildNodes();
        for(int i=0;i<children.getLength();i++){
            Node child = children.item(i);
            if("road".equals(child.getNodeName())){
                NamedNodeMap attrs = child.getAttributes();
                int index = Integer.parseInt(attrs.getNamedItem("index").getNodeValue());
                float x = Float.parseFloat(attrs.getNamedItem("x").getNodeValue());
                float z = Float.parseFloat(attrs.getNamedItem("z").getNodeValue());

                tileConfig.roadDetailList.add(new TileRoadDetail(index,x,z));
            }
        }
    }

    private void loadListeners(MapTileConfig tileConfig,Node triggers){
        NodeList children = triggers.getChildNodes();
        for(int i=0;i<children.getLength();i++){
            Node listener = children.item(i);
            if("listener".equals(listener.getNodeName())){
                NamedNodeMap attrs = listener.getAttributes();
                String effectsStr = attrs.getNamedItem("effects").getNodeValue();
                String type = attrs.getNamedItem("type").getNodeValue();

                String[] strs = effectsStr.split(",");
                List<Integer> effects = new ArrayList<>();
                try{
                    for(String str : strs){
                        str = str.trim();
                        if(str != null && !str.equals("")){
                            effects.add(Integer.parseInt(str));
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                TileListenerConfig listenerConfig = new TileListenerConfig(effects,type);

                NodeList conditions = listener.getChildNodes();

                for(int j=0;j<conditions.getLength();j++){
                    Node condition = conditions.item(j);
                    if("condition".equals(condition.getNodeName())){
                        NamedNodeMap attrs2 = condition.getAttributes();
                        String type2 = "";
                        if(attrs2.getNamedItem("type") != null){
                            type2 = attrs2.getNamedItem("type").getNodeValue();
                        }
                        int time = 0;
                        if(attrs2.getNamedItem("time") != null){
                            time = Integer.parseInt(attrs2.getNamedItem("time").getNodeValue())/10;
                        }
                        float x = 0;
                        if(attrs2.getNamedItem("x") != null){
                            x = Float.parseFloat(attrs2.getNamedItem("x").getNodeValue());
                        }
                        float z = 0;
                        if(attrs2.getNamedItem("z") != null){
                            z = Float.parseFloat(attrs2.getNamedItem("z").getNodeValue());
                        }
                        float distance = 0;
                        if(attrs2.getNamedItem("distance") != null){
                            distance = Float.parseFloat(attrs2.getNamedItem("distance").getNodeValue());
                        }
                        int kill = 0;
                        if(attrs2.getNamedItem("kill") != null){
                            kill = Integer.parseInt(attrs2.getNamedItem("kill").getNodeValue());
                        }

                        TileListenerCondition listenerCondition = new TileListenerCondition(type2,time,x,z,distance,kill);
                        listenerConfig.addListenerCondition(listenerCondition);
                    }
                }

                tileConfig.listenerConfigList.add(listenerConfig);
            }
        }
    }

    private void loadEffects(MapTileConfig tileConfig,Node effects){
        NodeList children = effects.getChildNodes();
        for(int i=0;i<children.getLength();i++){
            Node child = children.item(i);
            if("effect".equals(child.getNodeName())){
                NamedNodeMap attrs = child.getAttributes();
                int id = 0;
                if(attrs.getNamedItem("id") != null){
                    id = Integer.parseInt(attrs.getNamedItem("id").getNodeValue());
                }
                String type = "";
                if(attrs.getNamedItem("type") != null){
                    type = attrs.getNamedItem("type").getNodeValue();
                }
                int npcGroupId = 0;
                if(attrs.getNamedItem("npc_group_id") != null){
                    npcGroupId = Integer.parseInt(attrs.getNamedItem("npc_group_id").getNodeValue());
                }
                int delayTime = 0;
                if(attrs.getNamedItem("delay_time") != null){
                    delayTime = Integer.parseInt(attrs.getNamedItem("delay_time").getNodeValue())/10;
                }
                int destroyEffectId = 0;
                if(attrs.getNamedItem("destroy_effect_id") != null){
                    destroyEffectId = Integer.parseInt(attrs.getNamedItem("destroy_effect_id").getNodeValue());
                }
                int skillId = 0;
                if(attrs.getNamedItem("skill_id") != null){
                    skillId = Integer.parseInt(attrs.getNamedItem("skill_id").getNodeValue());
                }
                int removeRoadPoint = 0;
                if(attrs.getNamedItem("remove_road_point") != null){
                    removeRoadPoint = Integer.parseInt(attrs.getNamedItem("remove_road_point").getNodeValue());
                }

                tileConfig.effectConfigList.add(new TileEffectConfig(id,type,npcGroupId,delayTime,destroyEffectId,skillId,removeRoadPoint));
            }
        }
    }

    public void getBattleMapInfoList(List<MapTileData> mapTileDataList){
        for(byte i=0;i<tileConfigs.size();i++){
            MapTileConfig tileConfig = tileConfigs.get(i);
            MapTileData mapTileData = new MapTileData();
            mapTileData.id = tileConfig.tileId;
            mapTileData.index = i;
            mapTileData.exit = tileConfig.exit;

            mapTileData.npcRecordInfoMap = tileConfig.npcRecordInfoMap;

            mapTileDataList.add(mapTileData);
        }
    }

}
