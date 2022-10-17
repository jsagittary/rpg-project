package com.dykj.rpg.game.module.event.level.event;

import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.module.event.core.AbstractEvent;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.task.consts.TaskFinishConditionEnum;
import com.dykj.rpg.game.module.task.consts.TaskOpenConditionEnum;
import com.dykj.rpg.game.module.task.logic.response.TaskConditions;
import com.dykj.rpg.game.module.task.logic.response.TaskResponse;
import com.dykj.rpg.game.module.task.logic.service.TaskService;
import com.dykj.rpg.game.module.task.logic.refresh.TaskScheduleRefreshUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description 角色等级变更时触发任务事件
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/9
 */
@Component
public class TriggerTaskEvent extends AbstractEvent
{
    @Resource
    private TaskService taskService;

    @Override
    public void doEvent(Object... prams) throws Exception
    {
        Player player = (Player) prams[0];
        //触发任务生成   任务前置类型: 等级
        TaskResponse taskResponse = taskService.triggerTaskPush(player, TaskOpenConditionEnum.GRADE);
        //避免对 角色等级达到X级 类型任务做重复任务刷新
        if (taskResponse.getCodeEnum().equals(ErrorCodeEnum.SUCCESS) && !taskResponse.getTaskRefresh())
        {
            /*
             * 触发任务进度刷新
             * 任务完成条件类型:
             *     角色等级达到X级
             */
            TaskScheduleRefreshUtil.commonSchedule(player, new TaskConditions(TaskFinishConditionEnum.ROLE_LEVEL));
        }
    }
}