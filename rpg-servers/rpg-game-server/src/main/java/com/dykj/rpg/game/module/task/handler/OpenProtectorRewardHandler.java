package com.dykj.rpg.game.module.task.handler;

import com.dykj.rpg.game.core.GameHandler;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.task.logic.service.TaskService;
import com.dykj.rpg.protocol.task.OpenProtectorRewardRq;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 开通守护者奖励handler
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/9
 */
public class OpenProtectorRewardHandler extends GameHandler<OpenProtectorRewardRq>
{
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void doHandler(OpenProtectorRewardRq openProtectorRewardRq, Player player)
    {
        logger.debug("玩家id:{}, 协议号:{}, 执行开通守护者奖励操作......", player.getPlayerId(), openProtectorRewardRq.getCode());
        TaskService taskService = BeanFactory.getBean(TaskService.class);
        taskService.openProtector(player, openProtectorRewardRq);
    }
}