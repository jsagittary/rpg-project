package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

import java.util.List;

/**
 * @Description 技能升级
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/22
*/
public class SkillUpgradeModel extends BaseConfig<Integer>
{
    private Integer id;//编号
    private Integer itemQualityType;//道具品质
    private Integer skillLevel;//技能等级
    private Integer successRate;//成功率
    private List<List<Integer>> needItem;//养成道具ID
    private List<List<Integer>> needQualityItem;//养成需求道具组

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getItemQualityType()
    {
        return this.itemQualityType;
    }

    public void setItemQualityType(Integer itemQualityType)
    {
        this.itemQualityType = itemQualityType;
    }

    public Integer getSkillLevel()
    {
        return this.skillLevel;
    }

    public void setSkillLevel(Integer skillLevel)
    {
        this.skillLevel = skillLevel;
    }

    public Integer getSuccessRate()
    {
        return this.successRate;
    }

    public void setSuccessRate(Integer successRate)
    {
        this.successRate = successRate;
    }

    public List<List<Integer>> getNeedItem()
    {
        return this.needItem;
    }

    public void setNeedItem(List<List<Integer>> needItem)
    {
        this.needItem = needItem;
    }

    public List<List<Integer>> getNeedQualityItem()
    {
        return this.needQualityItem;
    }

    public void setNeedQualityItem(List<List<Integer>> needQualityItem)
    {
        this.needQualityItem = needQualityItem;
    }

    @Override
    public Integer getKey()
    {
        return this.id;
    }

    @Override
    public String toString()
    {
        return "SkillUpgradeModel{" + "id=" + id + ", itemQualityType=" + itemQualityType + ", skillLevel=" + skillLevel + ", successRate=" + successRate + ", needItem=" + needItem + ", needQualityItem=" + needQualityItem + '}';
    }
}