package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

/**
 * @Description 技能buff触发
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/15
*/
public class SkillBuffTriggerModel extends BaseConfig<Integer>
{
    private Integer sbId;//技能装配联动ID
    private Integer sbMountSkill1;//条件1:装配技能1
    private Integer sbMountSkill2;//条件2:装配技能2
    private Integer sbSkillUse;//使用条件:使用技能
    private Integer sbBuff;//获得buff
    private Integer sbBuffTier;//获得buff层级

    public Integer getSbId()
    {
        return this.sbId;
    }

    public void setSbId(Integer sbId)
    {
        this.sbId = sbId;
    }

    public Integer getSbMountSkill1()
    {
        return this.sbMountSkill1;
    }

    public void setSbMountSkill1(Integer sbMountSkill1)
    {
        this.sbMountSkill1 = sbMountSkill1;
    }

    public Integer getSbMountSkill2()
    {
        return this.sbMountSkill2;
    }

    public void setSbMountSkill2(Integer sbMountSkill2)
    {
        this.sbMountSkill2 = sbMountSkill2;
    }

    public Integer getSbSkillUse()
    {
        return this.sbSkillUse;
    }

    public void setSbSkillUse(Integer sbSkillUse)
    {
        this.sbSkillUse = sbSkillUse;
    }

    public Integer getSbBuff()
    {
        return this.sbBuff;
    }

    public void setSbBuff(Integer sbBuff)
    {
        this.sbBuff = sbBuff;
    }

    public Integer getSbBuffTier()
    {
        return this.sbBuffTier;
    }

    public void setSbBuffTier(Integer sbBuffTier)
    {
        this.sbBuffTier = sbBuffTier;
    }

    @Override
    public Integer getKey()
    {
        return this.sbId;
    }
}