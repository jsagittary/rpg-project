package com.dykj.rpg.game.module.skill.handler;

import com.dykj.rpg.game.core.GameHandler;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.skill.service.SkillService;
import com.dykj.rpg.protocol.skill.SkillUpgradeListRq;
import com.dykj.rpg.util.spring.BeanFactory;

/**
 * @Description 技能升级
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/21
 */
public class SkillUpgradeHandler extends GameHandler<SkillUpgradeListRq>
{

    @Override
    public void doHandler(SkillUpgradeListRq skillUpgradeListRq, Player player)
    {
        SkillService skillService = BeanFactory.getBean(SkillService.class);
        skillService.skillUpgrade(player, skillUpgradeListRq);
    }
}