package com.dykj.rpg.game.module.item.service.strategy;

import com.dykj.rpg.common.config.dao.ItemDao;
import com.dykj.rpg.common.config.model.ItemModel;
import com.dykj.rpg.common.consts.CommonConsts;
import com.dykj.rpg.common.consts.ItemOperateEnum;
import com.dykj.rpg.common.consts.ItemTypeEnum;
import com.dykj.rpg.common.data.dao.PlayerInfoDao;
import com.dykj.rpg.common.data.dao.PlayerItemDao;
import com.dykj.rpg.common.data.dao.PlayerSkillDao;
import com.dykj.rpg.common.data.model.PlayerInfoModel;
import com.dykj.rpg.common.data.model.PlayerItemModel;
import com.dykj.rpg.common.data.model.PlayerSkillModel;
import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.module.attribute.service.AttributeService;
import com.dykj.rpg.game.module.event.level.LevelEventManager;
import com.dykj.rpg.game.module.item.consts.ItemOperateTypeEnum;
import com.dykj.rpg.game.module.item.consts.ItemPromp;
import com.dykj.rpg.game.module.item.response.ItemJoinModel;
import com.dykj.rpg.game.module.item.response.ItemResponse;
import com.dykj.rpg.game.module.item.service.ItemService;
import com.dykj.rpg.game.module.item.service.RandomItemService;
import com.dykj.rpg.game.module.item.service.strategy.core.ItemOperateRealize;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.task.consts.TaskFinishConditionEnum;
import com.dykj.rpg.game.module.task.logic.response.TaskConditions;
import com.dykj.rpg.game.module.task.logic.refresh.TaskScheduleRefreshUtil;
import com.dykj.rpg.game.module.util.CmdUtil;
import com.dykj.rpg.net.protocol.Protocol;
import com.dykj.rpg.protocol.item.*;
import com.dykj.rpg.protocol.player.RoleUpgradeRs;
import com.dykj.rpg.protocol.player.UpdPlayerInfoRs;
import com.dykj.rpg.protocol.skill.PlayerSkillNewRs;
import com.dykj.rpg.protocol.skill.SkillRs;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description 道具消耗
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/19
 */
@Service
public class ItemDepleteRealize implements ItemOperateRealize
{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private ItemDao itemDao;

	@Resource
	private PlayerSkillDao playerSkillDao;

	@Resource
	private PlayerItemDao playerItemDao;

	@Resource
	private PlayerInfoDao playerInfoDao;

	@Resource
	private ItemService itemService;

	@Resource
	private RandomItemService randomItemService;

	@Resource
	private AttributeService attributeService;

	@Override
	public ItemOperateTypeEnum itemOperating()
	{
		return ItemOperateTypeEnum.USE;
	}

	/**
	 * 实现道具消耗逻辑
	 * @param player 玩家信息
	 * @param itemUniversalListRq 请求协议参数
	 */
	@Override
	public void realize(Player player, ItemUniversalListRq itemUniversalListRq)
	{
		ErrorCodeEnum codeEnum = ErrorCodeEnum.SUCCESS;
		for (ItemUniversalRq itemUniversalRq : itemUniversalListRq.getItemUniversalArr())
		{
			if (0 == itemUniversalRq.getItemId() || itemUniversalRq.getItemNum() <= 0)
			{
				logger.error("玩家id:{}, 协议号:{}, 道具id或道具数量参数错误!", player.getPlayerId(), itemUniversalListRq.getCode());
				codeEnum = ErrorCodeEnum.CLIENT_PRAMS_ERROR;
				break;
			}

			ItemModel itemModel = itemDao.getConfigByKey(itemUniversalRq.getItemId());//道具基础配置信息
			if (null == itemModel || null == itemModel.getItemType())
			{
				logger.error("玩家id:{}, 道具id:{}, 对应道具基础配置表数据为空!", player.getPlayerId(), itemUniversalRq.getItemId());
				codeEnum = ErrorCodeEnum.CONFIG_ERROR;
				break;
			}
			//道具使用效果
			if (null == itemModel.getItemUseResult())
			{
				logger.error("玩家id:{}, 道具id:{}, 对应道具基础配置表\"道具使用效果\"为空!", player.getPlayerId(), itemModel.getItemId());
				codeEnum = ErrorCodeEnum.CONFIG_ERROR;
				break;
			}
			//拿到玩家对应道具
			PlayerItemModel playerItemModel = itemService.getItemInfo(player, itemModel.getItemId());
			if (null == playerItemModel)
			{
				logger.error("玩家id:{}, 道具id:{}, 当前道具不存在!", player.getPlayerId(), itemModel.getItemId());
				codeEnum = ErrorCodeEnum.ITEM_NOT_EXIST;
				break;
			}
			//技能书
			if (itemModel.getItemType() == ItemTypeEnum.SKILL_BOOK.getItemType())
			{
				codeEnum = this.handleSkillBook(player, itemModel, playerItemModel, itemUniversalRq.getItemNum());
				if (!codeEnum.equals(ErrorCodeEnum.SUCCESS))
					break;
			}
			//消耗品
			else if (itemModel.getItemType() == ItemTypeEnum.CONSUMABLES.getItemType())
			{
				codeEnum = this.handleConsumables(player, itemModel, playerItemModel, itemUniversalRq.getItemNum(), itemUniversalListRq);
				if (!codeEnum.equals(ErrorCodeEnum.SUCCESS))
					break;
			}
			/*
			 * 触发任务进度刷新
			 * 任务完成条件类型:
			 *      【激活期间】使用Y道具X次
			 */
			TaskScheduleRefreshUtil.commonSchedule(player,
					new TaskConditions(TaskFinishConditionEnum.DURING_ACTIVATION, TaskFinishConditionEnum.DuringActivationEnum.USE_ITEM, new Object[]{playerItemModel}));
		}

		if (!codeEnum.equals(ErrorCodeEnum.SUCCESS))
		{
			CmdUtil.sendErrorMsg(player.getSession(), itemUniversalListRq.getCode(), codeEnum);
		}
	}

