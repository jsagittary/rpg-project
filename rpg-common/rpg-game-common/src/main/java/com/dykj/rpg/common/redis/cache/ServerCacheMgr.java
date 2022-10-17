package com.dykj.rpg.common.redis.cache;

import com.dykj.rpg.common.module.uc.model.ServerInfoModel;
import com.dykj.rpg.redis.cache.redis.AbstractCacheRedisMap;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Component
public class ServerCacheMgr extends AbstractCacheRedisMap<ServerInfoModel> {

    @Resource
    private RedisScript<String> gameServerStartAction;

    @Resource
    private OnlinePlayerMgr onlinePlayerMgr;

    @Resource
    private OfflinePlayerMgr offlinePlayerMgr;

    public boolean isMaster(int serverId, String dubboAddress) {
        ServerInfoModel serverInfoModel = get(serverId);
        if (serverInfoModel == null) {
            return false;
        }
        return serverInfoModel.getAddress().equals(dubboAddress);
    }


    /**
     * 服务器在启动的时候 清理redis垃圾缓存
     * @param serverId
     * @param maxServerPlayerNum
     * @param expireMS
     * @return
     */
    public String beforeStartCompleted(int serverId, int maxServerPlayerNum, int expireMS) {
        List<String> keys = Arrays.asList(onlinePlayerMgr.path(), offlinePlayerMgr.path());
        return getRedisTemplate().execute(gameServerStartAction, keys, String.valueOf(serverId), String.valueOf(maxServerPlayerNum), String.valueOf(expireMS));
    }
}
