package com.dykj.rpg.common.log.temporary;

import com.dykj.rpg.common.log.BaseLog;

import java.util.Date;

/**
 * @Description 资源
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/16
 */
public class LogRresource extends BaseLog
{
    private Date logDate;//日志时间
    private Integer serverId;//服务器ID
    private Integer roleId;//玩家ID
    private String userId;//账号ID
    private String channel;//渠道号
    private Integer level;//玩家等级
    private Integer vipLevel;//vip等级
    private Integer type;//操作类型
    private Integer current;//当前数量
    private Integer change;//变化的数量
    private Integer opType;//操作类型子类型

    public LogRresource()
    {
    }

    public LogRresource(Date logDate, Integer serverId, Integer roleId, String userId, String channel, Integer level, Integer vipLevel, Integer type, Integer current, Integer change, Integer opType)
    {
        this.logDate = logDate;
        this.serverId = serverId;
        this.roleId = roleId;
        this.userId = userId;
        this.channel = channel;
        this.level = level;
        this.vipLevel = vipLevel;
        this.type = type;
        this.current = current;
        this.change = change;
        this.opType = opType;
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

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getCurrent()
    {
        return current;
    }

    public void setCurrent(Integer current)
    {
        this.current = current;
    }

    public Integer getChange()
    {
        return change;
    }

    public void setChange(Integer change)
    {
        this.change = change;
    }

    public Integer getOpType()
    {
        return opType;
    }

    public void setOpType(Integer opType)
    {
        this.opType = opType;
    }

    @Override
    public String toString()
    {
        return "LogRresource{" + "logDate=" + logDate + ", serverId=" + serverId + ", roleId=" + roleId + ", userId='" + userId + '\'' + ", channel='" + channel + '\'' + ", level=" + level + ", vipLevel=" + vipLevel + ", type=" + type + ", current=" + current + ", change=" + change + ", opType=" + opType + '}';
    }
}