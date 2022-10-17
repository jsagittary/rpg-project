package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

/**
 * @Description VIP相关信息
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/23
*/
public class VipModel extends BaseConfig<Integer>
{
    private Integer vipLevel;//vip等级
    private Integer fastIdleFreeNumber;//免费快速挂机次数
    private Integer fastIdlePayNumber;//付费购买快速挂机次数
    private Integer vipEmpiricalValue;//vip经验值

    public Integer getVipLevel()
    {
        return this.vipLevel;
    }

    public void setVipLevel(Integer vipLevel)
    {
        this.vipLevel = vipLevel;
    }

    public Integer getFastIdleFreeNumber()
    {
        return this.fastIdleFreeNumber;
    }

    public void setFastIdleFreeNumber(Integer fastIdleFreeNumber)
    {
        this.fastIdleFreeNumber = fastIdleFreeNumber;
    }

    public Integer getFastIdlePayNumber()
    {
        return this.fastIdlePayNumber;
    }

    public void setFastIdlePayNumber(Integer fastIdlePayNumber)
    {
        this.fastIdlePayNumber = fastIdlePayNumber;
    }

    public Integer getVipEmpiricalValue()
    {
        return this.vipEmpiricalValue;
    }

    public void setVipEmpiricalValue(Integer vipEmpiricalValue)
    {
        this.vipEmpiricalValue = vipEmpiricalValue;
    }

    @Override
    public Integer getKey()
    {
        return this.vipLevel;
    }

}