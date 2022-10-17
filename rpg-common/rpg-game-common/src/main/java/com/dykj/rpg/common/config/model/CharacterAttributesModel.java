package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

import java.util.List;

/**
 * @Description 主角属性
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/02/04
*/
public class CharacterAttributesModel extends BaseConfig<Integer>
{
    private Integer id;//编号
    private Integer growClass;//属性职业
    private List<Integer> level;//等级区间
    private List<String> mainattributes;//主要属性
    private List<String> generalattributes;//一般属性

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getGrowClass()
    {
        return this.growClass;
    }

    public void setGrowClass(Integer growClass)
    {
        this.growClass = growClass;
    }

    public List<Integer> getLevel()
    {
        return this.level;
    }

    public void setLevel(List<Integer> level)
    {
        this.level = level;
    }

    public List<String> getMainattributes()
    {
        return this.mainattributes;
    }

    public void setMainattributes(List<String> mainattributes)
    {
        this.mainattributes = mainattributes;
    }

    public List<String> getGeneralattributes()
    {
        return this.generalattributes;
    }

    public void setGeneralattributes(List<String> generalattributes)
    {
        this.generalattributes = generalattributes;
    }

    @Override
    public Integer getKey()
    {
        return this.id;
    }
}