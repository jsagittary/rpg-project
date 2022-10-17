package com.dykj.rpg.game.module.skill.handler;

import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.core.GameHandler;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.skill.service.SkillService;
import com.dykj.rpg.protocol.skill.SkillUpRq;
import com.dykj.rpg.util.spring.BeanFactory;

import java.util.Properties;

/**
 * @author jyb
 * @date 2020/12/9 14:46
 * @Description
 */
public class SkillUpHandler  extends GameHandler<SkillUpRq> {
    @Override
    public void doHandler(SkillUpRq skillUpRq, Player player) {
        SkillService skillService = BeanFactory.getBean(SkillService.class);
        ErrorCodeEnum errorCodeEnum = skillService.skillUp(player, skillUpRq);
        if (!errorCodeEnum.equals(ErrorCodeEnum.SUCCESS)) {
            sendError(player, errorCodeEnum);
        }
    }
}