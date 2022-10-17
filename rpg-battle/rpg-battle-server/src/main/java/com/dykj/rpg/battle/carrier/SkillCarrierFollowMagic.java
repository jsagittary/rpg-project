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
 * 载体类型--跟随魔法
 */
public class SkillCarrierFollowMagic {

    private static Logger logger = LoggerFactory.getLogger("SkillCarrierFollowMagic");

//    public SkillCarrierFollowMagic(MapLogic mapLogic, BasicRoleSkill hostSkill, SkillCharacterCarrierModel carrierModel, float[] startPosition, short direction, int activationTime, int durationTime, BattlePosition skillIndicatorPos, BattleRole triggerRole, BattleRole targetRole) {
//        super(mapLogic, hostSkill, carrierModel, startPosition, direction,activationTime,durationTime, skillIndicatorPos,triggerRole);
//
//        this.targetRole = targetRole;
//
//        this.clientShow = true;
//    }

    public static void update(BasicSkillCarrier skillCarrier,int frameNum) {

        if(skillCarrier.state != BasicSkillCarrier.CARRIER_STATE_DESTROY){
            if(skillCarrier.canCollision){
                skillCarrier.collisionTest();
            }
        }

        if(skillCarrier.state != BasicSkillCarrier.CARRIER_STATE_DESTROY){
            if(skillCarrier.targetRole!=null){
                float[] targetPos = skillCarrier.targetRole.getCurrentPosition();
                skillCarrier.setCurrentPosition(targetPos[0],targetPos[2]);
            }
        }
    }
}