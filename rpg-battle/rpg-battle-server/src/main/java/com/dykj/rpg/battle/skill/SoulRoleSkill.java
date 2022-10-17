package com.dykj.rpg.battle.skill;

import com.dykj.rpg.battle.attribute.SkillAttributes;
import com.dykj.rpg.battle.basic.BasicRoleSkill;
import com.dykj.rpg.battle.dictionary.StaticDictionary;
import com.dykj.rpg.battle.role.BattleRole;
import com.dykj.rpg.battle.util.CalculationUtil;
import com.dykj.rpg.common.config.model.SkillCharacterBasicModel;
import com.dykj.rpg.protocol.battle.BattlePosition;
import com.dykj.rpg.protocol.battle.SoulRoleBorn;
import com.dykj.rpg.protocol.battle.SoulRoleInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class SoulRoleSkill extends BasicRoleSkill {

    //private static final int MAX_FIND_SOUL_TIME = 6;

    private static Logger logger = LoggerFactory.getLogger("SoulRoleSkill");

    public static boolean releaseSkill(BasicRoleSkill roleSkill, BattleRole targetRole,BattlePosition skillIndicatorPos,boolean ignoreCdTime) {
        if (roleSkill.cdTime <= 0) {
            if (roleSkill.soulRolePos == null) {
                return false;
            }
            //设置施法时的锁定目标位置，为防止丢失目标而导致的释放指示器类型的技能无法正确自动选点
            if(roleSkill.hostRole.targetRole != null){
                float[] pos = roleSkill.hostRole.targetRole.getCurrentPosition();
                roleSkill.targetRolePos[0] = pos[0];
                roleSkill.targetRolePos[1] = pos[1];
                roleSkill.targetRolePos[2] = pos[2];
                roleSkill.targetRolePos[3] = 1;
            }else{
                roleSkill.targetRolePos[3] = 0;
            }
            roleSkill.cdTime = roleSkill.maxCdTime;
            //logger.info("释放灵魂之影技能 skillId[{}]   目标位置{}  ",roleSkill.skillId,roleSkill.targetRolePos==null?"丢失目标": Arrays.toString(roleSkill.targetRolePos));

            roleSkill.releaseSkillHandler(targetRole);
            return true;
        }
        return false;
    }

}