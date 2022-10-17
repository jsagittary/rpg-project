package com.dykj.rpg.game.module.card.handler;

import com.dykj.rpg.game.core.GameHandler;
import com.dykj.rpg.game.module.card.service.CardService;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.protocol.card.CardRq;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 卡池handler
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/29
 */
public class CardHandler extends GameHandler<CardRq>
{
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void doHandler(CardRq cardRq, Player player)
    {
        logger.debug("玩家id:{}, 协议号:{}, 执行抽卡操作......", player.getPlayerId(), cardRq.getCode());
        CardService cardService = BeanFactory.getBean(CardService.class);
        cardService.cardLogic(player, cardRq);
    }
}