package com.dykj.rpg.battle.carrier;

import com.dykj.rpg.battle.basic.BasicRoleSkill;
import com.dykj.rpg.battle.basic.BasicSkillCarrier;
import com.dykj.rpg.battle.constant.GameConstant;
import com.dykj.rpg.battle.detour.DetourHandler;
import com.dykj.rpg.battle.logic.MapLogic;
import com.dykj.rpg.battle.role.BattleRole;
import com.dykj.rpg.common.config.model.SkillCharacterCarrierModel;
import com.dykj.rpg.protocol.battle.BattlePosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 载体类型--地点位移
 */
public class SkillCarrierDirectionMove {

    private static Logger logger = LoggerFactory.getLogger("SkillCarrierDirectionMove");

//    public SkillCarrierDirectionMove(MapLogic mapLogic, BasicRoleSkill hostSkill, SkillCharacterCarrierModel carrierModel, float[] startPosition, short direction, int activationTime, int durationTime, BattlePosition skillIndicatorPos, BattleRole triggerRole, short targetDirection) {
//        super.init(mapLogic, hostSkill, carrierModel, startPosition, direction,activationTime,durationTime,skillIndicatorPos,triggerRole);
//
//        this.triggerRole = triggerRole;
//        this.direction = targetDirection;
//
//        this.clientShow = false;
//    }


    public static void update(BasicSkillCarrier skillCarrier, int frameNum) {

        if(skillCarrier.state != BasicSkillCarrier.CARRIER_STATE_DESTROY){

            if(skillCarrier.triggerRole != null && skillCarrier.direction >= 0){

                if(skillCarrier.moveSpeed > 0){
                    skillCarrier.setPrePosition();

                    float moveX = (float)Math.cos(skillCarrier.direction*Math.PI/180)*skillCarrier.moveSpeed/ GameConstant.GAME_FRAME;
                    float moveZ = (float)Math.sin(skillCarrier.direction*Math.PI/180)*skillCarrier.moveSpeed/ GameConstant.GAME_FRAME;

                    float posX = skillCarrier.currentPosition[0] + moveX;
                    float posY = skillCarrier.currentPosition[2] + moveZ;

                    //当移动的坐标点在可行走区域上时才有效，否则结束载体的生命周期\
                    if(skillCarrier.mapLogic.isPointOnMesh(posX,posY)){
                    //if(mapLogic.getPositionYOnMap(newPos[0],newPos[2]) != DetourHandler.invalidPos){
                        skillCarrier.setCurrentPosition(posX,posY);

                        //只改变受影响的对象的位置，不改变其朝向
                        skillCarrier.triggerRole.setCurrentPosition(posX,posY);

                        skillCarrier.triggerRole.setStillState(GameConstant.FRAME_TIME*2);
                    }else{
                        //设置载体的生命时间为0，在下一帧运行时
                        skillCarrier.durationTime = 0;
                    }

                }

            }
        }
    }
}