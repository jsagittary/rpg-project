package com.dykj.rpg.common.data.model;

import com.dykj.rpg.db.annotation.Column;
import com.dykj.rpg.db.annotation.PrimaryKey;
import com.dykj.rpg.db.data.BaseModel;

/**
 * @Author: lyc
 * @Date: 2021/4/2
 * @Description: 玩家卡池结果表
 */
public class PlayerCardResultModel extends BaseModel
{
    /**
     * 玩家id
     */
    @Column(primaryKey = PrimaryKey.GENERAL)
    private int playerId;

    /**
     * 卡池id
     */
    @Column(primaryKey = PrimaryKey.GENERAL)
    private int cardId;

    /**
     * 抽卡结果
     */
    private String cardResult;

    public int getPlayerId()
    {
        return playerId;
    }

    public void setPlayerId(int playerId)
    {
        this.playerId = playerId;
    }

    public int getCardId()
    {
        return cardId;
    }

    public void setCardId(int cardId)
    {
        this.cardId = cardId;
    }

    public String getCardResult()
    {
        return cardResult;
    }

    public void setCardResult(String cardResult)
    {
        this.cardResult = cardResult;
    }

    public long primaryKey()
    {
        return playerId;
    }

    public Long primary2Key()
    {
        return (long) cardId;
    }

    @Override
    public String toString()
    {
        return "PlayerCardResultModel{" + "playerId=" + playerId + ", cardId=" + cardId + ", cardResult='" + cardResult + '\'' + '}';
    }
}
