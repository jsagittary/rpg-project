package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

import java.util.List;

/**
 * @Description 装备词条实际内容
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/02/19
*/
public class EqEntryEffectModel extends BaseConfig<Integer>
{
    private Integer equipEntryEffectId;//自定义id
    private List<String> entryBigType;//属性大类型
    private List<Integer> type;//技能参数对应的定位配置
    private List<Integer> entryScore;//词条评分系数
    private List<Integer> entryValueRange;//属性值域id
    private Integer entryRank;//词条排序规则

    public Integer getEquipEntryEffectId()
    {
        return this.equipEntryEffectId;
    }

    public void setEquipEntryEffectId(Integer equipEntryEffectId)
    {
        this.equipEntryEffectId = equipEntryEffectId;
    }

    public List<String> getEntryBigType()
    {
        return this.entryBigType;
    }

    public void setEntryBigType(List<String> entryBigType)
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

    public List<Integer> getEntryScore()
    {
        return this.entryScore;
    }

    public void setEntryScore(List<Integer> entryScore)
    {
        this.entryScore = entryScore;
    }

    public List<Integer> getEntryValueRange()
    {
        return this.entryValueRange;
    }

    public void setEntryValueRange(List<Integer> entryValueRange)
    {
        this.entryValueRange = entryValueRange;
    }

    public Integer getEntryRank()
    {
        return this.entryRank;
    }

    public void setEntryRank(Integer entryRank)
    {
        this.entryRank = entryRank;
    }

    @Override
    public Integer getKey()
    {
        return this.equipEntryEffectId;
    }
}