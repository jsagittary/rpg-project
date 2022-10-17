package com.dykj.rpg.battle.handler;

import com.dykj.rpg.battle.logic.BattleContainer;
import com.dykj.rpg.battle.manager.BattleManager;
import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.net.core.UdpSession;
import com.dykj.rpg.net.handler.core.AbstractClientHandler;
import com.dykj.rpg.net.protocol.BitArray;
import com.dykj.rpg.net.protocol.ProtocolPool;
import com.dykj.rpg.protocol.battle.StopBattleRq;
import com.dykj.rpg.protocol.battle.StopBattleRs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StopBattleHandler extends AbstractClientHandler<StopBattleRq> {
    Logger logger = LoggerFactory.getLogger("StopBattleHandler");
    @Override
    protected void doHandler(StopBattleRq stopBattleRq, ISession session) {
        logger.info(stopBattleRq.toString());
        int playerId = ((UdpSession)session).playerId;
        StopBattleRs response = (StopBattleRs) ProtocolPool.getInstance().borrowProtocol(StopBattleRs.code);

        BattleContainer battleContainer = BattleManager.getInstance().getBattleContainerByPlayerId(playerId);
        if(battleContainer != null){
            response.setResult(true);
        }else{
            response.setResult(false);
        }
        logger.info(response.toString());
        sendMsg(session,response);

        if(battleContainer != null) {
            logger.info("client stop battle !!!");
            battleContainer.finishBattle(BattleContainer.BATTLE_FINISH_TYPE_MANUAL, false, 10);
        }
    }

}
