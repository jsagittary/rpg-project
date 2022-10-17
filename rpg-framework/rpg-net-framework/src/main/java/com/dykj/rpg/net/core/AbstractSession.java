package com.dykj.rpg.net.core;


import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: jyb
 * @Date: 2018/12/22 14:23
 * @Description:
 */
public abstract class AbstractSession implements ISession {

    /**
     * SessionId
     */
    protected int sessionId;

    /**
     * 持有者
     */
    protected ISessionHolder sessionHolder;

    /**
     * 某些属性
     */
    protected Map<String, Object> attribute = new ConcurrentHashMap<>();

    /**
     * 通道
     */
    protected Channel channel;


    private static final AtomicInteger SESSION_GENERATOR = new AtomicInteger(1);

    public AbstractSession(Channel channel) {
        this.sessionId = SESSION_GENERATOR.getAndIncrement();
        this.channel = channel;
    }

    @Override
    public int getId() {
        return sessionId;
    }

    @Override
    public ISessionHolder getSessionHolder() {
        return sessionHolder;
    }


    @Override
    public void sessionClosed() {
        if (sessionHolder != null) {
            if (sessionHolder.getSession() != null) {
                if (sessionHolder.getSession().getId() <= getId()) {
                    sessionHolder.sessionClosed();
                }
            }
        }
    }

    @Override
    public boolean write(byte[] message) {
        return false;
    }

    @Override
    public void setSessionHolder(ISessionHolder sessionHolder) {
        this.sessionHolder = sessionHolder;
    }

    @Override
    public Channel channel() {
        return channel;
    }

    @Override
    public void attribute(String key, Object o) {
        attribute.put(key, o);
    }

    @Override
    public Object getAttribute(String key) {
        return attribute.get(key);
    }

    @Override
    public void removeObject(String key) {
        attribute.remove(key);
    }

    @Override
    public String toString() {
        return "AbstractSession{" +
                "sessionId=" + sessionId +
                ", sessionHolder=" + sessionHolder +
                ", attribute=" + attribute +
                ", channel=" + channel +
                '}';
    }
}
