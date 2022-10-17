package com.dykj.rpg.battle.skill;

import com.dykj.rpg.battle.attribute.SkillAttributes;
import com.dykj.rpg.battle.basic.BasicRoleSkill;
import com.dykj.rpg.battle.role.BattleRole;
import com.dykj.rpg.protocol.battle.BattlePosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElementRoleSkill {

    private static Logger logger = LoggerFactory.getLogger("ElementRoleSkill");

//    public ElementRoleSkill(BattleRole hostRole, SkillAttributes skillAttributes, int skillSeat) {
//        super(hostRole, skillAttributes, skillSeat);
//    }

    public static boolean releaseSkill(BasicRoleSkill roleSkill,BattleRole targetRole,BattlePosition skillIndicatorPos, boolean ignoreCdTime) {

        if(roleSkill.skillType == BasicRoleSkill.SKILL_RELEASE_TYPE_ELEMENT){
            roleSkill.curElementReleaseNum ++;
            if(roleSkill.curElementReleaseNum >= roleSkill.maxElementReleaseNum){
                roleSkill.curElementReleaseNum = 0;
                roleSkill.releaseSkillHandler(roleSkill.hostRole.targetRole);
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
