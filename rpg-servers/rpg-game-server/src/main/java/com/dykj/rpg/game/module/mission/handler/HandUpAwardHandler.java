package com.dykj.rpg.game.module.mission.handler;

import com.dykj.rpg.game.core.GameHandler;
import com.dykj.rpg.game.module.mission.service.MissionService;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.protocol.mission.HandUpAwardRq;
import com.dykj.rpg.util.spring.BeanFactory;

/**
 * @author jyb
 * @date 2020/12/22 17:52
 * @Description
 */
public class HandUpAwardHandler extends GameHandler<HandUpAwardRq> {
    @Override
    public void doHandler(HandUpAwardRq handUpAwardRq, Player player) {
        BeanFactory.getBean(MissionService.class).openHandUpAward(player);
    }
}