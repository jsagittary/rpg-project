package com.dykj.rpg.client.config;

public class Server {
    private String address;
    private int load;
    private String upkeepMessage;
    private String name;
    private int zoneId;
    private int serverId;
    private int mark;
    private boolean openServer;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    public String getUpkeepMessage() {
        return upkeepMessage;
    }

    public void setUpkeepMessage(String upkeepMessage) {
        this.upkeepMessage = upkeepMessage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public boolean isOpenServer() {
        return openServer;
    }

    public void setOpenServer(boolean openServer) {
        this.openServer = openServer;
    }

    @Override
    public String toString() {
        return "Server{" +
                "address='" + address + '\'' +
                ", load=" + load +
                ", upkeepMessage='" + upkeepMessage + '\'' +
                ", name='" + name + '\'' +
                ", zoneId=" + zoneId +
                ", serverId=" + serverId +
                ", mark=" + mark +
                ", openServer=" + openServer +
                '}';
    }
}
