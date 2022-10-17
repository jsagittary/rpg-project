package com.dykj.rpg.battle.carrier;

import com.dykj.rpg.battle.basic.BasicRoleSkill;
import com.dykj.rpg.battle.basic.BasicSkillCarrier;
import com.dykj.rpg.battle.logic.MapLogic;
import com.dykj.rpg.battle.role.BattleRole;
import com.dykj.rpg.common.config.model.SkillCharacterCarrierModel;
import com.dykj.rpg.protocol.battle.BattlePosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 载体类型--地点魔法
 */
public class SkillCarrierPositionMagic {

    private static Logger logger = LoggerFactory.getLogger("SkillCarrierPositionMagic");

    public static void update(BasicSkillCarrier skillCarrier, int frameNum) {

        if(skillCarrier.state != BasicSkillCarrier.CARRIER_STATE_DESTROY){
            if(skillCarrier.canCollision) {
                skillCarrier.collisionTest();
            }
        }
    }
}