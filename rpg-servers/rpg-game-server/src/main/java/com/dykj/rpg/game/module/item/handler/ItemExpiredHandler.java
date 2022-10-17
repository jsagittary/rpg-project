package com.dykj.rpg.game.module.item.handler;

import com.dykj.rpg.game.core.GameHandler;
import com.dykj.rpg.game.module.item.service.ItemService;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.protocol.item.ItemExpiredRq;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 物品过期handler
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/01
 */
public class ItemExpiredHandler extends GameHandler<ItemExpiredRq>
{
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void doHandler(ItemExpiredRq expiredItemRq, Player player)
    {
        logger.debug("玩家id: {}, 协议号: {}, 执行处理物品过期操作......", player.getPlayerId(), expiredItemRq.getCode());
        ItemService itemService = BeanFactory.getBean(ItemService.class);
        itemService.handleItemExpired(player, expiredItemRq);
    }
}