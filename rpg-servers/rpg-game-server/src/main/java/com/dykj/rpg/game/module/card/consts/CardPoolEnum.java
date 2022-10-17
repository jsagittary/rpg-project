package com.dykj.rpg.game.module.card.consts;

/**
 * @Description 卡池判定枚举
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/4/1
 */
public enum CardPoolEnum
{
    GLOBAL_CARD_POOL(1),//全局卡池
    CURRENT_CARD_POOL(2)//当前卡池
    ;
    private int type;

    CardPoolEnum(int type)
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
