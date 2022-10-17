package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

/**
 * @Description 常量
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/15
*/
public class ConfigModel extends BaseConfig<Integer>
{
    private Integer id;//编号
    private String configIndex;//字段名
    private String note;//注释
    private String value;//字段值
    private String type;//属性类型

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getConfigIndex()
    {
        return this.configIndex;
    }

    public void setConfigIndex(String configIndex)
    {
        this.configIndex = configIndex;
    }

    public String getNote()
    {
        return this.note;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public String getValue()
    {
        return this.value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getType()
    {
        return this.type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    @Override
    public Integer getKey()
    {
        return this.id;
    }
}