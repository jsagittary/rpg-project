package com.dykj.rpg.client.game.player.handler;

import com.dykj.rpg.client.ClientSession;
import com.dykj.rpg.client.core.Cmd;
import com.dykj.rpg.client.handler.BeforeHandler;
import com.dykj.rpg.client.msg.CmdMsg;
import com.dykj.rpg.common.consts.cmd.LoginCmdConsts;
import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.net.handler.core.AbstractClientHandler;
import com.dykj.rpg.protocol.login.RegisterMsgRs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: jyb
 * @Date: 2020/9/4 17:13
 * @Description:
 */
public class RegisterRsHandler extends AbstractClientHandler<RegisterMsgRs> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void doHandler(RegisterMsgRs registerMsgRs, ISession session) {
        logger.info("注册成功  {} ",registerMsgRs.toString());
        ClientSession clientSession = (ClientSession) session;
        clientSession.getLoginMsgPram().setPlayerRs(registerMsgRs.getPlayer());
    }
}
