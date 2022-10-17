package com.dykj.rpg.game.module.event.mission;

import com.dykj.rpg.game.module.event.core.AbstractEventManager;
import com.dykj.rpg.game.module.event.mission.event.MissionEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description 关卡进度事件管理器
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/1/15
 */
@Component
public class MissionEventManager extends AbstractEventManager
{

    @Resource
    private MissionEvent missionEvent;

    @Override
    public void registerEvent()
    {
        addEvent(missionEvent);
    }
}
