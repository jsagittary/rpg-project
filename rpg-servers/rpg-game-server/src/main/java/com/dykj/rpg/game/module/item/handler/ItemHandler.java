package com.dykj.rpg.game.module.item.handler;

import com.dykj.rpg.game.core.GameHandler;
import com.dykj.rpg.game.module.item.consts.ItemOperateTypeEnum;
import com.dykj.rpg.game.module.item.service.ItemService;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.protocol.item.ItemUniversalListRq;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @Description 背包handler
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/01
 */
public class ItemHandler extends GameHandler<ItemUniversalListRq>
{
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void doHandler(ItemUniversalListRq itemUniversalListRq, Player player)
    {
        logger.debug("玩家id:{}, 协议号:{}, 根据背包操作类型:{} 处理相应操作......", player.getPlayerId(), itemUniversalListRq.getCode(),
                Arrays.stream(ItemOperateTypeEnum.values()).filter(e -> e.getItemOperateType() == itemUniversalListRq.getOperation()).findFirst().get().getItemOperateDesc());
        ItemService itemService = BeanFactory.getBean(ItemService.class);
        itemService.handleItemOperate(player, itemUniversalListRq);
    }
}