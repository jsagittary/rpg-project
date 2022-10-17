package com.dykj.rpg.battle.skill;

import com.dykj.rpg.battle.attribute.SkillAttributes;
import com.dykj.rpg.battle.basic.BasicRoleSkill;
import com.dykj.rpg.battle.role.BattleRole;
import com.dykj.rpg.protocol.battle.BattlePosition;
import com.dykj.rpg.protocol.game2battle.EnterBattleSkillInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManualRoleSkill {

    private static Logger logger = LoggerFactory.getLogger("ManualRoleSkill");

//    public ManualRoleSkill(BattleRole hostRole, SkillAttributes skillAttributes, int skillSeat) {
//        super(hostRole, skillAttributes, skillSeat);
//    }

    public static boolean releaseSkill(BasicRoleSkill roleSkill,BattleRole targetRole,BattlePosition skillIndicatorPos,boolean ignoreCdTime) {

        if(roleSkill.cdTime <= 0){
            if(roleSkill.skillType == BasicRoleSkill.SKILL_RELEASE_TYPE_MANUAL){
                //做一次自动技能的打断
                roleSkill.getHostRole().skillManager.breakAutoReleaseSkill();

                int skillSourceNum = roleSkill.hostRole.attributeManager.getSkillSourceNumByType(roleSkill.energyType);

                if(skillSourceNum < roleSkill.energyNum){
                    //足够
                    logger.info("skill source not enough !!! skillId["+roleSkill.skillId+"] skillSourceType["+roleSkill.energyType+"] curSourceNum["+skillSourceNum+"] needSourceNum["+roleSkill.energyNum+"]");
                    logger.info(roleSkill.hostRole.attributeManager.getSkillSourceStrByType(roleSkill.energyType));
                    return false;
                }

                if(!roleSkill.ignoreIndicatorPos){
                    roleSkill.skillIndicatorPos = skillIndicatorPos;
                }

                //设置施法时的锁定目标位置，为防止丢失目标而导致的释放指示器类型的技能无法正确自动选点
                if(targetRole != null){
                    float[] pos = targetRole.getCurrentPosition();

                    roleSkill.targetRolePos[0] = pos[0];
                    roleSkill.targetRolePos[1] = pos[1];
                    roleSkill.targetRolePos[2] = pos[2];
                    roleSkill.targetRolePos[3] = 1;
                }else{
                    roleSkill.targetRolePos[3] = 0;
                }

                roleSkill.hostRole.attributeManager.addSkillSourceNumByType(roleSkill.skillBasicModel.getEnergyType(),-roleSkill.energyNum);

                roleSkill.cdTime = roleSkill.maxCdTime;
                roleSkill.releaseSkillHandler(targetRole);
                roleSkill.hostRole.startReleaseSkillAnim(roleSkill);
                return true;
            }
        }

        return false;
    }

    public static boolean breakSkill() {
        return false;
    }
}
