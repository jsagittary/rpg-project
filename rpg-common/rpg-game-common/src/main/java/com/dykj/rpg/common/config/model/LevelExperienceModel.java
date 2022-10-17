package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

/**
 * @Description 角色等级经验值
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/30
*/
public class LevelExperienceModel extends BaseConfig<Integer>
{
    private Integer id;//编号
    private Integer level;//等级
    private Integer experience;//当前等级累积经验值

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getLevel()
    {
        return this.level;
    }

    public void setLevel(Integer level)
    {
        this.level = level;
    }

    public Integer getExperience()
    {
        return this.experience;
    }

    public void setExperience(Integer experience)
    {
        this.experience = experience;
    }

    @Override
    public Integer getKey()
    {
        return this.id;
    }
}