package com.dykj.rpg.common.data.model;

import com.dykj.rpg.db.annotation.Column;
import com.dykj.rpg.db.annotation.PrimaryKey;
import com.dykj.rpg.db.data.BaseModel;

import java.util.Date;


/**
 * @Author: lyc
 * @Date: 2021/3/29
 * @Description: 玩家抽卡记录类
 */
public class PlayerCardRecordModel extends BaseModel
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
     * 卡池状态(0-关闭, 1-开启)
     */
    private int cardStatus;

    /**
     * 按钮id
     */
    @Column(primaryKey = PrimaryKey.GENERAL)
    private int buttonId;

    /**
     * 按钮抽取次数
     */
    private int buttonExtractNumber;

    /**
     * 卡池按钮最后一次抽取时间
     */
    private Date buttonLastExtractTime;

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

    public int getCardStatus()
    {
        return cardStatus;
    }

    public void setCardStatus(int cardStatus)
    {
        this.cardStatus = cardStatus;
    }

    public int getButtonId()
    {
        return buttonId;
    }

    public void setButtonId(int buttonId)
    {
        this.buttonId = buttonId;
    }

    public int getButtonExtractNumber()
    {
        return buttonExtractNumber;
    }

    public void setButtonExtractNumber(int buttonExtractNumber)
    {
        this.buttonExtractNumber = buttonExtractNumber;
    }

    public Date getButtonLastExtractTime()
    {
        return buttonLastExtractTime;
    }

    public void setButtonLastExtractTime(Date buttonLastExtractTime)
    {
        this.buttonLastExtractTime = buttonLastExtractTime;
    }

    public Integer primaryKey()
    {
        return this.playerId;
    }

    public Integer primary2Key()
    {
        return this.cardId;
    }

    public Integer primary3Key()
    {
        return this.buttonId;
    }

    @Override
    public String toString()
    {
        return "PlayerCardRecordModel{" + "playerId=" + playerId + ", cardId=" + cardId + ", cardStatus=" + cardStatus + ", buttonId=" + buttonId + ", buttonExtractNumber=" + buttonExtractNumber + ", buttonLastExtractTime=" + buttonLastExtractTime + '}';
    }
}
