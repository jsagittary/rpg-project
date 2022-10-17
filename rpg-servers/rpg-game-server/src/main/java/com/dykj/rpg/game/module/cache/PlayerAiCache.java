package com.dykj.rpg.game.module.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.dykj.rpg.common.data.model.PlayerAiModel;

/**
 * @author CaoBing
 * @date 2021/4/7 19:07
 * @Description:
 */
public class PlayerAiCache {

	private Map<Integer, PlayerAiModel> playerAiModelMap = new ConcurrentHashMap<>();

	public Map<Integer, PlayerAiModel> getPlayerAiModelMap() {
		return playerAiModelMap;
	}

	public void setPlayerAiModelMap(Map<Integer, PlayerAiModel> playerAiModelMap) {
		this.playerAiModelMap = playerAiModelMap;
	}

	public PlayerAiModel getByAiPos(int AiPos) {
		for (PlayerAiModel p : playerAiModelMap.values()) {
			if (p.getPosition() == AiPos) {
				return p;
			}
		}
		return null;
	}

	/**
	 * 获得玩家穿戴的雕纹
	 *
	 * @return
	 */
	public List<PlayerAiModel> playerAis() {
		List<PlayerAiModel> playerAiModels = new ArrayList<>();
		for (PlayerAiModel playerAiModel : playerAiModelMap.values()) {
			if (playerAiModel.getPosition() == -1) {
				continue;
			}
			playerAiModels.add(playerAiModel);
		}
		return playerAiModels;
	}
}
