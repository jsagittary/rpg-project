package com.dykj.rpg.client.game.player.handler;

import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.net.handler.core.AbstractClientHandler;
import com.dykj.rpg.protocol.item.ItemExpandRs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 背包扩展handler
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/01
 */
public class PlayerItemExpandHandler extends AbstractClientHandler<ItemExpandRs>
{
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void doHandler(ItemExpandRs itemExpandRs, ISession session)
    {
        logger.info("玩家扩展背包, 返回协议:{}", itemExpandRs.toString());
    }
}