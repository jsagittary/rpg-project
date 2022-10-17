package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

import java.util.List;

/**
 * @Description 任务表
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/03/04
*/
public class TaskBasicModel extends BaseConfig<Integer>
{
    private Integer taskId;//任务ID
    private List<List<Integer>> taskOpen;//任务开启条件
    private Integer taskType;//任务类型
    private List<List<Integer>> taskFinish;//任务完成条件
    private List<List<Integer>> taskReward;//奖励
    private List<List<Integer>> taskRewardExtra;//守护者奖励

    public Integer getTaskId()
    {
        return this.taskId;
    }

    public void setTaskId(Integer taskId)
    {
        this.taskId = taskId;
    }

    public List<List<Integer>> getTaskOpen()
    {
        return this.taskOpen;
    }

    public void setTaskOpen(List<List<Integer>> taskOpen)
    {
        this.taskOpen = taskOpen;
    }

    public Integer getTaskType()
    {
        return this.taskType;
    }

    public void setTaskType(Integer taskType)
    {
        this.taskType = taskType;
    }

    public List<List<Integer>> getTaskFinish()
    {
        return this.taskFinish;
    }

    public void setTaskFinish(List<List<Integer>> taskFinish)
    {
        this.taskFinish = taskFinish;
    }

    public List<List<Integer>> getTaskReward()
    {
        return this.taskReward;
    }

    public void setTaskReward(List<List<Integer>> taskReward)
    {
        this.taskReward = taskReward;
    }

    public List<List<Integer>> getTaskRewardExtra()
    {
        return this.taskRewardExtra;
    }

    public void setTaskRewardExtra(List<List<Integer>> taskRewardExtra)
    {
        this.taskRewardExtra = taskRewardExtra;
    }

    @Override
    public Integer getKey()
    {
        return this.taskId;
    }
}