package com.dykj.rpg.common.redis.cache;

import com.dykj.rpg.common.consts.CommonConsts;
import com.dykj.rpg.common.consts.ServerModelConsts;
import com.dykj.rpg.common.redis.data.InstanceData;
import com.dykj.rpg.common.redis.data.ServerData;
import com.dykj.rpg.redis.cache.redis.AbstractCacheRedisMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author jyb
 * @date 2020/12/31 16:21
 * @Description
 */
@Component
public class ServerNewCacheMgr extends AbstractCacheRedisMap<ServerData> {

    private InstanceData instance;


    private Logger logger = LoggerFactory.getLogger(getClass());


    @Resource
    private RedisScript<String> gameServerStartAction;

    @Resource
    private OnlinePlayerMgr onlinePlayerMgr;

    @Resource
    private OfflinePlayerMgr offlinePlayerMgr;

    /**
     * 是否为主
     *
     * @return
     */
    public boolean isRemoteCache() {
        if (instance == null) {
            return false;
        }
        if (instance.getServerModel() != ServerModelConsts.MASTER_SLAVE) {
            return false;
        }
        ServerData serverData = get(instance.getServerId());
        if (serverData.getInstance() == null) {
            return false;
        }
        return serverData.getInstance().equals(instance);
    }

    public InstanceData getInstance() {
        return instance;
    }

    public void setInstance(InstanceData instance) {
        this.instance = instance;
    }

    public String getNacosAddress() {
        return instance.getIp() + ":" + instance.getPort();
    }


    /**
     * 通过玩家技术服务器id
     *
     * @param playerId
     * @return
     */
    public int calculatePlayerServerId(int playerId) {
        int serverId = playerId / CommonConsts.DB_AUTO_INCREMENT;
        if (0 >= serverId) {
            // 走这里，就是运维又设置错 id 了
            logger.error("calculatePlayerServerId error (1)  : playerId  {}  serverId {} ", playerId, serverId);
            return -1;
        }
        ServerData serverData = get(serverId);
        if (serverData == null) {
            logger.error("calculatePlayerServerId error (2) : playerId  {}  serverId {} ", playerId, serverId);
            return -1;
        }
        return serverData.getServerModel().getServerId();
    }

    /**
     * 服务器在启动的时候 清理redis垃圾缓存
     *
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