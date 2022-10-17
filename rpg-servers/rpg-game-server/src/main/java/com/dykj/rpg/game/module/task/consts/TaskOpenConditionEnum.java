package com.dykj.rpg.game.module.task.consts;

/**
 * @Description 任务开启条件枚举
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/6
 */
public enum TaskOpenConditionEnum
{
    DEFAULT(0),//默认开放
    GRADE(1),//等级
    CHECKPOINT_PROGRESS(2),//关卡进度
    PRE_TASK(3);//前置任务id

    private int type;

    TaskOpenConditionEnum(int type)
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
