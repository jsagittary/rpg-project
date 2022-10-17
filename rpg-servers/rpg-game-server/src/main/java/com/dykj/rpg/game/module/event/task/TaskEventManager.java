package com.dykj.rpg.game.module.event.task;

import com.dykj.rpg.game.module.event.core.AbstractEventManager;
import com.dykj.rpg.game.module.event.task.event.TaskEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description 任务事件管理器
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/19
 */
@Component
public class TaskEventManager extends AbstractEventManager
{

    @Resource
    private TaskEvent taskEvent;

    @Override
    public void registerEvent()
    {
        addEvent(taskEvent);
    }
}
