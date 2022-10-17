package com.dykj.rpg.common.module.uc.model;

import com.dykj.rpg.db.annotation.Column;
import com.dykj.rpg.db.annotation.PrimaryKey;
import com.dykj.rpg.db.data.BaseModel;
import com.dykj.rpg.redis.cache.base.ICacheMapPrimaryKey;

import java.util.Date;

/**
 * @Author: jyb
 * @Date: 2020/9/4 9:53
 * @Description:
 */
public class AccountInfoModel extends BaseModel implements ICacheMapPrimaryKey {

    /**
     * 不同渠道userId可能重复，由我们系统自动分配一个唯一id
     */
    @Column(primaryKey = PrimaryKey.INCREMENT)
    private int accountKey;
    /**
     * SDK方用户ID,pc端存的是unity输入的账号
     */
    private String account;

    /**
     * 渠道
     */
    private String channel;

    /***
     * 是否是内部帐号，1：是，0：否
     */
    private int internal;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后登录时间
     */
    private Date loginTime;

    /**
     * 最后登出时间
     */
    private Date logoutTime;
    /**
     * 最后登录服务器
     */
    private int lastLoginServer;

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

    public int getInternal() {
        return internal;
    }

    public void setInternal(int internal) {
        this.internal = internal;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Date getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(Date logoutTime) {
        this.logoutTime = logoutTime;
    }

    public int getLastLoginServer() {
        return lastLoginServer;
    }

    public void setLastLoginServer(int lastLoginServer) {
        this.lastLoginServer = lastLoginServer;
    }

    @Override
    public String primaryKey() {
        return channel + ":" + account;
    }
}
