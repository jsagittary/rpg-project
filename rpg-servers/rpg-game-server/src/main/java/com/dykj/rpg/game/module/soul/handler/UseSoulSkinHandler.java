package com.dykj.rpg.game.module.soul.handler;

import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.core.GameHandler;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.soul.servcie.SoulService;
import com.dykj.rpg.protocol.soul.UseSoulSkinRq;

/**
 * @author jyb
 * @date 2021/4/26 17:16
 * @Description
 */
public class UseSoulSkinHandler extends GameHandler<UseSoulSkinRq> {
    @Override
    public void doHandler(UseSoulSkinRq useSoulSkinRq, Player player) {
        ErrorCodeEnum errorCodeEnum = getBean(SoulService.class).useSoulSkin(useSoulSkinRq.getSoulId(), player);
        if (errorCodeEnum != ErrorCodeEnum.SUCCESS) {
            sendError(player, errorCodeEnum);
        }
    }
}