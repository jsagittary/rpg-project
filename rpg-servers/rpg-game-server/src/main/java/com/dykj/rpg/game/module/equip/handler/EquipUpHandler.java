package com.dykj.rpg.game.module.equip.handler;

import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.core.GameHandler;
import com.dykj.rpg.game.module.equip.service.EquipService;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.protocol.equip.EquipUpRq;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jyb
 * @date 2020/11/30 10:37
 * @Description
 */
public class EquipUpHandler extends GameHandler<EquipUpRq> {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void doHandler(EquipUpRq equipUpRq, Player player) {
        ErrorCodeEnum errorCodeEnum =BeanFactory.getBean(EquipService.class).equipUP(player,equipUpRq);
        if(errorCodeEnum!=ErrorCodeEnum.SUCCESS){
            sendError(player,errorCodeEnum);
        }
    }
}