package com.dykj.rpg.game.module.player.handler;

import com.dykj.rpg.common.consts.CommonConsts;
import com.dykj.rpg.common.data.cache.PlayerNameCacheMgr;
import com.dykj.rpg.common.module.uc.model.AccountInfoModel;
import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.consts.LoginTypeEnum;
import com.dykj.rpg.game.core.BeforeGameHandler;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.player.service.PlayerService;
import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.protocol.login.RegisterMsgRq;
import com.dykj.rpg.protocol.login.RegisterMsgRs;
import com.dykj.rpg.protocol.player.PlayerRs;
import com.dykj.rpg.util.spring.BeanFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class RegisterHandler extends BeforeGameHandler<RegisterMsgRq> {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    protected void doHandler(RegisterMsgRq msg, ISession session) {
        logger.info("RegisterMsgRq session {}  name {}  byte[]{}:",session.getId(),msg.getName(), Arrays.toString(msg.getName().getBytes()));
        Object accountObject = session.getAttribute(CommonConsts.ACCOUNT_KYE);
        if (accountObject == null) {
            sendError(session, ErrorCodeEnum.ACCOUNT_NOT_EXIST);
            logger.error("RegisterHandler doHandler error {} ", ErrorCodeEnum.ACCOUNT_NOT_EXIST);
            return;
        }
        PlayerService playerService = BeanFactory.getBean(PlayerService.class);

        if (StringUtils.isBlank(msg.getName())) {
            logger.error("RegisterHandler doHandler name is null  ");
            return;
        }
        if (!playerService.isExist(msg.getProfession())) {
            sendError(session, ErrorCodeEnum.CHARACTER_NOT_EXIST);
            logger.error("RegisterHandler doHandler error {} ", ErrorCodeEnum.CHARACTER_NOT_EXIST);
            return;
        }
        boolean repeat = BeanFactory.getBean(PlayerNameCacheMgr.class).addName(msg.getName(), true);
        if (!repeat) {
            sendError(session, ErrorCodeEnum.PLAYER_NAME_REPEAT);
            logger.error("RegisterHandler doHandler name repeat {} ", ErrorCodeEnum.PLAYER_NAME_REPEAT);
            return;
        }
        Player player = playerService.register(msg, (AccountInfoModel) accountObject);
        RegisterMsgRs playerRs =playerService.registerMsg(player);
        sendMsg(session,playerRs);

        //登录后续数据发送
        player.online(session);
        playerService.enterGameSuccess(player, LoginTypeEnum.REGISTER);
        logger.info("RegisterHandler success  {}",player.cache().getPlayerInfoModel().toString());
    }
}
