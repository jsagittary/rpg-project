package com.dykj.rpg.game.kafka.listener;

import com.dykj.rpg.common.kafka.consts.KafkaCmdConsts;
import com.dykj.rpg.common.consts.TopicConsts;
import com.dykj.rpg.net.kafka.AbstractListener;
import com.dykj.rpg.net.kafka.KafkaMsg;
import com.dykj.rpg.net.kafka.annotation.KafkaCmdListener;
import com.dykj.rpg.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jyb
 * @date 2021/1/6 11:33
 * @Description
 */
@KafkaCmdListener(cmd = KafkaCmdConsts.REGIST_SERVER_ALL_BROAST)
public class RegisterAllListener extends AbstractListener {
    private static Logger logger = LoggerFactory.getLogger("game");
    @Override
    public void doListen(KafkaMsg msg) {
        Integer server = JsonUtil.toInstance(msg.getParms(), Integer.class);
        logger.info("RegisterAllListener regist :serverid {}  regist topic {} ",server, TopicConsts.SERVER_ALL_TOPIC);
    }
}