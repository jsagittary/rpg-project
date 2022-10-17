package com.dykj.rpg.game.module.event.destroy;

import com.dykj.rpg.game.module.event.core.AbstractEventManager;
import com.dykj.rpg.game.module.event.destroy.event.CacheDestroyEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: jyb
 * @Date: 2020/9/14 13:57
 * @Description:
 */
@Component
public class PlayerDestroyManager extends AbstractEventManager {
    @Resource
    private CacheDestroyEvent cacheDestroyEvent;

    @Override
    public void registerEvent() {
        addEvent(cacheDestroyEvent);
    }
}
