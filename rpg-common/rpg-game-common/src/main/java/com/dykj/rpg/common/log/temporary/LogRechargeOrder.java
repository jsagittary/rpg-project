package com.dykj.rpg.common.log.temporary;

import com.dykj.rpg.common.log.BaseLog;

import java.util.Date;

/**
 * @Description 订单日志表
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/16
 */
public class LogRechargeOrder extends BaseLog
{
    private Date logDate;//日志时间
    private Integer serverId;//服务器ID
    private Integer roleId;//玩家ID
    private String userId;//账号ID
    private String channel;//渠道号
    private Integer level;//玩家等级
    private Integer vipLevel;//vip等级
    private Integer rechargeId;//平台订单号
    private Integer price;//价格
    private Integer sycee;//元宝数量
    private Integer first;//是否是首冲
    private Integer firstById;//第一次购买的id
    private Integer manual;
    private String orderNum;//订单号

    public LogRechargeOrder()
    {
    }

    public LogRechargeOrder(Date logDate, Integer serverId, Integer roleId, String userId, String channel, Integer level, Integer vipLevel, Integer rechargeId, Integer price, Integer sycee, Integer first, Integer firstById, Integer manual, String orderNum)
    {
        this.logDate = logDate;
        this.serverId = serverId;
        this.roleId = roleId;
        this.userId = userId;
        this.channel = channel;
        this.level = level;
        this.vipLevel = vipLevel;
        this.rechargeId = rechargeId;
        this.price = price;
        this.sycee = sycee;
        this.first = first;
        this.firstById = firstById;
        this.manual = manual;
        this.orderNum = orderNum;
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

    public Integer getRechargeId()
    {
        return rechargeId;
    }

    public void setRechargeId(Integer rechargeId)
    {
        this.rechargeId = rechargeId;
    }

    public Integer getPrice()
    {
        return price;
    }

    public void setPrice(Integer price)
    {
        this.price = price;
    }

    public Integer getSycee()
    {
        return sycee;
    }

    public void setSycee(Integer sycee)
    {
        this.sycee = sycee;
    }

    public Integer getFirst()
    {
        return first;
    }

    public void setFirst(Integer first)
    {
        this.first = first;
    }

    public Integer getFirstById()
    {
        return firstById;
    }

    public void setFirstById(Integer firstById)
    {
        this.firstById = firstById;
    }

    public Integer getManual()
    {
        return manual;
    }

    public void setManual(Integer manual)
    {
        this.manual = manual;
    }

    public String getOrderNum()
    {
        return orderNum;
    }

    public void setOrderNum(String orderNum)
    {
        this.orderNum = orderNum;
    }

    @Override
    public String toString()
    {
        return "LogRechargeOrder{" + "logDate=" + logDate + ", serverId=" + serverId + ", roleId=" + roleId + ", userId='" + userId + '\'' + ", channel='" + channel + '\'' + ", level=" + level + ", vipLevel=" + vipLevel + ", rechargeId=" + rechargeId + ", price=" + price + ", sycee=" + sycee + ", first=" + first + ", firstById=" + firstById + ", manual=" + manual + ", orderNum='" + orderNum + '\'' + '}';
    }
}