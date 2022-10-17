package com.dykj.rpg.client.game.player.handler;

import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.net.handler.core.AbstractClientHandler;
import com.dykj.rpg.protocol.common.HeartBeatRs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HeartbeatRsHandler extends AbstractClientHandler<HeartBeatRs> {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    protected void doHandler(HeartBeatRs heartBeatRs, ISession session) {
        logger.info("心跳 sessionId {} 时间 {}", session.getId(), System.currentTimeMillis());
    }

}
