package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

import java.util.List;

/**
 * @Description 技能升星
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/04/22
*/
public class SkillStarEffectModel extends BaseConfig<Integer>
{
    private Integer skillId;//技能编号
    private List<Integer> starLevel;//星级
    private List<Integer> skillLimitnum;//技能等级上限
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

    public List<Integer> getStarLevel()
    {
        return this.starLevel;
    }

    public void setStarLevel(List<Integer> starLevel)
    {
        this.starLevel = starLevel;
    }

    public List<Integer> getSkillLimitnum()
    {
        return this.skillLimitnum;
    }

    public void setSkillLimitnum(List<Integer> skillLimitnum)
    {
        this.skillLimitnum = skillLimitnum;
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