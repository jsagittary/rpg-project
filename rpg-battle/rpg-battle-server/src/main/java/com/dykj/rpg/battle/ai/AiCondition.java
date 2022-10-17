package com.dykj.rpg.battle.ai;

import com.dykj.rpg.battle.logic.MapLogic;
import com.dykj.rpg.battle.role.BattleRole;

public class AiCondition {
    /**
     * ---------------------------------距离条件----------------------------------
     */
    //全局敌人距离触发
    public static final int AI_CONDITION_ENEMY_DISTANCE_ALL = 101;
    //锁定的角色距离触发
    public static final int AI_CONDITION_ENEMY_DISTANCE_TARGET = 102;

    /**
     * ---------------------------------血量条件----------------------------------
     */
    //自身血量
    public static final int AI_CONDITION_BLOOD_SELF = 201;
    //目标血量
    public static final int AI_CONDITION_BLOOD_TARGET = 202;

    /**
     * ---------------------------------技能条件----------------------------------
     */
    //技能ID
    public static final int AI_CONDITION_SKILL_ID = 301;
    //技能类型 备注
    public static final int AI_CONDITION_SKILL_TYPE = 302;

    public static boolean matchEnemyDistanceAll(BattleRole battleRole,int[] params){
        return false;
    }

    public static boolean matchEnemyDistanceTarget(BattleRole battleRole,int[] params){
        if(battleRole.targetRole == null){
            return false;
        }

        float minDis = params[0]/100f;
        float maxDis = params[1]/100f;

        float[] rolePos = battleRole.currentPosition;
        float[] targetPos = battleRole.targetRole.currentPosition;
        float distance = (float)Math.sqrt(Math.pow(rolePos[0]-targetPos[0],2)+Math.pow(rolePos[2]-targetPos[2],2));
        if(distance >= minDis && distance <= maxDis){
            return true;
        }

        return false;
    }


    /**
     * 自身血量
     * [血量表示类型,最小血量值,最大血量值]
     * 血量表示类型:1=数值，2=比值(50%直接填50)
     */
    public static boolean matchBloodSelf(BattleRole battleRole,int[] params){
        int curBlood = battleRole.attributeManager.curBlood;
        int type = params[0];
        int min = params[1];
        int max = params[2];
        if(type == 1){
            if(curBlood >= min && curBlood <= max){
                return true;
            }
        }
        if(type == 2){
            int maxBlood = battleRole.attributeManager.maxBlood;
            int rate = curBlood*100/maxBlood;
            if(rate >= min && rate <= max){
                return true;
            }
        }
        return false;
    }

    public static boolean matchSkillId(BattleRole battleRole,int[] params){
        int skillId = params[0];
        if(skillId == battleRole.skillManager.soulSkillId){
            return true;
        }
        return false;
    }

}
