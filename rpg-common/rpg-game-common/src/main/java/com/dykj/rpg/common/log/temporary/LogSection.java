package com.dykj.rpg.common.log.temporary;

import com.dykj.rpg.common.log.BaseLog;

import java.util.Date;

/**
 * @Description 玩家副本记录
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/16
 */
public class LogSection extends BaseLog
{
    private Date logDate;//日志时间
    private Integer serverId;//服务器ID
    private Integer playerId;//玩家ID
    private String userId;//账号ID
    private String channel;//渠道号
    private Integer level;//玩家等级
    private Integer vipLevel;//vip等级
    private Integer stageId;//副本id，t_s_stage_data.id
    private Integer win;//当是否胜利
    private Integer star;//星
    private String role;//角色列表，用 | 竖线分隔
    private String power;//角色战斗力列表，用 | 竖线分隔
    private Integer totalPower;//角色列表战斗力和
    private Integer useTime;//用时，单位秒

    public LogSection()
    {
    }

    public LogSection(Date logDate, Integer serverId, Integer playerId, String userId, String channel, Integer level, Integer vipLevel, Integer stageId, Integer win, Integer star, String role, String power, Integer totalPower, Integer useTime)
    {
        this.logDate = logDate;
        this.serverId = serverId;
        this.playerId = playerId;
        this.userId = userId;
        this.channel = channel;
        this.level = level;
        this.vipLevel = vipLevel;
        this.stageId = stageId;
        this.win = win;
        this.star = star;
        this.role = role;
        this.power = power;
        this.totalPower = totalPower;
        this.useTime = useTime;
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

    public Integer getPlayerId()
    {
        return playerId;
    }

    public void setPlayerId(Integer playerId)
    {
        this.playerId = playerId;
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

    public Integer getStageId()
    {
        return stageId;
    }

    public void setStageId(Integer stageId)
    {
        this.stageId = stageId;
    }

    public Integer getWin()
    {
        return win;
    }

    public void setWin(Integer win)
    {
        this.win = win;
    }

    public Integer getStar()
    {
        return star;
    }

    public void setStar(Integer star)
    {
        this.star = star;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    public String getPower()
    {
        return power;
    }

    public void setPower(String power)
    {
        this.power = power;
    }

    public Integer getTotalPower()
    {
        return totalPower;
    }

    public void setTotalPower(Integer totalPower)
    {
        this.totalPower = totalPower;
    }

    public Integer getUseTime()
    {
        return useTime;
    }

    public void setUseTime(Integer useTime)
    {
        this.useTime = useTime;
    }

    @Override
    public String toString()
    {
        return "LogSection{" + "logDate=" + logDate + ", serverId=" + serverId + ", playerId=" + playerId + ", userId='" + userId + '\'' + ", channel='" + channel + '\'' + ", level=" + level + ", vipLevel=" + vipLevel + ", stageId=" + stageId + ", win=" + win + ", star=" + star + ", role='" + role + '\'' + ", power='" + power + '\'' + ", totalPower=" + totalPower + ", useTime=" + useTime + '}';
    }
}