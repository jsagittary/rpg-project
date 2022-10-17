package com.dykj.rpg.client.handler;

import com.dykj.rpg.client.ClientSession;
import com.dykj.rpg.client.config.LoginCmdThread;
import com.dykj.rpg.client.config.LoginMsgPram;
import com.dykj.rpg.client.config.ReconnectThread;
import com.dykj.rpg.client.config.SendMsgThreadManager;
import com.dykj.rpg.client.consts.ChannelAttrKey;
import com.dykj.rpg.client.game.player.service.LoginService;
import com.dykj.rpg.client.msg.CmdMsg;
import com.dykj.rpg.client.servlet.PlayerController;
import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.net.handler.core.ClientHandler;
import com.dykj.rpg.net.handler.core.ClientHandlerManager;
import com.dykj.rpg.util.spring.BeanFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @Author: jyb
 * @Date: 2019/1/8 14:34
 * @Description:
 */
public class GameClientHandler extends SimpleChannelInboundHandler<CmdMsg> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, CmdMsg cmdMsg) throws Exception {
        ISession session = channelHandlerContext.channel().attr(ChannelAttrKey.SESSIONS).get();
        if (session == null) {
            logger.error("GameClientHandler error  session {} is  not exist ", channelHandlerContext.channel().toString());
            return;
        }
        ClientHandlerManager clientHandlerManager = BeanFactory.getBean(ClientHandlerManager.class);
        ClientHandler clientHandler = clientHandlerManager.getHandler(cmdMsg.getCmd());
        if (clientHandler == null) {
            logger.error("clientHandler is not exist cmd {} ", cmdMsg.getCmd());
            return;
        }
        clientHandler.handler(cmdMsg.getBytes(), session);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ClientSession session = new ClientSession(ctx.channel());

        SendMsgThreadManager manager = BeanFactory.getBean(SendMsgThreadManager.class);
        manager.addSession(session);
        LoginService loginService = BeanFactory.getBean(LoginService.class);
        LoginMsgPram loginMsgPram = manager.getLoginMsgPramMap().get(ctx.channel());
        if (loginMsgPram == null) {
            ctx.close();
            logger.error("GameClientHandler channelActive error  :loginMsgPram is not exist");
            return;
        }
        session.setLoginMsgPram(loginMsgPram);
        ctx.channel().attr(ChannelAttrKey.SESSIONS).set(session);
        if (loginMsgPram.isReconnect()) {
            loginService.reconnect(loginMsgPram, session);
        } else {
            loginService.getPlayersRq(loginMsgPram, session);
        }
        logger.info("GameClientHandler channelActive {} ", loginMsgPram.toString());
        // loginService.login(session, loginCmdThread.getServerId());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ClientSession session = ctx.channel().attr(ChannelAttrKey.SESSIONS).get();
        if (session != null) {
            SendMsgThreadManager manager = BeanFactory.getBean(SendMsgThreadManager.class);
            manager.removeSession(session);
            session.sessionClosed();
            if (PlayerController.reconnect != 0) {
                LoginMsgPram loginMsgPram = session.getLoginMsgPram();
                loginMsgPram.setReconnect(true);
                LoginCmdThread loginCmdThread = BeanFactory.getBean(LoginCmdThread.class);
                loginCmdThread.execLoginMsg(loginMsgPram);
            }
            logger.info("GameClientHandler channelInactive  sessionId {}", session.getId());
        } else {
            logger.info("GameClientHandler channelInactive  sessionId {}", "null");
        }

        super.channelInactive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        System.out.println("===================exceptionCaught=========================================");
    }
}
