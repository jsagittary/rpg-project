package com.dykj.rpg.game.module.player.handler;

import com.dykj.rpg.common.consts.CommonConsts;
import com.dykj.rpg.common.module.uc.model.AccountInfoModel;
import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.core.BeforeGameHandler;
import com.dykj.rpg.game.module.cache.PlayerCache;
import com.dykj.rpg.game.module.cache.logic.EquipCache;
import com.dykj.rpg.game.module.equip.service.EquipService;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.player.service.PlayerCacheService;
import com.dykj.rpg.game.module.player.service.PlayerService;
import com.dykj.rpg.game.module.server.logic.GameServerConfig;
import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.protocol.item.ItemRs;
import com.dykj.rpg.protocol.login.ChangePlayerRq;
import com.dykj.rpg.protocol.login.ChangePlayerRs;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * @author jyb
 * @date 2021/4/21 18:10
 * @Description
 */
public class ChangePlayerHandler extends BeforeGameHandler<ChangePlayerRq> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void doHandler(ChangePlayerRq changePlayerRq, ISession session) {

        Object accountObject = session.getAttribute(CommonConsts.ACCOUNT_KYE);
        if (accountObject == null) {
            sendError(session, ErrorCodeEnum.ACCOUNT_NOT_EXIST);
            logger.error("ChangePlayerHandler doHandler error {} ", ErrorCodeEnum.ACCOUNT_NOT_EXIST, changePlayerRq.toString());
            return;
        }
        GameServerConfig gameServerConfig = BeanFactory.getBean(GameServerConfig.class);
        Player player = BeanFactory.getBean(PlayerCacheService.class).getPlayerByAccount(changePlayerRq.getAccountKey(), gameServerConfig.getServerId());
        if (player == null) {
            sendError(session, ErrorCodeEnum.PLAYER_NOT_EXIST);
            logger.error("PlayerLoginNewHandler doHandler error-1 {} {} ", ErrorCodeEnum.PLAYER_NOT_EXIST, changePlayerRq.toString());
            return;
        }

        PlayerCache playerCache = player.getCaches().get(changePlayerRq.getPlayerId());
        if (playerCache == null) {
            sendError(session, ErrorCodeEnum.PLAYER_NOT_EXIST);
            logger.error("PlayerLoginNewHandler doHandler error-2 {} {} ", ErrorCodeEnum.PLAYER_NOT_EXIST, changePlayerRq.toString());
            return;
        }

        ChangePlayerRs changePlayerRs = BeanFactory.getBean(PlayerService.class).ChangePlayer(player, playerCache);
        sendMsg(session, changePlayerRs);
    }
}