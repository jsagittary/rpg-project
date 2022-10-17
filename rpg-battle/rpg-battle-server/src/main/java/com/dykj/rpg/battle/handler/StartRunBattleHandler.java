package com.dykj.rpg.battle.handler;

import com.dykj.rpg.battle.logic.BattleContainer;
import com.dykj.rpg.battle.manager.BattleManager;
import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.net.core.UdpSession;
import com.dykj.rpg.net.handler.core.AbstractClientHandler;
import com.dykj.rpg.net.protocol.ProtocolPool;
import com.dykj.rpg.protocol.battle.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartRunBattleHandler extends AbstractClientHandler<StartRunBattleRq> {
    Logger logger = LoggerFactory.getLogger("StartRunBattleHandler");
    @Override
    protected void doHandler(StartRunBattleRq startRunBattleRq, ISession session) {
        logger.info(startRunBattleRq.toString());
        /**
         * 查找已创建的战斗
         */
        BattleContainer battleContainer = BattleManager.getInstance().getBattleContainerByPlayerId(((UdpSession)session).playerId);

        if(battleContainer != null){
            battleContainer.startBattle();
            System.out.println("启动战斗成功");
        }else{
            logger.error("用户未先创建一个有效的战斗");
            BattleErrorMessage message = (BattleErrorMessage) ProtocolPool.getInstance().borrowProtocol(BattleErrorMessage.code);
            message.setErrorCode(1);
            message.setHandlerId(1);
            message.setErrMsg("用户未先创建一个有效的战斗");

            sendMsg(session,message);

            System.out.println("用户未先创建一个有效的战斗");
        }

    }

}
