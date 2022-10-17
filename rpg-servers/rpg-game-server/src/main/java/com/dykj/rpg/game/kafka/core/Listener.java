package com.dykj.rpg.game.kafka.core;


import com.dykj.rpg.common.cache.*;
import com.dykj.rpg.common.consts.CacheOperation;
import com.dykj.rpg.common.consts.TopicConsts;
import com.dykj.rpg.common.kafka.core.SyncKafkaRequest;
import com.dykj.rpg.common.redis.cache.ServerCacheMgr;
import com.dykj.rpg.common.redis.cache.ServerNewCacheMgr;
import com.dykj.rpg.db.consts.DBOperation;
import com.dykj.rpg.db.dao.DBKafkaMsg;
import com.dykj.rpg.db.data.BaseModel;
import com.dykj.rpg.game.module.server.logic.GameServerConfig;
import com.dykj.rpg.game.module.server.service.GameServerService;
import com.dykj.rpg.net.kafka.KafkaMsg;
import com.dykj.rpg.net.kafka.KafkaSyncDataTask;
import com.dykj.rpg.net.thread.MessageExecutor;
import com.dykj.rpg.util.JsonUtil;
import com.dykj.rpg.util.spring.BeanFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author: jianbo.gong
 * @version: 2018年6月8日 下午3:05:29
 * @describe:
 */

public class Listener {

    @Resource
    private GameServerConfig gameServerConfig;

    @Resource
    private ServerCacheMgr serverCacheMgr;

    @Resource
    private ServerNewCacheMgr serverNewCacheMgr;

    @Resource
    private GameServerService gameServerService;


    private Logger logger = LoggerFactory.getLogger("game");


    @KafkaListener(topics = {TopicConsts.SERVER_BROADCAST + "_" + "${serverId}"})
    public void listenServer(ConsumerRecord<?, ?> record) {
        listen(record);
    }

    @KafkaListener(topics = {TopicConsts.SERVER_ALL_TOPIC})
    public void listenAll(ConsumerRecord<?, ?> record) {
        listen(record);
    }

    private void listen(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            String context = message.toString();
            if (context != null) {
                KafkaMsg msg = JsonUtil.toInstance(context, KafkaMsg.class);
                KafkaSyncDataTask kafkaSyncDataTask = new KafkaSyncDataTask(msg);
                if (msg.getSignKey() != 0 && msg.isBack()) {
                    SyncKafkaRequest.instance.receive(msg);
                } else {
                    MessageExecutor.exeSyncDataTask(kafkaSyncDataTask);
                }
            }
        }
    }

//    @KafkaListener(id = CommonConsts.DB_LISTEN + "${serverId}", topics = {DBTopicConsts.DB_QUEUE_TOPIC + "_" + "${serverId}"}, idIsGroup = false)
//    public void listenMasterCache(ConsumerRecord<?, ?> record) {
//        listenCache(record);
//    }

    /**
     * @param record
     */
    private void listenCache(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        DBKafkaMsg msg;
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            String context = message.toString();
            if (context != null) {
                if (serverNewCacheMgr.isRemoteCache()) {
                    gameServerService.stopMasterKafkaListener();
                    return;
                }

                msg = JsonUtil.toInstance(context, DBKafkaMsg.class);
                Class cacheMgr = null;
                try {
                    cacheMgr = Class.forName(msg.getQueueName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                Class model = null;
                try {
                    model = Class.forName(msg.getModelName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    if (msg.getKeyNum() == 1) {
                        BaseModel baseModel = (BaseModel) JsonUtil.toInstance(msg.getData(), model);
                        DB1Model cache1Model = (DB1Model) baseModel;
                        ICache1Manager cache1Manager = (ICache1Manager) BeanFactory.getBean(cacheMgr);
                        if (msg.getOperation() == DBOperation.INSERT) {
                            //向数据库insert肯定要插入缓存，走DBOperation.INSERT
                            cache1Manager.addCache(baseModel);
                        } else if (msg.getOperation() == DBOperation.UPDATE) {
                            //数据库update肯定要update缓存，走DBOperation.UPDATE
                            cache1Manager.addCache(baseModel);
                        } else if (msg.getOperation() == DBOperation.DELETE) {
                            //数据库delete肯定要删除缓存，走DBOperation.DELETE
                            cache1Manager.removeCache(cache1Model.cachePrimaryKey());
                        } else if (msg.getOperation() == CacheOperation.ADD) {
                            //查询添加缓存
                            cache1Manager.addCache(baseModel);
                        } else if (msg.getOperation() == CacheOperation.UPDATE) {
                            //更新缓存肯定要更新数据数据库，DBOperation.DELETE
                            cache1Manager.addCache(baseModel);
                        } else if (msg.getOperation() == CacheOperation.REMOVE) {
                            cache1Manager.removeCache(baseModel);
                        }
                    } else if (msg.getKeyNum() == 2) {
                        ICache2Manager cache2Manager = (ICache2Manager) BeanFactory.getBean(cacheMgr);
                        // System.out.println("==============cache2Manager===================");
                        if (msg.getOperation() == DBOperation.INSERT) {
                            BaseModel baseModel = (BaseModel) JsonUtil.toInstance(msg.getData(), model);
                            //向数据库insert肯定要插入缓存，走DBOperation.INSERT
                            cache2Manager.updateValue(baseModel);
                        } else if (msg.getOperation() == DBOperation.UPDATE) {
                            BaseModel baseModel = (BaseModel) JsonUtil.toInstance(msg.getData(), model);
                            //数据库update肯定要update缓存，走DBOperation.UPDATE
                            cache2Manager.updateValue(baseModel);
                        } else if (msg.getOperation() == DBOperation.DELETE) {
                            BaseModel baseModel = (BaseModel) JsonUtil.toInstance(msg.getData(), model);
                            //数据库delete肯定要删除缓存，走DBOperation.DELETE
                            cache2Manager.removeValue(baseModel);
                        } else if (msg.getOperation() == CacheOperation.ADD) {
                            CacheMap cache = (CacheMap) JsonUtil.toInstance(msg.getData(), model);
                            //批量添加缓存
                            cache2Manager.addCache(cache);
                        } else if (msg.getOperation() == CacheOperation.UPDATE) {
                            CacheMap cache = (CacheMap) JsonUtil.toInstance(msg.getData(), model);
                            ////缓存update数据库肯定也要update，走CacheOperation.update
                            cache2Manager.addCache(cache);
                        } else if (msg.getOperation() == CacheOperation.REMOVE) {
                            CacheMap cache = (CacheMap) JsonUtil.toInstance(msg.getData(), model);
                            cache2Manager.removeCache(cache);
                        }
                    } else if (msg.getKeyNum() == -1) {
                        ICache0Manager cacheManager = (ICache0Manager) BeanFactory.getBean(cacheMgr);
                        if (msg.getOperation() == CacheOperation.ADD) {
                            cacheManager.addCache(JsonUtil.toInstance(msg.getData(), model));
                        } else if (msg.getOperation() == CacheOperation.UPDATE) {
                            ////缓存update数据库肯定也要update，走CacheOperation.update
                            cacheManager.updateCache(JsonUtil.toInstance(msg.getData(), model));
                        } else if (msg.getOperation() == CacheOperation.REMOVE) {
                            cacheManager.removeCache(JsonUtil.toInstance(msg.getData(), model));
                        }
                    }
                } catch (Exception e) {
                    if (msg != null) {
                        logger.error("Listener listenCache error  msg {} ", msg.toString());
                    }
                    e.printStackTrace();
                }
            }
        }
    }
}

