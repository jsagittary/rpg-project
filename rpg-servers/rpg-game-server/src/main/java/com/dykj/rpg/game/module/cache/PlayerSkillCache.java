package com.dykj.rpg.game.module.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.annotation.JSONField;
import com.dykj.rpg.common.data.model.PlayerSkillModel;

/**
 * @author jyb
 * @date 2020/11/11 15:15
 * @Description
 */
public class PlayerSkillCache {

	private Map<Integer, PlayerSkillModel> playerSkillModelMap = new ConcurrentHashMap<>();

	public Map<Integer, PlayerSkillModel> getPlayerSkillModelMap() {
		return playerSkillModelMap;
	}

	public void setPlayerSkillModelMap(Map<Integer, PlayerSkillModel> playerSkillModelMap) {
		this.playerSkillModelMap = playerSkillModelMap;
	}

	public void updateCache(PlayerSkillModel model) {
		playerSkillModelMap.put(model.getSkillId(), model);
	}

	public void queueDelete(PlayerSkillModel model) {
		PlayerSkillModel playerTaskModel = playerSkillModelMap.get(model.getSkillId());
		if (null != playerTaskModel) {
			playerSkillModelMap.remove(model.getSkillId());
		}
	}

	/***
	 * 获得玩家的技能
	 * 
	 * @return
	 */
	public List<PlayerSkillModel> getUpSkill() {
		List<PlayerSkillModel> playerSkillModels = new ArrayList<>();
		for (PlayerSkillModel payerSkillModel : playerSkillModelMap.values()) {
			if (payerSkillModel.getPosition() != -1) {
				playerSkillModels.add(payerSkillModel);
			}
		}
		return playerSkillModels;
	}

	public PlayerSkillModel getUpSkillByPosition(int position) {
		for (PlayerSkillModel payerSkillModel : playerSkillModelMap.values()) {
			if (payerSkillModel.getPosition() == position) {
				return payerSkillModel;
			}
		}
		return null;
	}

	public List<PlayerSkillModel> getTrainSkill() {
		List<PlayerSkillModel> playerSkillModels = new ArrayList<>();
		for (PlayerSkillModel payerSkillModel : playerSkillModelMap.values()) {
			if (payerSkillModel.getSoulChangeTime() != null) {
				playerSkillModels.add(payerSkillModel);
			}
		}
		return playerSkillModels;
	}

	/**
	 *位置是否被占用了
	 * @param pos
	 * @return
	 */
	public boolean posError(int pos){
		for (PlayerSkillModel payerSkillModel : playerSkillModelMap.values()) {
			if (payerSkillModel.getSoulChangePos()== pos) {
				return true;
			}
		}
		return  false;
	}
}