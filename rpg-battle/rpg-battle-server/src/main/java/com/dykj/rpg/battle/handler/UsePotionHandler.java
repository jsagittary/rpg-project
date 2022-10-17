package com.dykj.rpg.battle.handler;

import com.dykj.rpg.battle.logic.BattleContainer;
import com.dykj.rpg.battle.manager.BattleManager;
import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.net.core.UdpSession;
import com.dykj.rpg.net.handler.core.AbstractClientHandler;
import com.dykj.rpg.net.protocol.ProtocolPool;
import com.dykj.rpg.protocol.battle.BattleErrorMessage;
import com.dykj.rpg.protocol.battle.BattleUsePotionRq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsePotionHandler extends AbstractClientHandler<BattleUsePotionRq> {
    Logger logger = LoggerFactory.getLogger("UsePotionHandler");
    @Override
    protected void doHandler(BattleUsePotionRq battleUsePotionRq, ISession session) {
        logger.info(battleUsePotionRq.toString());
        int playerId = ((UdpSession)session).playerId;
        int potionId = battleUsePotionRq.getPotionId();
        if(potionId != 0){
            BattleContainer battleContainer = BattleManager.getInstance().getBattleContainerByPlayerId(playerId);
            if(battleContainer != null){
                battleContainer.waitHandlerDataManager.addUsePotionToWaitMap(playerId,battleUsePotionRq.getPotionId(),false);
            }else{
                BattleErrorMessage msg = (BattleErrorMessage) ProtocolPool.getInstance().borrowProtocol(BattleErrorMessage.code);
                msg.setErrorCode(1);
                msg.setHandlerId(2);
                msg.setErrMsg("战斗已结束！");

                sendMsg(session,msg);
            }
        }else{
            BattleErrorMessage msg = (BattleErrorMessage) ProtocolPool.getInstance().borrowProtocol(BattleErrorMessage.code);
            msg.setErrorCode(1);
            msg.setHandlerId(2);
            msg.setErrMsg("药水ID不能为0！");

            sendMsg(session,msg);
        }

    }
}
