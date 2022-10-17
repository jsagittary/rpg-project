package com.dykj.rpg.battle.ai;

import com.dykj.rpg.battle.basic.BasicRoleSkill;
import com.dykj.rpg.battle.logic.BattleContainer;
import com.dykj.rpg.battle.role.BattlePlayer;
import com.dykj.rpg.battle.role.BattleRole;
import com.dykj.rpg.battle.util.CalculationUtil;

public class AiAction {

    /**
     * ------------------------------移动类行为------------------------------
     */
    //向自己背后移动
    public static final int AI_ACTION_MOVE_BACK = 101;

    /**
     * -------------------------------释放技能类行为------------------------------
     */
    //给自己释放加血技能[喝生命药水]
    public static final int AI_ACTION_ADD_BLOOD_BY_POTION = 201;

    /**
     * -------------------------------寻找目标类行为------------------------------
     */
    //主角寻找最近的敌人
    public static final int AI_ACTION_FIND_TARGET_DISTANCE_NEARST = 301;
    //灵魂之影寻找最近的敌人
    public static final int AI_ACTION_SOUL_FIND_TARGET_DISTANCE_NEARST = 302;
    //主角寻找血量最少的敌人
    public static final int AI_ACTION_FIND_TARGET_BLOOD_LEAST = 303;
    //灵魂之影寻找血量最少的敌人
    public static final int AI_ACTION_SOUL_FIND_TARGET_BLOOD_LEAST = 304;

    public static boolean moveBack(BattleContainer battleContainer, BattleRole battleRole,int[] params){
        if(battleRole.targetRole != null){

            float[] playerPos = battleRole.currentPosition;
            float[] targetPos = battleRole.targetRole.currentPosition;
            float dir = CalculationUtil.vectorToAngle(playerPos[0]-targetPos[0],playerPos[2]-targetPos[0]);
            float distance = params[0]/100f;

            float targetX = playerPos[0]+(float)Math.cos(dir)*distance;
            float targetZ = playerPos[2]+(float)Math.sin(dir)*distance;

            ((BattlePlayer)battleRole).insertNextPosition(targetX,targetZ);

            return true;
        }
        return false;
    }

    public static boolean addBloodByPotion(BattleContainer battleContainer, BattleRole battleRole,int[] params){
        BasicRoleSkill roleSkill = battleRole.skillManager.getPotionSkillByPotionId(65001);
        if(roleSkill != null && roleSkill.potionNum > 0){
            battleContainer.waitHandlerDataManager.addUsePotionToWaitMap(battleRole.getPlayerId(),65001,true);
            return true;
        }
        return false;
    }

    public static boolean soulFindTargetBloodLeast(BattleContainer battleContainer, BattleRole battleRole,int[] params){
        BattleRole targetRole = battleRole.mapLogic.findBloodLeastEnemy();
        if(targetRole != null){
            battleRole.skillManager.setSoulTargetRole(targetRole);
            return true;
        }
        return false;
    }

}
