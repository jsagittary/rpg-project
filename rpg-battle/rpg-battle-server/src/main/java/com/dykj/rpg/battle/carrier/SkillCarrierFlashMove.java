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
 * 载体类型--瞬间移动
 */
public class SkillCarrierFlashMove{

    private static Logger logger = LoggerFactory.getLogger("SkillCarrierFlashMove");

    //private boolean hasFlash = false;

//    public void init(MapLogic mapLogic, BasicRoleSkill hostSkill, SkillCharacterCarrierModel carrierModel, float[] startPosition, short direction, int activationTime, int durationTime, BattlePosition skillIndicatorPos, BattleRole triggerRole, float[] targetPosition) {
//        super.init(mapLogic, hostSkill, carrierModel, startPosition, direction,activationTime,durationTime,skillIndicatorPos,triggerRole);
//
//        this.triggerRole = triggerRole;
//        this.targetPosition = targetPosition;
//
//        triggerRole.setFrozenState(durationTime*2);
//
//        this.clientShow = false;
//
//    }

    public static void update(BasicSkillCarrier skillCarrier,int frameNum) {

        //创建载体使角色不可见
        if(skillCarrier.state != BasicSkillCarrier.CARRIER_STATE_DESTROY){

            if(skillCarrier.triggerRole != null && skillCarrier.targetPosition != null){
                skillCarrier.triggerRole.setHideState(skillCarrier.durationTime*2);
            }
        }

        //销毁载体时将角色移动到指定地点
        if(skillCarrier.state == BasicSkillCarrier.CARRIER_STATE_DESTROY){

            if(skillCarrier.triggerRole != null && skillCarrier.targetPosition != null){
                System.out.println("carrier flash move");
                skillCarrier.triggerRole.setCurrentPosition(skillCarrier.targetPosition[0],skillCarrier.targetPosition[2]);
                skillCarrier.triggerRole.setStillState(500);
                skillCarrier.triggerRole.getMapLogic().setAgentPosition(skillCarrier.triggerRole.getCaIdx(), skillCarrier.triggerRole.getCurrentPosition());
            }
        }
    }
}