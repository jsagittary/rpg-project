package com.dykj.rpg.common.module.player.service;

import com.dykj.rpg.common.data.dao.GlobalPlayerDao;
import com.dykj.rpg.common.data.model.GlobalPlayerModel;
import com.dykj.rpg.common.data.model.PlayerInfoModel;
import com.dykj.rpg.common.redis.cache.OnlinePlayerMgr;
import com.dykj.rpg.common.redis.data.GlobalPlayerData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: jyb
 * @Date: 2020/10/9 15:35
 * @Description:
 */
@Service
public class GlobalPlayerService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private OnlinePlayerMgr onlinePlayerMgr;
    @Resource
    private GlobalPlayerDao globalPlayerDao;

    // 一天
    public final static int EXPIRE = 24 * 60 * 60 * 1000;


    public long getOnlineNum() {
        return onlinePlayerMgr.size();
    }


    public GlobalPlayerData cache(PlayerInfoModel player, boolean isOnline) {
        // 先从 online 获取，获取不到再从 offline 获取
        GlobalPlayerData globalPlayerData = getByPlayerId(player.getPlayerId());
        if (globalPlayerData == null) {
            return null;
        }
        try {
            onlinePlayerMgr.lock(player.getPlayerId());
            if (isOnline) {
                globalPlayerData.setOnline(true);
                onlinePlayerMgr.online(globalPlayerData);
            } else {
                globalPlayerData.setOnline(false);
                onlinePlayerMgr.offLine(globalPlayerData);
            }
        } finally {
            onlinePlayerMgr.unlock(player.getPlayerId());
        }
        return globalPlayerData;
    }

    /**
     * 该方法是redis拿不到去数据库加载
     * @param playerId
     * @return
     */
    public GlobalPlayerData getByPlayerId(int playerId) {
        return getByPlayerId(playerId, true);
    }

    /**
     * 从在线集合拿
     * @param playerId
     * @return
     */
    public GlobalPlayerData getOnline(int playerId) {
        return onlinePlayerMgr.getOnline(playerId);
    }


    public GlobalPlayerData getByPlayerId(int playerId, boolean autoCreate) {
        GlobalPlayerData globalPlayerData = onlinePlayerMgr.get(playerId);
        if (null != globalPlayerData) {
            return globalPlayerData;
        }
        // redis 在线、离线集合都拿不到数据，就去数据库拿
        try {
            onlinePlayerMgr.lock(playerId);
            globalPlayerData = onlinePlayerMgr.get(playerId);
            if (null != globalPlayerData) {
                return globalPlayerData;
            }
            if (autoCreate) {
                GlobalPlayerModel globalPlayerModel = globalPlayerDao.queryByPrimaykey(playerId);
                if (null != globalPlayerModel) {
                    globalPlayerData = new GlobalPlayerData(globalPlayerModel);
                    onlinePlayerMgr.update(globalPlayerData);
                    return globalPlayerData;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            onlinePlayerMgr.unlock(playerId);
        }
        logger.error("==============================  could not found player {}  ==============================", playerId);
        return null;
    }

    /**
     * update
     *
     * @param globalPlayerData
     */
    public void updateGlobalData(GlobalPlayerData globalPlayerData) {
        onlinePlayerMgr.update(globalPlayerData);
        GlobalPlayerModel globalPlayerModel = new GlobalPlayerModel(globalPlayerData);
        globalPlayerDao.queueUpdate(globalPlayerModel);

    }
}
