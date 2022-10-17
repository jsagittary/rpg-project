package com.dykj.rpg.game.module.rune.consts;

/**
 * @Description 符文类型枚举
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/5/22
 */
public enum RuneTypeEnum
{
    EXCLUSIVE(1)//专属




    ;

    private int type;

    RuneTypeEnum(int type)
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
