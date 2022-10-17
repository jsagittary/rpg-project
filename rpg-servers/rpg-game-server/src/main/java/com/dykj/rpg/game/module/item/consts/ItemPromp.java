package com.dykj.rpg.game.module.item.consts;

/**
 * @Description 道具弹出类型枚举
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/25
 */
public enum ItemPromp
{
    GENERIC(0, "不弹窗"),
    BULLET_FRAME(1, "弹窗")

    ;

    private int type;
    private String desc;

    ItemPromp(int type, String desc)
    {
        this.type = type;
        this.desc = desc;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }
}
