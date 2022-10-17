package com.dykj.rpg.client.msg;

import com.dykj.rpg.net.protocol.Protocol;

/**
 * @Author: jyb
 * @Date: 2019/1/8 14:12
 * @Description:
 */
public class CmdMsg {
    /**
     * 指令
     */
    private short cmd;
    /**
     * 内容
     */
    private byte[] bytes;

    /**
     * sessionId
     */
    private Number sessionId;

    /**
     * 协议
     */
    private Protocol protocol;


    public CmdMsg(short cmd, byte[] bytes, Number sessionId) {
        this.cmd = cmd;
        this.bytes = bytes;
        this.sessionId = sessionId;
    }

    public CmdMsg(short cmd, byte[] bytes) {
        this.cmd = cmd;
        this.bytes = bytes;
    }

    public CmdMsg(short cmd) {
        this.cmd = cmd;
    }

    public short getCmd() {
        return cmd;
    }

    public void setCmd(short cmd) {
        this.cmd = cmd;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public Number getSessionId() {
        return sessionId;
    }

    public void setSessionId(Number sessionId) {
        this.sessionId = sessionId;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    @Override
    public String toString() {
        return "CmdMsg{" +
                "cmd=" + cmd +
                ", sessionId=" + sessionId +
                '}';
    }

    public CmdMsg(Protocol protocol) {
        this.protocol = protocol;
    }
}
