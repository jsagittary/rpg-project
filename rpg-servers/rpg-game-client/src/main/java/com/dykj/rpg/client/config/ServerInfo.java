package com.dykj.rpg.client.config;

import java.util.List;

public class ServerInfo {
    private List<Zone> zone_list;

    private int defaultServerId;

    private int lastLoginServerId;

    private List<Server> server_list;

    public List<Zone> getZone_list() {
        return zone_list;
    }

    public void setZone_list(List<Zone> zone_list) {
        this.zone_list = zone_list;
    }

    public int getDefaultServerId() {
        return defaultServerId;
    }

    public void setDefaultServerId(int defaultServerId) {
        this.defaultServerId = defaultServerId;
    }

    public int getLastLoginServerId() {
        return lastLoginServerId;
    }

    public void setLastLoginServerId(int lastLoginServerId) {
        this.lastLoginServerId = lastLoginServerId;
    }

    public List<Server> getServer_list() {
        return server_list;
    }

    public void setServer_list(List<Server> server_list) {
        this.server_list = server_list;
    }
}
