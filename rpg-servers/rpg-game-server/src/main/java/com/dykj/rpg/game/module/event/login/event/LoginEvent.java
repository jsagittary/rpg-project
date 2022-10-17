package com.dykj.rpg.game.module.event.login.event;

import com.dykj.rpg.common.data.dao.PlayerInfoDao;
import com.dykj.rpg.common.data.model.PlayerInfoModel;
import com.dykj.rpg.common.module.player.service.GlobalPlayerService;
import com.dykj.rpg.common.redis.cache.OnlinePlayerMgr;
import com.dykj.rpg.common.redis.data.GlobalPlayerData;
import com.dykj.rpg.game.module.event.core.AbstractEvent;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.player.service.PlayerCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Author: jyb
 * @Date: 2020/9/5 15:17
 * @Description:
 */
@Component
public class LoginEvent extends AbstractEvent {

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
        playerCacheService.online(player);
        PlayerInfoModel info = player.cache().getPlayerInfoModel();
        info.setLoginTime(new Date());
        playerInfoDao.queueUpdate(info);
        GlobalPlayerData globalPlayerData =  globalPlayerService.cache(info,true);
        onlinePlayerMgr.online(globalPlayerData);
        logger.info("LoginEvent  do event  {}", info.toString());
    }
}
