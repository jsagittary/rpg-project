package com.dykj.rpg.game.module.event.level;

import com.dykj.rpg.game.module.event.core.AbstractEventManager;
import com.dykj.rpg.game.module.event.level.event.LevelEvent;
import com.dykj.rpg.game.module.event.level.event.TriggerTaskEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description 角色升级事件管理器
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/1/15
 */
@Component
public class LevelEventManager extends AbstractEventManager
{

    @Resource
    private LevelEvent levelEvent;

    @Resource
    private TriggerTaskEvent triggerTaskEvent;

    @Override
    public void registerEvent()
    {
        addEvent(levelEvent);
        addEvent(triggerTaskEvent);//注册任务事件
    }
}
