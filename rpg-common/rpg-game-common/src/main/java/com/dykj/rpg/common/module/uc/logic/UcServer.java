package com.dykj.rpg.common.module.uc.logic;

import com.dykj.rpg.common.module.uc.model.ServerModel;

import java.util.Date;

public class UcServer {
    /**
     * 服务器id
     */
    private int serverId;

    /**
     * 服务器名字
     */
    private String name;

    /***
     * 开服时间
     */
    private Date openTime;

    /**
     * 大区id
     */
    private int zoneId;
    /**
     * 大区标签
     */
    private int label;

    /**
     * 0:普通;1:新服;2:推荐'
     */
    private int mark;

    /**
     * 游戏服务器ip
     */
    private String host;

    /**
     * 游戏服端口
     */
    private int port;


    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


   /* public UcServer(ServerModel serverInfoModel) {
        this.serverId = serverInfoModel.getServerId();
        this.name = serverInfoModel.getName();
        this.openTime = serverInfoModel.getOpenTime();
        this.zoneId = serverInfoModel.getZoneId();
        this.label = serverInfoModel.getLabel();
        this.mark = serverInfoModel.getMark();
        this.host = serverInfoModel.getHost();
        this.port = serverInfoModel.getTcpPort();
    }
*/

    public UcServer(ServerModel serverInfoModel) {
        this.serverId = serverInfoModel.getServerId();
        this.name = serverInfoModel.getName();
        this.openTime = serverInfoModel.getOpenTime();
        this.zoneId = serverInfoModel.getZoneId();
        this.label = serverInfoModel.getLabel();
        this.mark = serverInfoModel.getMark();
//        this.host = serverInfoModel.getHost();
//        this.port = serverInfoModel.getTcpPort();
    }


    public UcServer() {
    }
}
