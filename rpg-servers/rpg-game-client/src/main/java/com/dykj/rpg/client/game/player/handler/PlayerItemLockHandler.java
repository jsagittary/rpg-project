package com.dykj.rpg.client.game.player.handler;

import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.net.handler.core.AbstractClientHandler;
import com.dykj.rpg.protocol.item.ItemLockRs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 物品上锁
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/23
 */
public class PlayerItemLockHandler extends AbstractClientHandler<ItemLockRs>
{
    private final Logger logger = LoggerFactory.getLogger(getClazz());

    @Override
    protected void doHandler(ItemLockRs itemLockRs, ISession session) {
        logger.info("物品上锁, 返回协议:{}", itemLockRs.toString());
    }
}