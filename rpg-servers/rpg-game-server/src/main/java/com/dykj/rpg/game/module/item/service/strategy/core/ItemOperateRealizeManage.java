package com.dykj.rpg.game.module.item.service.strategy.core;

import com.dykj.rpg.common.config.BaseManager;
import com.dykj.rpg.common.consts.CommonConsts;
import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.module.item.consts.ItemOperateTypeEnum;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.util.CmdUtil;
import com.dykj.rpg.protocol.item.ItemUniversalListRq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 背包操作实现管理类
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/19
 */
@Component
public class ItemOperateRealizeManage extends BaseManager {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 存储背包操作类型实现类 key-背包操作类型 value-具体操作类型实现
	 */
	private final Map<Integer, ItemOperateRealize> operateRealizeMap = new ConcurrentHashMap<Integer, ItemOperateRealize>();

	/**
	 * 匹配背包操作类型服务类
	 * 
	 * @param player
	 *            玩家信息
	 * @param itemUniversalListRq
	 *            背包请求协议
	 */
	public void execute(Player player, ItemUniversalListRq itemUniversalListRq) {
		try {
			ItemOperateRealize itemOperateRealize = operateRealizeMap.get(itemUniversalListRq.getOperation());
			if (null == itemOperateRealize) {
				logger.error("玩家id:{}, 协议号:{}, 背包操作类型:{}, 客户端参数错误!", player.getPlayerId(),
						itemUniversalListRq.getCode(), ItemOperateTypeEnum.SELL.getItemOperateDesc());
				CmdUtil.sendErrorMsg(player.getSession(), itemUniversalListRq.getCode(),
						ErrorCodeEnum.CLIENT_PRAMS_ERROR);
			} else {
				itemOperateRealize.realize(player, itemUniversalListRq);
			}
		} catch (Exception e) {
			logger.error("玩家id:{}, 协议号:{}, 背包操作类型:{}, 处理背包操作异常!", player.getPlayerId(), itemUniversalListRq.getCode(),
					ItemOperateTypeEnum.SELL.getItemOperateDesc(), e);
			CmdUtil.sendErrorMsg(player.getSession(), itemUniversalListRq.getCode(), ErrorCodeEnum.DATA_ERROR);
		}
	}

	/**
	 * 初始化所有ItemOperateRealize的实现类
	 * 
	 * @throws Exception
	 */
	public void load() throws Exception {
		// 拿到所有GmStrategy的实现类
		Map<String, ItemOperateRealize> stringItemOperateRealizeMap = app.getBeansOfType(ItemOperateRealize.class);
		for (String key : stringItemOperateRealizeMap.keySet()) {
			ItemOperateRealize itemOperateRealize = stringItemOperateRealizeMap.get(key);
			if (operateRealizeMap.containsKey(itemOperateRealize.itemOperating().getItemOperateType())) {
				logger.warn("背包操作类型:{} 重复!", Arrays.stream(ItemOperateTypeEnum.values())
						.filter(e -> e.getItemOperateType() == itemOperateRealize.itemOperating().getItemOperateType())
						.findFirst().get().getItemOperateDesc());
				System.exit(-1);
			}
			operateRealizeMap.put(itemOperateRealize.itemOperating().getItemOperateType(), itemOperateRealize);
		}
		logger.info("初始化背包操作类型服务[{}]完毕......", this.toString());
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		operateRealizeMap.values()
				.forEach(e -> stringBuilder.append(e.getClass().getSimpleName()).append(CommonConsts.STR_COMMA));
		stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		return stringBuilder.toString();
	}
}