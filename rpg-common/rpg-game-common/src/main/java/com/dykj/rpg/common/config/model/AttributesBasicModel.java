package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

/**
 * @Description attribute_basic
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/15
*/
public class AttributesBasicModel extends BaseConfig<Integer>
{
    private Integer id;//属性编号
    private String attributesIndex;//属性真实名
    private Integer attrSubType;//属性类型
    private String abbreviate;//字符名称

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getAttributesIndex()
    {
        return this.attributesIndex;
    }

    public void setAttributesIndex(String attributesIndex)
    {
        this.attributesIndex = attributesIndex;
    }

    public Integer getAttrSubType()
    {
        return this.attrSubType;
    }

    public void setAttrSubType(Integer attrSubType)
    {
        this.attrSubType = attrSubType;
    }

    public String getAbbreviate()
    {
        return this.abbreviate;
    }

    public void setAbbreviate(String abbreviate)
    {
        this.abbreviate = abbreviate;
    }

    @Override
    public Integer getKey()
    {
        return this.id;
    }
}