package com.dykj.rpg.client.config;

import com.dykj.rpg.protocol.player.PlayerRs;

/**
 * @Author: jyb
 * @Date: 2020/10/12 14:14
 * @Description:
 */
public class LoginMsgPram {
    /**
     * 账号
     */
    private String account;
    /**
     * 渠道
     */
    private String channel;
    /**
     * uc生成的key
     */
    private int accountKey;

    /**
     * 服务器
     */
    private int serverId;

    /**
     * 是否重连
     */
    private boolean reconnect;
    /**
     * 名字
     */
    private String name;

    /**
     * 登录注册玩家id
     */
    private PlayerRs playerRs;


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public LoginMsgPram(String account, String channel) {
        this.account = account;
        this.channel = channel;
    }

    public LoginMsgPram(String account, String channel, int serverId) {
        this.account = account;
        this.channel = channel;
        this.serverId = serverId;
    }

    public int getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(int accountKey) {
        this.accountKey = accountKey;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    @Override
    public String toString() {
        return "LoginMsgPram{" +
                "account='" + account + '\'' +
                ", channel='" + channel + '\'' +
                ", accountKey=" + accountKey +
                ", serverId=" + serverId +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isReconnect() {
        return reconnect;
    }

    public void setReconnect(boolean reconnect) {
        this.reconnect = reconnect;
    }

    public PlayerRs getPlayerRs() {
        return playerRs;
    }

    public void setPlayerRs(PlayerRs playerRs) {
        this.playerRs = playerRs;
    }
}
