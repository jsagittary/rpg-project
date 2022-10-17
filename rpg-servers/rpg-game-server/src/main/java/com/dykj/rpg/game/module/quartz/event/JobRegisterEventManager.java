package com.dykj.rpg.game.module.quartz.event;

import com.dykj.rpg.game.module.event.core.AbstractEventManager;
import com.dykj.rpg.game.module.quartz.event.QuartzJobRegisterEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author jyb
 * @date 2021/3/16 20:27
 * @Description
 */
@Component
public class JobRegisterEventManager extends AbstractEventManager {
    @Resource
    private QuartzJobRegisterEvent quartzJobRegisterEvent;

    @Override
    public void registerEvent() {
        addEvent(quartzJobRegisterEvent);
    }
}