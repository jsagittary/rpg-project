package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

import java.util.List;

/**
 * @Description 小物件
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/02/04
*/
public class LootMiniobjBasicModel extends BaseConfig<Integer>
{
    private Integer miniobjId;//小物件编号
    private List<Float> autoPickTime;//自动拾取时间
    private Integer linkType;//关联类型
    private List<Integer> itemThing;//物品类型和物品编号
    private List<Integer> miniobjNumber;//物品数量
    private Integer skillId;//技能编号

    public Integer getMiniobjId()
    {
        return this.miniobjId;
    }

    public void setMiniobjId(Integer miniobjId)
    {
        this.miniobjId = miniobjId;
    }

    public List<Float> getAutoPickTime()
    {
        return this.autoPickTime;
    }

    public void setAutoPickTime(List<Float> autoPickTime)
    {
        this.autoPickTime = autoPickTime;
    }

    public Integer getLinkType()
    {
        return this.linkType;
    }

    public void setLinkType(Integer linkType)
    {
        this.linkType = linkType;
    }

    public List<Integer> getItemThing()
    {
        return this.itemThing;
    }

    public void setItemThing(List<Integer> itemThing)
    {
        this.itemThing = itemThing;
    }

    public List<Integer> getMiniobjNumber()
    {
        return this.miniobjNumber;
    }

    public void setMiniobjNumber(List<Integer> miniobjNumber)
    {
        this.miniobjNumber = miniobjNumber;
    }

    public Integer getSkillId()
    {
        return this.skillId;
    }

    public void setSkillId(Integer skillId)
    {
        this.skillId = skillId;
    }

    @Override
    public Integer getKey()
    {
        return this.miniobjId;
    }
}