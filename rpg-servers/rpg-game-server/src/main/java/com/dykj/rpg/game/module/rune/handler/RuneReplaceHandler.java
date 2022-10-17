package com.dykj.rpg.game.module.rune.handler;

import com.dykj.rpg.game.core.GameHandler;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.rune.service.RuneService;
import com.dykj.rpg.protocol.rune.RuneReplaceRq;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 符文替换
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/5/21
 */
public class RuneReplaceHandler extends GameHandler<RuneReplaceRq>
{
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void doHandler(RuneReplaceRq runeReplaceRq, Player player)
    {
        logger.debug("玩家id: {}, 协议号: {}, 执行符文替换操作......", player.getPlayerId(), runeReplaceRq.getCode());
        BeanFactory.getBean(RuneService.class).runeReplace(player, runeReplaceRq);
    }
}