package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

import java.util.List;

/**
 * @Description 技能升级成长
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/22
*/
public class SkillUpgradeEffectModel extends BaseConfig<Integer>
{
    private Integer skillId;//技能编号
    private Integer growType;//养成方式
    private List<List<Integer>> growLevel;//养成等级
    private List<List<Integer>> entryBigType;//技能成长词条组
    private List<Integer> type;//技能成长ID组
    private List<List<Integer>> growValue;//成长值组

    public Integer getSkillId()
    {
        return this.skillId;
    }

    public void setSkillId(Integer skillId)
    {
        this.skillId = skillId;
    }

    public Integer getGrowType()
    {
        return this.growType;
    }

    public void setGrowType(Integer growType)
    {
        this.growType = growType;
    }

    public List<List<Integer>> getGrowLevel()
    {
        return this.growLevel;
    }

    public void setGrowLevel(List<List<Integer>> growLevel)
    {
        this.growLevel = growLevel;
    }

    public List<List<Integer>> getEntryBigType()
    {
        return this.entryBigType;
    }

    public void setEntryBigType(List<List<Integer>> entryBigType)
    {
        this.entryBigType = entryBigType;
    }

    public List<Integer> getType()
    {
        return this.type;
    }

    public void setType(List<Integer> type)
    {
        this.type = type;
    }

    public List<List<Integer>> getGrowValue()
    {
        return this.growValue;
    }

    public void setGrowValue(List<List<Integer>> growValue)
    {
        this.growValue = growValue;
    }

    @Override
    public Integer getKey()
    {
        return this.skillId;
    }
}