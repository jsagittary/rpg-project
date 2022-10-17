package com.dykj.rpg.game.module.equip.handler;

import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.core.GameHandler;
import com.dykj.rpg.game.module.equip.service.EquipService;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.protocol.equip.EquipPosUpRq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jyb
 * @date 2021/4/6 11:41
 * @Description
 */
public class EquipPosUpHandler extends GameHandler<EquipPosUpRq> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void doHandler(EquipPosUpRq equipPosUpRq, Player player) {
        if (equipPosUpRq.getPos() < 1 || equipPosUpRq.getPos() > 10) {
            sendError(player, ErrorCodeEnum.CLIENT_PRAMS_ERROR);
            return;
        }
        ErrorCodeEnum errorCodeEnum =getBean(EquipService.class).equipPosUp(player,equipPosUpRq.getPos());
        if(errorCodeEnum!=ErrorCodeEnum.SUCCESS){
            sendError(player, errorCodeEnum);
        }
    }
}