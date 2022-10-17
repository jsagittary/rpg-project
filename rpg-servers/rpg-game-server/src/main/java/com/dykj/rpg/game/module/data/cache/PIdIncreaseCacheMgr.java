package com.dykj.rpg.game.module.data.cache;

import com.dykj.rpg.common.consts.CommonConsts;
import com.dykj.rpg.common.data.dao.PlayerInfoDao;
import com.dykj.rpg.game.consts.RedisKeyConsts;
import com.dykj.rpg.game.module.server.logic.GameServerConfig;
import com.dykj.rpg.redis.cache.redis.AbstractCacheRedisAtomicInteger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: jyb
 * @Date: 2020/10/24 11:34
 * @Description: 玩家id自增缓存
 */
@Component
public class PIdIncreaseCacheMgr extends AbstractCacheRedisAtomicInteger {
    @Resource
    private PlayerInfoDao playerInfoDao;
    @Resource
    private GameServerConfig gameServerConfig;

    @Override
    public String keyPath() {
        return "server:" + gameServerConfig.getServerId() + ":" + RedisKeyConsts.PLAYER_ID_INCREASE;
    }


    @Override
    public int initValue() {
        int maxId = playerInfoDao.getMaxId() ;
        int minPlayerId = gameServerConfig.getServerId() * CommonConsts.DB_AUTO_INCREMENT ;
        if (maxId == 0 || maxId <= minPlayerId) {
            maxId = gameServerConfig.getServerId() * CommonConsts.DB_AUTO_INCREMENT;
        }
        return maxId;
    }
}
