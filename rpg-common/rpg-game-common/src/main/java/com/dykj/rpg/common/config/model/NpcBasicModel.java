package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

import java.util.List;

/**
 * @Description NPC基础表
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/02/04
*/
public class NpcBasicModel extends BaseConfig<Integer>
{
    private Integer npcId;//编号
    private Integer monstType;//怪物类型
    private Integer growClass;//属性职业
    private Integer aiId;//AI类型
    private List<Integer> npcSkill;//技能列表
    private Integer collisionRange;//碰撞范围
    private Integer bornDuration;//出生时间
    private Integer hitTimesStun;//产生硬直的受击次数
    private Integer stunTime;//硬直时长

    public Integer getNpcId()
    {
        return this.npcId;
    }

    public void setNpcId(Integer npcId)
    {
        this.npcId = npcId;
    }

    public Integer getMonstType()
    {
        return this.monstType;
    }

    public void setMonstType(Integer monstType)
    {
        this.monstType = monstType;
    }

    public Integer getGrowClass()
    {
        return this.growClass;
    }

    public void setGrowClass(Integer growClass)
    {
        this.growClass = growClass;
    }

    public Integer getAiId()
    {
        return this.aiId;
    }

    public void setAiId(Integer aiId)
    {
        this.aiId = aiId;
    }

    public List<Integer> getNpcSkill()
    {
        return this.npcSkill;
    }

    public void setNpcSkill(List<Integer> npcSkill)
    {
        this.npcSkill = npcSkill;
    }

    public Integer getCollisionRange()
    {
        return this.collisionRange;
    }

    public void setCollisionRange(Integer collisionRange)
    {
        this.collisionRange = collisionRange;
    }

    public Integer getBornDuration()
    {
        return this.bornDuration;
    }

    public void setBornDuration(Integer bornDuration)
    {
        this.bornDuration = bornDuration;
    }

    public Integer getHitTimesStun()
    {
        return this.hitTimesStun;
    }

    public void setHitTimesStun(Integer hitTimesStun)
    {
        this.hitTimesStun = hitTimesStun;
    }

    public Integer getStunTime()
    {
        return this.stunTime;
    }

    public void setStunTime(Integer stunTime)
    {
        this.stunTime = stunTime;
    }

    @Override
    public Integer getKey()
    {
        return this.npcId;
    }
}