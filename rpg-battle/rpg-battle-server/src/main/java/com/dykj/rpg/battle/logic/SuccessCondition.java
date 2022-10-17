package com.dykj.rpg.battle.logic;

import com.dykj.rpg.battle.config.LevelMapConfig;
import com.dykj.rpg.battle.constant.GameConstant;

import java.util.ArrayList;
import java.util.List;

public class SuccessCondition {

    /**
     * 在特定时间限制内完成其他胜利条件 (必需)
     */
    public final static byte SUCCESS_CONDITION_TIME_LIMIT = 1;
    /**
     * 杀光全部敌人
     */
    public final static byte SUCCESS_CONDITION_KILL_ENEMY_ALL = 2;
    /**
     * 累计击杀特定个数的敌人
     */
    public final static byte SUCCESS_CONDITION_KILL_ENEMY_NUM = 3;
    /**
     * 杀死某个特定敌人
     */
    public final static byte SUCCESS_CONDITION_KILL_ENEMY_SPECIAL = 4;

    public byte conditionType;

    public boolean clientShow;

    public List<Integer> conditionParams = new ArrayList<>();

    public int maxProgress;

    public int curProgress;

    public boolean result = false;

    public SuccessCondition(LevelMapConfig mapConfig,List<Integer> condition){
        if(condition.size() > 0){
            conditionType = Byte.parseByte(condition.get(0)+"");
        }

        if(condition.size() > 1){
            clientShow = condition.get(1) == 1;
        }

        if(condition.size() > 2){
            for(int i=2;i<condition.size();i++){
                conditionParams.add(condition.get(i));
            }
        }

        if(conditionType == SUCCESS_CONDITION_TIME_LIMIT){
            if(conditionParams.size()==0){
                maxProgress = GameConstant.SINGLE_BATTLE_MAX_TIME;
            }else{
                maxProgress = conditionParams.get(0)*1000;
            }
            curProgress = 0;
        }

        if(conditionType == SUCCESS_CONDITION_KILL_ENEMY_ALL){
            maxProgress = mapConfig.totalNpcNums;
            curProgress = 0;
        }

        if(conditionType == SUCCESS_CONDITION_KILL_ENEMY_NUM){
            maxProgress = conditionParams.get(0);
            curProgress = 0;
        }

    }

    /**
     * 创建一个默认的时间条件
     */
    public SuccessCondition(byte conditionType,LevelMapConfig mapConfig,int param){
        this.conditionType = conditionType;
        if(conditionType == SUCCESS_CONDITION_TIME_LIMIT){
            clientShow = false;
            maxProgress = param;
            curProgress = 0;
        }

        if(conditionType == SUCCESS_CONDITION_KILL_ENEMY_ALL){
            clientShow = true;
            maxProgress = mapConfig.totalNpcNums;
            curProgress = 0;
        }
    }

    public void addTimeInfo(int time){
        if(conditionType == SUCCESS_CONDITION_TIME_LIMIT){
            curProgress += time;
        }
    }

    public void addKillInfo(int npcId){
        if(conditionType == SUCCESS_CONDITION_KILL_ENEMY_ALL){
            curProgress ++;
            if(!result && curProgress >= maxProgress){
                result = true;
            }
        }
        if(conditionType == SUCCESS_CONDITION_KILL_ENEMY_NUM){
            curProgress ++;
            if(!result && curProgress >= maxProgress){
                result = true;
            }
        }
        if(conditionType == SUCCESS_CONDITION_KILL_ENEMY_SPECIAL){
            if(!result && npcId == conditionParams.get(0)){
                result = true;
            }
        }
    }


}
