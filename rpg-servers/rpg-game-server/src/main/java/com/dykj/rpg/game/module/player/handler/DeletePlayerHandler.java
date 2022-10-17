package com.dykj.rpg.game.module.player.handler;

import com.dykj.rpg.common.consts.CommonConsts;
import com.dykj.rpg.common.data.dao.PlayerInfoDao;
import com.dykj.rpg.common.data.model.PlayerInfoModel;
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
import com.dykj.rpg.protocol.login.ChangePlayerRs;
import com.dykj.rpg.protocol.player.DeletePlayerRq;
import com.dykj.rpg.protocol.player.DeletePlayerRs;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jyb
 * @date 2020/11/7 10:30
 * @Description
 */
public class DeletePlayerHandler extends BeforeGameHandler<DeletePlayerRq> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void doHandler(DeletePlayerRq deletePlayerRq, ISession session) {
        Object accountObject = session.getAttribute(CommonConsts.ACCOUNT_KYE);
        if (accountObject == null) {
            sendError(session, ErrorCodeEnum.ACCOUNT_NOT_EXIST);
            logger.error("ChangePlayerHandler doHandler error {} ", ErrorCodeEnum.ACCOUNT_NOT_EXIST, deletePlayerRq.toString());
            return;
        }
        AccountInfoModel accountInfoModel = (AccountInfoModel) accountObject;
        GameServerConfig gameServerConfig = BeanFactory.getBean(GameServerConfig.class);
        Player player = BeanFactory.getBean(PlayerCacheService.class).getPlayerByAccount(accountInfoModel.getAccountKey(), gameServerConfig.getServerId());
        if (player == null) {
            sendError(session, ErrorCodeEnum.PLAYER_NOT_EXIST);
            return;
        }
        PlayerCache playerCache = player.getPlayerCache(deletePlayerRq.getPlayerId());
        if (playerCache == null) {
            sendError(session, ErrorCodeEnum.PLAYER_NOT_EXIST);
            return;
        }
        playerCache.getPlayerInfoModel().setStatus(1);
        BeanFactory.getBean(PlayerInfoDao.class).queueUpdate(playerCache.getPlayerInfoModel());
        DeletePlayerRs deletePlayerRs = new DeletePlayerRs();
        if (player.getCaches().size() > 0) {
            for (PlayerCache p : player.getCaches().values()) {
                if (p.getPlayerInfoModel() != null && p.getPlayerInfoModel().getPlayerId() != playerCache.getPlayerId() && p.getPlayerInfoModel().getStatus() != 1) {
                    BeanFactory.getBean(PlayerService.class).chosePlayer(player, p.getPlayerInfoModel().getPlayerId());
                    deletePlayerRs.setChosePlayerId(p.getPlayerId());
                    break;
                }
            }
        }
        sendMsg(session, deletePlayerRs);
    }
}