	/**
	 * 处理技能书类型
	 * @param player 玩家信息
	 * @param itemModel 基础道具信息
	 * @param playerItemModel 玩家对应技能书道具
	 * @param itemNum 道具需要被扣减的数量
	 * @return 错误码
	 */
	private ErrorCodeEnum handleSkillBook(Player player, ItemModel itemModel, PlayerItemModel playerItemModel, Integer itemNum)
	{
		//如果道具基础信息表item_lock不为空并且等于2(支持上锁)并且玩家道具表的ItemLock等于2(已上锁)则表示当前道具已上锁
		if (null != itemModel.getItemLock() && itemModel.getItemLock() == 2 && playerItemModel.getItemLock() == 2)
		{
			logger.error("玩家id:{}, 道具id:{}, 当前道具已上锁!", player.getPlayerId(), itemModel.getItemId());
			return ErrorCodeEnum.ITEM_LOCKED;
		}
		PlayerSkillModel playerSkillModel = null;
		try
		{
			playerSkillModel = player.cache().getPlayerSkillCache().getPlayerSkillModelMap().get(Integer.valueOf(itemModel.getItemUseResult()));
		}
		catch (Exception e)
		{
			logger.error("玩家id:{}, 技能id:{}, 类型转换异常!", player.getPlayerId(), itemModel.getItemUseResult(), e);
			return ErrorCodeEnum.CONFIG_ERROR;
		}
		//判断技能表是有该技能无则新增有则弹错误码
		if (null != playerSkillModel)
		{
			logger.error("玩家id:{}, 技能id:{}, 当前技能已存在!", player.getPlayerId(), itemModel.getItemUseResult());
			return ErrorCodeEnum.SKILL_EXISTED;
		}

		//消耗当前技能书道具并新增对应技能
		ItemResponse itemResponse = itemService.updateItem(player, new ItemJoinModel(itemModel.getItemId(), -itemNum, ItemTypeEnum.SKILL_BOOK.getItemType()), ItemOperateEnum.USE, ItemPromp.GENERIC);
		if (itemResponse.getCodeEnum().equals(ErrorCodeEnum.SUCCESS))
		{
			//道具更新成功则新增技能信息
			playerSkillModel = new PlayerSkillModel();
			playerSkillModel.setPlayerId(player.getPlayerId());
			playerSkillModel.setSkillId(Integer.parseInt(itemModel.getItemUseResult()));
			playerSkillModel.setSkillLevel(1);
			playerSkillModel.setSkillExp(0);
			playerSkillModel.setPosition(-1);
			//TODO  注意临时添加技能类型后续要修改
			playerSkillModel.setSkillType(1);//临时添加后续要修改
			playerSkillModel.setSkillStarLevel(0);
			player.cache().getPlayerSkillCache().updateCache(playerSkillModel);
			playerSkillDao.queueInsert(playerSkillModel);
			//组装协议并推送新增的技能至客户端
			SkillRs skillRs = new SkillRs();
			skillRs.setSkillId(playerSkillModel.getSkillId());
			skillRs.setSkillLevel(playerSkillModel.getSkillLevel());
			skillRs.setSkillExp(playerSkillModel.getSkillExp());
			skillRs.setPosition(playerSkillModel.getPosition());
			skillRs.setSkillType(playerSkillModel.getSkillType());
			skillRs.setSkillStarLevel(playerSkillModel.getSkillStarLevel());
			PlayerSkillNewRs playerSkillNewRs = new PlayerSkillNewRs(skillRs);
			CmdUtil.sendMsg(player, playerSkillNewRs);
			//发送消耗后的道具信息至客户端
			CmdUtil.sendMsg(player, itemResponse.getUpdateItemListRs());
			logger.debug("玩家id:{}, 技能id:{}, 技能新增成功返回协议: {}", player.getPlayerId(), playerSkillModel.getSkillId(), playerSkillNewRs.toString());

			/*
			 * 触发任务进度刷新
			 * 任务完成条件类型:
			 *      【激活期间】学会X个Y品质（见下文，品质相关）或以上法术
			 *      【累计期间】学会X个Y品质（见下文，品质相关）或以上法术
			 */
			TaskScheduleRefreshUtil.learnSpellsSchedule(player, playerSkillModel);
		}
		return itemResponse.getCodeEnum();
	}

