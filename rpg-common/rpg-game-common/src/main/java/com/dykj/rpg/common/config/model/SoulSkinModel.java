package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

import java.util.List;

/**
 * @Description 灵魂之影表现表
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/03/29
*/
public class SoulSkinModel extends BaseConfig<Integer>
{
    private Integer soulSkinId;//灵魂之影
    private String soulName;//名称
    private List<Integer> condition;//职业条件
    private String soulSkinDisplay;//资源地址
    private Integer birthTime;//灵魂之影出生动画时长

    public Integer getSoulSkinId()
    {
        return this.soulSkinId;
    }

    public void setSoulSkinId(Integer soulSkinId)
    {
        this.soulSkinId = soulSkinId;
    }

    public String getSoulName()
    {
        return this.soulName;
    }

    public void setSoulName(String soulName)
    {
        this.soulName = soulName;
    }

    public List<Integer> getCondition()
    {
        return this.condition;
    }

    public void setCondition(List<Integer> condition)
    {
        this.condition = condition;
    }

    public String getSoulSkinDisplay()
    {
        return this.soulSkinDisplay;
    }

    public void setSoulSkinDisplay(String soulSkinDisplay)
    {
        this.soulSkinDisplay = soulSkinDisplay;
    }

    public Integer getBirthTime()
    {
        return this.birthTime;
    }

    public void setBirthTime(Integer birthTime)
    {
        this.birthTime = birthTime;
    }

    @Override
    public Integer getKey()
    {
        return this.soulSkinId;
    }
}