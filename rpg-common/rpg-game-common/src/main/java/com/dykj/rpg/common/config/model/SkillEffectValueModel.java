package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

import java.util.List;
import java.util.Map;

/**
 * @Description Sheet1
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/22
*/
public class SkillEffectValueModel extends BaseConfig<Integer>
{
    private Integer effectId;//效果ID
    private Integer effectType;//效果类型
    private String effectFormulaFirst;//效果计算公式1
    private String effectFormulaSecond;//效果计算公式2
    private List<Map> effectAttribute;//效果属性

    public Integer getEffectId()
    {
        return this.effectId;
    }

    public void setEffectId(Integer effectId)
    {
        this.effectId = effectId;
    }

    public Integer getEffectType()
    {
        return this.effectType;
    }

    public void setEffectType(Integer effectType)
    {
        this.effectType = effectType;
    }

    public String getEffectFormulaFirst()
    {
        return this.effectFormulaFirst;
    }

    public void setEffectFormulaFirst(String effectFormulaFirst)
    {
        this.effectFormulaFirst = effectFormulaFirst;
    }

    public String getEffectFormulaSecond()
    {
        return this.effectFormulaSecond;
    }

    public void setEffectFormulaSecond(String effectFormulaSecond)
    {
        this.effectFormulaSecond = effectFormulaSecond;
    }

    public List<Map> getEffectAttribute()
    {
        return this.effectAttribute;
    }

    public void setEffectAttribute(List<Map> effectAttribute)
    {
        this.effectAttribute = effectAttribute;
    }

    @Override
    public Integer getKey()
    {
        return this.effectId;
    }
}