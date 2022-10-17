package com.dykj.rpg.common.data.model;

import com.dykj.rpg.db.annotation.Column;
import com.dykj.rpg.db.annotation.PrimaryKey;
import com.dykj.rpg.db.data.BaseModel;

import java.util.Date;

/**
 * @Author: jyb
 * @Date: 2020/9/3 15:16
 * @Description: 玩家的DB类
 */
public class PlayerInfoModel extends BaseModel{
    /**
     * 玩家id
     */
    @Column(primaryKey = PrimaryKey.GENERAL)
    private int playerId;
    /**
     * 服务器id
     */
    private int serverId;
    /**
     * 帐号id，uc生成的key，同一个账号唯一
     */
    private int accountKey;
    /**
     * 昵称，名称
     */
    private String name;

    /**
     * 职业
     */
    private int profession;
    /**
     *
     * 创建时间
     */
    private Date createdTime;
    /**
     * 最后登入时间
     */
    private Date loginTime;
    /**
     * 0,未使用  1 使用
     */
    private int choose;
    /**
     * 最后登出时间
     */
    private Date logoutTime;

    /**
     * 0,正常 1 删除
     */
    private int  status;

    /**
     * 等级
     */
    private int lv;

    /**
     * 经验
     */
    private int exp;

    /**
     * 背包容量
     */
    private int backCapacity;

    /**
     * vip等级
     */
    private int vipLv;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(int accountKey) {
        this.accountKey = accountKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProfession() {
        return profession;
    }

    public void setProfession(int profession) {
        this.profession = profession;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
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

    @Override
    public String toString() {
        return "PlayerInfoModel{" +
                "playerId=" + playerId +
                ", serverId=" + serverId +
                ", accountKey=" + accountKey +
                ", name='" + name + '\'' +
                ", profession=" + profession +
                ", createdTime=" + createdTime +
                ", loginTime=" + loginTime +
                ", choose=" + choose +
                ", logoutTime=" + logoutTime +
                '}';
    }

    public PlayerInfoModel copy(PlayerInfoModel playerInfoModel)
    {
        PlayerInfoModel copyPlayerInfo = new PlayerInfoModel();
        copyPlayerInfo.setPlayerId(playerInfoModel.getPlayerId());
        copyPlayerInfo.setServerId(playerInfoModel.getServerId());
        copyPlayerInfo.setAccountKey(playerInfoModel.getAccountKey());
        copyPlayerInfo.setName(playerInfoModel.getName());
        copyPlayerInfo.setProfession(playerInfoModel.getProfession());
        copyPlayerInfo.setCreatedTime(playerInfoModel.getCreatedTime());
        copyPlayerInfo.setLoginTime(playerInfoModel.getLoginTime());
        copyPlayerInfo.setChoose(playerInfoModel.getChoose());
        copyPlayerInfo.setLogoutTime(playerInfoModel.getLogoutTime());
        copyPlayerInfo.setStatus(playerInfoModel.getStatus());
        copyPlayerInfo.setLv(playerInfoModel.getLv());
        copyPlayerInfo.setExp(playerInfoModel.getExp());
        copyPlayerInfo.setBackCapacity(playerInfoModel.getBackCapacity());
        copyPlayerInfo.setVipLv(playerInfoModel.getVipLv());
        return playerInfoModel;
    }

    public int getChoose() {
        return choose;
    }

    public void setChoose(int choose) {
        this.choose = choose;
    }

    public PlayerInfoModel() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }



    public PlayerInfoModel(int playerId) {
        this.playerId = playerId;
    }

    public int getLv() {
        return lv;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getBackCapacity()
    {
        return backCapacity;
    }

    public void setBackCapacity(int backCapacity)
    {
        this.backCapacity = backCapacity;
    }

    public int getVipLv() {
        return vipLv;
    }

    public void setVipLv(int vipLv) {
        this.vipLv = vipLv;
    }
}
