package com.dykj.rpg.common.redis.data;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.dykj.rpg.common.consts.ServerKey;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author jyb
 * @date 2020/12/31 14:40
 * @Description
 */
public class InstanceData {
    private String instanceId;
    private String ip;
    private int port;
    private boolean healthy = true;
    private Map<String, String> metadata = new HashMap();
    private int serverId;
    private int  serverModel;


    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isHealthy() {
        return healthy;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public InstanceData(Instance instance,int serverModel,int serverId) {
        instanceId = instance.getInstanceId();
        ip = instance.getIp();
        port = instance.getPort();
        healthy = instance.isHealthy();
        metadata.putAll(instance.getMetadata());
        this.serverModel = serverModel;
        this.serverId =serverId;
    }


    public InstanceData(Instance instance,int serverId) {
        instanceId = instance.getInstanceId();
        ip = instance.getIp();
        port = instance.getPort();
        healthy = instance.isHealthy();
        metadata.putAll(instance.getMetadata());
        this.serverId =serverId;
    }

    public InstanceData() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstanceData that = (InstanceData) o;
        return port == that.port &&
                serverId == that.serverId &&
                Objects.equals(ip, that.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port, serverId);
    }

    @Override
    public String toString() {
        return "InstanceData{" +
                "instanceId='" + instanceId + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", healthy=" + healthy +
                ", jettyPort" + metadata.get(ServerKey.JETTY_PORT) +
                ", nettyPort" + metadata.get(ServerKey.NETTY_PORT) +
                ", nacosPort" + metadata.get(ServerKey.NACOS_PORT) +
                '}';
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getServerModel() {
        return serverModel;
    }

    public void setServerModel(int serverModel) {
        this.serverModel = serverModel;
    }
}