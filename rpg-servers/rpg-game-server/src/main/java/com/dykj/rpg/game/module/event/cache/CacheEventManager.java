package com.dykj.rpg.game.module.event.cache;

import javax.annotation.Resource;

import com.dykj.rpg.game.module.event.cache.event.*;
import org.springframework.stereotype.Component;

import com.dykj.rpg.game.module.event.core.AbstractEventManager;

/**
 * @author jyb
 * @date 2021/5/8 15:47
 * @Description
 */
@Component
public class CacheEventManager extends AbstractEventManager {

    @Resource
    private LoginCacheEvent loginCacheEvent;

    @Resource
    private RuneCacheEvent runeCacheEvent;

    @Resource
    private SkillCacheEvent skillCacheEvent;

    @Resource
    private EquipCacheEvent equipCacheEvent;

    @Resource
    private EquipPosCacheEvent equipPosCacheEvent;

    @Resource
    private AttributeCacheEvent attributeCacheEvent;

    @Resource
    private ItemCacheEvent itemCacheEvent;

    @Resource
    private TaskCacheEvent taskCacheEvent;

    @Resource
    private SoulCacheEvent soulCacheEvent;

    @Resource
    private MailCacheEvent mailCacheEvent;

    @Resource
    private AiCacheEvent aiCacheEvent;

    @Resource
    private MissionCacheEvent missionCacheEvent;


    @Override
    public void registerEvent() {

        //登录
        addEvent(loginCacheEvent);

        //符文
        addEvent(runeCacheEvent);

        //技能
        addEvent(skillCacheEvent);

        //道具
        addEvent(itemCacheEvent);

        //装备
        addEvent(equipCacheEvent);

        //装备栏
        addEvent(equipPosCacheEvent);

        //灵魂之影技能
        addEvent(soulCacheEvent);

        //属性 ，放在所有养成后面
        addEvent(attributeCacheEvent);

        //邮件
        addEvent(mailCacheEvent);

        //技巧
        addEvent(aiCacheEvent);

        //关卡
        addEvent(missionCacheEvent);

        //任务
        addEvent(taskCacheEvent);
    }
}
