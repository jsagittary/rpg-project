package com.dykj.rpg.game.module.task.handler;

import com.dykj.rpg.game.core.GameHandler;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.task.logic.service.TaskService;
import com.dykj.rpg.protocol.task.TaskListRq;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 打开任务界面handler
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/4/19
 */
public class OpenTaskInterfaceHandler extends GameHandler<TaskListRq>
{
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void doHandler(TaskListRq taskListRq, Player player)
    {
        logger.debug("玩家id:{}, 协议号:{}, 打开任务界面执行加载数据操作......", player.getPlayerId(), taskListRq.getCode());
        TaskService taskService = BeanFactory.getBean(TaskService.class);
        taskService.loadTaskData(player, taskListRq);
    }
}