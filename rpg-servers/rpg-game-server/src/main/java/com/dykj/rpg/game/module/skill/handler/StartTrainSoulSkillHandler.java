package com.dykj.rpg.game.module.skill.handler;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.dykj.rpg.protocol.skill.TrainSoulInfo;
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
import com.dykj.rpg.game.module.skill.service.SkillService;
import com.dykj.rpg.protocol.skill.StartTrainSoulSkillRq;
import com.dykj.rpg.protocol.skill.StartTrainSoulSkillRs;
import com.dykj.rpg.protocol.skill.TrainSoulSkillRs;
import com.dykj.rpg.util.spring.BeanFactory;

/**
 * @author jyb
 * @date 2021/2/23 11:07
 * @Description
 */
public class StartTrainSoulSkillHandler extends GameHandler<StartTrainSoulSkillRq> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void doHandler(StartTrainSoulSkillRq startTrainSoulSkillRq, Player player) {
        PlayerSkillCache playerSkillCache = player.cache().getPlayerSkillCache();
        if (playerSkillCache == null) {
            logger.error("StartTrainSoulSkillHandler error playerSkillCache is not exist {}  ", player.getPlayerId());
            return;
        }

        //校验客户端参数
        Set<Integer> check = new HashSet<>();
        for (TrainSoulInfo info : startTrainSoulSkillRq.getInfos()){
            if(check.contains(info.getTrainSoulPos())){
                logger.error("StartTrainSoulSkillHandler error : pos {}   is repeat   ", info.getTrainSoulPos());
                sendError(player,ErrorCodeEnum.CLIENT_PRAMS_ERROR);
                return;
            }
            check.add(info.getTrainSoulPos());
        }

        //验证vip上限
        int num = BeanFactory.getBean(SkillService.class).getVipSoulNum(player.cache().getPlayerInfoModel().getVipLv());
        if (num <playerSkillCache.getTrainSkill().size() + startTrainSoulSkillRq.getInfos().size()) {
            sendError(player, ErrorCodeEnum.TRAIN_SOUL_SKILL_IS_ENOUGH);
            return;
        }
        for (TrainSoulInfo info : startTrainSoulSkillRq.getInfos()) {
            PlayerSkillModel playerSkillModel = playerSkillCache.getPlayerSkillModelMap().get(info.getSkillId());
            if (playerSkillModel == null) {
                logger.error("StartTrainSoulSkillHandler error skill is not exist playerId {}  ,skillId {}  ", player.getPlayerId(), playerSkillModel.getSkillId());
                return;
            }
            if (playerSkillModel.getSkillType() == SkillTypeConsts.SOUL_SKILL) {
                logger.error("StartTrainSoulSkillHandler error skill is  SOUL_SKILL playerId {}  ,skillId {}  ", player.getPlayerId(), playerSkillModel.getSkillId());
                return;
            }

            if (playerSkillModel.getSoulChangeTime() != null) {
                logger.error("StartTrainSoulSkillHandler error :skill  already train playerId {}  ,skillId {}  ", player.getPlayerId(), playerSkillModel.getSkillId());
                return;
            }
            if(playerSkillCache.posError(info.getTrainSoulPos())){
                logger.error("StartTrainSoulSkillHandler error : pos {}   is error   ", info.getTrainSoulPos());
                sendError(player,ErrorCodeEnum.CLIENT_PRAMS_ERROR);
                return;
            }
            SkillCharacterBasicModel skillCharacterBasicModel = BeanFactory.getBean(SkillCharacterBasicDao.class).getConfigByKey(playerSkillModel.getSkillId());
            if (skillCharacterBasicModel == null) {
                logger.error("StartTrainSoulSkillHandler error : skillCharacterBasicModel is not exist ");
                sendError(player, ErrorCodeEnum.DATA_ERROR);
                return;
            }

            SoulChangeTimeModel soulChangeTimeModel = BeanFactory.getBean(SoulChangeTimeDao.class).getConfigByKey(skillCharacterBasicModel.getItemQualityType());
            if (soulChangeTimeModel == null) {
                logger.error("StartTrainSoulSkillHandler error : soulChangeTimeModel is not exist ");
                sendError(player, ErrorCodeEnum.DATA_ERROR);
                return;
            }
        }
        StartTrainSoulSkillRs startTrainSoulSkillRs = new StartTrainSoulSkillRs();
        for (TrainSoulInfo info : startTrainSoulSkillRq.getInfos()) {
            PlayerSkillModel playerSkillModel = playerSkillCache.getPlayerSkillModelMap().get(info.getSkillId());
            SkillCharacterBasicModel skillCharacterBasicModel = BeanFactory.getBean(SkillCharacterBasicDao.class).getConfigByKey(playerSkillModel.getSkillId());
            SoulChangeTimeModel soulChangeTimeModel = BeanFactory.getBean(SoulChangeTimeDao.class).getConfigByKey(skillCharacterBasicModel.getItemQualityType());
            playerSkillModel.setSoulChangeTime(new Date());
            playerSkillModel.setSoulChangePos(info.getTrainSoulPos());
            BeanFactory.getBean(PlayerSkillDao.class).queueUpdate(playerSkillModel);
            TrainSoulSkillRs trainSoulSkillRs = new TrainSoulSkillRs(info.getSkillId(), playerSkillModel.getSoulChangeTime().getTime() + soulChangeTimeModel.getTrainingTime() * 1000,info.getTrainSoulPos());
            startTrainSoulSkillRs.getTrainSoulSkills().add(trainSoulSkillRs);
        }
        sendMsg(player, startTrainSoulSkillRs);

    }
}