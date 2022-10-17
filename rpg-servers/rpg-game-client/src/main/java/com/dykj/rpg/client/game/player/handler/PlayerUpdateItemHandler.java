package com.dykj.rpg.client.game.player.handler;

import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.net.handler.core.AbstractClientHandler;
import com.dykj.rpg.protocol.item.UpdateItemListRs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/23
 */
public class PlayerUpdateItemHandler extends AbstractClientHandler<UpdateItemListRs>
{
    private final Logger logger = LoggerFactory.getLogger(getClazz());

    @Override
    protected void doHandler(UpdateItemListRs updateItemListRs, ISession session) {
        logger.info("更新物品成功, 返回协议:{}", updateItemListRs.toString());
    }
}