package com.dykj.rpg.game.module.skill.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dykj.rpg.common.attribute.consts.SkillTypeConsts;
import com.dykj.rpg.common.config.dao.SkillCharacterBasicDao;
import com.dykj.rpg.common.config.dao.SoulChangeTimeDao;
import com.dykj.rpg.common.config.model.SkillCharacterBasicModel;
import com.dykj.rpg.common.config.model.SoulChangeTimeModel;
import com.dykj.rpg.common.data.dao.PlayerSkillDao;
import com.dykj.rpg.common.data.model.PlayerSkillModel;
import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.core.GameHandler;
import com.dykj.rpg.game.module.cache.PlayerSkillCache;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.task.logic.refresh.TaskScheduleRefreshUtil;
import com.dykj.rpg.protocol.skill.AwardTrainSoulSkillRq;
import com.dykj.rpg.protocol.skill.AwardTrainSoulSkillRs;
import com.dykj.rpg.util.spring.BeanFactory;

/**
 * @author jyb
 * @date 2021/2/23 11:07
 * @Description
 */
public class AwardTrainSoulSkillHandler extends GameHandler<AwardTrainSoulSkillRq> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void doHandler(AwardTrainSoulSkillRq awardTrainSoulSkillRq, Player player) {
        PlayerSkillCache playerSkillCache = player.cache().getPlayerSkillCache();
        if (playerSkillCache == null) {
            logger.error("AwardTrainSoulSkillHandler error playerSkillCache is not exist {}  ", player.getPlayerId());
            return;
        }
        PlayerSkillModel playerSkillModel = playerSkillCache.getPlayerSkillModelMap().get(awardTrainSoulSkillRq.getSkillId());
        if (playerSkillModel == null) {
            logger.error("AwardTrainSoulSkillHandler error skill is not exist playerId {}  ,skillId {}  ", player.getPlayerId(), awardTrainSoulSkillRq.getSkillId());
            return;
        }
        if (playerSkillModel.getSkillType() == SkillTypeConsts.SOUL_SKILL) {
            logger.error("AwardTrainSoulSkillHandler error skill is  SOUL_SKILL playerId {}  ,skillId {}  ", player.getPlayerId(),awardTrainSoulSkillRq.getSkillId());
            return;
        }

        if (playerSkillModel.getSoulChangeTime() == null) {
            logger.error("AwardTrainSoulSkillHandler error :skill  already train playerId {}  ,skillId {}  ", player.getPlayerId(), awardTrainSoulSkillRq.getSkillId());
            return;
        }
//        //验证vip上限
//        int num = BeanFactory.getBean(SkillService.class).getVipSoulNum(player.getInfo().getVipLv());
//        if (num < playerSkillCache.getTrainSkill().size()) {
//            sendError(player, ErrorCodeEnum.TRAIN_SOUL_SKILL_IS_ENOUGH);
//            return;
//        }
        SkillCharacterBasicModel skillCharacterBasicModel = BeanFactory.getBean(SkillCharacterBasicDao.class).getConfigByKey(playerSkillModel.getSkillId());
        if (skillCharacterBasicModel == null) {
            logger.error("AwardTrainSoulSkillHandler error : skillCharacterBasicModel is not exist ");
            sendError(player, ErrorCodeEnum.DATA_ERROR);
            return;
        }
        SoulChangeTimeModel soulChangeTimeModel = BeanFactory.getBean(SoulChangeTimeDao.class).getConfigByKey(skillCharacterBasicModel.getItemQualityType());
        if (soulChangeTimeModel == null) {
            logger.error("AwardTrainSoulSkillHandler error : soulChangeTimeModel is not exist ");
            sendError(player, ErrorCodeEnum.DATA_ERROR);
            return;
        }

        if (System.currentTimeMillis() < playerSkillModel.getSoulChangeTime().getTime() + soulChangeTimeModel.getTrainingTime() *  1000) {
            logger.error("AwardTrainSoulSkillHandler train time error :  playerId {}  ,skillId {}  ", player.getPlayerId(), playerSkillModel.getSkillId());
            sendError(player, ErrorCodeEnum.TRAIN_SOUL_SKILL_TIME_ERROR);
            return;
        }
        playerSkillModel.setSkillType(SkillTypeConsts.SOUL_SKILL);
        playerSkillModel.setSoulChangeTime(null);
        playerSkillModel.setSoulChangePos(0);
        BeanFactory.getBean(PlayerSkillDao.class).queueUpdate(playerSkillModel);
        sendMsg(player, new AwardTrainSoulSkillRs());
        /*
         * 触发任务进度刷新
         * 任务完成条件类型:
         *      【激活期间】完成灵魂法术淬炼X次
         *      【累计期间】完成灵魂法术淬炼X次
         */
        TaskScheduleRefreshUtil.spellsTemperSchedule(player);
    }
}