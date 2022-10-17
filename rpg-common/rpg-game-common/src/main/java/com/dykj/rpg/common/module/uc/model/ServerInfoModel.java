package com.dykj.rpg.common.module.uc.model;

import com.dykj.rpg.db.annotation.Column;
import com.dykj.rpg.db.annotation.PrimaryKey;
import com.dykj.rpg.db.data.BaseModel;
import com.dykj.rpg.redis.cache.base.ICacheMapPrimaryKey;

import java.util.Date;

/**
 * 服务器信息
 */
public class ServerInfoModel extends BaseModel implements ICacheMapPrimaryKey {
    /**
     * 服务器Id
     */
    @Column(primaryKey = PrimaryKey.GENERAL)
    private int serverId;

    /**
     * 服务器名字
     */
    private String name;

    /**
     * 0:普通;1:新服;2:推荐
     */
    private int mark;

    /**
     * 是否为默认显示的服务器
     */
    private int defaultMark;


   private String host;

    /**
     * tcpPort
     */
   private int tcpPort;

    /***
     * httpPort
     */
   private int httpPort;

    /**
     * 在线人数上限
     */
    private int maxLoginCount;

    /**
     * 注册人数上限
     */
    private int maxRegCount;

    /**
     * 开服时间
     */
    private Date openTime;

    /**
     * 分区id
     */
    private int zoneId;

    /**
     * 维护公告
     */
    private String upkeepMessage;

    /**
     * 最低版本号
     */
    private String minVersion;

    /**
     * 最高版本号
     */
    private String maxVersion;
    /**
     * 标签
     */
    private int label;

    /***
     * ip:port;ip:port
     */
    private  String addressList;
    /**
     * 当前在用的ip:prot
     */
    private  String address;


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

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public int getDefaultMark() {
        return defaultMark;
    }

    public void setDefaultMark(int defaultMark) {
        this.defaultMark = defaultMark;
    }


    public int getMaxLoginCount() {
        return maxLoginCount;
    }

    public void setMaxLoginCount(int maxLoginCount) {
        this.maxLoginCount = maxLoginCount;
    }

    public int getMaxRegCount() {
        return maxRegCount;
    }

    public void setMaxRegCount(int maxRegCount) {
        this.maxRegCount = maxRegCount;
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

    public String getUpkeepMessage() {
        return upkeepMessage;
    }

    public void setUpkeepMessage(String upkeepMessage) {
        this.upkeepMessage = upkeepMessage;
    }

    public String getMinVersion() {
        return minVersion;
    }

    public void setMinVersion(String minVersion) {
        this.minVersion = minVersion;
    }

    public String getMaxVersion() {
        return maxVersion;
    }

    public void setMaxVersion(String maxVersion) {
        this.maxVersion = maxVersion;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    @Override
    public Object primaryKey() {
        return serverId;
    }

    public String getAddressList() {
        return addressList;
    }

    public void setAddressList(String addressList) {
        this.addressList = addressList;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
