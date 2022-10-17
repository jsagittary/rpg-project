package com.dykj.rpg.battle.skill;

import com.dykj.rpg.battle.basic.BasicRoleSkill;
import com.dykj.rpg.battle.role.BattleRole;
import com.dykj.rpg.protocol.battle.BattlePosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PotionRoleSkill {

    private static Logger logger = LoggerFactory.getLogger("PotionRoleSkill");

    public static boolean releaseSkill(BasicRoleSkill roleSkill, BattleRole targetRole, BattlePosition skillIndicatorPos, boolean ignoreCdTime) {

        if(roleSkill.skillType == BasicRoleSkill.SKILL_RELEASE_TYPE_POTION){

            if(roleSkill.potionNum <= 0){
                return false;
            }

            if(ignoreCdTime){
                roleSkill.potionNum--;
                roleSkill.releaseSkillHandler(null);
                roleSkill.hostRole.startReleaseSkillAnim(roleSkill);
                return true;
            }

            if(roleSkill.cdTime <= 0){
                roleSkill.potionNum--;
                roleSkill.cdTime = roleSkill.maxCdTime;
                roleSkill.releaseSkillHandler(null);
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
