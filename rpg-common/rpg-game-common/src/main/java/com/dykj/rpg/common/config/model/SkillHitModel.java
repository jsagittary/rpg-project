package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

import java.util.List;

/**
 * @Description 技能hit
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/15
*/
public class SkillHitModel extends BaseConfig<Integer>
{
    private Integer hitId;//#id
    private String hitNameMyself;//命中方式名称
    private Integer hitPredicate;//命中判定方式
    private List<Float> hitShapeParam;//形状参数
    private List<Integer> hitBuffs;//命中后产生buff
    private Integer hitBuffHost;//命中后buff载体
    private List<Integer> hitBulletFiremount;//子弹出生挂载点
    private Integer hitBulletAutoTarget;//子弹是否自动锁定敌人
    private List<Integer> hitBulletParam;//子弹参数
    private Integer hitBulletThrough;//子弹是否击穿敌人
    private Integer hitAttributeEffect;//命中后产生直接属性效果
    private Integer hitVfxMount;//技能命中特效挂载点
    private Integer hitBulletFission;//技能子弹是否可在击中敌人后裂变（二次伤害）
    private List<Integer> hitFissionDetail;//裂变详情
    private Integer hitShare;//伤害是否均摊

    public Integer getHitId()
    {
        return this.hitId;
    }

    public void setHitId(Integer hitId)
    {
        this.hitId = hitId;
    }

    public String getHitNameMyself()
    {
        return this.hitNameMyself;
    }

    public void setHitNameMyself(String hitNameMyself)
    {
        this.hitNameMyself = hitNameMyself;
    }

    public Integer getHitPredicate()
    {
        return this.hitPredicate;
    }

    public void setHitPredicate(Integer hitPredicate)
    {
        this.hitPredicate = hitPredicate;
    }

    public List<Float> getHitShapeParam()
    {
        return this.hitShapeParam;
    }

    public void setHitShapeParam(List<Float> hitShapeParam)
    {
        this.hitShapeParam = hitShapeParam;
    }

    public List<Integer> getHitBuffs()
    {
        return this.hitBuffs;
    }

    public void setHitBuffs(List<Integer> hitBuffs)
    {
        this.hitBuffs = hitBuffs;
    }

    public Integer getHitBuffHost()
    {
        return this.hitBuffHost;
    }

    public void setHitBuffHost(Integer hitBuffHost)
    {
        this.hitBuffHost = hitBuffHost;
    }

    public List<Integer> getHitBulletFiremount()
    {
        return this.hitBulletFiremount;
    }

    public void setHitBulletFiremount(List<Integer> hitBulletFiremount)
    {
        this.hitBulletFiremount = hitBulletFiremount;
    }

    public Integer getHitBulletAutoTarget()
    {
        return this.hitBulletAutoTarget;
    }

    public void setHitBulletAutoTarget(Integer hitBulletAutoTarget)
    {
        this.hitBulletAutoTarget = hitBulletAutoTarget;
    }

    public List<Integer> getHitBulletParam()
    {
        return this.hitBulletParam;
    }

    public void setHitBulletParam(List<Integer> hitBulletParam)
    {
        this.hitBulletParam = hitBulletParam;
    }

    public Integer getHitBulletThrough()
    {
        return this.hitBulletThrough;
    }

    public void setHitBulletThrough(Integer hitBulletThrough)
    {
        this.hitBulletThrough = hitBulletThrough;
    }

    public Integer getHitAttributeEffect()
    {
        return this.hitAttributeEffect;
    }

    public void setHitAttributeEffect(Integer hitAttributeEffect)
    {
        this.hitAttributeEffect = hitAttributeEffect;
    }

    public Integer getHitVfxMount()
    {
        return this.hitVfxMount;
    }

    public void setHitVfxMount(Integer hitVfxMount)
    {
        this.hitVfxMount = hitVfxMount;
    }

    public Integer getHitBulletFission()
    {
        return this.hitBulletFission;
    }

    public void setHitBulletFission(Integer hitBulletFission)
    {
        this.hitBulletFission = hitBulletFission;
    }

    public List<Integer> getHitFissionDetail()
    {
        return this.hitFissionDetail;
    }

    public void setHitFissionDetail(List<Integer> hitFissionDetail)
    {
        this.hitFissionDetail = hitFissionDetail;
    }

    public Integer getHitShare()
    {
        return this.hitShare;
    }

    public void setHitShare(Integer hitShare)
    {
        this.hitShare = hitShare;
    }

    @Override
    public Integer getKey()
    {
        return this.hitId;
    }
}