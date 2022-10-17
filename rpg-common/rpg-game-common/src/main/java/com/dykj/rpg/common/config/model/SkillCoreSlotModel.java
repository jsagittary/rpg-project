package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

import java.util.List;

/**
 * @Description 核心槽效果
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/01/21
*/
public class SkillCoreSlotModel extends BaseConfig<Integer>
{
    private Integer id;//序号
    private Integer skillId;//技能编号
    private Integer skillPosition;//技能位置
    private List<List<Integer>> entryBigType;//技能成长词条组
    private List<Integer> type;//技能成长ID组
    private List<Integer> growValue;//成长值组
    private Integer display;//文本描述

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getSkillId()
    {
        return this.skillId;
    }

    public void setSkillId(Integer skillId)
    {
        this.skillId = skillId;
    }

    public Integer getSkillPosition()
    {
        return this.skillPosition;
    }

    public void setSkillPosition(Integer skillPosition)
    {
        this.skillPosition = skillPosition;
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

    public List<Integer> getGrowValue()
    {
        return this.growValue;
    }

    public void setGrowValue(List<Integer> growValue)
    {
        this.growValue = growValue;
    }

    public Integer getDisplay()
    {
        return this.display;
    }

    public void setDisplay(Integer display)
    {
        this.display = display;
    }

    @Override
    public Integer getKey()
    {
        return this.id;
    }
}