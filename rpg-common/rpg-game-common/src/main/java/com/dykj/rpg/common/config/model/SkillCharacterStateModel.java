package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

import java.util.List;

/**
 * @Description 技能状态
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/04/10
*/
public class SkillCharacterStateModel extends BaseConfig<Integer>
{
    private Integer stateId;//状态ID
    private String displayName;//名称
    private Integer stateNature;//状态性质
    private Integer elementType;//null
    private Integer stateGroup;//状态分组
    private Integer stateType;//状态类型
    private Integer disappearDie;//死亡清除
    private Integer disappearEffect;//驱散类型
    private Integer replace;//替换类型
    private Integer multiType;//群体类型
    private Integer superpositionType;//叠加类型
    private Integer superpositionNumMax;//叠加上限
    private Integer durationTime;//持续时间
    private Integer dissipateType;//消散类型
    private List<Integer> effectId;//效果ID
    private List<List<Integer>> effectIdReserve;//养成后效果ID
    private Integer triggerProbability;//状态触发基础概率
    private Integer triggerProbabilityReserve;//状态触发养成概率

    public Integer getStateId()
    {
        return this.stateId;
    }

    public void setStateId(Integer stateId)
    {
        this.stateId = stateId;
    }

    public String getDisplayName()
    {
        return this.displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public Integer getStateNature()
    {
        return this.stateNature;
    }

    public void setStateNature(Integer stateNature)
    {
        this.stateNature = stateNature;
    }

    public Integer getElementType()
    {
        return this.elementType;
    }

    public void setElementType(Integer elementType)
    {
        this.elementType = elementType;
    }

    public Integer getStateGroup()
    {
        return this.stateGroup;
    }

    public void setStateGroup(Integer stateGroup)
    {
        this.stateGroup = stateGroup;
    }

    public Integer getStateType()
    {
        return this.stateType;
    }

    public void setStateType(Integer stateType)
    {
        this.stateType = stateType;
    }

    public Integer getDisappearDie()
    {
        return this.disappearDie;
    }

    public void setDisappearDie(Integer disappearDie)
    {
        this.disappearDie = disappearDie;
    }

    public Integer getDisappearEffect()
    {
        return this.disappearEffect;
    }

    public void setDisappearEffect(Integer disappearEffect)
    {
        this.disappearEffect = disappearEffect;
    }

    public Integer getReplace()
    {
        return this.replace;
    }

    public void setReplace(Integer replace)
    {
        this.replace = replace;
    }

    public Integer getMultiType()
    {
        return this.multiType;
    }

    public void setMultiType(Integer multiType)
    {
        this.multiType = multiType;
    }

    public Integer getSuperpositionType()
    {
        return this.superpositionType;
    }

    public void setSuperpositionType(Integer superpositionType)
    {
        this.superpositionType = superpositionType;
    }

    public Integer getSuperpositionNumMax()
    {
        return this.superpositionNumMax;
    }

    public void setSuperpositionNumMax(Integer superpositionNumMax)
    {
        this.superpositionNumMax = superpositionNumMax;
    }

    public Integer getDurationTime()
    {
        return this.durationTime;
    }

    public void setDurationTime(Integer durationTime)
    {
        this.durationTime = durationTime;
    }

    public Integer getDissipateType()
    {
        return this.dissipateType;
    }

    public void setDissipateType(Integer dissipateType)
    {
        this.dissipateType = dissipateType;
    }

    public List<Integer> getEffectId()
    {
        return this.effectId;
    }

    public void setEffectId(List<Integer> effectId)
    {
        this.effectId = effectId;
    }

    public List<List<Integer>> getEffectIdReserve()
    {
        return this.effectIdReserve;
    }

    public void setEffectIdReserve(List<List<Integer>> effectIdReserve)
    {
        this.effectIdReserve = effectIdReserve;
    }

    public Integer getTriggerProbability()
    {
        return this.triggerProbability;
    }

    public void setTriggerProbability(Integer triggerProbability)
    {
        this.triggerProbability = triggerProbability;
    }

    public Integer getTriggerProbabilityReserve()
    {
        return this.triggerProbabilityReserve;
    }

    public void setTriggerProbabilityReserve(Integer triggerProbabilityReserve)
    {
        this.triggerProbabilityReserve = triggerProbabilityReserve;
    }

    @Override
    public Integer getKey()
    {
        return this.stateId;
    }
}