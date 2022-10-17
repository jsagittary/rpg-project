package com.dykj.rpg.game.module.event.login;

import com.dykj.rpg.game.module.event.core.AbstractEventManager;
import com.dykj.rpg.game.module.event.login.event.LogoutEvent;
import com.dykj.rpg.game.module.player.logic.Player;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: jyb
 * @Date: 2020/9/5 15:20
 * @Description:
 */
@Component
public class LogoutEventManager extends AbstractEventManager {
    @Resource
    private LogoutEvent logoutEvent;

    @Override
    public void registerEvent() {
        addEvent(logoutEvent);

    }
}
