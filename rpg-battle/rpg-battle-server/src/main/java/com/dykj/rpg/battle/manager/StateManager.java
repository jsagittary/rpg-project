package com.dykj.rpg.battle.manager;

import com.dykj.rpg.battle.constant.*;
import com.dykj.rpg.battle.role.BattleRole;
import com.dykj.rpg.common.attribute.consts.AttributeBasicConstant;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 角色状态管理器 主要包含角色行为目标和行动限制状态
 */
public class StateManager {

    private BattleRole battleRole;

    /**
     * 角色的基础状态ID (被动状态)
     */
    private byte basicStateId = 0;
    /**
     * 角色的基础状态持续时间
     */
    private int basicStateTime = 0;
    /**
     * 角色行为目的 (主动状态)
     */
    private byte purpos = RolePurposConstant.ROLE_PURPOS_NULL;

    /**
     * 角色的buff状态列表 key=状态ID value=状态剩余时间
     */
    private Map<Integer,Integer> buffStateMap;

    private int printDataModelId = 2;

    public StateManager(BattleRole battleRole){
        this.battleRole = battleRole;
        buffStateMap = new ConcurrentHashMap<>();
    }

    public void init(){
    }

    public void release(){
        basicStateId = 0;
        basicStateTime = 0;
        purpos = RolePurposConstant.ROLE_PURPOS_NULL;

        buffStateMap.clear();
    }

    public boolean addBasicState(byte stateId,int stateTime){

//        if(battleRole.getModelId() == printDataModelId){
//            System.out.println("addBasicState modelId = "+printDataModelId+" stateId = "+stateId+" stateTime = "+stateTime+" time = "+System.currentTimeMillis());
//        }

        if(basicStateId != RoleStateConstant.ROLE_STATE_DEATH){
            //死亡优先级最高
            if(stateId == RoleStateConstant.ROLE_STATE_DEATH){
                basicStateId = RoleStateConstant.ROLE_STATE_DEATH;
                basicStateTime = 0;
                purpos = RolePurposConstant.ROLE_PURPOS_NULL;
                battleRole.setTargetNull();
                return true;
            }

            //恢复自由身优先级次之
            if(stateId == RoleStateConstant.ROLE_STATE_FREE){
                basicStateId = RoleStateConstant.ROLE_STATE_FREE;
                basicStateTime = 0;
                purpos = RolePurposConstant.ROLE_PURPOS_NULL;
                battleRole.setTargetNull();
                return true;
            }

            if(stateTime == 0){
                //basicStateId = RoleStateConstant.ROLE_STATE_FREE;
                return true;
            }

            if(basicStateId == stateId){
                basicStateTime = Math.max(this.basicStateTime,stateTime);
                purpos = RolePurposConstant.ROLE_PURPOS_NULL;
                battleRole.setTargetNull();
                return true;
            }else{
                basicStateId = stateId;
                basicStateTime = stateTime;
                purpos = RolePurposConstant.ROLE_PURPOS_NULL;
                battleRole.setTargetNull();
                return true;
            }
        }else{
            return false;
        }
    }

    public void addBuffState(int stateId,int time){
        buffStateMap.put(stateId,time);
    }

    public void update(int frameNum){
        /**
         * 角色状态持续时间刷新
         */
        if(basicStateId != RoleStateConstant.ROLE_STATE_DEATH){
            if(basicStateTime > 0){
                basicStateTime -= GameConstant.FRAME_TIME;
                if(basicStateTime <= 0){
                    basicStateTime = 0;
                    basicStateId = RoleStateConstant.ROLE_STATE_FREE;
                }
            }
        }

        if(buffStateMap.size() > 0){
            Set<Integer> set = buffStateMap.keySet();
            for(int key : set){
                int lastTime = buffStateMap.get(key) - GameConstant.FRAME_TIME;
                if(lastTime <= 0){
                    buffStateMap.remove(key);
                }else{
                    buffStateMap.put(key,lastTime);
                }
            }
        }

//        if(battleRole.getModelId() == printDataModelId){
//            System.out.println("updateBasicState modelId = "+printDataModelId+" stateId = "+basicStateId+" stateTime = "+basicStateTime+" time = "+System.currentTimeMillis());
//        }

    }


    public byte getState(){
        return basicStateId;
    }

    public void addPurpos(byte _purpos){
        if(basicStateId == RoleStateConstant.ROLE_STATE_FREE){
            purpos = _purpos;
        }
    }

    public byte getPurpos(){
        return purpos;
    }

    public float getMoveSpeed(){
        if(basicStateId == RoleStateConstant.ROLE_STATE_FREE && (purpos == RolePurposConstant.ROLE_PURPOS_FIND_POSITION || purpos == RolePurposConstant.ROLE_PURPOS_FIND_OBJECT || purpos == RolePurposConstant.ROLE_PURPOS_FIND_EXIT)){
            return (battleRole.getBuffManager().getAttributeFromBuff(AttributeBasicConstant.YI_DONG_SU_DU_PAO_ZHI,0))/10000f;
        }else{
            return 0;
        }
    }

    public float getDetourMoveSpeed(){
        if(battleRole.getRoleType() != RoleTypeConstant.BATTLE_ROLE_PLAYER && basicStateId == RoleStateConstant.ROLE_STATE_FREE && (purpos == RolePurposConstant.ROLE_PURPOS_FIND_POSITION || purpos == RolePurposConstant.ROLE_PURPOS_FIND_OBJECT)){
            return (battleRole.getBuffManager().getAttributeFromBuff(AttributeBasicConstant.YI_DONG_SU_DU_PAO_ZHI,0))/10000f;
        }else{
            return 0;
        }
    }

    /**
     * 判断玩家是否可以正常行动
     * @return
     */
    public boolean canActionFree(){
        if(basicStateId == RoleStateConstant.ROLE_STATE_FREE){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 判断玩家是否需要从新寻找目标
     * @return
     */
    public boolean needFindNewTarget(){
        if(basicStateId == RoleStateConstant.ROLE_STATE_FREE && purpos == RolePurposConstant.ROLE_PURPOS_NULL){
            return true;
        }
        return false;
    }

    /**
     * 判断是否处于移动状态
     * @return
     */
    public boolean isMoveToTarget(){
        if(purpos == RolePurposConstant.ROLE_PURPOS_FIND_OBJECT ||
                purpos == RolePurposConstant.ROLE_PURPOS_FIND_POSITION ||
                purpos == RolePurposConstant.ROLE_PURPOS_FIND_EXIT){
            return true;
        }
        return false;
    }

}
