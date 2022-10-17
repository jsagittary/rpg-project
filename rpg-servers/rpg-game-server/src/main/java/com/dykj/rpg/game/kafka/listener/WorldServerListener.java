package com.dykj.rpg.game.kafka.listener;

import com.dykj.rpg.common.kafka.consts.KafkaCmdConsts;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.player.service.PlayerCacheService;
import com.dykj.rpg.game.module.util.CmdUtil;
import com.dykj.rpg.net.kafka.AbstractListener;
import com.dykj.rpg.net.kafka.KafkaMsg;
import com.dykj.rpg.net.kafka.annotation.KafkaCmdListener;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KafkaCmdListener(cmd = KafkaCmdConsts.WORLD_SERVER_BROADCAST)
public class WorldServerListener extends AbstractListener {

    private static Logger logger = LoggerFactory.getLogger(WorldServerListener.class);

    @Override
    public void doListen(KafkaMsg msg) {
        PlayerCacheService playerCacheService = BeanFactory.getBean(PlayerCacheService.class);
        for (Player player : playerCacheService.getOnlinePlayers().values()) {
            CmdUtil.sendMsg(player, msg.getBytes());
        }
    }
}
