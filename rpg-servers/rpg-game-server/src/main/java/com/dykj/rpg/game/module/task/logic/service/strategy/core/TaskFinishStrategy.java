package com.dykj.rpg.game.module.task.logic.service.strategy.core;

import com.dykj.rpg.common.data.model.PlayerTaskModel;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.task.logic.response.TaskConditions;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * @Description 任务完成条件策略基类
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/15
 */
public abstract class TaskFinishStrategy implements InitializingBean
{
    /**
     * 策略方法
     * @param player 玩家信息
     * @param playerTaskModel 玩家任务
     * @param taskFinish 任务完成条件配置
     * @param conditionsModel 参数
     * @return 玩家任务
     */
    public abstract PlayerTaskModel realize(Player player, PlayerTaskModel playerTaskModel, List<Integer> taskFinish, TaskConditions conditionsModel);
}