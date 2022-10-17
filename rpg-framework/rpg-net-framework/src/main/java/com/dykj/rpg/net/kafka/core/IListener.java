package com.dykj.rpg.net.kafka.core;


import com.dykj.rpg.net.protocol.Protocol;
import com.dykj.rpg.net.thread.CmdThreadEnum;

/**
 * @Author: jyb
 * @Date: 2020/9/23 13:42
 * @Description:
 */
public interface IListener<T extends Protocol> {

    CmdThreadEnum getThreadEnum();

    void doListen(T msg);
}