	/**
	 * 处理消耗品类型
	 * @param player 玩家信息
	 * @param itemModel 基础道具信息
	 * @param playerItemModel 玩家对应奖励宝箱道具
	 * @param itemNum 道具需要被扣减的数量
	 * @param itemUniversalListRq 请求协议
	 * @return 错误码
	 */
	private ErrorCodeEnum handleConsumables(Player player, ItemModel itemModel, PlayerItemModel playerItemModel,
											Integer itemNum, ItemUniversalListRq itemUniversalListRq)
	{
		ErrorCodeEnum errorCodeEnum;
		List<ItemJoinModel> itemJoinModels = new ArrayList<>();//存储使用宝箱后获得的道具
		for (int i = 0; i < itemNum; i++)
		{
			//奖励宝箱
			if (playerItemModel.getItemTypeDetail() == ItemTypeEnum.ConsumablesTypeEnum.TREASURE_CHEST.getSubclassType())
			{
				errorCodeEnum = this.rewardChest(player, itemModel, itemJoinModels);
				if (!errorCodeEnum.equals(ErrorCodeEnum.SUCCESS))
				{
					return errorCodeEnum;
				}
			}
			//随机宝箱
			else if (playerItemModel.getItemTypeDetail() == ItemTypeEnum.ConsumablesTypeEnum.RANDOM_TREASURE_CHEST.getSubclassType())
			{
				errorCodeEnum = this.randomTreasureChest(player, player.cache().getPlayerInfoModel().getProfession(), itemModel, itemJoinModels);
				if (!errorCodeEnum.equals(ErrorCodeEnum.SUCCESS))
				{
					return errorCodeEnum;
				}
			}
			//经验药水
			else if (playerItemModel.getItemTypeDetail() == ItemTypeEnum.ConsumablesTypeEnum.EXPERIENCE_POTION.getSubclassType())
			{
				int[] effectArr = null;
				try
				{
					effectArr = Stream.of(itemModel.getItemUseResult().replaceAll(CommonConsts.STR_BRACKETS, "").split(CommonConsts.STR_COLON)).mapToInt(Integer::valueOf).toArray();
				}
				catch (Exception e)
				{
					logger.error("玩家id:{}, 道具id:{}, 解析道具基础配置表\"道具使用效果\"异常!", player.getPlayerId(), itemModel.getItemId(), e);
					return ErrorCodeEnum.CONFIG_ERROR;
				}
				ItemModel experienceItem = itemDao.getConfigByKey(effectArr[0]);
				if (null == experienceItem || null == experienceItem.getItemType())
				{
					logger.error("玩家id:{}, 道具id:{}, 获取经验值基础道具信息为空!", player.getPlayerId(), effectArr[0]);
					return ErrorCodeEnum.CONFIG_ERROR;
				}
				itemJoinModels.add(new ItemJoinModel(effectArr[0], effectArr[1], experienceItem.getItemType()));
			}
		}

		ErrorCodeEnum codeEnum = ErrorCodeEnum.SUCCESS;
		//如果是奖励宝箱
		if (playerItemModel.getItemTypeDetail() == ItemTypeEnum.ConsumablesTypeEnum.TREASURE_CHEST.getSubclassType())
		{
			//合并相同道具数量(根据道具id和道具类型)
			itemJoinModels = new ArrayList<>(itemJoinModels.stream().collect(Collectors.toMap(e -> e.getItemId() + CommonConsts.STR_COLON + e.getItemType(), a -> a, (o1, o2) ->
			{
				o1.setItemNum(o1.getItemNum() + o2.getItemNum());
				return o1;
			})).values());
			codeEnum = this.updLogic(player, itemModel, playerItemModel, itemNum, itemJoinModels);
		}
		//如果是随机宝箱
		else if (playerItemModel.getItemTypeDetail() == ItemTypeEnum.ConsumablesTypeEnum.RANDOM_TREASURE_CHEST.getSubclassType())
		{
			codeEnum = this.updLogic(player, itemModel, playerItemModel, itemNum, itemJoinModels);
		}
		//如果是经验药水
		else if (playerItemModel.getItemTypeDetail() == ItemTypeEnum.ConsumablesTypeEnum.EXPERIENCE_POTION.getSubclassType())
		{
			ItemJoinModel joinModel = new ItemJoinModel(playerItemModel.getItemId(), -itemNum, playerItemModel.getItemType());
			itemJoinModels.add(joinModel);
			itemService.batchUpdateItemPush(player, itemJoinModels, ItemOperateEnum.USE, ItemPromp.GENERIC);
		}
		return codeEnum;
	}

