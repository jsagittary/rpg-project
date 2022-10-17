package com.dykj.rpg.game.module.cache;

import com.dykj.rpg.common.data.model.PlayerItemModel;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 玩家背包Cache
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/18
 */
@Component
public class PlayerItemCache {

	/**
	 * 道具缓存
	 */
	private final Map<Long, PlayerItemModel> itemCacheMap = new ConcurrentHashMap<>();

	public PlayerItemModel getCache(Long itemId) {
		return itemCacheMap.get(itemId);
	}

	public void updateCache(PlayerItemModel model) {
		itemCacheMap.put(model.primary2Key(), model);
	}

	public void deleteCache(PlayerItemModel model) {
		PlayerItemModel playerItemModel = itemCacheMap.get(model.primary2Key());
		if (null != playerItemModel) {
			itemCacheMap.remove(model.primary2Key());
		}
	}

	public Map<Long, PlayerItemModel> getItemCacheMap() {
		return itemCacheMap;
	}

	public Collection<PlayerItemModel> values() {
		return itemCacheMap.values();
	}
}
