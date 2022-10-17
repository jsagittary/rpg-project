package com.dykj.rpg.game.module.skill.handler;

import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.core.GameHandler;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.skill.service.SkillService;
import com.dykj.rpg.protocol.skill.SkillDownRq;
import com.dykj.rpg.protocol.skill.SkillDownRs;
import com.dykj.rpg.protocol.skill.SkillUpRq;
import com.dykj.rpg.util.spring.BeanFactory;

/**
 * @author jyb
 * @date 2020/12/9 14:46
 * @Description
 */
public class SkillDownHandler extends GameHandler<SkillDownRq> {
    @Override
    public void doHandler(SkillDownRq skillDownRq, Player player) {
        SkillService skillService = BeanFactory.getBean(SkillService.class);
        ErrorCodeEnum errorCodeEnum = skillService.skillDown(player, skillDownRq);
        if (!errorCodeEnum.equals(ErrorCodeEnum.SUCCESS)) {
            sendError(player, errorCodeEnum);
        }
    }
}