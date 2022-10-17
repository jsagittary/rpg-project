package com.dykj.rpg.game.module.soul.handler;

import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.core.GameHandler;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.soul.servcie.SoulService;
import com.dykj.rpg.protocol.soul.BuySoulSkinRq;

/**
 * @author jyb
 * @date 2021/4/26 16:24
 * @Description
 */
public class BuySoulSkinHandler extends GameHandler<BuySoulSkinRq> {

    @Override
    public void doHandler(BuySoulSkinRq buySoulSkinRq, Player player) {
        ErrorCodeEnum errorCodeEnum = getBean(SoulService.class).buySoulSkin(buySoulSkinRq.getSoulId(), player);
        if (errorCodeEnum != ErrorCodeEnum.SUCCESS) {
            sendError(player, errorCodeEnum);
        }
    }
}