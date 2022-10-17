package com.dykj.rpg.common.log.temporary;

import com.dykj.rpg.common.log.BaseLog;

import java.util.Date;

/**
 * @Description 活动日志表
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/16
 */
public class LogActivity extends BaseLog
{
    private Date logDate;//日志时间
    private Integer serverId;//服务器ID
    private Integer roleId;//玩家ID
    private Integer userId;//账号ID
    private String channel;//渠道号
    private Integer level;//玩家等级
    private Integer vipLevel;//vip等级
    private Integer type;//类型
    private String reward;//奖励

    public LogActivity(){};

    public LogActivity(Date logDate, Integer serverId, Integer roleId, Integer userId, String channel, Integer level, Integer vipLevel, Integer type, String reward)
    {
        this.logDate = logDate;
        this.serverId = serverId;
        this.roleId = roleId;
        this.userId = userId;
        this.channel = channel;
        this.level = level;
        this.vipLevel = vipLevel;
        this.type = type;
        this.reward = reward;
    }

    public Date getLogDate()
    {
        return logDate;
    }

    public void setLogDate(Date logDate)
    {
        this.logDate = logDate;
    }

    public Integer getServerId()
    {
        return serverId;
    }

    public void setServerId(Integer serverId)
    {
        this.serverId = serverId;
    }

    public Integer getRoleId()
    {
        return roleId;
    }

    public void setRoleId(Integer roleId)
    {
        this.roleId = roleId;
    }

    public Integer getUserId()
    {
        return userId;
    }

    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public String getChannel()
    {
        return channel;
    }

    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    public Integer getLevel()
    {
        return level;
    }

    public void setLevel(Integer level)
    {
        this.level = level;
    }

    public Integer getVipLevel()
    {
        return vipLevel;
    }

    public void setVipLevel(Integer vipLevel)
    {
        this.vipLevel = vipLevel;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public String getReward()
    {
        return reward;
    }

    public void setReward(String reward)
    {
        this.reward = reward;
    }

    @Override
    public String toString()
    {
        return "LogActivity{" + "logDate=" + logDate + ", serverId=" + serverId + ", roleId=" + roleId + ", userId=" + userId + ", channel='" + channel + '\'' + ", level=" + level + ", vipLevel=" + vipLevel + ", type=" + type + ", reward='" + reward + '\'' + '}';
    }
}