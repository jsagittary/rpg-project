package com.dykj.rpg.game.core;

import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.util.CmdUtil;
import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.net.handler.core.AbstractClientHandler;
import com.dykj.rpg.net.protocol.Protocol;
import com.dykj.rpg.net.protocol.Serializer;
import com.dykj.rpg.net.thread.CmdThreadEnum;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: jyb
 * @Date: 2020/9/9 16:37
 * @Description:
 */
public abstract class GameHandler<T extends Protocol> extends AbstractClientHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void handler(byte[] bytes, ISession session) {
        Protocol t = null;
        try {
            t = Serializer.deserialize(bytes, clazz);
        } catch (Exception e) {
            sendError(session, ErrorCodeEnum.ERROR);
            logger.error("GameHandler deserialize error {} {} ", getClass().getSimpleName(), clazz.getSimpleName());
            return;
        }
        doHandler(t, session);
    }


    @Override
    protected void doHandler(Protocol protocol, ISession session) {
        if (session.getSessionHolder() == null) {
            logger.error("{} doHandler error  SessionHolder is null {} ", this.getClass().getSimpleName(), protocol.toString());
            session.channel().close();
            return;
        }
        Player player = (Player) session.getSessionHolder();
        player.setRefreshTime();
        doHandler((T) protocol, player);
    }

    public abstract void doHandler(T t, Player player);

    /**
     * 推消息
     *
     * @param player
     * @param protocol
     */
    public void sendMsg(Player player, Protocol protocol) {
        if (player.getSession() != null && player.getSession().isActive()) {
            super.sendMsg(player.getSession(), protocol);
        }
    }

    @Override
    public CmdThreadEnum getThread() {
        return CmdThreadEnum.LOGIC;
    }


    public void sendError(ISession session, ErrorCodeEnum errorCode) {
        CmdUtil.sendErrorMsg(session, getCode(), errorCode);
    }

    public void sendError(ISession session, ErrorCodeEnum errorCode, String... prams) {
        CmdUtil.sendErrorMsg(session, getCode(), errorCode, prams);
    }

    public void sendError(Player player, ErrorCodeEnum errorCode, String... prams) {
        CmdUtil.sendErrorMsg(player.getSession(), getCode(), errorCode, prams);
    }

    public <V> V getBean(Class<V> clazz) {
        return BeanFactory.getBean(clazz);
    }
}
