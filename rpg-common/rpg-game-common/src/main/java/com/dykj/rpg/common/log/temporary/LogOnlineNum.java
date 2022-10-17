package com.dykj.rpg.common.log.temporary;

import com.dykj.rpg.common.log.BaseLog;

import java.util.Date;

/**
 * @Description 在线人数日志
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/16
 */
public class LogOnlineNum extends BaseLog
{
    private Date logDate;//日志时间
    private Integer serverId;//服务器ID
    private Integer number;//在线人数

    public LogOnlineNum()
    {
    }

    public LogOnlineNum(Date logDate, Integer serverId, Integer number)
    {
        this.logDate = logDate;
        this.serverId = serverId;
        this.number = number;
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

    public Integer getNumber()
    {
        return number;
    }

    public void setNumber(Integer number)
    {
        this.number = number;
    }

    @Override
    public String toString()
    {
        return "LogOnlineNum{" + "logDate=" + logDate + ", serverId=" + serverId + ", number=" + number + '}';
    }
}