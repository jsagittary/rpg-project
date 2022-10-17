package com.dykj.rpg.client.game.player.handler;

import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.net.handler.core.AbstractClientHandler;
import com.dykj.rpg.protocol.item.ItemListRs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 玩家背包列表
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/23
 */
public class PlayerItemListHandler extends AbstractClientHandler<ItemListRs>
{
    private final Logger logger = LoggerFactory.getLogger(getClazz());

    @Override
    protected void doHandler(ItemListRs itemListRs, ISession session) {
        logger.info("玩家返回背包列表:{}", itemListRs.toString());
    }
}