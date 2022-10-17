package com.dykj.rpg.client.game.player.handler;

import com.dykj.rpg.client.ClientSession;
import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.net.handler.core.AbstractClientHandler;
import com.dykj.rpg.protocol.login.LoginMsgRs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: jyb
 * @Date: 2020/9/9 14:58
 * @Description:
 */
public class LoginRsNewHandler extends AbstractClientHandler<LoginMsgRs> {
    private Logger logger = LoggerFactory.getLogger(getClazz());

    @Override
    protected void doHandler(LoginMsgRs loginMsgRs, ISession session) {
        logger.info("登录成功 player {} ", loginMsgRs.toString());
        //发送一个进入战斗服的请求
//        EnterBattleServerRq request = new EnterBattleServerRq();
//        request.setBattleType((byte) 1);
//        request.setMapId(1);
        ((ClientSession) session).getLoginMsgPram().setPlayerRs(loginMsgRs.getPlayer());
      //  sendMsg(session, request);
    }
}
