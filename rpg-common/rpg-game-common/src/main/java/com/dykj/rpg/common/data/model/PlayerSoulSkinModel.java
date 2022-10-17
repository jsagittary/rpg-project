package com.dykj.rpg.common.data.model;

import com.dykj.rpg.db.annotation.Column;
import com.dykj.rpg.db.annotation.PrimaryKey;
import com.dykj.rpg.db.data.BaseModel;

/**
 * @author jyb
 * @date 2021/4/26 11:27
 * @Description
 */
public class PlayerSoulSkinModel extends BaseModel {

    /**
     * 玩家id
     */
    @Column(primaryKey = PrimaryKey.GENERAL)
    private  int playerId;

    /**
     * 用砖石解锁的灵魂之影皮肤
     */
    @Column(primaryKey = PrimaryKey.GENERAL)
    private int soulSkinId;

    /**
     * 是否穿戴 0 未穿戴 1穿戴
     */
    private int  useSoulSkin;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getUseSoulSkin() {
        return useSoulSkin;
    }

    public void setUseSoulSkin(int useSoulSkin) {
        this.useSoulSkin = useSoulSkin;
    }

    public int getSoulSkinId() {
        return soulSkinId;
    }

    public void setSoulSkinId(int soulSkinId) {
        this.soulSkinId = soulSkinId;
    }
}