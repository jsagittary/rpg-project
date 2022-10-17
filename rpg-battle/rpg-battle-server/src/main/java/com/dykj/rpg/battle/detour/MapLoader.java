package com.dykj.rpg.battle.detour;

import com.dykj.rpg.battle.config.*;
import com.dykj.rpg.battle.constant.GameConstant;
import com.dykj.rpg.battle.dictionary.StaticDictionary;
import com.dykj.rpg.battle.logic.MapTileData;
import com.dykj.rpg.battle.logic.SuccessCondition;
import com.dykj.rpg.common.config.model.MisBasicModel;
import com.dykj.rpg.protocol.battle.BattleMapInfo;
import org.recast4j.detour.NavMesh;
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
import java.util.*;

public class MapLoader {

    Logger logger = LoggerFactory.getLogger("MapLoader");

    private static MapLoader instance;

    public String tileConfigFileName = "tile_config";

    /**
     * key = 地图ID
     */
    Map<Integer, NavMesh> navMeshMap;

    Map<Integer, GameInputGeomProvider> geomMap;

    /**
     * key = 地图ID
     */
    Map<Integer,TilePortalConfig> portalConfigMap;

    /**
     * key = 关卡ID
     */
    Map<Integer, LevelMapConfig> levelMapConfigMap;

    List<TilePortalConfig> portalConfigList;

    /**
     * key = 地图入口ID
     */
    Map<Integer, List<TilePortalConfig>> enterConfigMap;

    Map<Integer,String> idTileNames ;

    /**
     * 关卡配置文件
     */
    Map<Integer,MisBasicModel> idMisBasicModels ;

    private MapLoader(){

        navMeshMap = new HashMap<>();
        geomMap = new HashMap<>();
        portalConfigMap = new HashMap<>();
        levelMapConfigMap = new HashMap<>();
        portalConfigList = new ArrayList<>();
        enterConfigMap = new HashMap<>();

        idTileNames = new HashMap<>();

        idMisBasicModels = new HashMap<>();

    }

    public static MapLoader getInstance(){
        if(instance == null){
            instance = new MapLoader();
        }
        return instance;
    }

    public void loadMap(){
        loadTileIdAndName();
        loadLevelIdAndName();
        loadTileConfig();
        loadLevelConfig();
        System.out.println("load map and level config success !!!");
    }

