package com.dykj.rpg.battle.carrier;

import com.dykj.rpg.battle.basic.BasicRoleSkill;
import com.dykj.rpg.battle.basic.BasicSkillCarrier;
import com.dykj.rpg.battle.constant.GameConstant;
import com.dykj.rpg.battle.logic.MapLogic;
import com.dykj.rpg.battle.role.BattleRole;
import com.dykj.rpg.common.config.model.SkillCharacterCarrierModel;
import com.dykj.rpg.protocol.battle.BattlePosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 载体类型--目标子弹
 */
public class SkillCarrierTargetBullet {

    private static Logger logger = LoggerFactory.getLogger("SkillCarrierTargetBullet");

    public static void update(BasicSkillCarrier skillCarrier,int frameNum) {

        if(skillCarrier.targetRole != null){
            float[] targetPosition = skillCarrier.targetRole.getCurrentPosition();
            //float direction = CalculationUtil.vectorToAngle(targetPosition[0]-currentPosition[0],targetPosition[2]-currentPosition[2]);

            skillCarrier.setPrePosition();

            float moveX = 0;
            float moveZ = 0;

            float nextPosX = skillCarrier.currentPosition[0];
            float nextPosZ = skillCarrier.currentPosition[2];

            double dist = Math.sqrt(Math.pow(targetPosition[0]-skillCarrier.currentPosition[0], 2)+Math.pow(targetPosition[2]-skillCarrier.currentPosition[2], 2));
            if(dist != 0){
                moveX = (float)((targetPosition[0]-skillCarrier.currentPosition[0]) * (skillCarrier.moveSpeed/ GameConstant.GAME_FRAME)/dist);
                moveZ = (float)((targetPosition[2]-skillCarrier.currentPosition[2]) * (skillCarrier.moveSpeed/ GameConstant.GAME_FRAME)/dist);
                if(moveX != 0){
                    //移动距离与目标距离比例 只有当rate>1时未到达目标，rate < 1 (包括 0 和负数) 均判断为已到达
                    float rate = ((float)targetPosition[0]-(float)skillCarrier.currentPosition[0])/moveX;
                    if(rate > 1){
                        nextPosX = skillCarrier.currentPosition[0]+moveX;
                    }else{
                        nextPosX = targetPosition[0];
                    }
                }
                if(moveZ != 0){
                    //移动距离与目标距离比例 只有当rate>1时未到达目标，rate < 1 (包括 0 和负数) 均判断为已到达
                    float rate = ((float)targetPosition[2]-(float)skillCarrier.currentPosition[2])/moveZ;
                    if(rate > 1){
                        nextPosZ = skillCarrier.currentPosition[2]+moveZ;
                    }else{
                        nextPosZ = targetPosition[2];
                    }
                }
            }

            //float[] nextPos = new float[]{nextPosX,skillCarrier.currentPosition[1],nextPosZ};
            skillCarrier.setCurrentPosition(nextPosX,nextPosZ);

            if(skillCarrier.canCollision){
                //做一次碰撞检测
                skillCarrier.collisionTest();
            }

        }

    }
}