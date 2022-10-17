package com.dykj.rpg.game.module.mission.handler;

import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.core.GameHandler;
import com.dykj.rpg.game.module.mission.service.MissionService;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.protocol.mission.GetHandUpAwardRq;
import com.dykj.rpg.util.spring.BeanFactory;

/**
 * @author jyb
 * @date 2020/12/23 11:11
 * @Description
 */
public class GetHandUpAwardHandler extends GameHandler<GetHandUpAwardRq> {
    @Override
    public void doHandler(GetHandUpAwardRq getHandUpAwardRq, Player player) {
        ErrorCodeEnum errorCodeEnum = BeanFactory.getBean(MissionService.class).getOpenHandUpAward(player);
        if (errorCodeEnum != ErrorCodeEnum.SUCCESS) {
            sendError(player, errorCodeEnum);
        }
    }
}