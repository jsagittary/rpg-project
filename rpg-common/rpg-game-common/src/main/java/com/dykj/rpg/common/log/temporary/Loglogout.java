package com.dykj.rpg.common.log.temporary;

import com.dykj.rpg.common.log.BaseLog;

import java.util.Date;

/**
 * @Description 登出日志
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/16
 */
public class Loglogout extends BaseLog
{
    private Date logDate;//日志时间
    private Integer serverId;//服务器ID
    private Integer roleId;//玩家ID
    private String userId;//账号ID
    private String channel;//渠道号
    private Integer level;//玩家等级
    private Integer vipLevel;//vip等级
    private Date logonDate;
    private Integer olSeconds;
    private Integer teamExp;

    public Loglogout()
    {
    }

    public Loglogout(Date logDate, Integer serverId, Integer roleId, String userId, String channel, Integer level, Integer vipLevel, Date logonDate, Integer olSeconds, Integer teamExp)
    {
        this.logDate = logDate;
        this.serverId = serverId;
        this.roleId = roleId;
        this.userId = userId;
        this.channel = channel;
        this.level = level;
        this.vipLevel = vipLevel;
        this.logonDate = logonDate;
        this.olSeconds = olSeconds;
        this.teamExp = teamExp;
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

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
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

    public Date getLogonDate()
    {
        return logonDate;
    }

    public void setLogonDate(Date logonDate)
    {
        this.logonDate = logonDate;
    }

    public Integer getOlSeconds()
    {
        return olSeconds;
    }

    public void setOlSeconds(Integer olSeconds)
    {
        this.olSeconds = olSeconds;
    }

    public Integer getTeamExp()
    {
        return teamExp;
    }

    public void setTeamExp(Integer teamExp)
    {
        this.teamExp = teamExp;
    }

    @Override
    public String toString()
    {
        return "Loglogout{" + "logDate=" + logDate + ", serverId=" + serverId + ", roleId=" + roleId + ", userId='" + userId + '\'' + ", channel='" + channel + '\'' + ", level=" + level + ", vipLevel=" + vipLevel + ", logonDate=" + logonDate + ", olSeconds=" + olSeconds + ", teamExp=" + teamExp + '}';
    }
}