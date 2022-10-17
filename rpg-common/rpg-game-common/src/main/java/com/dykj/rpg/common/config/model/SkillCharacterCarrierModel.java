package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

import java.util.List;

/**
 * @Description 技能载体
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/02/22
*/
public class SkillCharacterCarrierModel extends BaseConfig<Integer>
{
    private Integer carrierId;//载体ID
    private Integer carrierForm;//载体形态
    private Integer carrierType;//载体类型
    private Integer specialType;//特殊类型
    private List<Integer> formParm;//载体参数
    private Integer collision;//碰撞
    private Integer range;//射程
    private Integer repelType;//互斥类型
    private Integer hinderType;//障碍类型
    private Integer heightLocus;//轨迹
    private List<List<Integer>> targetType;//对象配置组合
    private List<List<Integer>> filterType;//条件与参数组合
    private Integer moveSpeed;//运动速度
    private Integer gAcceleration;//重力加速度
    private Integer effectTimes;//作用次数
    private Integer disappear;//够次数后消失
    private Integer durationTime;//持续时间
    private List<Integer> effectId;//效果ID
    private List<List<Integer>> effectIdReserve;//养成后效果ID

    public Integer getCarrierId()
    {
        return this.carrierId;
    }

    public void setCarrierId(Integer carrierId)
    {
        this.carrierId = carrierId;
    }

    public Integer getCarrierForm()
    {
        return this.carrierForm;
    }

    public void setCarrierForm(Integer carrierForm)
    {
        this.carrierForm = carrierForm;
    }

    public Integer getCarrierType()
    {
        return this.carrierType;
    }

    public void setCarrierType(Integer carrierType)
    {
        this.carrierType = carrierType;
    }

    public Integer getSpecialType()
    {
        return this.specialType;
    }

    public void setSpecialType(Integer specialType)
    {
        this.specialType = specialType;
    }

    public List<Integer> getFormParm()
    {
        return this.formParm;
    }

    public void setFormParm(List<Integer> formParm)
    {
        this.formParm = formParm;
    }

    public Integer getCollision()
    {
        return this.collision;
    }

    public void setCollision(Integer collision)
    {
        this.collision = collision;
    }

    public Integer getRange()
    {
        return this.range;
    }

    public void setRange(Integer range)
    {
        this.range = range;
    }

    public Integer getRepelType()
    {
        return this.repelType;
    }

    public void setRepelType(Integer repelType)
    {
        this.repelType = repelType;
    }

    public Integer getHinderType()
    {
        return this.hinderType;
    }

    public void setHinderType(Integer hinderType)
    {
        this.hinderType = hinderType;
    }

    public Integer getHeightLocus()
    {
        return this.heightLocus;
    }

    public void setHeightLocus(Integer heightLocus)
    {
        this.heightLocus = heightLocus;
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

    public Integer getMoveSpeed()
    {
        return this.moveSpeed;
    }

    public void setMoveSpeed(Integer moveSpeed)
    {
        this.moveSpeed = moveSpeed;
    }

    public Integer getGAcceleration()
    {
        return this.gAcceleration;
    }

    public void setGAcceleration(Integer gAcceleration)
    {
        this.gAcceleration = gAcceleration;
    }

    public Integer getEffectTimes()
    {
        return this.effectTimes;
    }

    public void setEffectTimes(Integer effectTimes)
    {
        this.effectTimes = effectTimes;
    }

    public Integer getDisappear()
    {
        return this.disappear;
    }

    public void setDisappear(Integer disappear)
    {
        this.disappear = disappear;
    }

    public Integer getDurationTime()
    {
        return this.durationTime;
    }

    public void setDurationTime(Integer durationTime)
    {
        this.durationTime = durationTime;
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

    @Override
    public Integer getKey()
    {
        return this.carrierId;
    }
}