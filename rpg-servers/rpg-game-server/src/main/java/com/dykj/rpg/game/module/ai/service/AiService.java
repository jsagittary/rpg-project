package com.dykj.rpg.game.module.ai.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.dykj.rpg.common.config.dao.AiCharacterBasicDao;
import com.dykj.rpg.common.config.dao.ItemDao;
import com.dykj.rpg.common.config.model.AiCharacterBasicModel;
import com.dykj.rpg.common.config.model.ItemModel;
import com.dykj.rpg.common.consts.ItemOperateEnum;
import com.dykj.rpg.common.consts.ItemTypeEnum;
import com.dykj.rpg.common.data.dao.PlayerAiDao;
import com.dykj.rpg.common.data.model.PlayerAiModel;
import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.module.cache.PlayerAiCache;
import com.dykj.rpg.game.module.item.consts.ItemPromp;
import com.dykj.rpg.game.module.item.response.ItemJoinModel;
import com.dykj.rpg.game.module.item.response.ItemResponse;
import com.dykj.rpg.game.module.item.service.ItemService;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.util.CmdUtil;
import com.dykj.rpg.protocol.ai.AiOffRq;
import com.dykj.rpg.protocol.ai.AiOffRs;
import com.dykj.rpg.protocol.ai.AiReplaceRq;
import com.dykj.rpg.protocol.ai.AiReplaceRs;
import com.dykj.rpg.protocol.ai.AiRs;
import com.dykj.rpg.protocol.ai.AiUpdateParamRq;
import com.dykj.rpg.protocol.ai.AiUpdateParamRs;
import com.dykj.rpg.protocol.ai.AiWearRq;
import com.dykj.rpg.protocol.ai.AiWearRs;
import com.dykj.rpg.protocol.game2battle.AiBattleRs;
import com.dykj.rpg.protocol.item.ItemRs;
import com.dykj.rpg.protocol.item.UpdateItemListRs;

/**
 * @author CaoBing
 * @date 2021/4/8 11:01
 * @Description:
 */
