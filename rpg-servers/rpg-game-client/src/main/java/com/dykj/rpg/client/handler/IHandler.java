package com.dykj.rpg.client.handler;

import com.dykj.rpg.client.msg.CmdMsg;
import com.dykj.rpg.net.core.ISession;

/**
 * @Author: jyb
 * @Description:
 */
public interface IHandler {

    void handler(CmdMsg cmdMsg, ISession session) throws  Exception;
    /**
     * 注册
     * @return
     */
    short registerSendCmd();
}
