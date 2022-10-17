package com.dykj.rpg.game.module.item.handler;

import com.dykj.rpg.game.core.GameHandler;
import com.dykj.rpg.game.module.item.service.ItemService;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.protocol.item.ItemLockRq;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 物品上锁handler
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/01
 */
public class ItemLockHandler extends GameHandler<ItemLockRq>
{
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void doHandler(ItemLockRq itemLockRq, Player player)
    {
        logger.debug("玩家id: {}, 协议号: {}, 执行物品上锁操作......", player.getPlayerId(), itemLockRq.getCode());
        ItemService itemService = BeanFactory.getBean(ItemService.class);
        itemService.handleItemLock(player, itemLockRq);
    }
}