package com.dykj.rpg.battle.manager;

import com.alibaba.fastjson.support.odps.udf.CodecCheck;
import com.dykj.rpg.battle.config.LevelMapConfig;
import com.dykj.rpg.battle.constant.BattleTypeConstant;
import com.dykj.rpg.battle.constant.GameConstant;
import com.dykj.rpg.battle.detour.MapLoader;
import com.dykj.rpg.battle.logic.SuccessCondition;
import com.dykj.rpg.common.config.model.MisBasicModel;
import com.dykj.rpg.net.protocol.ProtocolPool;
import com.dykj.rpg.protocol.battle.SuccessConditionInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SuccessConditionManager {

    public final static byte SUCCESS_CONDITION_RESULT_CONTINUE = 0;

    public final static byte SUCCESS_CONDITION_RESULT_FAIL = 1;

    public final static byte SUCCESS_CONDITION_RESULT_SUCCESS = 2;

    private List<SuccessCondition> successConditions = new ArrayList<>(10);

    private SuccessCondition timeCondition;

    private List<SuccessCondition> killConditions = new ArrayList<>(10);

    private int killConditionSize = 0;

    public SuccessConditionManager(){

    }

    public void init(byte battleType, int levelId){
        LevelMapConfig mapConfig = MapLoader.getInstance().getLevelMapConfigByLevelId(levelId);
        if(levelId != 0){
            MapLoader.getInstance().getBattleSuccessConditionByLevelId(levelId,successConditions);

            for(SuccessCondition successCondition : successConditions){
                if(successCondition.conditionType == SuccessCondition.SUCCESS_CONDITION_TIME_LIMIT){
                    timeCondition = successCondition;
                }
                if(successCondition.conditionType == SuccessCondition.SUCCESS_CONDITION_KILL_ENEMY_ALL ||
                        successCondition.conditionType == SuccessCondition.SUCCESS_CONDITION_KILL_ENEMY_NUM ||
                        successCondition.conditionType == SuccessCondition.SUCCESS_CONDITION_KILL_ENEMY_SPECIAL){
                    killConditions.add(successCondition);
                }
            }

            killConditionSize = killConditions.size();

            if(timeCondition == null){
                timeCondition = new SuccessCondition(SuccessCondition.SUCCESS_CONDITION_TIME_LIMIT,mapConfig,GameConstant.SINGLE_BATTLE_MAX_TIME);
            }

            return;
        }

        //测试关卡
        String conditionStr = GameConstant.TEST_MAP_CONDITION;
        String[] conditions = conditionStr.trim().split(",");

        List<List<Integer>> conditionList = new ArrayList<>();
        for(String conditionParams : conditions){
            List<Integer> list = new ArrayList<>();
            String[] params = conditionParams.trim().split(":");
            for(String param : params){
                list.add(Integer.parseInt(param.trim()));
            }
            conditionList.add(list);
        }
        if(conditionList != null && conditionList.size() > 0){
            for(List<Integer> condition : conditionList){
                SuccessCondition successCondition = new SuccessCondition(mapConfig,condition);

                if(successCondition.conditionType == SuccessCondition.SUCCESS_CONDITION_TIME_LIMIT){
                    timeCondition = successCondition;
                }
                if(successCondition.conditionType == SuccessCondition.SUCCESS_CONDITION_KILL_ENEMY_ALL ||
                        successCondition.conditionType == SuccessCondition.SUCCESS_CONDITION_KILL_ENEMY_NUM ||
                        successCondition.conditionType == SuccessCondition.SUCCESS_CONDITION_KILL_ENEMY_SPECIAL){
                    killConditions.add(successCondition);
                }
            }
        }

        killConditionSize = killConditions.size();

        if(timeCondition == null){
            timeCondition = new SuccessCondition(SuccessCondition.SUCCESS_CONDITION_TIME_LIMIT,mapConfig,GameConstant.SINGLE_BATTLE_MAX_TIME);
        }

    }

    public void release(){
        successConditions.clear();
        timeCondition = null;
        killConditions.clear();
    }

    public List<SuccessConditionInfo> getShowConditions(){
        List<SuccessConditionInfo> list = new ArrayList<>();
        if(timeCondition.clientShow){
            SuccessConditionInfo conditionInfo = (SuccessConditionInfo) ProtocolPool.getInstance().borrowProtocol(SuccessConditionInfo.code);
            conditionInfo.setConditionType(timeCondition.conditionType);
            conditionInfo.setConditionParams(timeCondition.conditionParams);
            List<Integer> list2 = conditionInfo.getProgress();
            list2.add(timeCondition.curProgress);
            list2.add(timeCondition.maxProgress);
            //conditionInfo.setProgress(Arrays.asList(timeCondition.curProgress,timeCondition.maxProgress));
            conditionInfo.setResult(timeCondition.result);
            list.add(conditionInfo);
        }

        for(SuccessCondition condition : killConditions){
            if(condition.clientShow){
                SuccessConditionInfo conditionInfo = (SuccessConditionInfo) ProtocolPool.getInstance().borrowProtocol(SuccessConditionInfo.code);
                conditionInfo.setConditionType(condition.conditionType);
                conditionInfo.setConditionParams(condition.conditionParams);
                List<Integer> list2 = conditionInfo.getProgress();
                list2.add(condition.curProgress);
                list2.add(condition.maxProgress);
                //conditionInfo.setProgress(Arrays.asList(condition.curProgress,condition.maxProgress));
                conditionInfo.setResult(condition.result);
                list.add(conditionInfo);
            }
        }

        return list;
    }

    /**
     * 添加时间，返回战斗结果 0=战斗继续，1=战斗失败，2=战斗胜利
     * @return
     */
    public byte addTimeInfo(){
        timeCondition.addTimeInfo(GameConstant.FRAME_TIME);

        boolean result = true;
        for(SuccessCondition successCondition : killConditions){
            if(!successCondition.result){
                result = false;
                break;
            }
        }

        if(timeCondition.curProgress < timeCondition.maxProgress){
            if(killConditions.size() == 0 || !result){
                return SUCCESS_CONDITION_RESULT_CONTINUE;
            }

            return SUCCESS_CONDITION_RESULT_SUCCESS;

        }

        if(killConditions.size() == 0){ //存活类型，时间到了直接判断战斗胜利
            return SUCCESS_CONDITION_RESULT_SUCCESS;
        }

        if(result){
            return SUCCESS_CONDITION_RESULT_SUCCESS;
        }

        return SUCCESS_CONDITION_RESULT_FAIL;

    }

    /**
     * 添加击杀数量
     * @return
     */
    public void addKillInfo(int npcId){
        if(killConditionSize > 0){
            for(SuccessCondition successCondition : killConditions){
                successCondition.addKillInfo(npcId);
            }
        }
    }

    /**
     * 获取战斗剩余时间
     */
    public int getLastBattleTime(){
        return timeCondition.maxProgress-timeCondition.curProgress;
    }

}
