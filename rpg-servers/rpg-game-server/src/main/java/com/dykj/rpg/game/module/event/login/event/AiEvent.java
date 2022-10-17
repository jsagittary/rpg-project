package com.dykj.rpg.game.module.event.login.event;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.dykj.rpg.common.data.dao.PlayerAiDao;
import com.dykj.rpg.common.data.model.PlayerAiModel;
import com.dykj.rpg.game.module.cache.PlayerAiCache;
import com.dykj.rpg.game.module.event.core.AbstractEvent;
import com.dykj.rpg.game.module.player.logic.Player;

/**
 * @author CaoBing
 * @date 2021/4/8 19:58
 * @Description:
 */
@Component
public class AiEvent extends AbstractEvent {

	@Resource
	private PlayerAiDao playerAiDao;

	@Override
	public void doEvent(Object... prams) throws Exception {
		Player player = (Player) prams[0];
		initPlayerAiCache(player);
	}

	/**
	 * 初始化雕纹信息
	 *
	 * @param player
	 */
	private void initPlayerAiCache(Player player) {
		PlayerAiCache playerAiCache = player.cache().getPlayerAiCache();
		if (playerAiCache == null) {
			playerAiCache = new PlayerAiCache();
			List<PlayerAiModel> playerAiModels = playerAiDao.getAiEntryModels(player.getPlayerId());
			if (playerAiModels != null && playerAiModels.size() > 0) {
				// 初始化技巧缓存
				for (PlayerAiModel playerAiModel : playerAiModels) {
					playerAiCache.getPlayerAiModelMap().put(playerAiModel.getAiId(), playerAiModel);
				}
			}
		}
		player.cache().setPlayerAiCache(playerAiCache);
	}
}
