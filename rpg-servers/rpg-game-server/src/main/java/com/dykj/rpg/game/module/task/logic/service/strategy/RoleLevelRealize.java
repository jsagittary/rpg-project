package com.dykj.rpg.game.module.task.logic.service.strategy;

import com.dykj.rpg.common.data.model.PlayerInfoModel;
import com.dykj.rpg.common.data.model.PlayerTaskModel;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.task.consts.TaskFinishConditionEnum;
import com.dykj.rpg.game.module.task.logic.response.TaskConditions;
import com.dykj.rpg.game.module.task.logic.service.strategy.core.TaskFinishFactory;
import com.dykj.rpg.game.module.task.logic.service.strategy.core.TaskFinishStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description 任务完成条件 - 角色等级达到X级
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/15
 */
@Component
public class RoleLevelRealize extends TaskFinishStrategy
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 策略方法
     * @param player 玩家信息
     * @param playerTaskModel 玩家任务
     * @param taskFinish 任务完成条件配置
     * @param conditionsModel 参数集
     * @return 玩家任务
     */
    @Override
    public PlayerTaskModel realize(Player player, PlayerTaskModel playerTaskModel, List<Integer> taskFinish, TaskConditions conditionsModel)
    {
        PlayerInfoModel playerInfoModel = player.cache().getPlayerInfoModel();
        //如果当前角色等级大于等于任务配置的等级则设置任务状态为已完成、任务进度为配置项
        if (playerInfoModel.getLv() >= taskFinish.get(1))
        {
            playerTaskModel.setTaskSchedule(taskFinish.get(1));
            playerTaskModel.setTaskStatus(2);
        }
        else
        {
            playerTaskModel.setTaskSchedule(playerInfoModel.getLv());
        }
        logger.debug("玩家id:{}, 刷新任务状态完毕, playerTaskModel:{}", player.getPlayerId(), playerTaskModel.toString());
        return playerTaskModel;
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        TaskFinishFactory.register(TaskFinishConditionEnum.ROLE_LEVEL, this);
    }
}