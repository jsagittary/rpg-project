package com.dykj.rpg.game.module.event.task.event;

import com.dykj.rpg.game.module.event.core.AbstractEvent;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.task.logic.response.TaskConditions;
import com.dykj.rpg.game.module.task.logic.service.TaskService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 任务事件
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/9
 */
@Component
public class TaskEvent extends AbstractEvent
{
    @Resource
    private TaskService taskService;

    @Override
    public void doEvent(Object... prams) throws Exception
    {
        Player player = (Player) prams[0];
        List<TaskConditions> list = TaskConditions.analyzeConditions(prams);
        //触发任务进度刷新
        taskService.taskFinishRealize(player, list);
    }


}