	/**
	 * 奖励宝箱
	 */
	private ErrorCodeEnum rewardChest(Player player, ItemModel itemModel, List<ItemJoinModel> itemJoinModels)
	{
		//使用效果
		List<Map<Integer, Integer>> effectList = null;
		try
		{
			effectList = Stream.of(itemModel.getItemUseResult().replaceAll(CommonConsts.STR_BRACKETS, "").split(CommonConsts.STR_COMMA))
					.map(e ->
					{
						Map<Integer, Integer> map = new HashMap<>();
						String[] arr = e.split(CommonConsts.STR_COLON);
						map.put(Integer.valueOf(arr[0]), Integer.valueOf(arr[1]));
						return map;
					}).collect(Collectors.toList());
		}
		catch (Exception e)
		{
			logger.error("玩家id:{}, 道具id:{}, 解析道具基础配置表\"道具使用效果\"异常!", player.getPlayerId(), itemModel.getItemId(), e);
			return ErrorCodeEnum.CONFIG_ERROR;
		}
		for (Map<Integer, Integer> map : effectList)
		{
			Integer depleItemId = map.keySet().stream().findFirst().get();//宝箱中获得的道具id
			Integer depleItemNum = map.get(depleItemId);//宝箱中获得的道具数量
			ItemModel depleModel = itemDao.getConfigByKey(depleItemId);
			if (null == depleModel)
			{
				logger.error("玩家id:{}, 道具id:{}, 获取奖励宝箱中对应道具基础配置表数据为空!", player.getPlayerId(), depleItemId);
				return ErrorCodeEnum.CONFIG_ERROR;
			}
			itemJoinModels.add(new ItemJoinModel(depleItemId, depleItemNum, depleModel.getItemType()));
		}
		return ErrorCodeEnum.SUCCESS;
	}

	/**
	 * 随机宝箱
	 */
	private ErrorCodeEnum randomTreasureChest(Player player, int character, ItemModel itemModel, List<ItemJoinModel> itemJoinModels)
	{
		List<List<Integer>> drops = null;
		try
		{
			drops = Stream.of(itemModel.getItemUseResult().replaceAll(CommonConsts.STR_BRACKETS, ""))
					.map(e -> Arrays.stream(e.split(CommonConsts.STR_COLON)).map(Integer::valueOf).collect(Collectors.toList()))
					.collect(Collectors.toList());
		}
		catch (Exception e)
		{
			logger.error("玩家id:{}, 道具id:{}, 解析道具基础配置表\"道具使用效果\"异常!", player.getPlayerId(), itemModel.getItemId(), e);
			return ErrorCodeEnum.CONFIG_ERROR;
		}
		List<ItemJoinModel> result = randomItemService.randomItem(drops, character);
		if (null == result || result.isEmpty())
		{
			logger.error("玩家id:{}, 道具id:{}, 掉落组:{}, 根据配置的掉落组随机出的物品列表为空!", player.getPlayerId(), itemModel.getItemId(), itemModel.getItemUseResult());
			return ErrorCodeEnum.CONFIG_ERROR;
		}
		else
		{
			//随机宝箱开出的东西会有重复则需要合并相同道具数量
			randomItemService.addItemJoinModel(itemJoinModels,result);
		}
		return ErrorCodeEnum.SUCCESS;
	}

	/**
	 * 消耗逻辑
	 * @param player 玩家信息
	 * @param itemModel 基础道具信息
	 * @param playerItemModel 玩家对应奖励宝箱道具
	 * @param itemNum 道具需要被扣减的数量
	 * @param itemJoinModels 需要被更新的道具列表
	 * @return 响应码
	 */
	private ErrorCodeEnum updLogic(Player player, ItemModel itemModel, PlayerItemModel playerItemModel, Integer itemNum,
								   List<ItemJoinModel> itemJoinModels)
	{
		//更新消耗道具
		ItemJoinModel joinModel = new ItemJoinModel(playerItemModel.getItemId(), -itemNum, playerItemModel.getItemType());
		itemJoinModels.add(joinModel);
		itemService.batchUpdateItemPush(player, itemJoinModels, ItemOperateEnum.USE, ItemPromp.BULLET_FRAME);
		return ErrorCodeEnum.SUCCESS;
	}
}