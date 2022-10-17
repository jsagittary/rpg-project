package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

import java.util.List;

/**
 * @Description 技能升星消耗
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/04/22
*/
public class SkillStarUseModel extends BaseConfig<Integer>
{
    private Integer id;//编号
    private Integer itemQualityType;//道具品质
    private Integer skillStarLevel;//技能升星
    private List<List<Integer>> needItem;//养成道具ID
    private List<List<Integer>> needQualityItem;//养成需求道具组
    private Integer oneselfNumber;//消耗本体数量
    private Integer runeNumber;//符文槽数量

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

    public Integer getSkillStarLevel()
    {
        return this.skillStarLevel;
    }

    public void setSkillStarLevel(Integer skillStarLevel)
    {
        this.skillStarLevel = skillStarLevel;
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

    public Integer getOneselfNumber()
    {
        return this.oneselfNumber;
    }

    public void setOneselfNumber(Integer oneselfNumber)
    {
        this.oneselfNumber = oneselfNumber;
    }

    public Integer getRuneNumber()
    {
        return this.runeNumber;
    }

    public void setRuneNumber(Integer runeNumber)
    {
        this.runeNumber = runeNumber;
    }

    @Override
    public Integer getKey()
    {
        return this.id;
    }
}