package com.dykj.rpg.game.module.player.handler;

import com.dykj.rpg.common.consts.CommonConsts;
import com.dykj.rpg.common.module.uc.model.AccountInfoModel;
import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.consts.LoginTypeEnum;
import com.dykj.rpg.game.core.BeforeGameHandler;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.player.service.PlayerService;
import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.protocol.login.LoginMsgRq;
import com.dykj.rpg.protocol.login.LoginMsgRs;
import com.dykj.rpg.protocol.login.OfflinePlayerRs;
import com.dykj.rpg.protocol.player.PlayerRs;
import com.dykj.rpg.util.JsonUtil;
import com.dykj.rpg.util.date.DateUtils;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @Author: jyb
 * @Date: 2020/9/9 13:59
 * @Description:
 */
public class LoginHandler extends BeforeGameHandler<LoginMsgRq> {


    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void doHandler(LoginMsgRq msg, ISession session) {
        Object accountObject = session.getAttribute(CommonConsts.ACCOUNT_KYE);
        if (accountObject == null) {
            sendError(session, ErrorCodeEnum.ACCOUNT_NOT_EXIST);
            logger.error("PlayerLoginNewHandler doHandler error {} ", ErrorCodeEnum.ACCOUNT_NOT_EXIST, msg.toString());
            return;
        }
        PlayerService playerService = BeanFactory.getBean(PlayerService.class);
        Player player = playerService.getPlayerCache((AccountInfoModel) accountObject);
        if (player == null) {
            sendError(session, ErrorCodeEnum.PLAYER_NOT_EXIST);
            logger.error("PlayerLoginNewHandler doHandler error {} {} ", ErrorCodeEnum.PLAYER_NOT_EXIST, msg.toString());
            return;
        }
        LoginMsgRs loginMsgRs = new LoginMsgRs();
        ISession exist = player.getSession();
        //你的账号在异地登录
        if (exist != null && exist.isActive()) {
            OfflinePlayerRs offlinePlayerRs = new OfflinePlayerRs(1);
            sendMsg(session, offlinePlayerRs);
            return;
        }
        //跟换角色
        playerService.chosePlayer(player, msg.getPlayerId());
        PlayerRs playerRs = BeanFactory.getBean(PlayerService.class).playerRs(player.cache().getPlayerInfoModel());
        loginMsgRs.setPlayer(playerRs);
        sendMsg(session, loginMsgRs);
        player.online(session);
        playerService.enterGameSuccess(player, LoginTypeEnum.LOGIN);
        logger.info("player login success {} ", player.cache().getPlayerInfoModel().toString());
    }
}
