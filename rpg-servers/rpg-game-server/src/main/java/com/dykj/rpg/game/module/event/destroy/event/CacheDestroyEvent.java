package com.dykj.rpg.game.module.event.destroy.event;

import com.dykj.rpg.common.data.dao.PlayerInfoDao;
import com.dykj.rpg.common.data.dao.PlayerSkillDao;
import com.dykj.rpg.game.module.ai.service.AiService;
import com.dykj.rpg.game.module.card.service.CardService;
import com.dykj.rpg.game.module.equip.service.EquipService;
import com.dykj.rpg.game.module.event.core.AbstractEvent;
import com.dykj.rpg.game.module.item.service.ItemService;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.task.logic.service.TaskService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: jyb
 * @Date: 2020/11/2 14:06
 * @Description:
 */
@Component
public class CacheDestroyEvent extends AbstractEvent {
	@Resource
	private PlayerInfoDao playerInfoDao;

	@Resource
	private ItemService itemService;

	@Resource
	private PlayerSkillDao playerSkillDao;

	@Resource
	private EquipService equipService;

	@Resource
	private TaskService taskService;

	@Resource
	private CardService cardService;

	@Resource
	private AiService aiService;

	@Override
	public void doEvent(Object... prams) throws Exception {
		Player player = (Player) prams[0];
		// playerInfoDao.removeCache(Long.valueOf(player.getAccount().getAccountKey()));
		// itemService.destroyItemCache(player);//玩家下线从缓存中移除背包信息
		// playerSkillDao.removeCache(Long.valueOf(player.getPlayerId()));
		// taskService.destroyTaskCache(player);//玩家下线从缓存中移除任务信息
		// equipService.destroyEquipCache(player);
		// cardService.destroyCardCache(player);//玩家下线从缓存中移除抽卡记录及抽卡结果信息
		// aiService.destroyAiCache(player);//玩家下线从缓存中移除雕纹
		player.clearPlayerCache();
	}
}
