package com.dykj.rpg.common.module.uc.model;

import com.dykj.rpg.db.annotation.Column;
import com.dykj.rpg.db.annotation.PrimaryKey;
import com.dykj.rpg.db.data.BaseModel;
import com.dykj.rpg.redis.cache.base.ICacheMapPrimaryKey;
import com.dykj.rpg.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author jyb
 * @date 2020/12/31 14:25
 * @Description
 */
public class ServerModel extends BaseModel implements ICacheMapPrimaryKey {
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

    /**
     * 账号白名单
     */
    private String accountWhite;

    /**
     * ip白名单
     */
    private String ipWhite;


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

    public String getAccountWhite() {
        return accountWhite;
    }

    public void setAccountWhite(String accountWhite) {
        this.accountWhite = accountWhite;
    }

    public String getIpWhite() {
        return ipWhite;
    }

    public void setIpWhite(String ipWhite) {
        this.ipWhite = ipWhite;
    }


    public List<String> getAccountWhiteLists() {
        List<String> accountWhiteList = null;
        if (accountWhite != null && !accountWhite.equals("")) {
            try {
                accountWhiteList = JsonUtil.toList(accountWhite, String.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return accountWhiteList;
    }

    public List<String> getIpWhiteLists() {
        List<String> ipWhiteList = null;
        if (ipWhite != null && !ipWhite.equals("")) {
            try {
                ipWhiteList = JsonUtil.toList(ipWhite, String.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ipWhiteList;
    }

    @Override
    public Object primaryKey() {
        return serverId;
    }

    @Override
    public String toString() {
        return "ServerModel{" +
                "serverId=" + serverId +
                ", name='" + name + '\'' +
                ", mark=" + mark +
                ", defaultMark=" + defaultMark +
                ", maxLoginCount=" + maxLoginCount +
                ", maxRegCount=" + maxRegCount +
                ", openTime=" + openTime +
                ", zoneId=" + zoneId +
                ", upkeepMessage='" + upkeepMessage + '\'' +
                ", minVersion='" + minVersion + '\'' +
                ", maxVersion='" + maxVersion + '\'' +
                ", label=" + label +
                ", accountWhite='" + accountWhite + '\'' +
                ", ipWhite='" + ipWhite + '\'' +
                '}';
    }

    public static void main(String[] args) {
        List<String> ips = new ArrayList<>();
        ips.add("192.168.0.101");

        List<String> accounts = new ArrayList<>();
        accounts.add("张三");

        System.err.println(JsonUtil.toJsonString(ips));
        System.err.println(JsonUtil.toJsonString(accounts));
    }
}