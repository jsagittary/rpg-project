package com.dykj.rpg.net.handler.core;

import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.net.msg.CmdMsg;
import com.dykj.rpg.net.protocol.BitArray;
import com.dykj.rpg.net.protocol.Protocol;
import com.dykj.rpg.net.thread.CmdThreadEnum;

/**
 * @Author: jyb
 * @Date: 2020/9/9 13:38
 * @Description:
 */
public interface ClientHandler<T extends Protocol>{
    /**
     * 主要是封装前端跟服务器协议的执行逻辑，或者服务器与服务器直接的有协议的通信
     *
     * @param bytes
     * @param session
     */
    void handler(byte[] bytes, ISession session);

    /**
     * 当前指令的现成
     * @return
     */
    CmdThreadEnum getThread();


    /**
     * 协议T 的类型
     * @return
     */
    Class getClazz();

    /**
     * 指令号
     * @return
     */
    short getCode();
}

