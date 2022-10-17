package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

/**
 * @Description 技能buff类型
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/15
*/
public class SkillBuffTypeModel extends BaseConfig<Integer>
{
    private Integer buffType;//buff类型
    private Integer sameIdAdditive;//相同buff_id叠加计算方式
    private Integer sameTypeAdditive;//相同buff_type不同buff_id叠加计算方式
    private Integer buffTierMax;//最高可叠层数

    public Integer getBuffType()
    {
        return this.buffType;
    }

    public void setBuffType(Integer buffType)
    {
        this.buffType = buffType;
    }

    public Integer getSameIdAdditive()
    {
        return this.sameIdAdditive;
    }

    public void setSameIdAdditive(Integer sameIdAdditive)
    {
        this.sameIdAdditive = sameIdAdditive;
    }

    public Integer getSameTypeAdditive()
    {
        return this.sameTypeAdditive;
    }

    public void setSameTypeAdditive(Integer sameTypeAdditive)
    {
        this.sameTypeAdditive = sameTypeAdditive;
    }

    public Integer getBuffTierMax()
    {
        return this.buffTierMax;
    }

    public void setBuffTierMax(Integer buffTierMax)
    {
        this.buffTierMax = buffTierMax;
    }

    @Override
    public Integer getKey()
    {
        return this.buffType;
    }
}