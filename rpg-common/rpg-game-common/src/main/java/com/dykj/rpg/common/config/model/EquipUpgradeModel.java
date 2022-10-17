package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

import java.util.List;

/**
 * @Description 技能升级
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/04/06
*/
public class EquipUpgradeModel extends BaseConfig<Integer>
{
    private Integer id;//编号
    private Integer partType;//部位
    private Integer classType;//职业
    private Integer partLevel;//部位等级
    private List<List<Integer>> needItem;//养成道具ID
    private List<List<Integer>> upgradeAttribute;//养成效果

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getPartType()
    {
        return this.partType;
    }

    public void setPartType(Integer partType)
    {
        this.partType = partType;
    }

    public Integer getClassType()
    {
        return this.classType;
    }

    public void setClassType(Integer classType)
    {
        this.classType = classType;
    }

    public Integer getPartLevel()
    {
        return this.partLevel;
    }

    public void setPartLevel(Integer partLevel)
    {
        this.partLevel = partLevel;
    }

    public List<List<Integer>> getNeedItem()
    {
        return this.needItem;
    }

    public void setNeedItem(List<List<Integer>> needItem)
    {
        this.needItem = needItem;
    }

    public List<List<Integer>> getUpgradeAttribute()
    {
        return this.upgradeAttribute;
    }

    public void setUpgradeAttribute(List<List<Integer>> upgradeAttribute)
    {
        this.upgradeAttribute = upgradeAttribute;
    }

    @Override
    public Integer getKey()
    {
        return this.id;
    }
}