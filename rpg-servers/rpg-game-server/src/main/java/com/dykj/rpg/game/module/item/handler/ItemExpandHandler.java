package com.dykj.rpg.game.module.item.handler;

import com.dykj.rpg.game.core.GameHandler;
import com.dykj.rpg.game.module.item.service.ItemService;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.protocol.item.ItemExpandRq;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 背包扩展handler
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/01
 */
public class ItemExpandHandler extends GameHandler<ItemExpandRq>
{
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void doHandler(ItemExpandRq itemExpandRq, Player player)
    {
        logger.debug("玩家id: {}, 协议号: {}, 执行背包扩展操作......", player.getPlayerId(), itemExpandRq.getCode());
        ItemService itemService = BeanFactory.getBean(ItemService.class);
        itemService.handleItemExpand(player, itemExpandRq);
    }
}