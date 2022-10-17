package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

/**
 * @Description 灵魂之影转化时间表
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/02/23
*/
public class SoulChangeTimeModel extends BaseConfig<Integer>
{
    private Integer rankNum;//稀有度
    private Integer trainingTime;//训练时长

    public Integer getRankNum()
    {
        return this.rankNum;
    }

    public void setRankNum(Integer rankNum)
    {
        this.rankNum = rankNum;
    }

    public Integer getTrainingTime()
    {
        return this.trainingTime;
    }

    public void setTrainingTime(Integer trainingTime)
    {
        this.trainingTime = trainingTime;
    }

    @Override
    public Integer getKey()
    {
        return this.rankNum;
    }
}