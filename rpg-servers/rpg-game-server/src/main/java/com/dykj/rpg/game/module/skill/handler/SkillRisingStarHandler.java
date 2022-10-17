package com.dykj.rpg.game.module.skill.handler;

import com.dykj.rpg.game.core.GameHandler;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.skill.service.SkillService;
import com.dykj.rpg.protocol.skill.SkillRisingStarListRq;
import com.dykj.rpg.util.spring.BeanFactory;

/**
 * @Description 技能升星
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/21
 */
public class SkillRisingStarHandler extends GameHandler<SkillRisingStarListRq>
{

    @Override
    public void doHandler(SkillRisingStarListRq skillRisingStarListRq, Player player)
    {
        SkillService skillService = BeanFactory.getBean(SkillService.class);
        skillService.skillRisingStar(player, skillRisingStarListRq);
    }
}