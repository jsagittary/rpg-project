package com.dykj.rpg.game.module.task.handler;

import com.dykj.rpg.game.core.GameHandler;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.task.logic.service.TaskService;
import com.dykj.rpg.protocol.task.ProtectorRewardExpiredRq;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 守护者奖励过期handler
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/11
 */
public class ProtectorRewardExpiredHandler extends GameHandler<ProtectorRewardExpiredRq>
{
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void doHandler(ProtectorRewardExpiredRq protectorRewardExpiredRq, Player player)
    {
        logger.debug("玩家id:{}, 协议号:{}, 刷新守护者奖励是否过期......", player.getPlayerId(), protectorRewardExpiredRq.getCode());
        TaskService taskService = BeanFactory.getBean(TaskService.class);
        taskService.protectorRewardExpired(player, protectorRewardExpiredRq);
    }
}