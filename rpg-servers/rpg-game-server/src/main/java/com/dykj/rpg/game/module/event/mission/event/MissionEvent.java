package com.dykj.rpg.game.module.event.mission.event;

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
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 关卡结算时触发事件
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/9
 */
@Component
public class MissionEvent extends AbstractEvent
{
    @Resource
    private TaskService taskService;

    @Override
    public void doEvent(Object... prams) throws Exception
    {
        Player player = (Player) prams[0];
        //触发任务生成   任务前置类型: 关卡进度
        TaskResponse taskResponse = taskService.triggerTaskPush(player, TaskOpenConditionEnum.CHECKPOINT_PROGRESS);
        List<TaskConditions> list = new ArrayList<>();
        //避免对【激活期间】挑战X次任意关卡类型任务做重复任务刷新
        if (taskResponse.getCodeEnum().equals(ErrorCodeEnum.SUCCESS) && !taskResponse.getTaskRefresh())
        {
            /*
             * 触发任务进度刷新
             * 任务完成条件类型:
             *      【激活期间】挑战X次任意关卡
             */
            list.add(new TaskConditions(TaskFinishConditionEnum.DURING_ACTIVATION, TaskFinishConditionEnum.DuringActivationEnum.CHALLENGE_MISSION));
        }
        /*
         * 触发任务进度刷新
         * 任务完成条件类型:
         *      挑战通过关卡XXX
         */
        list.add(new TaskConditions(TaskFinishConditionEnum.DURING_ACTIVATION, TaskFinishConditionEnum.DuringActivationEnum.CHALLENGE_MISSION));
        taskService.taskFinishRealize(player, list);
    }
}