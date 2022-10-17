package com.dykj.rpg.battle.carrier;

import com.dykj.rpg.battle.basic.BasicRoleSkill;
import com.dykj.rpg.battle.basic.BasicSkillCarrier;
import com.dykj.rpg.battle.constant.GameConstant;
import com.dykj.rpg.battle.detour.DetourHandler;
import com.dykj.rpg.battle.logic.MapLogic;
import com.dykj.rpg.battle.role.BattleRole;
import com.dykj.rpg.battle.util.CalculationUtil;
import com.dykj.rpg.common.config.model.SkillCharacterCarrierModel;
import com.dykj.rpg.protocol.battle.BattlePosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;


/**
 * 载体类型--地点位移
 */
public class SkillCarrierTargetMove{

    private static Logger logger = LoggerFactory.getLogger("SkillCarrierTargetMove");

    public static void update(BasicSkillCarrier skillCarrier, int frameNum) {

        if(skillCarrier.state != BasicSkillCarrier.CARRIER_STATE_DESTROY){
            float targetX = 0;
            float targetZ = 0;
			if(skillCarrier.triggerRole == null){
				return;
			}
            if(skillCarrier.targetRole != null){
                targetX = skillCarrier.targetRole.getCurrentPosition()[0];
                targetZ = skillCarrier.targetRole.getCurrentPosition()[2];
            }else{
                if(skillCarrier.targetCarrier != null){
                    targetX = skillCarrier.targetCarrier.getCurrentPosition()[0];
                    targetZ = skillCarrier.targetCarrier.getCurrentPosition()[2];
                }
            }
            float[] triggerPos = skillCarrier.triggerRole.getCurrentPosition();
            skillCarrier.setCurrentPosition(triggerPos[0],triggerPos[2]);

            if(skillCarrier.moveSpeed > 0){
                skillCarrier.setPrePosition();

                skillCarrier.direction = (short)CalculationUtil.vectorToAngle(targetX-skillCarrier.currentPosition[0],targetZ-skillCarrier.currentPosition[2]);

                float moveX = (float)Math.cos(skillCarrier.direction*Math.PI/180)*skillCarrier.moveSpeed/ GameConstant.GAME_FRAME;
                float moveZ = (float)Math.sin(skillCarrier.direction*Math.PI/180)*skillCarrier.moveSpeed/ GameConstant.GAME_FRAME;

                if(Math.abs(moveX) > Math.abs(targetX-skillCarrier.currentPosition[0])){
                    moveX = targetX-skillCarrier.currentPosition[0];
                }
                if(Math.abs(moveZ) > Math.abs(targetZ-skillCarrier.currentPosition[2])){
                    moveZ = targetZ-skillCarrier.currentPosition[2];
                }

                float posX = skillCarrier.currentPosition[0] + moveX;
                float posZ = skillCarrier.currentPosition[2] + moveZ;

                //当移动的坐标点在可行走区域上时才有效，否则结束载体的生命周期
                if(skillCarrier.mapLogic.isPointOnMesh(posX,posZ)){
                //if(mapLogic.getPositionYOnMap(newPos[0],newPos[2]) != DetourHandler.invalidPos){
                    skillCarrier.setCurrentPosition(posX,posZ);

                    //只改变受影响的对象的位置，不改变其朝向
                    skillCarrier.triggerRole.setCurrentPosition(posX,posZ);

                    //System.out.println(Arrays.toString(newPos));

                    skillCarrier.triggerRole.setStillState(GameConstant.FRAME_TIME*2);

                }else{
                    //设置载体的生命时间为0，在下一帧运行时清除
                    skillCarrier.durationTime = 0;
                }

            }


        }
    }
}