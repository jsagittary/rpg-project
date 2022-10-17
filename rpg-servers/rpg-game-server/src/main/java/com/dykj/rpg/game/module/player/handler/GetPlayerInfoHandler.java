package com.dykj.rpg.game.module.player.handler;

import com.dykj.rpg.common.consts.CommonConsts;
import com.dykj.rpg.common.module.uc.model.AccountInfoModel;
import com.dykj.rpg.common.redis.cache.AccountCacheMgr;
import com.dykj.rpg.common.redis.cache.LoginDataCacheMgr;
import com.dykj.rpg.common.redis.data.LoginData;
import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.core.BeforeGameHandler;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.player.service.PlayerService;
import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.protocol.login.GetPlayerInfoRq;
import com.dykj.rpg.protocol.login.GetPlayerInfoRs;
import com.dykj.rpg.protocol.player.PlayerRs;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: jyb
 * @Date: 2020/9/9 13:59
 * @Description:
 */
public class GetPlayerInfoHandler extends BeforeGameHandler<GetPlayerInfoRq> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void doHandler(GetPlayerInfoRq msg, ISession session) {
        PlayerService playerService = BeanFactory.getBean(PlayerService.class);
         AccountCacheMgr accountCacheMgr = BeanFactory.getBean(AccountCacheMgr.class);
        LoginDataCacheMgr loginDataCacheMgr = BeanFactory.getBean(LoginDataCacheMgr.class);
        LoginData loginData = loginDataCacheMgr.get(msg.getAccountKey());
        if (loginData == null) {
            sendError(session, ErrorCodeEnum.LOGIN_MSG_ERROR);
            logger.error("GetPlayerInfoHandler doHandler error {}:{} ", ErrorCodeEnum.LOGIN_MSG_ERROR, msg.toString());
            return;
        }
        AccountInfoModel accountInfoModel = accountCacheMgr.get(loginData.getAccount(), loginData.getChannel());
        if (accountInfoModel == null) {
            sendError(session, ErrorCodeEnum.ACCOUNT_NOT_EXIST);
            logger.error("GetPlayerInfoHandler doHandler error {}: {} ", ErrorCodeEnum.ACCOUNT_NOT_EXIST, msg.toString());
            return;
        }
        session.attribute(CommonConsts.ACCOUNT_KYE, accountInfoModel);
        Player player=playerService.initPlayerInfo(accountInfoModel.getAccountKey(), loginData.getServerId());
        GetPlayerInfoRs getPlayerInfoRs = playerService.getPlayerInfoRs(player);
        List<Integer> playerIds = getPlayerInfoRs.getPlayers().stream().map(PlayerRs::getPlayerId).collect(Collectors.toList());
        session.attribute(CommonConsts.PLAYER_IDS, playerIds);
        sendMsg(session, getPlayerInfoRs);
    }
}
