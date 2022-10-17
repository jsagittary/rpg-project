package com.dykj.rpg.game.ice.client;

import java.util.HashMap;
import java.util.Map;

public class BattleClientManager {

    private static BattleClientManager instance;

    private Map<Integer,BattleClient> battleClients;

    private BattleClientManager(){
        battleClients = new HashMap<>();
    }

    public static BattleClientManager getInstance() {
        if(instance == null){
            instance = new BattleClientManager();
        }
        return instance;
    }
}
