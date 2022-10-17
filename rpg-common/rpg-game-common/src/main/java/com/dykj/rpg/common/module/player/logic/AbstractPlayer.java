package com.dykj.rpg.common.module.player.logic;

import com.dykj.rpg.net.core.AbstractSessionHolder;
import com.dykj.rpg.net.core.ISession;

/**
 * @Author: jyb
 * @Date: 2018/12/22 18:34
 * @Description:
 */
public abstract class AbstractPlayer extends AbstractSessionHolder implements IPlayer {
    /**
     * session
     */
    protected volatile transient ISession session;

    /**
     * 刷新时间
     */
    protected long refreshTime;

    /**
     * 上一次的session的id
     */
    private int lastSessionId;

    /**
     * 正在使用的角色
     */
    protected int playerId;


    public AbstractPlayer(int playerId) {
        this.playerId = playerId;
    }

    public AbstractPlayer() {
    }

    @Override
    public void sessionClosed() {
        offline();
    }

    @Override
    public void setLastSessionId(int sessionId) {
        this.lastSessionId = sessionId;
    }

    @Override
    public void online(ISession session) {
        this.session = session;
    }


    @Override
    public ISession getSession() {
        return session;
    }

    public long getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime() {
        this.refreshTime = System.currentTimeMillis();
    }

    @Override
    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
