package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

/**
 * @Description 任务类型表
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/03/04
*/
public class TaskTypeModel extends BaseConfig<Integer>
{
    private Integer taskTypeId;//任务类型id
    private Integer taskNumber;//最大可激活任务数
    private String refreshTime;//刷新时间

    public Integer getTaskTypeId()
    {
        return this.taskTypeId;
    }

    public void setTaskTypeId(Integer taskTypeId)
    {
        this.taskTypeId = taskTypeId;
    }

    public Integer getTaskNumber()
    {
        return this.taskNumber;
    }

    public void setTaskNumber(Integer taskNumber)
    {
        this.taskNumber = taskNumber;
    }

    public String getRefreshTime()
    {
        return this.refreshTime;
    }

    public void setRefreshTime(String refreshTime)
    {
        this.refreshTime = refreshTime;
    }

    @Override
    public Integer getKey()
    {
        return this.taskTypeId;
    }
}