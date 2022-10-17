package com.dykj.rpg.net.handler;

import com.dykj.rpg.net.msg.ICmdMsg;
import com.dykj.rpg.net.thread.CmdThreadEnum;
import com.dykj.rpg.net.core.ISession;

/**
 * @Author: jyb
 * @Date: 2018/12/22 18:02
 * @Description:
 */
public interface IHandler<T extends ICmdMsg> {
    /**
     * 主要是封装前端跟服务器协议的执行逻辑，或者服务器与服务器直接的有协议的通信
     *
     * @param cmdMsg
     * @param session
     * @throws Exception
     */
    void handler(T cmdMsg, ISession session) throws Exception;

    /**
     * 当前指令的现成
     * @return
     */
    CmdThreadEnum getThread();
}
