package com.dykj.rpg.client.handler;


import com.dykj.rpg.client.msg.CmdMsg;
import com.dykj.rpg.net.core.ISession;

/**
 * @Author: jyb
 * @Date: 2019/1/8 18:11
 * @Description:
 */
public abstract class BeforeHandler<T> implements IHandler {

    @Override
    public void handler(CmdMsg cmdMsg, ISession session) throws Exception {
            doHandler(session,decode(cmdMsg),cmdMsg);
    }

    /**
     * 解码
     *
     * @return
     */
    protected abstract T decode(CmdMsg cmdMsg) throws  Exception;


    /**
     * 执行handler
     */
    protected abstract void doHandler(ISession session, T t, CmdMsg msg);

    @Override
    public short registerSendCmd() {
        return 0;
    }
}
