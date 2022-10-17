package com.dykj.rpg.game.module.mission.handler;

import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.core.GameHandler;
import com.dykj.rpg.game.module.mission.service.MissionService;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.protocol.mission.GetQuickHandUpAwardRq;
import com.dykj.rpg.util.spring.BeanFactory;

/**
 * @author jyb
 * @date 2020/12/23 19:13
 * @Description
 */
public class GetQuickHandUpAwardHandler  extends GameHandler<GetQuickHandUpAwardRq> {
    @Override
    public void doHandler(GetQuickHandUpAwardRq getQuickHandUpAwardRq, Player player) {
        ErrorCodeEnum errorCodeEnum = BeanFactory.getBean(MissionService.class).getHandUpAwardHandler(player);
        if(errorCodeEnum!=ErrorCodeEnum.SUCCESS){
            sendError(player,errorCodeEnum);
        }
    }
}