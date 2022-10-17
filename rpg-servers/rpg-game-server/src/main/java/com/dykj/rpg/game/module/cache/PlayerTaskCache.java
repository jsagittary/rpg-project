package com.dykj.rpg.game.module.cache;

import com.dykj.rpg.common.data.model.PlayerTaskModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 玩家任务Cache
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/4
 */
public class PlayerTaskCache {

	/**
	 * 任务缓存
	 */
	private final Map<Long, PlayerTaskModel> taskCacheMap = new ConcurrentHashMap<>();

	public PlayerTaskModel getCache(Long taskId) {
		return taskCacheMap.get(taskId);
	}

	public void updateCache(PlayerTaskModel model) {
		taskCacheMap.put(model.primary2Key(), model);
	}

	public void queueDelete(PlayerTaskModel model) {
		PlayerTaskModel playerTaskModel = taskCacheMap.get(model.primary2Key());
		if (null != playerTaskModel) {
			taskCacheMap.remove(model.primary2Key());
		}
	}

	public Map<Long, PlayerTaskModel> getTaskCacheMap() {
		return taskCacheMap;
	}

	public Collection<PlayerTaskModel> values() {
		return taskCacheMap.values();
	}

	/**
	 * 根据任务类型、触发类型为1(已触发状态)的任务列表
	 * 
	 * @param taskType
	 *            任务类型
	 * @return 符合任务类型的任务列表
	 */
	public List<PlayerTaskModel> getTaskList(Integer... taskType) {
		List<PlayerTaskModel> list = new ArrayList<>();
		if (null != taskCacheMap.values() && !taskCacheMap.values().isEmpty()) {
			for (PlayerTaskModel playerTaskModel : taskCacheMap.values()) {
				// 触发状态为1-已触发
				if (playerTaskModel.getTriggerStatus() == 1) {
					for (Integer inte : taskType) {
						if (playerTaskModel.getTaskType() == inte) {
							list.add(playerTaskModel);
						}
					}
				}
			}
		}
		return list;
	}
}
