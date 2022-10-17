package com.dykj.rpg.game.module.mission.handler;

import com.dykj.rpg.game.core.GameHandler;
import com.dykj.rpg.game.module.mission.service.MissionService;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.protocol.mission.QuickHandUpRq;
import com.dykj.rpg.util.spring.BeanFactory;

/**
 * @author jyb
 * @date 2020/12/23 14:20
 * @Description
 */
public class QuickHandUpHandler extends GameHandler<QuickHandUpRq> {

    @Override
    public void doHandler(QuickHandUpRq quickHandUpRq, Player player) {
        BeanFactory.getBean(MissionService.class).quickHandUp(player);
    }
}