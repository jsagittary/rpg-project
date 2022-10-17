package com.dykj.rpg.common.kafka.service;

import com.dykj.rpg.common.kafka.consts.KafkaCmdConsts;
import com.dykj.rpg.common.consts.TopicConsts;
import com.dykj.rpg.common.kafka.core.SyncKafkaRequest;
import com.dykj.rpg.common.redis.cache.ServerNewCacheMgr;
import com.dykj.rpg.net.kafka.KafkaMsg;
import com.dykj.rpg.net.thread.MessageExecutor;
import com.dykj.rpg.util.JsonUtil;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author jyb
 * @date 2019/2/11 18:42
 */
@Component
public class KafkaSenderService implements IKafkaSenderService {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;
    @Resource
    private ServerNewCacheMgr serverNewCacheMgr;

    private transient Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void send2AllPlayers(short cmd, byte[] bytes) {
        KafkaMsg kafkaMsg = new KafkaMsg(KafkaCmdConsts.WORLD_SERVER_BROADCAST, cmd, bytes);
        MessageExecutor.exeSendMsgTask(() -> {
            try {
                kafkaTemplate.send(TopicConsts.SERVER_ALL_TOPIC, JsonUtil.toJsonString(kafkaMsg));
            } catch (Exception e) {
                logger.error("KafkaService sendMsg2ServerAsync error {} ", e);
            }
        });
    }

    @Override
    public void send2Players(Collection<Integer> playerIds, byte[] bytes) {
        send2Players(playerIds, KafkaCmdConsts.ONLINE_BROADCAST, bytes, null);
    }


    @Override
    public void send2Players(Collection<Integer> playerIds, short kafkaCmd, Object object) {
        send2Players(playerIds, kafkaCmd, null, object);
    }

    @Override
    public void send2Players(Collection<Integer> playerIds, short kafkaCmd, byte[] bytes) {
        send2Players(playerIds, kafkaCmd, bytes, null);
    }

    @Override
    public void send2AllServers(short kafkaCmd, Object object) {
        KafkaMsg kafkaMsg = new KafkaMsg(kafkaCmd, JsonUtil.toJsonString(object));
        MessageExecutor.exeSendMsgTask(() -> {
            try {
                kafkaTemplate.send(TopicConsts.SERVER_ALL_TOPIC, JsonUtil.toJsonString(kafkaMsg));
            } catch (Exception e) {
                logger.error("KafkaService sendMsg2ServerAsync error {} ", e);
            }
        });
    }

    @Override
    public void send2AllServers(short kafkaCmd, byte[] bytes) {
        KafkaMsg kafkaMsg = new KafkaMsg(kafkaCmd, bytes);
        MessageExecutor.exeSendMsgTask(() -> {
            try {
                kafkaTemplate.send(TopicConsts.SERVER_ALL_TOPIC, JsonUtil.toJsonString(kafkaMsg));
            } catch (Exception e) {
                logger.error("KafkaService sendMsg2ServerAsync error {} ", e);
            }
        });

    }

    private void broadcast2WorldPlayers(List<Integer> playerIds, int serverId, byte[] bytes) {
        send2Players(playerIds, serverId, KafkaCmdConsts.ONLINE_BROADCAST, bytes, null);
    }

    private void send2Players(Collection<Integer> playerIds, short kafkaCmd, byte[] bytes, Object object) {
        if (null != playerIds && 0 < playerIds.size()) {
            int serverId;
            Map<Integer, List<Integer>> map = new HashMap<>();
            for (int id : playerIds) {
                serverId = BeanFactory.getBean(ServerNewCacheMgr.class).calculatePlayerServerId(id);
                if (!map.containsKey(serverId)) {
                    map.put(serverId, new ArrayList<>());
                }
                map.get(serverId).add(id);
            }
            for (int sid : map.keySet()) {
                send2Players(map.get(sid), sid, kafkaCmd, bytes, object);
            }
        }
    }

    private void send2Players(List<Integer> playerIds, int serverId, short kafkaCmd, byte[] bytes, Object object) {
        KafkaMsg kafkaMsg = new KafkaMsg();
        kafkaMsg.setKafkaCmd(kafkaCmd);
        kafkaMsg.setBytes(bytes);
        kafkaMsg.setParms(JsonUtil.toJsonString(object));
        kafkaMsg.setPlayerIds(playerIds);
        if (serverNewCacheMgr.containsKey(serverId)) {
            sendMsg2ServerAsync(serverId, kafkaMsg);
        }
    }

    /**
     * 异步推送
     *
     * @param serverId
     * @param kafkaMsg kafka 消息
     */
    public void sendMsg2ServerAsync(int serverId, KafkaMsg kafkaMsg) {
        MessageExecutor.exeSendMsgTask(() -> {
            try {
                kafkaTemplate.send(TopicConsts.SERVER_BROADCAST + "_" + serverId, JsonUtil.toJsonString(kafkaMsg));
            } catch (Exception e) {
                logger.error("KafkaService sendMsg2ServerAsync error {} ", e);
            }
        });
    }

    /**
     * 异步推送
     *
     * @param serverId
     * @param kafkaMsg kafka 消息
     */
    public void sendMsg2ServerAsync$(int serverId, KafkaMsg kafkaMsg) {
        try {
            kafkaTemplate.send(TopicConsts.SERVER_BROADCAST + "_" + serverId, JsonUtil.toJsonString(kafkaMsg));
        } catch (Exception e) {
            logger.error("KafkaService sendMsg2ServerAsync error {} ", e);
        }
    }


    /**
     * 异步推送
     *
     * @param playerId
     * @param kafkaMsg kafka 消息
     */
    public void sendMsg2PlayerAsync(int playerId, KafkaMsg kafkaMsg) {
        int serverId = serverNewCacheMgr.calculatePlayerServerId(playerId);
        kafkaMsg.setThreadKey(playerId);
        if (serverId < 0) {
            logger.error("KafkaService sendMsg2PlayerAsync error  player {}  server is not exist ", playerId);
            return;
        }
        MessageExecutor.exeSendMsgTask(() -> {
            try {
                kafkaTemplate.send(TopicConsts.SERVER_BROADCAST + "_" + serverId, JsonUtil.toJsonString(kafkaMsg));
            } catch (Exception e) {
                logger.error("KafkaService sendMsg2ServerAsync error {} ", e);
            }
        });
    }


    /**
     * 异步推送消息 获得结果
     *
     * @param playerId
     * @param kafkaMsg kafka 消息
     */
    @Deprecated
    public KafkaMsg sendMsg2PlayerSync(int playerId, KafkaMsg kafkaMsg) {
        int serverId = serverNewCacheMgr.calculatePlayerServerId(playerId);
        kafkaMsg.setThreadKey(playerId);
        if (serverId < 0) {
            logger.error("KafkaService sendMsg2PlayerAsync error  player { }  server is not exist ", playerId);
            return null;
        }
        return sendMsg2ServerSync(serverId, kafkaMsg);
    }


    /**
     * 同步推送，并且返回消息
     *
     * @param serverId
     * @param kafkaMsg kafka 消息
     */
    @Deprecated
    public KafkaMsg sendMsg2ServerSync(int serverId, KafkaMsg kafkaMsg) {
        return SyncKafkaRequest.instance.send(serverId, kafkaMsg);
    }

}
