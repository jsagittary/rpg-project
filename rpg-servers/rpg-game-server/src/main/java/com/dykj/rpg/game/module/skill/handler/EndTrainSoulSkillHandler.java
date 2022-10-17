package com.dykj.rpg.game.module.skill.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dykj.rpg.common.data.dao.PlayerSkillDao;
import com.dykj.rpg.common.data.model.PlayerSkillModel;
import com.dykj.rpg.game.core.GameHandler;
import com.dykj.rpg.game.module.cache.PlayerSkillCache;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.protocol.skill.EndTrainSoulSkillRq;
import com.dykj.rpg.protocol.skill.EndTrainSoulSkillRs;
import com.dykj.rpg.util.spring.BeanFactory;

/**
 * @author jyb
 * @date 2021/2/23 11:07
 * @Description
 */
public class EndTrainSoulSkillHandler extends GameHandler<EndTrainSoulSkillRq> {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void doHandler(EndTrainSoulSkillRq endTrainSoulSkillRq, Player player) {
    	PlayerSkillCache playerSkillCache = player.cache().getPlayerSkillCache();
        if (playerSkillCache == null) {
            logger.error("EndTrainSoulSkillHandler error playerSkillCache is not exist {}  ", player.getPlayerId());
            return;
        }
        PlayerSkillModel playerSkillModel = playerSkillCache.getPlayerSkillModelMap().get(endTrainSoulSkillRq.getSkillId());
        if (playerSkillModel == null) {
            logger.error("EndTrainSoulSkillHandler error skill is not exist playerId {}  ,skillId {}  ", player.getPlayerId(), endTrainSoulSkillRq.getSkillId());
            return;
        }
        playerSkillModel.setSoulChangeTime(null);
        playerSkillModel.setSoulChangePos(0);
        BeanFactory.getBean(PlayerSkillDao.class).queueUpdate(playerSkillModel);
        sendMsg(player, new EndTrainSoulSkillRs());
    }
}