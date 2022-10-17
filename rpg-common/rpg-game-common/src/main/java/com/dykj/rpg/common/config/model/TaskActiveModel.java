package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

import java.util.List;

/**
 * @Description 活跃度宝箱表
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/03/04
*/
public class TaskActiveModel extends BaseConfig<Integer>
{
    private Integer id;//自增ID
    private Integer taskType;//任务类型
    private Integer active;//活跃度
    private List<List<Integer>> reward;//奖励

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getTaskType()
    {
        return this.taskType;
    }

    public void setTaskType(Integer taskType)
    {
        this.taskType = taskType;
    }

    public Integer getActive()
    {
        return this.active;
    }

    public void setActive(Integer active)
    {
        this.active = active;
    }

    public List<List<Integer>> getReward()
    {
        return this.reward;
    }

    public void setReward(List<List<Integer>> reward)
    {
        this.reward = reward;
    }

    @Override
    public Integer getKey()
    {
        return this.id;
    }
}