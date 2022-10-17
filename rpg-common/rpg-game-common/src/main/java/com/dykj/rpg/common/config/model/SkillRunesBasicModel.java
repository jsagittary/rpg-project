package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

/**
 * @Description 法术符文基础
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/05/20
*/
public class SkillRunesBasicModel extends BaseConfig<Integer>
{
    private Integer id;//编号
    private Integer qualityType;//品质
    private Integer type;//符文类型
    private Integer skillId;//法术ID

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getQualityType()
    {
        return this.qualityType;
    }

    public void setQualityType(Integer qualityType)
    {
        this.qualityType = qualityType;
    }

    public Integer getType()
    {
        return this.type;
    }

    public void setType(Integer type)
    {
        this.type = type;
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
        return this.id;
    }
}