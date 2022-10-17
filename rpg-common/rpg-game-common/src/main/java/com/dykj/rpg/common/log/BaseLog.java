package com.dykj.rpg.common.log;

/**
 * @author jyb
 * @date 2020/11/12 11:16
 * @Description
 */
public class BaseLog {
    /**
     *游戏ID（末日血战，冒险世界 等）
     */
    private String game_id = "烛光勇士";
    /**
     * 业务ID （3399,3367）
     */
    private int biz_id = 3399;
    /**
     * 产品线ID
     */
    private int prod_id = 6666;
    /**
     * 系统版本（Android，iOS，H5等）
     */
    private String os = "Android";
    /**
     *设备ID
     */
    private String uuid ="PONJKLLJDA";
    /**
     * ip
     */
    private String ip = "192.168.0.37";

    /**
     * IOS 6+的设备id字段
     */
    private String idfa = "xxxaaakkk";

    /**
     * 安卓的设备 ID 的 md5 摘要
     */
    private String imei = "ooopppoo";
    /**
     * Android Q及更高版本的设备号
     */
    private String oaid = "xxxxxxxxx";
    /**
     * 安卓id原值的md5
     */
    private String androidid = "EDF98DSJSAD";
    /**
     * 移动设备mac地址,转换成大写字母,去掉“:”，并且取md5摘要后的结果
     */
    private String mac = "RTDDWDQDWDASD";

    private String ua = "";

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    public int getBiz_id() {
        return biz_id;
    }

    public void setBiz_id(int biz_id) {
        this.biz_id = biz_id;
    }

    public int getProd_id() {
        return prod_id;
    }

    public void setProd_id(int prod_id) {
        this.prod_id = prod_id;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIdfa() {
        return idfa;
    }

    public void setIdfa(String idfa) {
        this.idfa = idfa;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getOaid() {
        return oaid;
    }

    public void setOaid(String oaid) {
        this.oaid = oaid;
    }

    public String getAndroidid() {
        return androidid;
    }

    public void setAndroidid(String androidid) {
        this.androidid = androidid;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }
}