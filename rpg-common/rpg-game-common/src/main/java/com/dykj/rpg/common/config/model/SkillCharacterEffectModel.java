package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

import java.util.List;

/**
 * @Description 技能效果
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/02/23
*/
public class SkillCharacterEffectModel extends BaseConfig<Integer>
{
    private Integer effectId;//效果ID
    private Integer effectType;//效果类型
    private List<List<Integer>> condition;//条件判定
    private List<List<Integer>> targetType;//对象配置组合
    private List<List<Integer>> filterType;//条件与参数组合
    private Integer operationDelay;//操作延迟
    private Integer effectInterval;//效果间隔
    private Integer effectTimes;//效果最大次数
    private Integer operation;//魔法操作
    private String operationParmA;//操作参数A
    private List<List<Integer>> operationParmB;//操作参数B
    private Integer damageProp;//百分比伤害
    private Integer damageValue;//值伤害

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

    public List<List<Integer>> getCondition()
    {
        return this.condition;
    }

    public void setCondition(List<List<Integer>> condition)
    {
        this.condition = condition;
    }

    public List<List<Integer>> getTargetType()
    {
        return this.targetType;
    }

    public void setTargetType(List<List<Integer>> targetType)
    {
        this.targetType = targetType;
    }

    public List<List<Integer>> getFilterType()
    {
        return this.filterType;
    }

    public void setFilterType(List<List<Integer>> filterType)
    {
        this.filterType = filterType;
    }

    public Integer getOperationDelay()
    {
        return this.operationDelay;
    }

    public void setOperationDelay(Integer operationDelay)
    {
        this.operationDelay = operationDelay;
    }

    public Integer getEffectInterval()
    {
        return this.effectInterval;
    }

    public void setEffectInterval(Integer effectInterval)
    {
        this.effectInterval = effectInterval;
    }

    public Integer getEffectTimes()
    {
        return this.effectTimes;
    }

    public void setEffectTimes(Integer effectTimes)
    {
        this.effectTimes = effectTimes;
    }

    public Integer getOperation()
    {
        return this.operation;
    }

    public void setOperation(Integer operation)
    {
        this.operation = operation;
    }

    public String getOperationParmA()
    {
        return this.operationParmA;
    }

    public void setOperationParmA(String operationParmA)
    {
        this.operationParmA = operationParmA;
    }

    public List<List<Integer>> getOperationParmB()
    {
        return this.operationParmB;
    }

    public void setOperationParmB(List<List<Integer>> operationParmB)
    {
        this.operationParmB = operationParmB;
    }

    public Integer getDamageProp()
    {
        return this.damageProp;
    }

    public void setDamageProp(Integer damageProp)
    {
        this.damageProp = damageProp;
    }

    public Integer getDamageValue()
    {
        return this.damageValue;
    }

    public void setDamageValue(Integer damageValue)
    {
        this.damageValue = damageValue;
    }

    @Override
    public Integer getKey()
    {
        return this.effectId;
    }
}