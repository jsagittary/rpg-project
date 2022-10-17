package com.dykj.rpg.game.module.ai.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.core.GameHandler;
import com.dykj.rpg.game.module.ai.service.AiService;
import com.dykj.rpg.game.module.cache.PlayerAiCache;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.protocol.ai.AiReplaceRq;
import com.dykj.rpg.util.spring.BeanFactory;

/**
 * @author CaoBing
 * @date 2021/4/19 16:12
 * @Description:
 */
public class AiReplaceHandler extends GameHandler<AiReplaceRq> {
    private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void doHandler(AiReplaceRq aiReplaceRq, Player player) {
		PlayerAiCache playerAiCache = player.cache().getPlayerAiCache();
        if (playerAiCache == null) {
            logger.error("GameHandler doHandler error : plAiCache not exist {}", player.getPlayerId());
            return;
        }
        
        ErrorCodeEnum errorCodeEnum = BeanFactory.getBean(AiService.class).aiReplace(player, aiReplaceRq);
        if (errorCodeEnum != ErrorCodeEnum.SUCCESS) {
            sendError(player, errorCodeEnum);
        }
		
	}
}
