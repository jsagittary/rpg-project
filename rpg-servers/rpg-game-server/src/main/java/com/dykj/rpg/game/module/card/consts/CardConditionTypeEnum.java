package com.dykj.rpg.game.module.card.consts;

/**
 * @Description 抽卡条件类型枚举
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/4/1
 */
public enum CardConditionTypeEnum
{
    CARD_NUMBER(1);//抽卡次数

    private int type;

    CardConditionTypeEnum(int type)
    {
        this.type = type;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }
}
