package com.dykj.rpg.net.core;

import com.dykj.rpg.net.protocol.Protocol;
import io.netty.channel.Channel;

import java.util.Map;

/**
 * @Author: jyb
 * @Date: 2018/12/22 12:23
 * @Description: session
 */
public interface ISession {
    /**
     * session id
     *
     * @return
     */
    int getId();

    /**
     * Returns {@code true} if the tcp/ip socket channel is open an may get
     * active later
     */
    boolean isOpen();

    /**
     * Return {@code true} if the tcp/ip socket channel is active and so
     * connected.
     */
    boolean isActive();

    /**
     * 当通信管道关闭时回调此方法通知会话
     */
    void sessionClosed();

    /**
     * 写出数据
     *
     * @param message 数据对象
     * @return 如果当前会话可以写出数据且将数据写出到通信管道时返回true，否则返回false
     */
    boolean write(Object message);


    /**
     * 写出数据
     *
     * @param message 数据对象
     * @return 如果当前会话可以写出数据且将数据写出到通信管道时返回true，否则返回false
     */
    boolean write(byte[] message);

    /**
     * session持有者
     *
     * @return
     */
    ISessionHolder getSessionHolder();


    /**
     * 设置session
     * @param sessionHolder
     */
    void setSessionHolder(ISessionHolder sessionHolder);
    /**
     * 通道
     *
     * @return
     */
    Channel channel();

    /**
     * 像session绑定某个对象
     *
     * @param key
     * @param o
     * @return
     */
    void attribute(String key, Object o);

    /**
     * 通过key 获取Object
     *
     * @param key
     * @return
     */
    Object getAttribute(String key);

    /**
     * 通过key 移除object
     *
     * @param key
     */
    void removeObject(String key);

}
