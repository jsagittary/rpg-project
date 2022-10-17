package com.dykj.rpg.battle.ai;

import com.dykj.rpg.battle.constant.GameConstant;
import com.dykj.rpg.battle.logic.BattleContainer;
import com.dykj.rpg.battle.logic.BattleObject;
import com.dykj.rpg.battle.role.BattleRole;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 基础ai逻辑
 */
public class BasicAiLogic extends BattleObject {

    private final static int CONDITION_PARAMS_SIZE = 10;

    private final static int ACTION_PARAMS_SIZE = 10;

    private BattleRole battleRole;

    private BattleContainer battleContainer;

    public int aiId;

    private int triggerType ;

    private int conditionId;

    private int[] conditionParams = new int[CONDITION_PARAMS_SIZE];

    private int actionId;

    private int[] actionParams = new int[ACTION_PARAMS_SIZE];

    public int maxCdTime;

    public int curCdTime;

    public byte state;

    public BasicAiLogic(){

    }

    public void init(BattleContainer battleContainer,BattleRole battleRole,int aiId,int _triggerType,int _conditionId,List<Integer> _conditionParams,int _actionId,List<Integer> _actionParams,int cdTime){

        this.battleContainer = battleContainer;
        this.battleRole = battleRole;

        this.aiId = aiId;
        this.triggerType = _triggerType;

        this.conditionId = _conditionId;
        if(_conditionParams != null){
            for(int i=0;i<_conditionParams.size();i++){
                this.conditionParams[i] = _conditionParams.get(i);
            }
        }

        this.actionId = _actionId;
        if(_actionParams != null){
            for(int i=0;i<_actionParams.size();i++){
                this.actionParams[i] = _actionParams.get(i);
            }
        }

        this.maxCdTime = cdTime/10;
        this.curCdTime = 0;

    }


    @Override
    public void release() {
        triggerType = 0;

        conditionId = 0;

        for(int i = 0;i<CONDITION_PARAMS_SIZE;i++){
            conditionParams[i] = 0;
        }

        actionId = 0;

        for(int i = 0;i<ACTION_PARAMS_SIZE;i++){
            actionParams[i] = 0;
        }

        maxCdTime = 100;
        curCdTime = 0;
    }

    @Override
    public void selfCheak() {

    }

    public void update(){
        if(curCdTime > 0){
            curCdTime -= GameConstant.FRAME_TIME;
        }
    }

    /**
     * 触发判断
     * @return
     */
    public boolean trigger(){
        if(curCdTime <= 0){
            curCdTime = 1000; //添加1s判断间隔期
            return true;
        }

        return false;
    }

    /**
     * 条件判断
     * @return
     */
    public boolean matchCondition() {

        if(conditionId == AiCondition.AI_CONDITION_ENEMY_DISTANCE_ALL){
            return true;
        }

        if(conditionId == AiCondition.AI_CONDITION_ENEMY_DISTANCE_TARGET){
            return AiCondition.matchEnemyDistanceTarget(battleRole,conditionParams);
        }

        if(conditionId == AiCondition.AI_CONDITION_BLOOD_SELF){
            return AiCondition.matchBloodSelf(battleRole,conditionParams);
        }

        if(conditionId == AiCondition.AI_CONDITION_BLOOD_TARGET){
            return false;
        }

        if(conditionId == AiCondition.AI_CONDITION_SKILL_ID){
            return AiCondition.matchSkillId(battleRole,conditionParams);
        }

        return false;
    }

    /**
     * 触发行为
     * @return
     */
    public boolean action() {

        if(actionId == AiAction.AI_ACTION_MOVE_BACK){
            return AiAction.moveBack(battleContainer,battleRole,actionParams);
        }

        if(actionId == AiAction.AI_ACTION_ADD_BLOOD_BY_POTION){
            return AiAction.addBloodByPotion(battleContainer,battleRole,actionParams);
        }

        if(actionId == AiAction.AI_ACTION_SOUL_FIND_TARGET_BLOOD_LEAST){
            return AiAction.soulFindTargetBloodLeast(battleContainer,battleRole,actionParams);
        }

        return false;
    }

}
