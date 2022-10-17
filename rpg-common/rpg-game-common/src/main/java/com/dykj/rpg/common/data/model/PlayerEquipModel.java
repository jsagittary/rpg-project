package com.dykj.rpg.common.data.model;

import com.dykj.rpg.db.annotation.Column;
import com.dykj.rpg.db.annotation.PrimaryKey;
import com.dykj.rpg.db.data.BaseModel;

/**
 * @author jyb
 * @date 2020/11/23 10:58
 * @Description
 */
public class PlayerEquipModel extends BaseModel {

    /**
     * 实例id
     */
    @Column(primaryKey = PrimaryKey.GENERAL)
    private long instanceId;

    /**
     * 玩家id
     */
    private int playerId;

    /***
     * 装备id
     */
    private int equipId;

    /**
     *  '装备位置  -1，表示未装备在背包，0~9未装备栏的位置',
     */
    private  int equipPos;

    /**
     * 基础评分
     */
    private  int baseScore;

    /**
     * 装备上锁(0-不可上锁 1-已解锁 2-已上锁)
     */
    private int equipLock;


    public long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(long instanceId) {
        this.instanceId = instanceId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getEquipId() {
        return equipId;
    }

    public void setEquipId(int equipId) {
        this.equipId = equipId;
    }

    public int getEquipPos() {
        return equipPos;
    }

    public void setEquipPos(int equipPos) {
        this.equipPos = equipPos;
    }

    public int getBaseScore() {
        return baseScore;
    }

    public void setBaseScore(int baseScore) {
        this.baseScore = baseScore;
    }

    public int getEquipLock()
    {
        return equipLock;
    }

    public void setEquipLock(int equipLock)
    {
        this.equipLock = equipLock;
    }

    @Override
    public String toString()
    {
        return "PlayerEquipModel{" + "instanceId=" + instanceId + ", playerId=" + playerId + ", equipId=" + equipId + ", equipPos=" + equipPos + ", baseScore=" + baseScore + ", equipLock=" + equipLock + '}';
    }
}