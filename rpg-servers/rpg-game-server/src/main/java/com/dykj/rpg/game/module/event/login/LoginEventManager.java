package com.dykj.rpg.game.module.event.login;

import com.dykj.rpg.game.module.event.core.AbstractEventManager;
import com.dykj.rpg.game.module.event.login.event.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: jyb
 * @Date: 2020/9/5 15:20
 * @Description:
 *
 * 登录初始化话数据
 */
@Component
public class LoginEventManager extends AbstractEventManager {
    @Resource
    private LoginEvent loginEvent;

    @Resource
    private AttrRefreshEvent attrRefreshEvent;

    @Resource
    private EquipEvent equipEvent;
    /**
     * 登陆初始化背包事件
     */
    @Resource
    private LoginItemEvent loginItemEvent;

    @Resource
    private LoginSkillEvent loginSkillEvent;

    @Resource
    private LoginMissionEvent loginMissionEvent;

    @Resource
    private LoginTaskEvent loginTaskEvent;
    
    @Resource
    private AiEvent aiEvent;

    @Resource
    private LoginMailEvent mailEvent;

    @Resource
    private LoginAttacheEvent loginAttacheEvent;
    @Resource
    private  LoginSoulSkinEvent loginSoulSkinEvent;

    @Override
    public void registerEvent() {
        addEvent(loginEvent);

        addEvent(aiEvent);
        
        addEvent(equipEvent);

        addEvent(loginItemEvent);

        addEvent(loginSkillEvent);

        addEvent(loginTaskEvent);

        addEvent(loginAttacheEvent);

        addEvent(loginSoulSkinEvent);

        //TODO  注意要在养成模块的后面
        addEvent(attrRefreshEvent);
        //关卡信息
        addEvent(loginMissionEvent);

        //邮件
        addEvent(mailEvent);
    }
}