    public void loadTileIdAndName(){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream is = TilePortalConfig.class.getClassLoader().getResourceAsStream("obj/"+tileConfigFileName+".xml");
            Document doc = builder.parse(is);
            NodeList tiles = doc.getElementsByTagName("tile");
            if(tiles != null){
                for(int i=0;i<tiles.getLength();i++){
                    Node tile = tiles.item(i);
                    NamedNodeMap attrs = tile.getAttributes();
                    int id = Integer.parseInt(attrs.getNamedItem("id").getNodeValue());
                    String name = attrs.getNamedItem("name").getNodeValue();

                    idTileNames.put(id,name);

                }
            }
        } catch (Exception e) {
            logger.error("file  {}  load error ","obj/"+tileConfigFileName+".xml");
            e.printStackTrace();
        }
    }

    public void loadLevelIdAndName(){

        List<MisBasicModel> models = StaticDictionary.getInstance().getAllMisBasicModel();

        for(MisBasicModel model : models){
            idMisBasicModels.put(model.getMisId(),model);
        }

        if(GameConstant.TEST_MAP_NAME != null && !GameConstant.TEST_MAP_NAME.trim().equals("")){
            MisBasicModel model = new MisBasicModel();
            model.setMisId(0);
            model.setMisMap(GameConstant.TEST_MAP_NAME);
            model.setMonstLevel(1);
            idMisBasicModels.put(0,model);
        }


//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        try {
//            DocumentBuilder builder = factory.newDocumentBuilder();
//            InputStream is = TilePortalConfig.class.getClassLoader().getResourceAsStream("level/"+levelConfigFileName+".xml");
//            Document doc = builder.parse(is);
//            NodeList tiles = doc.getElementsByTagName("level");
//            if(tiles != null){
//                for(int i=0;i<tiles.getLength();i++){
//                    Node tile = tiles.item(i);
//                    NamedNodeMap attrs = tile.getAttributes();
//                    int id = Integer.parseInt(attrs.getNamedItem("id").getNodeValue());
//                    String name = attrs.getNamedItem("name").getNodeValue();
//
//                    idLevelNames.put(id,name);
//
//                }
//            }
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        } catch (SAXException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void loadTileConfig(){

        Iterator<Integer> keys = idTileNames.keySet().iterator();
        while(keys.hasNext()){
            int mapId = keys.next();
            //System.out.println("load map "+idTileNames.get(mapId));
            createNavMesh(mapId,"obj/"+idTileNames.get(mapId)+".obj");
            TilePortalConfig portalConfig = new TilePortalConfig(mapId,"obj/"+idTileNames.get(mapId)+".xml");
            portalConfigMap.put(mapId,portalConfig);
            portalConfigList.add(portalConfig);
            for(TilePortalDetail portalDetail : portalConfig.portalDetails){
                if(portalDetail != null && portalDetail.type.equals("enter")){
                    List<TilePortalConfig> detailList = enterConfigMap.get(portalDetail.id);
                    if(detailList == null){
                        detailList = new ArrayList<>();
                        enterConfigMap.put(portalDetail.id, detailList);
                    }
                    detailList.add(portalConfig);
                }
            }
        }
    }

    public void loadLevelConfig(){
        Iterator<Integer> keys = idMisBasicModels.keySet().iterator();
        while(keys.hasNext()){
            int levelId = keys.next();
            String xmlName = idMisBasicModels.get(levelId).getMisMap();
            levelMapConfigMap.put(levelId,new LevelMapConfig(levelId,"level/"+xmlName));
        }
    }

    private void createNavMesh(int mapId,String mapPath) {
        GameInputGeomProvider geom = new ObjImporter().load(ObjImporter.class.getClassLoader().getResourceAsStream(mapPath));
        NavMesh navMesh = new NavMesh(new RecastMeshBuilder(geom).getMeshData(), 6, 0);
        geomMap.put(mapId,geom);
        navMeshMap.put(mapId,navMesh);
    }

    public NavMesh getNavMeshById(int mapId){
        return navMeshMap.get(mapId);
    }

    public GameInputGeomProvider getGeomById(int mapId){
        return geomMap.get(mapId);
    }

    public String getMapNameById(int mapId){
        return idTileNames.get(mapId);
    }

    public float[] getExitPosById(int tileId,byte exit){
        TilePortalConfig portalConfig = portalConfigMap.get(tileId);
        TilePortalDetail portalDetail = portalConfig.portalDetails[exit-1];
        return new float[]{portalDetail.x, portalDetail.z};
    }

    public float[] getEnterPosById(int tileId){
        TilePortalConfig portalConfig = portalConfigMap.get(tileId);
        for(TilePortalDetail portalDetail : portalConfig.portalDetails){
            if(portalDetail != null && portalDetail.type.equals("enter")){
                return new float[]{portalDetail.x, portalDetail.z};
            }
        }
        return null;
    }

    public TilePortalConfig getTilePortalById(int tileId){
        return portalConfigMap.get(tileId);
    }

    public int getTileIdByPath(String path){
        Set<Integer> set = idTileNames.keySet();
        for(int key : set){
            if(path.endsWith(idTileNames.get(key))){
                return key;
            }
        }
        return 0;
    }

    /**
     *
     * @param levelId
     * @param tileDataList
     * @return 根据配置文件获取关卡战斗类型
     */
    public int[] getBattleMapInfoByLevelId(int levelId,List<MapTileData> tileDataList){

        LevelMapConfig mapConfig = levelMapConfigMap.get(levelId);
        if(mapConfig == null){
            logger.error("can not found level config file by levelId["+levelId+"] !!!");
            return null;
        }
        mapConfig.getBattleMapInfoList(tileDataList);

        return new int[]{mapConfig.battleType,mapConfig.npcRounds};
    }

    public LevelMapConfig getLevelMapConfigByLevelId(int levelId){
        return levelMapConfigMap.get(levelId);
    }

    public void getBattleSuccessConditionByLevelId(int levelId,List<SuccessCondition> successConditions){

        LevelMapConfig mapConfig = levelMapConfigMap.get(levelId);

        MisBasicModel misBasicModel = idMisBasicModels.get(levelId);
        List<List<Integer>> conditionList = misBasicModel.getConditionType();
        if(conditionList != null && conditionList.size() > 0){
            for(List<Integer> condition : conditionList){
                SuccessCondition successCondition = new SuccessCondition(mapConfig,condition);
                successConditions.add(successCondition);
            }
        }

    }

    public int getMonsterLevelByLevelId(int levelId){

        LevelMapConfig mapConfig = levelMapConfigMap.get(levelId);

        MisBasicModel misBasicModel = idMisBasicModels.get(levelId);
        if(misBasicModel != null){
            return misBasicModel.getMonstLevel();
        }

        return 1;

    }

    public MapTileConfig getTileConfigByLevelIdAndIndex(int levelId, int index){
        return levelMapConfigMap.get(levelId).tileConfigs.get(index);
    }

    /**
     * 创建随机地图
     * @param num
     * @return
     */
    public List<BattleMapInfo> createRandomMap(int num){
        List<BattleMapInfo> results = new ArrayList<>();
        Random random = new Random(System.currentTimeMillis());
        int[] x = new int[num];
        int[] y = new int[num];

        while(true){
            //int startMapId = random.nextInt(mapConfigList.size());
            int startTileId = 2;

            TilePortalConfig portalConfig = portalConfigList.get(startTileId);
            for(int i=0;i<num;i++){
                int exitNum = 0;
                int[] exitArray = new int[portalConfig.portalDetails.length];
                for(int j=0;j<portalConfig.portalDetails.length;j++){
                    TilePortalDetail portalDetail = portalConfig.portalDetails[j];
                    if(portalDetail != null && portalDetail.type.equals("exit")){
                        exitArray[exitNum] = portalDetail.id;
                        exitNum ++ ;
                    }
                }
                if(exitNum == 0){
                    System.out.println("当前地块没有出口啊！！！ mapPath = "+portalConfig.mapPath);
                    return results;
                }

                int exitIndex = exitArray[random.nextInt(exitNum)];

                BattleMapInfo battleMapInfo = new BattleMapInfo();
                battleMapInfo.setId(portalConfig.id);
                battleMapInfo.setIndex((byte)i);
                battleMapInfo.setExit((byte)exitIndex);

                results.add(battleMapInfo);
                if(i == 0){
                    x[i] = 0;
                    y[i] = 0;
                }else{
                    int nextX = x[i-1];
                    int nextY = y[i-1];
                    if(exitIndex == 1){
                        nextY += 1;
                    }
                    if(exitIndex == 2){
                        nextX -= 1;
                    }
                    if(exitIndex == 3){
                        nextY -= 1;
                    }
                    if(exitIndex == 4){
                        nextX += 1;
                    }

                    boolean discover = false;
                    for(int k=0;k<i;k++){
                        if(x[k] == nextX && y[k] == nextY){
                            discover = true;
                        }
                    }
                    if(discover){
                        continue;
                    }

                    x[i] = nextX;
                    y[i] = nextY;
                }

                //设置下一个地块
                int nextIndex = random.nextInt(enterConfigMap.get((exitIndex+1)%4 + 1).size());
                portalConfig = enterConfigMap.get((exitIndex+1)%4 + 1).get(nextIndex);
            }

            return results;
        }

    }

}