@Service
public class AiService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private ItemDao itemDao;

	@Resource
	private PlayerAiDao playerAiDao;

	@Resource
	private AiCharacterBasicDao aiCharacterBasicDao;

	@Resource
	private ItemService itemService;

	/**
	 * 添加雕纹
	 *
	 * @param itemId
	 * @param player
	 * @param itemOperateEnum
	 * @return
	 */
	public ItemRs addAi(int itemId, Player player, ItemOperateEnum itemOperateEnum) {
		PlayerAiCache playerAiCache = player.cache().getPlayerAiCache();
		if (null == playerAiCache) {
			return null;
		}

		PlayerAiModel playerAiModel = playerAiCache.getPlayerAiModelMap().get(itemId);
		// 判断是否拥有相同的雕纹
		// 1.有就直接分解为碎片 2.没有直接添加
		if (null != playerAiModel) {
			ItemModel itemModel = itemDao.getConfigByKey(itemId);
			if (itemModel == null) {
				logger.error("item config {}  not exist ", itemId);
				return null;
			}

			Integer itemSellItem = itemModel.getItemSellItem();
			if (null == itemSellItem) {
				logger.error("item config error {}  not itemSellItem exist ", itemId);
				return null;
			}

			ItemModel configByKey = itemDao.getConfigByKey(itemSellItem);
			if (configByKey == null) {
				logger.error("item config  {}  not exist ", itemSellItem);
				return null;
			}

			Integer itemSellCurrencyPrice = itemModel.getItemSellCurrencyPrice();
			if (null == itemSellCurrencyPrice) {
				logger.error("item config error {}  not itemSellCurrencyPrice exist ", itemSellItem);
				return null;
			}
			ItemResponse itemResponse = itemService.updateItem(player,
					new ItemJoinModel(itemSellItem, itemSellCurrencyPrice, configByKey.getItemType()), itemOperateEnum,
					ItemPromp.GENERIC);

			if (null == itemResponse.getUpdateItemListRs()) {
				logger.error("item config error {}  not itemSellCurrencyPrice exist ", itemSellItem);
				return null;
			}

			UpdateItemListRs protocol = itemResponse.getUpdateItemListRs();
			return protocol.getItemArr().get(0);
		}
		playerAiModel = initAi(itemId, player);
		return itemRs(playerAiModel);
	}

	/**
	 * 初始化一个雕纹
	 *
	 * @param itemId
	 * @param player
	 * @return
	 */
	public PlayerAiModel initAi(int itemId, Player player) {
		ItemModel itemModel = itemDao.getConfigByKey(itemId);
		if (itemModel == null) {
			logger.error("item config {}  not exist ", itemId);
			return null;
		}

		String itemUseResult = itemModel.getItemUseResult();
		if (null == itemUseResult) {
			logger.error("item config error {}  not have aiSkill", itemId);
			return null;
		}

		AiCharacterBasicModel aiCharacterBasic = aiCharacterBasicDao.getConfigByKey(Integer.parseInt(itemUseResult));
		if (null == aiCharacterBasic) {
			logger.error("ai config error {}  not have ai", itemUseResult);
			return null;
		}

		int aiConditionParam = 0;
		int aiActionParam = 0;
		try {
			List<Integer> aiConditionRange = aiCharacterBasic.getAiConditionRange();
			if (null != aiConditionRange && aiConditionRange.size() > 0) {
				Integer index = aiConditionRange.get(0);
				List<Integer> aiConditionParams = aiCharacterBasic.getAiConditionParams();
				if (null != aiConditionParams && aiConditionParams.size() > 0) {
					aiConditionParam = aiConditionParams.get(index - 1);
				}
			}

			List<Integer> aiActionRange = aiCharacterBasic.getAiActionRange();
			if (null != aiActionRange && aiActionRange.size() > 0) {
				Integer index = aiActionRange.get(0);
				List<Integer> aiActionParams = aiCharacterBasic.getAiActionParams();
				if (null != aiActionParams && aiActionParams.size() > 0) {
					aiActionParam = aiActionParams.get(index - 1);
				}
			}
		} catch (Exception e) {
			logger.error("ai config error {}  not have ai", itemUseResult);
			return null;
		}

		PlayerAiModel playerAiModel = new PlayerAiModel(player.getPlayerId(), itemId, -1, aiConditionParam,
				aiActionParam);
		playerAiDao.insert(playerAiModel);
		player.cache().getPlayerAiCache().getPlayerAiModelMap().put(itemId, playerAiModel);

		return playerAiModel;
	}

	public ItemRs itemRs(PlayerAiModel playerAiModel) {
		ItemRs itemRs = new ItemRs();
		itemRs.setItemId(playerAiModel.getAiId());
		itemRs.setItemType(ItemTypeEnum.AI.getItemType());
		itemRs.setItemNum(1);

		itemRs.setAi(aiRs(playerAiModel));
		return itemRs;
	}

	public AiRs aiRs(PlayerAiModel playerAiModel) {
		AiRs aiRs = new AiRs();
		aiRs.setItemId(playerAiModel.getAiId());

		ItemModel itemCoifg = itemDao.getConfigByKey(playerAiModel.getAiId());
		AiCharacterBasicModel aiConfig = aiCharacterBasicDao
				.getConfigByKey(Integer.parseInt(itemCoifg.getItemUseResult()));
		aiRs.setAiId(aiConfig.getAiId());
		aiRs.setPosition(playerAiModel.getPosition());
		aiRs.setAiConditionParam(playerAiModel.getaiConditionParam());
		aiRs.setAiActionParam(playerAiModel.getAiActionParam());
		return aiRs;
	}

	public AiBattleRs aiBattleRs(PlayerAiModel playerAiModel) {
		AiBattleRs aiBattleRs = new AiBattleRs();
		aiBattleRs.setItemId(playerAiModel.getAiId());
		aiBattleRs.setPosition(playerAiModel.getPosition());

		ItemModel itemConfig = itemDao.getConfigByKey(playerAiModel.getAiId());
		AiCharacterBasicModel configByKey = aiCharacterBasicDao
				.getConfigByKey(Integer.parseInt(itemConfig.getItemUseResult()));
		aiBattleRs.setAiId(configByKey.getAiId());

		int conditionParam = playerAiModel.getaiConditionParam();
		List<Integer> aiConditionConfig = configByKey.getAiConditionParams();
		if (null != aiConditionConfig) {
			List<Integer> aiConditionParams = new ArrayList<>(aiConditionConfig);
			if (conditionParam != 0) {
				List<Integer> aiConditionRange = configByKey.getAiConditionRange();
				if (null != aiConditionRange && aiConditionRange.size() > 0) {
					int index = aiConditionRange.get(0);
					if (aiConditionParams.size() > 0) {
						aiConditionParams.set(index - 1, conditionParam);
						aiBattleRs.setAiConditionParam(aiConditionParams);
					}
				}
			} else {
				aiBattleRs.setAiActionParam(aiConditionParams);
			}
		}

		int actionParam = playerAiModel.getAiActionParam();
		List<Integer> aiActionConfig = configByKey.getAiActionParams();
		if (null != aiActionConfig) {
			List<Integer> aiActionParams = new ArrayList<>(aiActionConfig);
			if (actionParam != 0) {
				List<Integer> aiActionRange = configByKey.getAiActionRange();
				if (null != aiActionRange && aiActionRange.size() > 0) {
					Integer index = aiActionRange.get(0);
					if (aiActionRange.size() > 0) {
						aiActionParams.set(index - 1, actionParam);
						aiBattleRs.setAiActionParam(aiActionParams);
					}
				}
			} else {
				aiBattleRs.setAiActionParam(aiActionParams);
			}
		}
		return aiBattleRs;
	}

	/**
	 * 获得一个玩家的所有雕纹信息
	 *
	 * @param player
	 * @return
	 */
	public List<ItemRs> getAis(Player player) {
		List<ItemRs> itemRs = new ArrayList<>();
		PlayerAiCache playerAiCache = player.cache().getPlayerAiCache();
		if (playerAiCache == null) {
			return itemRs;
		}
		for (PlayerAiModel playerAiModel : playerAiCache.getPlayerAiModelMap().values()) {
			itemRs.add(itemRs(playerAiModel));

		}
		return itemRs;
	}

	/**
	 * 穿戴雕紋
	 *
	 * @param player
	 * @param aiWearRq
	 * @return
	 */
	public ErrorCodeEnum aiWear(Player player, AiWearRq aiWearRq) {
		PlayerAiCache playerAiCache = player.cache().getPlayerAiCache();
		if (playerAiCache == null) {
			logger.error("AipUP error ： playerAiCache not exist {} ", player.getPlayerId());
			return ErrorCodeEnum.DATA_ERROR;
		}

		PlayerAiModel playerAiModel = playerAiCache.getPlayerAiModelMap().get(aiWearRq.getItemId());
		if (playerAiModel == null) {
			return ErrorCodeEnum.PLAYER_HAS_NO_SKILL;
		}

		ItemModel itemModel = itemDao.getConfigByKey(aiWearRq.getItemId());
		if (itemModel == null) {
			logger.error("item config {}  not exist ", aiWearRq.getItemId());
			return null;
		}

		AiWearRs AiWearRs = new AiWearRs();
		PlayerAiModel offAi = playerAiCache.getByAiPos(aiWearRq.getPos());

		if (offAi != null) {
			offAi.setPosition(-1);
			AiWearRs.setDownItem(itemRs(offAi));
			playerAiDao.queueUpdate(offAi);
			playerAiCache.getPlayerAiModelMap().put(offAi.getAiId(), offAi);
		}

		playerAiModel.setPosition(aiWearRq.getPos());
		AiWearRs.setUpItem(itemRs(playerAiModel));
		playerAiDao.queueUpdate(playerAiModel);
		player.cache().getPlayerAiCache().getPlayerAiModelMap().put(playerAiModel.getAiId(), playerAiModel);

		// TODO 这里要更新玩家属性相关
		CmdUtil.sendMsg(player, AiWearRs);

		return ErrorCodeEnum.SUCCESS;
	}

	/**
	 * 卸下雕纹
	 *
	 * @param player
	 * @param aiOffRq
	 * @return
	 */
	public ErrorCodeEnum aiOff(Player player, AiOffRq aiOffRq) {
		PlayerAiCache playerAiCache = player.cache().getPlayerAiCache();
		if (playerAiCache == null) {
			logger.error("AipDown error ： playerAiCache not exist {} ", player.getPlayerId());
			return ErrorCodeEnum.DATA_ERROR;
		}

		PlayerAiModel offAi = playerAiCache.getByAiPos(aiOffRq.getPos());
		if (offAi == null) {
			return ErrorCodeEnum.DATA_ERROR;
		}

		AiOffRs aiOffRs = new AiOffRs();
		offAi.setPosition(-1);
		aiOffRs.setItem(itemRs(offAi));

		playerAiDao.queueUpdate(offAi);
		// TODO 这里要更新玩家属性相关
		CmdUtil.sendMsg(player, aiOffRs);
		return ErrorCodeEnum.SUCCESS;
	}

	/**
	 * 雕纹替换
	 *
	 * @param player
	 * @param replaceRq
	 * @return
	 */
	public ErrorCodeEnum aiReplace(Player player, AiReplaceRq replaceRq) {
		PlayerAiCache playerAiCache = player.cache().getPlayerAiCache();
		if (playerAiCache == null) {
			logger.error("AipDown error ： playerAiCache not exist {} ", player.getPlayerId());
			return ErrorCodeEnum.DATA_ERROR;
		}

		PlayerAiModel poAiModel1 = playerAiCache.getByAiPos(replaceRq.getPos1());
		PlayerAiModel poAiModel2 = playerAiCache.getByAiPos(replaceRq.getPos2());

		if (poAiModel1 == null) {
			return ErrorCodeEnum.DATA_ERROR;
		}

		AiReplaceRs aiReplaceRs = new AiReplaceRs();
		poAiModel1.setPosition(replaceRq.getPos2());
		aiReplaceRs.setItem1(itemRs(poAiModel1));
		playerAiDao.queueUpdate(poAiModel1);

		if (poAiModel2 != null) {
			poAiModel2.setPosition(replaceRq.getPos1());
			aiReplaceRs.setItem2(itemRs(poAiModel2));
			playerAiDao.queueUpdate(poAiModel2);
		}

		CmdUtil.sendMsg(player, aiReplaceRs);
		return ErrorCodeEnum.SUCCESS;
	}

	/**
	 * 修改雕纹技能参数值
	 *
	 * @param player
	 * @param updateParamRq
	 * @return
	 */
	public ErrorCodeEnum aiUpdateParam(Player player, AiUpdateParamRq updateParamRq) {
		PlayerAiCache playerAiCache = player.cache().getPlayerAiCache();
		if (playerAiCache == null) {
			logger.error("aiUpdateParam error ： playerAiCache not exist {} ", player.getPlayerId());
			return ErrorCodeEnum.DATA_ERROR;
		}

		int itemId = updateParamRq.getItemId();
		int aiConditionParam = updateParamRq.getAiConditionParam();
		int aiActionParam = updateParamRq.getAiActionParam();

		if (itemId == 0) {
			logger.error("aiUpdateParam error ： client_prams_error", player.getPlayerId());
			return ErrorCodeEnum.CLIENT_PRAMS_ERROR;
		}

		PlayerAiModel playerAiModel = playerAiCache.getPlayerAiModelMap().get(itemId);
		if (playerAiModel == null) {
			return ErrorCodeEnum.DATA_ERROR;
		}

		playerAiModel.setaiConditionParam(aiConditionParam);
		playerAiModel.setAiActionParam(aiActionParam);

		AiUpdateParamRs aiUpdateParamRs = new AiUpdateParamRs();
		aiUpdateParamRs.setItem(itemRs(playerAiModel));
		playerAiDao.queueUpdate(playerAiModel);
		player.cache().getPlayerAiCache().getPlayerAiModelMap().put(playerAiModel.getAiId(), playerAiModel);

		// TODO 这里要更新玩家属性相关
		CmdUtil.sendMsg(player, aiUpdateParamRs);
		return ErrorCodeEnum.SUCCESS;
	}
}
