package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

/**
 * @Description 技能参数定义
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/01/18
*/
public class SkillAttrBasicModel extends BaseConfig<Integer>
{
    private Integer skillAttrId;//参数ID
    private Integer skillMain;//表ID
    private String skillParameter;//表头
    private Integer displayName;//显示名

    public Integer getSkillAttrId()
    {
        return this.skillAttrId;
    }

    public void setSkillAttrId(Integer skillAttrId)
    {
        this.skillAttrId = skillAttrId;
    }

    public Integer getSkillMain()
    {
        return this.skillMain;
    }

    public void setSkillMain(Integer skillMain)
    {
        this.skillMain = skillMain;
    }

    public String getSkillParameter()
    {
        return this.skillParameter;
    }

    public void setSkillParameter(String skillParameter)
    {
        this.skillParameter = skillParameter;
    }

    public Integer getDisplayName()
    {
        return this.displayName;
    }

    public void setDisplayName(Integer displayName)
    {
        this.displayName = displayName;
    }

    @Override
    public Integer getKey()
    {
        return this.skillAttrId;
    }
}