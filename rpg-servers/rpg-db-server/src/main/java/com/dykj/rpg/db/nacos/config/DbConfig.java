package com.dykj.rpg.db.nacos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author jyb
 * @date 2021/1/14 15:47
 * @Description
 */
@Component
public class DbConfig {

    @Value("${db.server.node}")
    private int dbNode;

    @Value("${server.path}")
    private String path;

    @Value("${serverId}")
    private int serverId;
    @Value("${db.jetty.port}")
    private int jettyPort;

    public int getDbNode() {
        return dbNode;
    }

    public void setDbNode(int dbNode) {
        this.dbNode = dbNode;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getJettyPort() {
        return jettyPort;
    }

    public void setJettyPort(int jettyPort) {
        this.jettyPort = jettyPort;
    }
}