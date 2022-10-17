package com.dykj.rpg.common.log.temporary;

import com.dykj.rpg.common.log.BaseLog;

import java.util.Date;

/**
 * @Description 装备日志表
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/16
 */
public class LogEquip extends BaseLog
{
    private Date logDate;//日志时间
    private Integer serverId;//服务器ID
    private Integer roleId;//玩家ID
    private Integer userId;//账号ID
    private String channel;//渠道号
    private Integer level;//玩家等级
    private Integer vipLevel;//vip等级
    private Long insId;
    private Integer tplId;
    private Integer opType;
    private Integer type;//类型
    private Integer kind;//奖励
    private Integer changeType;

    public LogEquip()
    {
    }

    public LogEquip(Date logDate, Integer serverId, Integer roleId, Integer userId, String channel, Integer level, Integer vipLevel, Long insId, Integer tplId, Integer opType, Integer type, Integer kind, Integer changeType)
    {
        this.logDate = logDate;
        this.serverId = serverId;
        this.roleId = roleId;
        this.userId = userId;
        this.channel = channel;
        this.level = level;
        this.vipLevel = vipLevel;
        this.insId = insId;
        this.tplId = tplId;
        this.opType = opType;
        this.type = type;
        this.kind = kind;
        this.changeType = changeType;
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

    public Long getInsId()
    {
        return insId;
    }

    public void setInsId(Long insId)
    {
        this.insId = insId;
    }

    public Integer getTplId()
    {
        return tplId;
    }

    public void setTplId(Integer tplId)
    {
        this.tplId = tplId;
    }

    public Integer getOpType()
    {
        return opType;
    }

    public void setOpType(Integer opType)
    {
        this.opType = opType;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getKind()
    {
        return kind;
    }

    public void setKind(Integer kind)
    {
        this.kind = kind;
    }

    public Integer getChangeType()
    {
        return changeType;
    }

    public void setChangeType(Integer changeType)
    {
        this.changeType = changeType;
    }

    @Override
    public String toString()
    {
        return "LogEquip{" + "logDate=" + logDate + ", serverId=" + serverId + ", roleId=" + roleId + ", userId=" + userId + ", channel='" + channel + '\'' + ", level=" + level + ", vipLevel=" + vipLevel + ", insId=" + insId + ", tplId=" + tplId + ", opType=" + opType + ", type=" + type + ", kind=" + kind + ", changeType=" + changeType + '}';
    }
}