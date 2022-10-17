package com.dykj.rpg.client.game.player.handler;

import com.dykj.rpg.client.ClientSession;
import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.net.handler.core.AbstractClientHandler;
import com.dykj.rpg.protocol.login.GetPlayerInfoRs;
import com.dykj.rpg.protocol.login.LoginMsgRq;
import com.dykj.rpg.protocol.login.RegisterMsgRq;
import com.dykj.rpg.protocol.player.PlayerRs;
import com.dykj.rpg.util.random.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Author: jyb
 * @Date: 2020/10/14 11:16
 * @Description:
 */
public class GetPlayersRsHandler extends AbstractClientHandler<GetPlayerInfoRs> {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    protected void doHandler(GetPlayerInfoRs getPlayerInfoRs, ISession session) {
        ClientSession clientSession = (ClientSession)session;
        List<PlayerRs> playerRs = getPlayerInfoRs.getPlayers();
        if (playerRs.size() > 0) {
            LoginMsgRq loginMsg = new LoginMsgRq();
            PlayerRs player = playerRs.get(0);
            loginMsg.setPlayerId(player.getPlayerId());
            session.write(loginMsg);
            logger.info("GetPlayersRsHandler send login {} ",loginMsg.toString());
        } else {
            RegisterMsgRq registerMsgRq = new RegisterMsgRq();
            registerMsgRq.setName(clientSession.getLoginMsgPram().getName());
            registerMsgRq.setProfession(RandomUtil.randomBetween(1,2));
 			registerMsgRq.setSex(RandomUtil.randomBetween(1,2));
            registerMsgRq.setSex(RandomUtil.randomBetween(1,2));
            session.write(registerMsgRq);
            logger.info("GetPlayersRsHandler send register {} ",registerMsgRq.toString());
        }
    }
}
