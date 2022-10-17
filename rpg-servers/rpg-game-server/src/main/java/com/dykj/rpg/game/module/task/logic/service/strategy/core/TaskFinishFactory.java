package com.dykj.rpg.game.module.task.logic.service.strategy.core;

import com.dykj.rpg.game.module.task.consts.TaskFinishConditionEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 任务完成条件工厂基类
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/15
 */
public class TaskFinishFactory
{
    private static final Map<TaskFinishConditionEnum, TaskFinishStrategy> strategyMap = new HashMap<>();

    /**
     * 根据任务完成条件类型枚举获取对应实现类
     * @param taskFinishConditionEnum 任务完成条件枚举
     * @return 任务完成条件策略基类
     */
    public static TaskFinishStrategy getInvokeStrategy(TaskFinishConditionEnum taskFinishConditionEnum)
    {
        return strategyMap.get(taskFinishConditionEnum);
    }

    /**
     * 注册对应任务完成条件类型枚举的对应实现类
     * @param taskFinishConditionEnum 任务完成条件枚举
     * @param taskFinishStrategy 任务完成条件策略基类
     */
     public static void register(TaskFinishConditionEnum taskFinishConditionEnum, TaskFinishStrategy taskFinishStrategy)
     {
         strategyMap.put(taskFinishConditionEnum, taskFinishStrategy);
     }
}