//package com.dykj.rpg.game.module.gm.service;
//
//import com.dykj.rpg.common.kafka.consts.KafkaCmdConsts;
//import com.dykj.rpg.common.kafka.service.KafkaSenderService;
//import com.dykj.rpg.game.module.gm.core.GmStrategy;
//import com.dykj.rpg.net.kafka.KafkaMsg;
//import com.dykj.rpg.util.JsonUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
///**
// * @author jyb
// * @date 2021/1/27 14:20
// * @Description
// */
//@Service
//public class KafkaGmService extends GmStrategy {
//
//    private Logger logger = LoggerFactory.getLogger(getClass());
//
//    @Resource
//    private KafkaSenderService kafkaSenderService;
//
//    @Override
//    public String serviceName() {
//        return "kafka";
//    }
//
//    public void sync() {
//        long now = System.currentTimeMillis();
//        logger.info("KafkaGmService sync success ");
//        List<Integer> playerIds = new ArrayList<>();
//        playerIds.add(player.getPlayerId());
//        playerIds.add(player.getPlayerId());
//        KafkaMsg kafkaMsg = kafkaSenderService.sendMsg2PlayerSync(player.getPlayerId(), new KafkaMsg(KafkaCmdConsts.KAFKA_SYN_TEST, Arrays.asList(player.getPlayerId()), JsonUtil.toJsonString(playerIds)));
//        logger.info("KafkaGmService sync result {}  time {} ", kafkaMsg.toString(),System.currentTimeMillis()-now);
//    }
//
//
//    public void async() {
//        logger.info("KafkaGmService async success ");
//        kafkaSenderService.sendMsg2PlayerAsync(player.getPlayerId(), new KafkaMsg(KafkaCmdConsts.KAFKA_ASYNC_TEST, "test"));
//
//    }
//}