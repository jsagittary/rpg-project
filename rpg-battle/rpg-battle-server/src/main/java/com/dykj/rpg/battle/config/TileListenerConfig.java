package com.dykj.rpg.battle.config;

import java.util.ArrayList;
import java.util.List;

public class TileListenerConfig {

    public final static byte TRIGGER_TYPE_AND = 1;

    public final static byte TRIGGER_TYPE_OR = 2;

    public List<Integer> effects;

    public String typeStr;

    public int type;

    public List<TileListenerCondition> listenerConditions;

    public TileListenerConfig(List<Integer> effects, String type) {
        this.effects = effects;
        this.typeStr = type;
        listenerConditions = new ArrayList<>();

        if(type.equals("and")){
            this.type = TRIGGER_TYPE_AND;
        }
        if(type.equals("or")){
            this.type = TRIGGER_TYPE_OR;
        }
    }

    public void addListenerCondition(TileListenerCondition listenerCondition){
        listenerConditions.add(listenerCondition);
    }

    public TileListenerConfig copy(){
        TileListenerConfig triggerConfig = new TileListenerConfig(effects,typeStr);
        for(int i=0;i<listenerConditions.size();i++){
            triggerConfig.addListenerCondition(listenerConditions.get(i).copy());
        }
        return triggerConfig;
    }

}
