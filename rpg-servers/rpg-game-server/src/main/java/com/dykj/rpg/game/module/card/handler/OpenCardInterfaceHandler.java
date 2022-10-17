package com.dykj.rpg.game.module.card.handler;

import com.dykj.rpg.game.core.GameHandler;
import com.dykj.rpg.game.module.card.service.CardService;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.protocol.card.CardListRq;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 打开抽卡界面
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/4/7
 */
public class OpenCardInterfaceHandler extends GameHandler<CardListRq>
{
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void doHandler(CardListRq cardListRq, Player player)
    {
        logger.debug("玩家id:{}, 协议号:{}, 打开抽卡界面执行加载数据操作......", player.getPlayerId(), cardListRq.getCode());
        CardService cardService = BeanFactory.getBean(CardService.class);
        cardService.loadCardData(player, cardListRq);
    }
}