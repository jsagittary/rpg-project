package com.dykj.rpg.uc.nacos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author jyb
 * @date 2020/12/29 20:26
 * @Description
 */
@Component
public class UcConfig {

    @Value("${uc.nodes}")
    private String ucNodes;

    @Value("${server.path}")
    private String path;

    public String getUcNodes() {
        return ucNodes;
    }

    public void setUcNodes(String ucNodes) {
        this.ucNodes = ucNodes;
    }

    private String[] getPhysicalMachineIds() {
        if (ucNodes == null || ucNodes.equals("")) {
            return null;
        }
        String[] nodes = ucNodes.split("\\,");
        return nodes;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}