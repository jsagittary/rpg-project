package com.dykj.rpg.game.module.card.consts;

/**
 * @Description 抽卡开放条件枚举
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/4/7
 */
public enum CardConditionsEnum
{
    PROFESSION(1),//职业
    NUMBER_DRAWS(2);//抽取次数
    private int type;

    CardConditionsEnum(int type)
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
