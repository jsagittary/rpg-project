package com.dykj.rpg.net.core;

import com.dykj.rpg.net.handler.core.ClientHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: jyb
 * @Date: 2020/10/26 11:31
 * @Description:
 */
public class NetWork implements Runnable {


    private Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * session
     */
    private ISession session;

    /**
     * 消息
     */
    private byte[] bytes;

    /**
     * 命令
     */
    private ClientHandler handler;

    public NetWork(ISession session, byte[] bytes, ClientHandler handler) {
        this.session = session;
        this.bytes = bytes;
        this.handler = handler;
    }

    @Override
    public void run() {
        handler.handler(bytes, session);
    }
}
