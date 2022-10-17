package com.dykj.rpg.game.module.cache;

import com.dykj.rpg.common.data.model.PlayerRuneModel;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Description 玩家符文Cache
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/5/20
 */
@Component
public class PlayerRuneCache
{

	/**
	 * 道具缓存
	 */
	private final Map<Long, PlayerRuneModel> runeCacheMap = new ConcurrentHashMap<>();

	public PlayerRuneModel getCache(Long runeId) {
		return runeCacheMap.get(runeId);
	}

	public void updateCache(PlayerRuneModel model) {
		runeCacheMap.put(model.primary2Key(), model);
	}

	public void deleteCache(PlayerRuneModel model) {
		PlayerRuneModel playerRuneModel = runeCacheMap.get(model.primary2Key());
		if (null != playerRuneModel) {
			runeCacheMap.remove(model.primary2Key());
		}
	}

	public Map<Long, PlayerRuneModel> getRuneCacheMap() {
		return runeCacheMap;
	}

	public Collection<PlayerRuneModel> values() {
		return runeCacheMap.values();
	}

	/**
	 * 根据技能id查询绑定的符文列表
	 * @param skillId 技能id
	 */
	public List<PlayerRuneModel> getRuneList(int skillId)
	{
		return runeCacheMap.values().stream().filter(e -> e.getSkillId() == skillId).collect(Collectors.toList());
	}

	/**
	 * 根据技能id和符文id查询当前符文
	 * @param skillId 技能id
	 * @param runeId 符文id
	 */
	public PlayerRuneModel getRuneInfo(int skillId, int runeId)
	{
		List<PlayerRuneModel> playerRuneModelList = this.getRuneList(skillId);
		if (null != playerRuneModelList && !playerRuneModelList.isEmpty())
		{
			Optional<PlayerRuneModel> first = playerRuneModelList.stream().filter(e -> e.getRuneId() == runeId).findFirst();
			if (first.isPresent())
			{
				return first.get();
			}
		}
		return null;
	}
}
