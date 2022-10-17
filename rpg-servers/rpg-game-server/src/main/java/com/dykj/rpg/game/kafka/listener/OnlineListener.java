package com.dykj.rpg.game.kafka.listener;

import com.dykj.rpg.common.kafka.consts.KafkaCmdConsts;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.player.service.PlayerCacheService;
import com.dykj.rpg.net.kafka.AbstractListener;
import com.dykj.rpg.net.kafka.KafkaMsg;
import com.dykj.rpg.net.kafka.annotation.KafkaCmdListener;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KafkaCmdListener(cmd = KafkaCmdConsts.ONLINE_BROADCAST)
public class OnlineListener extends AbstractListener {
    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void doListen(KafkaMsg msg) {
        if (msg.getPlayerIds() == null || msg.getPlayerIds().size() < 1) {
            log.error("player is null  msg =" + msg.toString());
            return;
        }
        for (int playerId : msg.getPlayerIds()) {
            Player player = BeanFactory.getBean(PlayerCacheService.class).getCache(playerId);
            if (player != null && player.getSession() != null) {
                player.getSession().write(msg.getBytes());
            }
        }
    }
}
