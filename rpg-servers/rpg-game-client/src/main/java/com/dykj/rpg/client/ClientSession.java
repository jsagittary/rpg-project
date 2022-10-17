package com.dykj.rpg.client;

import com.dykj.rpg.client.config.LoginMsgPram;
import com.dykj.rpg.client.msg.CmdMsg;
import com.dykj.rpg.net.core.AbstractSession;
import com.dykj.rpg.net.protocol.Protocol;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: jyb
 * @Date: 2019/1/8 14:25
 * @Description:
 */
public class ClientSession extends AbstractSession {


    /**
     * 重连次数
     */
    private  int reconnectTime;

    /**
     * 登录参数
     */
    private LoginMsgPram loginMsgPram;

    private Map<Short, Long> msgTime = new ConcurrentHashMap<>();

    private static final AtomicInteger ATOMIC_INTEGER_IDS = new AtomicInteger(1);



//    @Override
//    public boolean write(Object message) {
//        CmdMsg cmdMsg = (CmdMsg) message;
//        //把心跳排除掉
//        if(cmdMsg.getCmd()!=0){
//
//        }
//        channel.writeAndFlush(message);
//        return Boolean.TRUE;
//    }

    @Override
    public boolean write(Object message) {
        if(message instanceof Protocol){
            Protocol cmdMsg =(Protocol)message;
            //把心跳排除掉
            if(cmdMsg.getCode()!=0){
            }
            channel.writeAndFlush(message);
        }



        return Boolean.TRUE;
    }

    @Override
    public boolean write(byte[] message) {
        if(message!=null&&message.length>0){
            channel.writeAndFlush(message);
        }
        return Boolean.TRUE;
    }

    public ClientSession(Channel channel) {
        super(channel);
    }

    public long getSendMsgTime(short cmd) {
        Long time = msgTime.remove(cmd);
        if (time != null) {
            return time;
        } else {
            return 0;
        }
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void sessionClosed() {
        channel.close();
    }



    public int getReconnectTime() {
        return reconnectTime;
    }

    public void setReconnectTime(int reconnectTime) {
        this.reconnectTime = reconnectTime;
    }

    public LoginMsgPram getLoginMsgPram() {
        return loginMsgPram;
    }

    public void setLoginMsgPram(LoginMsgPram loginMsgPram) {
        this.loginMsgPram = loginMsgPram;
    }
}
