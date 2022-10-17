package com.dykj.rpg.client.config;

import com.dykj.rpg.common.module.uc.logic.UcServer;

public class LoginServerInfo implements  Comparable<LoginServerInfo>{

    private UcServer server;

    @Override
    public int compareTo(LoginServerInfo o) {
        if (this.time>o.time){
            return  1;
        }
        return -1;
    }

    private int  time;

    public UcServer getServer() {
        return server;
    }

    public void setServer(UcServer server) {
        this.server = server;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public LoginServerInfo(UcServer server, int time) {
        this.server = server;
        this.time = time;
    }
}
