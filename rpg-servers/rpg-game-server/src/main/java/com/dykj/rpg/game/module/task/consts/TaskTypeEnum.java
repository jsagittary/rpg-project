package com.dykj.rpg.game.module.task.consts;

/**
 * @Description 任务类型枚举
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/16
 */
public enum TaskTypeEnum
{
    MAIN_LINE(1),//主线
    DAILY(2),//日常
    WEEK(3);//周常

    private int type;

    TaskTypeEnum(int type)
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
