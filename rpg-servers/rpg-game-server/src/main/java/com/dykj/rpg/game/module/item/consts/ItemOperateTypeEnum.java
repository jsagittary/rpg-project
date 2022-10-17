package com.dykj.rpg.game.module.item.consts;

/**
 * @Description 背包操作类型枚举
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/25
 */
public enum ItemOperateTypeEnum
{
    GM(1001, "GM指令添加"),//GM指令添加
    SELL(1002, "道具出售"),//出售
    EXCHANGE(1003, "道具兑换"),//兑换
    DECOMPOSITION(1004, "道具分解"),//分解
    USE(1005, "道具使用"),//使用
    DISCARD(1006, "道具丢弃");//丢弃

    private int itemOperateType;//操作类型
    private String itemOperateDesc;//操作描述

    ItemOperateTypeEnum(int itemOperateType, String itemOperateDesc)
    {
        this.itemOperateType = itemOperateType;
        this.itemOperateDesc = itemOperateDesc;
    }

    public int getItemOperateType()
    {
        return itemOperateType;
    }

    public void setItemOperateType(int itemOperateType)
    {
        this.itemOperateType = itemOperateType;
    }

    public String getItemOperateDesc()
    {
        return itemOperateDesc;
    }

    public void setItemOperateDesc(String itemOperateDesc)
    {
        this.itemOperateDesc = itemOperateDesc;
    }
}
