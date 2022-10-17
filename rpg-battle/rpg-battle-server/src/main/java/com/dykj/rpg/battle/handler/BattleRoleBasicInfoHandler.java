package com.dykj.rpg.battle.handler;

import com.dykj.rpg.battle.logic.BattleContainer;
import com.dykj.rpg.battle.manager.BattleManager;
import com.dykj.rpg.battle.role.BattleRole;
import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.net.core.UdpSession;
import com.dykj.rpg.net.handler.core.AbstractClientHandler;
import com.dykj.rpg.net.protocol.Protocol;
import com.dykj.rpg.net.protocol.ProtocolPool;
import com.dykj.rpg.protocol.battle.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class BattleRoleBasicInfoHandler extends AbstractClientHandler<BattleRoleBasicInfoRq> {
    Logger logger = LoggerFactory.getLogger("BattleRoleBasicInfoHandler");
    @Override
    protected void doHandler(BattleRoleBasicInfoRq battleRoleBasicInfoRq, ISession session) {
        logger.info(battleRoleBasicInfoRq.toString());
        int playerId = ((UdpSession)session).playerId;

        BattleRoleBasicInfoRs basicInfoRs = (BattleRoleBasicInfoRs) ProtocolPool.getInstance().borrowProtocol(BattleRoleBasicInfoRs.code);
        List<BattleRoleBasic> roleBasicList = basicInfoRs.getRoleBasics();
        BattleContainer battleContainer = BattleManager.getInstance().getBattleContainerByPlayerId(playerId);
        if(battleContainer != null){
            List<BattleRole> roles = battleContainer.getValidBattleRoles();
            if(roles != null && roles.size() > 0){
                for(BattleRole battleRole : roles){
                    if(battleRole != null){

                        BattleRoleBasic roleBasic = new BattleRoleBasic();
                        roleBasic.setId(battleRole.getModelId());
                        roleBasic.setType(battleRole.getRoleType());
                        roleBasic.setRoleId(battleRole.getRoleId());
                        roleBasic.setLevel(battleRole.getRoleLevel());
                        roleBasic.setMaxBlood(battleRole.attributeManager.maxBlood);
                        roleBasic.setMaxSoulEnergy(battleRole.attributeManager.maxSoulEnergy);
                        roleBasic.setSkillSourceType(battleRole.skillSourceType);
                        roleBasic.setMaxSkillSourceNum(battleRole.attributeManager.getMaxSkillSourceNumByType(battleRole.skillSourceType));

                        roleBasicList.add(roleBasic);
                    }
                }
            }

            logger.info(basicInfoRs.toString());

            sendMsg(session,basicInfoRs);

        }else{
            BattleErrorMessage battleErrorMessage = (BattleErrorMessage)ProtocolPool.getInstance().borrowProtocol(BattleErrorMessage.code);
            battleErrorMessage.setErrorCode(1);
            battleErrorMessage.setHandlerId(0);
            battleErrorMessage.setErrMsg("战斗已经结束 ！！！");
            sendMsg(session,battleErrorMessage);
            return;
        }

    }
}
