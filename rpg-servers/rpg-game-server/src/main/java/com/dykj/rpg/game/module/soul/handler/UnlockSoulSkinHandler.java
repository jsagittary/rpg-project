package com.dykj.rpg.game.module.soul.handler;

import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.core.GameHandler;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.soul.servcie.SoulService;
import com.dykj.rpg.protocol.soul.UnlockSoulSkinRq;

/**
 * @author jyb
 * @date 2021/4/26 14:11
 * @Description
 */
public class UnlockSoulSkinHandler extends GameHandler<UnlockSoulSkinRq> {
    @Override
    public void doHandler(UnlockSoulSkinRq unlockSoulSkinRq, Player player) {
        ErrorCodeEnum errorCodeEnum = getBean(SoulService.class).unlockSoulSkin(unlockSoulSkinRq.getSoulId(), player);
        if (errorCodeEnum != ErrorCodeEnum.SUCCESS) {
            sendError(player, errorCodeEnum);
        }
    }
}