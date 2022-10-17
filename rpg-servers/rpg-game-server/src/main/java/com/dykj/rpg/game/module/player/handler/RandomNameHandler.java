package com.dykj.rpg.game.module.player.handler;

import com.dykj.rpg.game.core.BeforeGameHandler;
import com.dykj.rpg.game.module.player.service.PlayerService;
import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.protocol.player.RandomNameRq;
import com.dykj.rpg.protocol.player.RandomNameRs;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 随机名称handler
 * @Author: jyb
 * @Date: 2020/9/11 11:26
 * @Description:
 */
public class RandomNameHandler extends BeforeGameHandler<RandomNameRq>
{
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    protected void doHandler(RandomNameRq randomNameRq, ISession session)
    {
        PlayerService playerService = BeanFactory.getBean(PlayerService.class);
        playerService.generateRolesName(randomNameRq, session);
    }
}
