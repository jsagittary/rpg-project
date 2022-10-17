package com.dykj.rpg.common.module.player.logic;

import com.dykj.rpg.net.core.ISession;

/**
 * @Author: jyb
 * @Date: 2018/12/22 18:32
 * @Description:
 */
public interface IPlayer{
    /**
     * 获取玩家id
     * @return
     */
    int getPlayerId();
    /**
     * 上线
     *
     * @param session 通信会话
     *
     */
    void online(ISession session);

    /**
     * 下线
     */
    void offline();

    /**
     * 玩家session
     * @return
     */
    ISession getSession();

    /**
     * 上次刷新的时间
     * @return
     */
    long getRefreshTime();

    /**
     * 设置刷新的时间
     * @param
     */
    void setRefreshTime() ;

}
