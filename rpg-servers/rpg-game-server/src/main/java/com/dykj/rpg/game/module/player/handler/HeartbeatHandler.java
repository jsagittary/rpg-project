package com.dykj.rpg.game.module.player.handler;

import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.net.handler.core.AbstractClientHandler;
import com.dykj.rpg.protocol.common.HeartBeatRq;
import com.dykj.rpg.protocol.common.HeartBeatRs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeartbeatHandler extends AbstractClientHandler<HeartBeatRq> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void doHandler(HeartBeatRq heartBeatRq, ISession session) {

        HeartBeatRs heartBeatRs = new HeartBeatRs(System.currentTimeMillis());
        if (session.getSessionHolder() != null) {
            Player player = (Player) session.getSessionHolder();
            player.setRefreshTime();

            //logger.info("心跳 sessionId {} 时间 playerId{}  playerName{} ", session.getId(),player.getPlayerId(),player.getInfo().getName());
        }
        //logger.info("===心跳 sessionId {}==== ", session.getId());
        sendMsg(session, heartBeatRs);
    }
}
