package com.dykj.rpg.common.log;

import java.util.Date;

/**
 * @author jyb
 * @date 2020/11/12 11:29
 * @Description
 */
public class LogoutLog extends BaseLog {

    private Date logDate;

    private int serverId;

    private int roleId;

    private int userId;

    private String channel;

    private int level;

    private int vipLevel;

    private Date logonDate;

    private int olSeconds;

    private int teamExp;

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public Date getLogonDate() {
        return logonDate;
    }

    public void setLogonDate(Date logonDate) {
        this.logonDate = logonDate;
    }

    public int getOlSeconds() {
        return olSeconds;
    }

    public void setOlSeconds(int olSeconds) {
        this.olSeconds = olSeconds;
    }

    public int getTeamExp() {
        return teamExp;
    }

    public void setTeamExp(int teamExp) {
        this.teamExp = teamExp;
    }
}