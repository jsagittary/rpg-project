package com.dykj.rpg.battle.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapLogicPool {

    private static Logger logger = LoggerFactory.getLogger("MapLogicPool");

    private static Map<Integer, List<MapLogic>> mapLogics = new HashMap<>();

    public static MapLogic borrowMapLogic(int tileId){
        List<MapLogic> logicList = mapLogics.get(tileId);
        if(logicList == null){
            logicList = new ArrayList<>();
            mapLogics.put(tileId,logicList);
        }

        for(MapLogic mapLogic : logicList){
            if(!mapLogic.using){
                mapLogic.using = true;
                return mapLogic;
            }
        }

        MapLogic mapLogic = new MapLogic(tileId);
        mapLogic.using = true;
        logicList.add(mapLogic);
        return mapLogic;

    }

    public static void restoreMapLogic(MapLogic _mapLogic){

        if(_mapLogic == null){
            return;
        }

        List<MapLogic> logicList = mapLogics.get(_mapLogic.tileId);
        if(logicList == null){
            logger.error("restore map logic error !!! can not found the map logic with tileId = "+_mapLogic.tileId+" !!!");
            //return;
        }

        _mapLogic.using = false;
        _mapLogic.release();

    }

}
