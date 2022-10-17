package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

import java.util.List;

/**
 * @Description 法术符文效果
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/05/22
*/
public class SkillRunesEffectModel extends BaseConfig<Integer>
{
    private Integer id;//自增id
    private Integer runesName;//符文id
    private List<List<Integer>> entryBigType;//法术符文效果修正列
    private List<Integer> type;//法术符文效果修正行
    private List<List<Integer>> growValue;//修正值

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getRunesName()
    {
        return this.runesName;
    }

    public void setRunesName(Integer runesName)
    {
        this.runesName = runesName;
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
        return this.id;
    }
}