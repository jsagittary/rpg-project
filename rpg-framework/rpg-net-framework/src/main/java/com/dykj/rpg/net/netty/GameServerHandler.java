package com.dykj.rpg.net.netty;

import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.net.core.ISessionHolder;
import com.dykj.rpg.net.handler.core.ClientHandler;
import com.dykj.rpg.net.handler.core.ClientHandlerManager;
import com.dykj.rpg.net.msg.CmdMsg;
import com.dykj.rpg.net.netty.consts.ChannelAttrKey;
import com.dykj.rpg.net.thread.MessageExecutor;
import com.dykj.rpg.util.spring.BeanFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: jyb
 * @Date: 2018/12/22 17:28
 * @Description:
 */
public class GameServerHandler extends SimpleChannelInboundHandler<CmdMsg> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Logger reconnectLog = LoggerFactory.getLogger("reconnect");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CmdMsg msg) throws Exception {

        ClientHandler clientHandler = BeanFactory.getBean(ClientHandlerManager.class).getHandler(msg.getCmd());
        if (clientHandler == null) {
            logger.error("GameServerHandler s2CHandler is not exist  cmd {}", msg.getCmd());
            return;
        }
        ISession session = ctx.channel().attr(ChannelAttrKey.SESSIONS).get();
        if (session == null) {
            logger.error("GameServerHandler session is not exist  channel {}", ctx.channel().toString());
            return;
        }
        MessageExecutor.exeClientMsg(msg.getBody(), session, clientHandler);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
       /* System.err.println("有连接过来。。。");*/
        ISession session = new NettySession(ctx.channel());
        ctx.channel().attr(ChannelAttrKey.SESSIONS).set(session);
        reconnectLog.info("GameServerHandler channelActive {}",ctx.channel().toString());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ISession session = ctx.channel().attr(ChannelAttrKey.SESSIONS).get();
        int playerId = 0;
        if (session != null) {
            if (session.getSessionHolder() != null) {
                playerId = session.getSessionHolder().getHolderId();
            }
            reconnectLog.info("GameServerHandler channelInactive session : {} ,playerId {}", session.toString(), playerId);
            session.sessionClosed();
        } else {
            reconnectLog.info("GameServerHandler channel : {}  ", ctx.channel().toString());
        }
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        logger.error("GameServerHandler exceptionCaught ",cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                ISession session = ctx.channel().attr(ChannelAttrKey.SESSIONS).get();
                if (session != null) {
                    ISessionHolder sessionHolder = session.getSessionHolder();
                    int playerId = 0;
                    if (sessionHolder != null) {
                        playerId = sessionHolder.getHolderId();
                    }
                    reconnectLog.info("GameServerHandler userEventTriggered: session {} ,player {} ", session.toString(), playerId);
                }
                ctx.close();
            }
        }
    }

}
