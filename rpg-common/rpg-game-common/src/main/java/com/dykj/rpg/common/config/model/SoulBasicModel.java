package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

import java.util.List;

/**
 * @Description 灵魂之影
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/04/26
*/
public class SoulBasicModel extends BaseConfig<Integer>
{
    private Integer soulSkinId;//灵魂之影
    private String soulName;//名称
    private List<Integer> condition;//职业条件
    private Integer birthTime;//灵魂之影出生动画时长
    private List<List<Integer>> open;//默认开放条件
    private List<List<Integer>> openUse;//开放条件
    private List<List<Integer>> correct;//技能修正

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

    public Integer getBirthTime()
    {
        return this.birthTime;
    }

    public void setBirthTime(Integer birthTime)
    {
        this.birthTime = birthTime;
    }

    public List<List<Integer>> getOpen()
    {
        return this.open;
    }

    public void setOpen(List<List<Integer>> open)
    {
        this.open = open;
    }

    public List<List<Integer>> getOpenUse()
    {
        return this.openUse;
    }

    public void setOpenUse(List<List<Integer>> openUse)
    {
        this.openUse = openUse;
    }

    public List<List<Integer>> getCorrect()
    {
        return this.correct;
    }

    public void setCorrect(List<List<Integer>> correct)
    {
        this.correct = correct;
    }

    @Override
    public Integer getKey()
    {
        return this.soulSkinId;
    }
}