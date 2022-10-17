package com.dykj.rpg.game.module.player.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dykj.rpg.common.consts.CommonConsts;
import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.consts.LoginTypeEnum;
import com.dykj.rpg.game.core.BeforeGameHandler;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.player.service.PlayerCacheService;
import com.dykj.rpg.game.module.player.service.PlayerService;
import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.protocol.login.ReconnectRq;
import com.dykj.rpg.protocol.login.ReconnectRs;
import com.dykj.rpg.protocol.player.PlayerRs;
import com.dykj.rpg.util.spring.BeanFactory;

/**
 * @Author: jyb
 * @Date: 2020/9/9 13:59
 * @Description:
 */
public class ReconnectHandler extends BeforeGameHandler<ReconnectRq> {


    private Logger logger = LoggerFactory.getLogger(getClass());

    private Logger roleLog = LoggerFactory.getLogger("ROLE");

    @Override
    protected void doHandler(ReconnectRq msg, ISession session) {
        PlayerService playerService= BeanFactory.getBean(PlayerService.class);
        PlayerCacheService playerCacheService = BeanFactory.getBean(PlayerCacheService.class);
        Player player =playerCacheService.getCache(msg.getPlayerId());
        if(player==null){
            sendError(session, ErrorCodeEnum.LOGIN_MSG_ERROR);
            logger.error("ReconnectHandler doHandler error {} {} ", ErrorCodeEnum.LOGIN_MSG_ERROR,msg.toString());
            return;
        }
        if(player.getAccount()==null){
            sendError(session, ErrorCodeEnum.LOGIN_MSG_ERROR);
            logger.error("ReconnectHandler doHandler error {} {} ", ErrorCodeEnum.LOGIN_MSG_ERROR,msg.toString());
            return;
        }
        ISession exist = player.getSession();
        //你的账号在异地登录
        if (exist != null && exist.isActive()) {
            exist.sessionClosed();
            return;
        }
        PlayerRs playerRs = BeanFactory.getBean(PlayerService.class).playerRs(player.cache().getPlayerInfoModel());
        ReconnectRs reconnectRs = new ReconnectRs();
        reconnectRs.setPlayer(playerRs);
        playerService.chosePlayer(player,msg.getPlayerId());
        sendMsg(session, reconnectRs);
        session.attribute(CommonConsts.ACCOUNT_KYE, player.getAccountInfoModel());
        player.online(session);
        playerService.enterGameSuccess(player, LoginTypeEnum.RECONNECT);
        logger.info("ReconnectHandler Reconnect success {} ",playerRs.toString());
    }
}
