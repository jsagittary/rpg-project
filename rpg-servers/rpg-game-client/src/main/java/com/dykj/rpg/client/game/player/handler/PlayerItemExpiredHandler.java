package com.dykj.rpg.client.game.player.handler;

import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.net.handler.core.AbstractClientHandler;
import com.dykj.rpg.protocol.item.ItemExpiredItemRs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 清理过期物品handler
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/01
 */
public class PlayerItemExpiredHandler extends AbstractClientHandler<ItemExpiredItemRs>
{
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void doHandler(ItemExpiredItemRs itemExpiredItemRs, ISession session)
    {
        logger.info("清理玩家过期物品, 返回协议:{}", itemExpiredItemRs.toString());
    }
}