package com.dykj.rpg.game.module.event.login.event;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.dykj.rpg.common.data.dao.PlayerInfoDao;
import com.dykj.rpg.common.module.player.service.GlobalPlayerService;
import com.dykj.rpg.common.redis.cache.OnlinePlayerMgr;
import com.dykj.rpg.common.redis.data.GlobalPlayerData;
import com.dykj.rpg.game.module.event.core.AbstractEvent;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.player.service.PlayerCacheService;

/**
 * @Author: jyb
 * @Date: 2020/9/7 9:39
 * @Description:
 */
@Component
public class LogoutEvent extends AbstractEvent {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private PlayerCacheService playerCacheService;
    @Resource
    private PlayerInfoDao playerInfoDao;
    @Resource
    private OnlinePlayerMgr onlinePlayerMgr;
    @Resource
    private GlobalPlayerService globalPlayerService;

    @Override
    public void doEvent(Object... prams) throws Exception {
        Player player = (Player) prams[0];
        if(player.cache().getPlayerInfoModel() !=null){
        	player.cache().getPlayerInfoModel().setLogoutTime(new Date());
            playerInfoDao.queueUpdate(player.cache().getPlayerInfoModel());
            GlobalPlayerData globalPlayerData =  globalPlayerService.cache(player.cache().getPlayerInfoModel(),true);
            onlinePlayerMgr.offLine(globalPlayerData);
        }
        playerCacheService.offline(player);
    }
}
