package com.dykj.rpg.common.consts;

/**
 * @Description
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/1/11
 */
public interface ItemType
{
    /**
     * 根据传入的道具子类型获取对应枚举值
     * @param typeEnum 道具子类
     * @return 枚举值
     */
    int getAppointSubclassType(Object typeEnum);
}
