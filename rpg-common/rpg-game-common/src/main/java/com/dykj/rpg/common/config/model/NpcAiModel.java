package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

/**
 * @Description ai类型
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/02/04
*/
public class NpcAiModel extends BaseConfig<Integer>
{
    private Integer aiId;//编号
    private Float aiViewRange;//视野范围
    private Integer aiAlertTime;//警觉时间
    private Float aiAlertRange;//警觉范围
    private Integer liveTime;//存活时间
    private Float moveRange;//随机移动范围
    private Integer miniMoveTime;//最小移动时间
    private Integer maxMoveTime;//最大移动时间
    private Integer breakWarTime;//脱战时间
    private Float breakWarRange;//脱战范围
    private Float walkSpeed;//步行速度
    private Float runSpeed;//奔跑速度
    private Float pursueRange;//追击距离
    private Float lockRange;//锁敌人距离
    private Float hatredRange;//仇恨范围
    private Integer patrolTime;//巡逻点停留时间

    public Integer getAiId()
    {
        return this.aiId;
    }

    public void setAiId(Integer aiId)
    {
        this.aiId = aiId;
    }

    public Float getAiViewRange()
    {
        return this.aiViewRange;
    }

    public void setAiViewRange(Float aiViewRange)
    {
        this.aiViewRange = aiViewRange;
    }

    public Integer getAiAlertTime()
    {
        return this.aiAlertTime;
    }

    public void setAiAlertTime(Integer aiAlertTime)
    {
        this.aiAlertTime = aiAlertTime;
    }

    public Float getAiAlertRange()
    {
        return this.aiAlertRange;
    }

    public void setAiAlertRange(Float aiAlertRange)
    {
        this.aiAlertRange = aiAlertRange;
    }

    public Integer getLiveTime()
    {
        return this.liveTime;
    }

    public void setLiveTime(Integer liveTime)
    {
        this.liveTime = liveTime;
    }

    public Float getMoveRange()
    {
        return this.moveRange;
    }

    public void setMoveRange(Float moveRange)
    {
        this.moveRange = moveRange;
    }

    public Integer getMiniMoveTime()
    {
        return this.miniMoveTime;
    }

    public void setMiniMoveTime(Integer miniMoveTime)
    {
        this.miniMoveTime = miniMoveTime;
    }

    public Integer getMaxMoveTime()
    {
        return this.maxMoveTime;
    }

    public void setMaxMoveTime(Integer maxMoveTime)
    {
        this.maxMoveTime = maxMoveTime;
    }

    public Integer getBreakWarTime()
    {
        return this.breakWarTime;
    }

    public void setBreakWarTime(Integer breakWarTime)
    {
        this.breakWarTime = breakWarTime;
    }

    public Float getBreakWarRange()
    {
        return this.breakWarRange;
    }

    public void setBreakWarRange(Float breakWarRange)
    {
        this.breakWarRange = breakWarRange;
    }

    public Float getWalkSpeed()
    {
        return this.walkSpeed;
    }

    public void setWalkSpeed(Float walkSpeed)
    {
        this.walkSpeed = walkSpeed;
    }

    public Float getRunSpeed()
    {
        return this.runSpeed;
    }

    public void setRunSpeed(Float runSpeed)
    {
        this.runSpeed = runSpeed;
    }

    public Float getPursueRange()
    {
        return this.pursueRange;
    }

    public void setPursueRange(Float pursueRange)
    {
        this.pursueRange = pursueRange;
    }

    public Float getLockRange()
    {
        return this.lockRange;
    }

    public void setLockRange(Float lockRange)
    {
        this.lockRange = lockRange;
    }

    public Float getHatredRange()
    {
        return this.hatredRange;
    }

    public void setHatredRange(Float hatredRange)
    {
        this.hatredRange = hatredRange;
    }

    public Integer getPatrolTime()
    {
        return this.patrolTime;
    }

    public void setPatrolTime(Integer patrolTime)
    {
        this.patrolTime = patrolTime;
    }

    @Override
    public Integer getKey()
    {
        return this.aiId;
    }
}