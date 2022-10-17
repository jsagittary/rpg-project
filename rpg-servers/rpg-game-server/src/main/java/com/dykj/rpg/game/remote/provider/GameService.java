package com.dykj.rpg.game.remote.provider;

import com.alibaba.dubbo.config.annotation.Service;
import com.dykj.rpg.common.redis.cache.ServerCacheMgr;
import com.dykj.rpg.common.remote.game.IGameService;
import com.dykj.rpg.game.module.server.logic.GameServerConfig;

import javax.annotation.Resource;

/**
 * @Author: jyb
 * @Date: 2020/9/15 11:28
 * @Description:
 */
@Service
public class GameService implements IGameService {

    public GameService() {
    }

    @Resource
    private ServerCacheMgr serverCacheMgr;

    @Resource
    private GameServerConfig gameServerConfig;

    @Override
    public String getHost(int serverId) {
//        if (serverId != gameServerConfig.getServerId()) {
//            return null;
//        }
//        ServerInfoModel serverInfoModel = serverCacheMgr.get(serverId);
//        if (!gameServerConfig.dubboAddress().equals(serverInfoModel.getAddress())) {
//            serverInfoModel.setAddress(gameServerConfig.dubboAddress());
//            serverCacheMgr.put(serverInfoModel);
//        }

        return "";//gameServerConfig.getAddress() + ":" + gameServerConfig.getPort();
    }
}
