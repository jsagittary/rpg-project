package com.dykj.rpg.common.data.model;

import com.dykj.rpg.db.annotation.Column;
import com.dykj.rpg.db.annotation.PrimaryKey;
import com.dykj.rpg.db.data.BaseModel;

/**
 * @author jyb
 * @date 2021/4/6 10:37
 * @Description
 */
public class EquipPosModel extends BaseModel {
    /**
     * 玩家id
     */
    @Column(primaryKey = PrimaryKey.GENERAL)
    private int playerId;
    /**
     * 装备位置
     */
    @Column(primaryKey = PrimaryKey.GENERAL)
    private int pos;
    /**
     * 位置等级
     */
    private int posLv;


    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getPosLv() {
        return posLv;
    }

    public void setPosLv(int posLv) {
        this.posLv = posLv;
    }
}