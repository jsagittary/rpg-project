package com.dykj.rpg.common.module.uc.logic;

import java.util.ArrayList;
import java.util.List;

public class ServerList {

    /***
     * 所有服务器
     */
    private List<UcServer> ucServers;
    /**
     * 最后登录服务器id
     */
    private  int lastServerId;

    /**
     * 服务端生成的唯一key
     */
    private int accountKey;


    public ServerList() {
        ucServers = new ArrayList<>();
    }

    public List<UcServer> getUcServers() {
        return ucServers;
    }

    public void setUcServers(List<UcServer> ucServers) {
        this.ucServers = ucServers;
    }

    public int getLastServerId() {
        return lastServerId;
    }

    public void setLastServerId(int lastServerId) {
        this.lastServerId = lastServerId;
    }

    public int getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(int accountKey) {
        this.accountKey = accountKey;
    }
}
