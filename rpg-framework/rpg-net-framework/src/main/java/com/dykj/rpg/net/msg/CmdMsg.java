package com.dykj.rpg.net.msg;

import com.dykj.rpg.net.core.ISession;

/**
 * @Author: jyb
 * @Date: 2018/12/22 17:04
 * @Description:
 */
public class CmdMsg implements  ICmdMsg{

    /**
     * 指令id
     */
    private short cmd;

    /**
     * 协议内容--包体
     */
    private byte[] body;
    /**
     * 玩家id
     */
    private int playerId;

    /**
     * Session
     */
    private ISession session;




    public CmdMsg(short cmd, byte[] body) {
        this.cmd = cmd;
        this.body = body;
    }

    public CmdMsg(short commandId, byte[] body, ISession session) {
        this.cmd = commandId;
        this.body = body;
        this.session = session;
    }

    public CmdMsg(short cmd) {
        this.cmd = cmd;
    }

    @Override
    public short getCmd() {
        return cmd;
    }


    public byte[] getBody() {
        return body;
    }


    public ISession getSession() {
        return session;
    }

    public void setSession(ISession session) {
        this.session = session;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
