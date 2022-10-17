package com.dykj.rpg.game.module.task.handler;

import com.dykj.rpg.game.core.GameHandler;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.task.logic.service.TaskService;
import com.dykj.rpg.protocol.task.ReceiveActivityRewardsRq;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 领取活跃度奖励handler
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/9
 */
public class ReceiveActivityRewardsHandler extends GameHandler<ReceiveActivityRewardsRq>
{
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void doHandler(ReceiveActivityRewardsRq receiveActivityRewardsRq, Player player)
    {
        logger.debug("玩家id:{}, 协议号:{}, 执行领取活跃度奖励操作......", player.getPlayerId(), receiveActivityRewardsRq.getCode());
        TaskService taskService = BeanFactory.getBean(TaskService.class);
        taskService.receiveActivityRewards(player, receiveActivityRewardsRq);
    }
}