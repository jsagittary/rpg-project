package com.dykj.rpg.game.kafka.listener;


import com.dykj.rpg.common.kafka.consts.KafkaCmdConsts;
import com.dykj.rpg.common.consts.TopicConsts;
import com.dykj.rpg.net.kafka.AbstractListener;
import com.dykj.rpg.net.kafka.KafkaMsg;
import com.dykj.rpg.net.kafka.annotation.KafkaCmdListener;
import com.dykj.rpg.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KafkaCmdListener(cmd = KafkaCmdConsts.REGIST_SERVER_BROAST)
public class RegistServerListener extends AbstractListener {

	private static Logger logger = LoggerFactory.getLogger("game");

	@Override
	public void doListen(KafkaMsg msg) {
		Integer server = JsonUtil.toInstance(msg.getParms(), Integer.class);
		logger.info("RegisterAllListener regist :serverid {}  regist topic {} ",server, TopicConsts.SERVER_BROADCAST);
	}

}
