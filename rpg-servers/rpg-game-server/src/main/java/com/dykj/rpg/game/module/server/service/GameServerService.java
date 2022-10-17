package com.dykj.rpg.game.module.server.service;

import com.dykj.rpg.common.consts.CommonConsts;
import com.dykj.rpg.common.data.cache.PlayerNameCacheMgr;
import com.dykj.rpg.common.data.dao.PlayerInfoDao;
import com.dykj.rpg.common.module.player.service.GlobalPlayerService;
import com.dykj.rpg.common.module.uc.model.ServerModel;
import com.dykj.rpg.common.redis.cache.ServerNewCacheMgr;
import com.dykj.rpg.db.queue.DbQueueManager;
import com.dykj.rpg.game.ice.client.BattleClient;
import com.dykj.rpg.game.kafka.KafkaConsumerListenerService;
import com.dykj.rpg.game.module.player.service.PlayerCacheService;
import com.dykj.rpg.game.module.quartz.event.JobRegisterEventManager;
import com.dykj.rpg.game.module.server.logic.GameServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @Author: jyb
 * @Date: 2020/9/7 10:39
 * @Description:
 */
@Service
public class GameServerService {

    @Resource
    private GameServerConfig gameServerConfig;

    @Resource
    private PlayerInfoDao playerInfoDao;

    @Resource
    private PlayerCacheService playerCacheService;

    @Resource
    private PlayerNameCacheMgr playerNameCacheMgr;

    @Resource
    private KafkaConsumerListenerService kafkaConsumerListenerService;

    @Resource
    private ServerNewCacheMgr serverNewCacheMgr;

    @Resource
    private JobRegisterEventManager jobRegisterEventManager;


    private Logger logger = LoggerFactory.getLogger(getClass());

    @PostConstruct
    public void startServer() {
        //  System.out.println();
        //校正redis缓存
        beforeStartCompleted();

        //查询名字集合
        playerNameCacheMgr.addAll(playerInfoDao.getNames());

        //跟测战斗服直连
        try {
            long now = System.currentTimeMillis();
            logger.info("GameServerService startServer  addBattleServer start");
            BattleClient.getInstance().start(gameServerConfig.getServerId());
            logger.info("GameServerService startServer  addBattleServer end time {} ", System.currentTimeMillis() - now);
        } catch (Exception e) {
            logger.error("GameServerService startServer error  : battleServer not open  {}  ", gameServerConfig.getServerId());
        }

        jobRegisterEventManager.doEvents(null);
    }


    public void beforeStartCompleted() {
        try {
            //  System.out.println();
            int serverId = gameServerConfig.getServerId();
            int maxNum = CommonConsts.DB_AUTO_INCREMENT;
            int expire = GlobalPlayerService.EXPIRE;
            // 做一些 redis 的操作
            serverNewCacheMgr.beforeStartCompleted(serverId, maxNum, expire);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void stopMasterKafkaListener() {
        String listener = CommonConsts.DB_LISTEN + gameServerConfig.getServerId();
        ServerModel serverModel = serverNewCacheMgr.get(DbQueueManager.getInstance().getServerId()).getServerModel();

        //开启了主从模式，就会远程去同步,但是主服务器是不会监听db和缓存的数据同步的
        if (serverNewCacheMgr.isRemoteCache()) {
            kafkaConsumerListenerService.stopListener(listener);
        }
    }

}
