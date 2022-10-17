package com.dykj.rpg.common.log.temporary;

import com.dykj.rpg.common.log.BaseLog;

import java.util.Date;

/**
 * @Description 玩家可恢复资源变更日志表
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/16
 */
public class LogPlayerRes extends BaseLog
{
    private Date logDate;//日志时间
    private Integer serverId;//服务器ID
    private Integer roleId;//玩家ID
    private String userId;//账号ID
    private String channel;//渠道号
    private Integer level;//玩家等级
    private Integer vipLevel;//vip等级
    private Integer type;//类型
    private Integer current;//剩余
    private Integer max;//最大
    private Integer change;//修改值
    private Integer costType;//购买才具有非0的值，购买消耗资源类型
    private Integer cost;//购买消耗

    public LogPlayerRes()
    {
    }

    public LogPlayerRes(Date logDate, Integer serverId, Integer roleId, String userId, String channel, Integer level, Integer vipLevel, Integer type, Integer current, Integer max, Integer change, Integer costType, Integer cost)
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
        this.max = max;
        this.change = change;
        this.costType = costType;
        this.cost = cost;
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

    public Integer getMax()
    {
        return max;
    }

    public void setMax(Integer max)
    {
        this.max = max;
    }

    public Integer getChange()
    {
        return change;
    }

    public void setChange(Integer change)
    {
        this.change = change;
    }

    public Integer getCostType()
    {
        return costType;
    }

    public void setCostType(Integer costType)
    {
        this.costType = costType;
    }

    public Integer getCost()
    {
        return cost;
    }

    public void setCost(Integer cost)
    {
        this.cost = cost;
    }

    @Override
    public String toString()
    {
        return "LogPlayerRes{" + "logDate=" + logDate + ", serverId=" + serverId + ", roleId=" + roleId + ", userId='" + userId + '\'' + ", channel='" + channel + '\'' + ", level=" + level + ", vipLevel=" + vipLevel + ", type=" + type + ", current=" + current + ", max=" + max + ", change=" + change + ", costType=" + costType + ", cost=" + cost + '}';
    }
}