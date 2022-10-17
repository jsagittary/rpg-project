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

public class SkillCarrierDirectionBullet {

    private static Logger logger = LoggerFactory.getLogger("SkillCarrierDirectionBullet");

    public static void update(BasicSkillCarrier skillCarrier,int frameNum) {

        if(skillCarrier.state != BasicSkillCarrier.CARRIER_STATE_DESTROY){
            if(skillCarrier.moveSpeed > 0){
                skillCarrier.setPrePosition();

                float moveX = (float)Math.cos(skillCarrier.direction*Math.PI/180)*skillCarrier.moveSpeed/ GameConstant.GAME_FRAME;
                float moveZ = (float)Math.sin(skillCarrier.direction*Math.PI/180)*skillCarrier.moveSpeed/ GameConstant.GAME_FRAME;

                skillCarrier.setCurrentPosition(skillCarrier.currentPosition[0] + moveX,skillCarrier.currentPosition[2] + moveZ);

            }

            if(skillCarrier.canCollision){
                //做一次碰撞检测
                skillCarrier.collisionTest();
            }
        }
    }
}