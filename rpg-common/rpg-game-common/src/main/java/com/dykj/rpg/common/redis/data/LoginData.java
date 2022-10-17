package com.dykj.rpg.common.redis.data;

/**
 * @Author: jyb
 * @Date: 2020/10/12 10:09
 * @Description:
 */
public class LoginData {
    /**
     * uc生成的唯一键
     */
    private int accountKey;
    /**
     * 账号
     */
    private String account;
    /**
     * 渠道
     */
    private String channel;

    /**
     * 服务器id
     */
    private int serverId;


    public int getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(int accountKey) {
        this.accountKey = accountKey;
    }

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

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public LoginData(int accountKey, String account, String channel, int serverId) {
        this.accountKey = accountKey;
        this.account = account;
        this.channel = channel;
        this.serverId = serverId;
    }

    public LoginData() {
    }
}
