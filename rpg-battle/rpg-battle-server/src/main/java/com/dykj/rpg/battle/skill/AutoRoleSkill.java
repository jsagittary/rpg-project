package com.dykj.rpg.battle.skill;

import com.dykj.rpg.battle.attribute.SkillAttributes;
import com.dykj.rpg.battle.basic.BasicRoleSkill;
import com.dykj.rpg.battle.logic.BattleContainer;
import com.dykj.rpg.battle.role.BattleRole;
import com.dykj.rpg.protocol.battle.BattlePosition;
import com.dykj.rpg.protocol.game2battle.EnterBattleSkillInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoRoleSkill {

    private static Logger logger = LoggerFactory.getLogger("AutoRoleSkill");

//    public AutoRoleSkill(BattleContainer battleContainer, BattleRole hostRole, SkillAttributes skillAttributes, int skillSeat) {
//        super();
//        super.init(battleContainer,hostRole,skillAttributes,skillSeat);
//    }

    public static boolean releaseSkill(BasicRoleSkill roleSkill,BattleRole targetRole,BattlePosition skillIndicatorPos,boolean ignoreCdTime){
        if(roleSkill.cdTime <= 0){

            if(roleSkill.skillType == BasicRoleSkill.SKILL_RELEASE_TYPE_AUTO){

                int skillSourceNum = roleSkill.hostRole.attributeManager.getSkillSourceNumByType(roleSkill.energyType);

                //技能资源量不足
                if(skillSourceNum < roleSkill.energyNum){

                    logger.info("skill source not enough !!! skillId["+roleSkill.skillId+"] skillSourceType["+roleSkill.energyType+"] curSourceNum["+skillSourceNum+"] needSourceNum["+roleSkill.energyNum+"]");
                    //logger.info(roleSkill.hostRole.attributeManager.getSkillSourceStrByType(roleSkill.energyType));
                    return false;
                }

                float distance = 0;
                //设置施法时的锁定目标位置，为防止丢失目标而导致的释放指示器类型的技能无法正确自动选点
                if(targetRole != null){
                    float[] hostRolePos = roleSkill.hostRole.currentPosition;
                    float[] targetRolePos = targetRole.getCurrentPosition();
                    roleSkill.targetRolePos[0] = targetRolePos[0];
                    roleSkill.targetRolePos[1] = targetRolePos[1];
                    roleSkill.targetRolePos[2] = targetRolePos[2];
                    roleSkill.targetRolePos[3] = 1;

                    distance = (float)Math.sqrt(Math.pow(targetRolePos[0]-hostRolePos[0],2)+Math.pow(targetRolePos[2]-hostRolePos[2],2));
                }else{
                    roleSkill.targetRolePos[3] = 0;
                }

                if(distance >= roleSkill.minDistance && distance <= roleSkill.maxDistance){
                    roleSkill.hostRole.attributeManager.addSkillSourceNumByType(roleSkill.energyType,-roleSkill.energyNum);

                    roleSkill.cdTime = roleSkill.maxCdTime;
                    roleSkill.releaseSkillHandler(targetRole);
                    roleSkill.hostRole.startReleaseSkillAnim(roleSkill);

                    return true;
                }

                return false;
            }
        }
        return false;
    }

    public static boolean breakSkill(BasicRoleSkill roleSkill){
        if(roleSkill.skillType == BasicRoleSkill.SKILL_RELEASE_TYPE_AUTO && roleSkill.beforeAttackTime > 0){
            roleSkill.singTime = 0;
            roleSkill.beforeAttackTime = 0;

            roleSkill.mapLogic.removeDestroySkill(roleSkill);
            return true;
        }
        return false;
    }
}
