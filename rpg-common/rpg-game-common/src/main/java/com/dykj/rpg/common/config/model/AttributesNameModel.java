package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

/**
 * @Description 属性名
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/02/20
*/
public class AttributesNameModel extends BaseConfig<Integer>
{
    private Integer id;//序号
    private String attrDisplayName;//显示名
    private String attrName;//名称
    private String fill;//填法
    private Integer scroeRule;//评分
    private Integer attributesCategory;//属性分类
    private Integer sortId;//排序序号

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getAttrDisplayName()
    {
        return this.attrDisplayName;
    }

    public void setAttrDisplayName(String attrDisplayName)
    {
        this.attrDisplayName = attrDisplayName;
    }

    public String getAttrName()
    {
        return this.attrName;
    }

    public void setAttrName(String attrName)
    {
        this.attrName = attrName;
    }

    public String getFill()
    {
        return this.fill;
    }

    public void setFill(String fill)
    {
        this.fill = fill;
    }

    public Integer getScroeRule()
    {
        return this.scroeRule;
    }

    public void setScroeRule(Integer scroeRule)
    {
        this.scroeRule = scroeRule;
    }

    public Integer getAttributesCategory()
    {
        return this.attributesCategory;
    }

    public void setAttributesCategory(Integer attributesCategory)
    {
        this.attributesCategory = attributesCategory;
    }

    public Integer getSortId()
    {
        return this.sortId;
    }

    public void setSortId(Integer sortId)
    {
        this.sortId = sortId;
    }

    @Override
    public Integer getKey()
    {
        return this.id;
    }
}