package com.dykj.rpg.battle.carrier;

import com.dykj.rpg.battle.basic.BasicRoleSkill;
import com.dykj.rpg.battle.basic.BasicSkillCarrier;
import com.dykj.rpg.battle.constant.GameConstant;
import com.dykj.rpg.battle.constant.RoleAnimConstant;
import com.dykj.rpg.battle.logic.MapLogic;
import com.dykj.rpg.battle.role.BattleRole;
import com.dykj.rpg.battle.util.CalculationUtil;
import com.dykj.rpg.common.config.model.SkillCharacterCarrierModel;
import com.dykj.rpg.protocol.battle.BattlePosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 载体类型--地点位移
 */
public class SkillCarrierPositionMove{

    private static Logger logger = LoggerFactory.getLogger("SkillCarrierPositionMove");

    public static void update(BasicSkillCarrier skillCarrier, int frameNum) {

        if(skillCarrier.state != BasicSkillCarrier.CARRIER_STATE_DESTROY){

            if(skillCarrier.triggerRole != null){
                //float direction = CalculationUtil.vectorToAngle(skillCarrier.targetPosition[0]-skillCarrier.currentPosition[0],skillCarrier.targetPosition[2]-skillCarrier.currentPosition[2]);

                skillCarrier.setPrePosition();

                float moveX = 0;
                float moveZ = 0;

                float nextPosX = skillCarrier.currentPosition[0];
                float nextPosZ = skillCarrier.currentPosition[2];


                double dist = Math.sqrt(Math.pow(skillCarrier.targetPosition[0]-skillCarrier.currentPosition[0], 2)+Math.pow(skillCarrier.targetPosition[2]-skillCarrier.currentPosition[2], 2));
                if(dist != 0){
                    moveX = (float)((skillCarrier.targetPosition[0]-skillCarrier.currentPosition[0]) * (skillCarrier.moveSpeed/ GameConstant.GAME_FRAME)/dist);
                    moveZ = (float)((skillCarrier.targetPosition[2]-skillCarrier.currentPosition[2]) * (skillCarrier.moveSpeed/ GameConstant.GAME_FRAME)/dist);
                    if(moveX != 0){
                        //移动距离与目标距离比例 只有当rate>1时未到达目标，rate < 1 (包括 0 和负数) 均判断为已到达
                        float rate = ((float)skillCarrier.targetPosition[0]-(float)skillCarrier.currentPosition[0])/moveX;
                        if(rate > 1){
                            nextPosX = skillCarrier.currentPosition[0]+moveX;
                        }else{
                            nextPosX = skillCarrier.targetPosition[0];
                        }
                    }
                    if(moveZ != 0){
                        //移动距离与目标距离比例 只有当rate>1时未到达目标，rate < 1 (包括 0 和负数) 均判断为已到达
                        float rate = ((float)skillCarrier.targetPosition[2]-(float)skillCarrier.currentPosition[2])/moveZ;
                        if(rate > 1){
                            nextPosZ = skillCarrier.currentPosition[2]+moveZ;
                        }else{
                            nextPosZ = skillCarrier.targetPosition[2];
                        }
                    }
                }

                skillCarrier.setCurrentPosition(nextPosX,nextPosZ);

                System.out.println("SkillCarrierPositionMove update nextPosX = "+nextPosX+" nextPosZ = "+nextPosZ);
                //只改变受影响的对象的位置，不改变其朝向
                skillCarrier.triggerRole.setCurrentPosition(nextPosX,nextPosZ);

                skillCarrier.triggerRole.setStillState(GameConstant.FRAME_TIME*2);

                if(skillCarrier.heightLocus == 2){
                    skillCarrier.triggerRole.animManager.addAnim(frameNum, RoleAnimConstant.ROLE_ANIM_JUMP,GameConstant.FRAME_TIME*2);
                }

                //System.out.println("carrier role move pos = "+ Arrays.toString(targetRole.getCurrentPosition()));

            }
        }
    }
}