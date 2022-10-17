package com.dykj.rpg.game.module.server.logic;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: jyb
 * @Date: 2020/9/7 10:33
 * @Description:
 */
@Component
public class GameServerConfig {
    @Value("${serverId}")
    private int serverId;
    @Value("${uc.url}")
    private String ucUrl;

    @Value("${game.jdbcUrl}")
    private String gameJdbcUrl;

    @Value("${server.model}")
    private int serverModel;

    @Value("${server.nodes}")
    private String serverNodes;

    @Value("${server.path}")
    private String path;


    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getUcUrl() {
        return ucUrl;
    }

    public void setUcUrl(String ucUrl) {
        this.ucUrl = ucUrl;
    }

    public String getGameJdbcUrl() {
        return gameJdbcUrl;
    }

    public void setGameJdbcUrl(String gameJdbcUrl) {
        this.gameJdbcUrl = gameJdbcUrl;
    }

    public int getServerModel() {
        return serverModel;
    }

    public void setServerModel(int serverModel) {
        this.serverModel = serverModel;
    }

    public int[] getNodes() {
        String[] nodes = serverNodes.split("\\,");
        int[] result = new int[nodes.length];
        int i = 0;
        for (String node : nodes) {
            result[i] = Integer.valueOf(nodes[i]);
            i++;
        }
        return result;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
