package com.dykj.rpg.net.core;

/**
 * @Author: jyb
 * @Date: 2018/12/22 12:28
 * @Description: session持有者
 */
public interface ISessionHolder {
    /**
     * 会话关闭时的回调。
     */
    void sessionClosed();

    /**
     * 获取持有的会话。
     *
     * @return
     */
    ISession getSession();

    /**
     * session  持有者id （一般为玩家id）
     * @return
     */
    int  getHolderId();


    /**
     * 上一次sessionId（做断线重连用）
     * @param sessionId
     * @return
     */
    void setLastSessionId(int sessionId);

}
