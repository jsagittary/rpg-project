//package com.dykj.rpg.game.kafka.listener;
//
//import com.dykj.rpg.common.kafka.consts.KafkaCmdConsts;
//import com.dykj.rpg.common.kafka.service.KafkaSenderService;
//import com.dykj.rpg.game.module.player.logic.Player;
//import com.dykj.rpg.game.module.player.service.PlayerCacheService;
//import com.dykj.rpg.net.kafka.AbstractListener;
//import com.dykj.rpg.net.kafka.KafkaMsg;
//import com.dykj.rpg.net.kafka.annotation.KafkaCmdListener;
//import com.dykj.rpg.util.JsonUtil;
//import com.dykj.rpg.util.spring.BeanFactory;
//
//import java.util.Arrays;
//import java.util.List;
//
///**
// * @author jyb
// * @date 2021/1/27 15:25
// * @Description
// */
//@KafkaCmdListener(cmd = KafkaCmdConsts.KAFKA_SYN_TEST)
//public class KafkaSyncListener extends AbstractListener {
//
//    @Override
//    public void doListen(KafkaMsg msg) {
//        List<Integer> playerIds = JsonUtil.toList(msg.getParms(), Integer.class);
//        int playerId = playerIds.get(0);
//        int targetPlayerId = playerIds.get(1);
//        Player player = BeanFactory.getBean(PlayerCacheService.class).getCache(targetPlayerId);
//        KafkaMsg kafkaMsg = new KafkaMsg();
//        kafkaMsg.setBack(true);
//        kafkaMsg.setSignKey(msg.getSignKey());
//        kafkaMsg.setParms(JsonUtil.toJsonString(player.getInfo()));
//        kafkaMsg.setPlayerIds(Arrays.asList(playerId));
//        BeanFactory.getBean(KafkaSenderService.class).sendMsg2PlayerAsync(playerId, kafkaMsg);
//    }
//}