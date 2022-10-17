package com.dykj.rpg.client.game.player.handler;

import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.net.handler.core.AbstractClientHandler;
import com.dykj.rpg.protocol.login.ReconnectRs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: jyb
 * @Date: 2020/10/14 15:43
 * @Description:
 */
public class ReconnectRsHandler extends AbstractClientHandler<ReconnectRs> {
    private Logger  logger = LoggerFactory.getLogger(getClass());
    @Override
    protected void doHandler(ReconnectRs reconnectRs, ISession session) {
        logger.info("ReconnectRsHandler  reconnect  success {}",reconnectRs.toString());
    }
}
