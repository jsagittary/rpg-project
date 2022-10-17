package com.dykj.rpg.common.data.model;

import com.dykj.rpg.common.redis.data.GlobalPlayerData;
import com.dykj.rpg.db.annotation.Column;
import com.dykj.rpg.db.annotation.PrimaryKey;
import com.dykj.rpg.db.data.BaseModel;

/**
 * @Author: jyb
 * @Date: 2020/10/9 14:48
 * @Description:
 */
public class GlobalPlayerModel extends BaseModel {
    /**
     * 玩家id
     */
    @Column(primaryKey = PrimaryKey.GENERAL)
    private int playerId;
    /**
     * 头像
     */
    private int portrait;
    /**
     * 服务器id
     */
    private int serverId;
    /**
     * uc生成的唯一key
     */
    private int accountKey;
    /**
     * 名字
     */
    private  String  name;

    public int getPortrait() {
        return portrait;
    }

    public void setPortrait(int portrait) {
        this.portrait = portrait;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(int accountKey) {
        this.accountKey = accountKey;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public GlobalPlayerModel() {
    }

    public GlobalPlayerModel(GlobalPlayerData globalPlayerData) {
        this.playerId = globalPlayerData.getPlayerId();
        this.portrait = globalPlayerData.getPortrait();
        this.serverId =globalPlayerData.getServerId();
        this.accountKey=globalPlayerData.getAccountKey();
        this.name = globalPlayerData.getName();
    }

}
