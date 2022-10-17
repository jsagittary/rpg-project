package com.dykj.rpg.common.redis.data;

import com.dykj.rpg.common.module.uc.model.ServerModel;
import com.dykj.rpg.redis.cache.base.ICacheMapPrimaryKey;

/**
 * @author jyb
 * @date 2020/12/31 14:29
 * @Description
 */
public class ServerData implements ICacheMapPrimaryKey {
    /**
     * 服务器信息
     */
    private ServerModel serverModel;
    /**
     * 健康实例
     */
    private InstanceData instance;

    public ServerModel getServerModel() {
        return serverModel;
    }

    public void setServerModel(ServerModel serverModel) {
        this.serverModel = serverModel;
    }

    public InstanceData getInstance() {
        return instance;
    }

    public void setInstance(InstanceData instance) {
        this.instance = instance;
    }

    @Override
    public Object primaryKey() {
        return serverModel.getServerId();
    }



}