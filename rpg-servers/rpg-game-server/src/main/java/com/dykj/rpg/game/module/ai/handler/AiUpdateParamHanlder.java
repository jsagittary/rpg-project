package com.dykj.rpg.game.module.ai.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.core.GameHandler;
import com.dykj.rpg.game.module.ai.service.AiService;
import com.dykj.rpg.game.module.cache.PlayerAiCache;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.protocol.ai.AiUpdateParamRq;
import com.dykj.rpg.util.spring.BeanFactory;

/**
 * @author CaoBing
 * @date 2021年4月26日
 * @Description:
 */
public class AiUpdateParamHanlder  extends GameHandler<AiUpdateParamRq> {
    private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void doHandler(AiUpdateParamRq updateParamRq, Player player) {
		PlayerAiCache playerAiCache = player.cache().getPlayerAiCache();
        if (playerAiCache == null) {
            logger.error("GameHandler doHandler error : plAiCache not exist {}", player.getPlayerId());
            return;
        }
        
        ErrorCodeEnum errorCodeEnum = BeanFactory.getBean(AiService.class).aiUpdateParam(player, updateParamRq);
        if (errorCodeEnum != ErrorCodeEnum.SUCCESS) {
            sendError(player, errorCodeEnum);
        }
	}
}
