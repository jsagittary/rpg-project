package com.dykj.rpg.game.kafka.listener;

import com.dykj.rpg.common.kafka.consts.KafkaCmdConsts;
//import com.dykj.rpg.common.kafka.service.KafkaSenderService;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.player.service.PlayerCacheService;
import com.dykj.rpg.net.kafka.AbstractListener;
import com.dykj.rpg.net.kafka.KafkaMsg;
import com.dykj.rpg.net.kafka.annotation.KafkaCmdListener;
import com.dykj.rpg.util.JsonUtil;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * @author jyb
 * @date 2021/1/27 15:25
 * @Description
 */
@KafkaCmdListener(cmd = KafkaCmdConsts.KAFKA_ASYNC_TEST)
public class KafkaAsyncListener extends AbstractListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void doListen(KafkaMsg msg) {
        logger.info("KafkaAsyncListener success msg ",msg.toString());
    }
}