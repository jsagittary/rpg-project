package com.dykj.rpg.game.module.battle.handler;

import com.dykj.rpg.game.module.attribute.service.AttributeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dykj.rpg.common.config.dao.MisBasicDao;
import com.dykj.rpg.common.config.model.MisBasicModel;
import com.dykj.rpg.common.data.model.PlayerMissionModel;
import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.core.GameHandler;
import com.dykj.rpg.game.module.battle.service.BattleService;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.protocol.gameBattle.StartBattleRq;
import com.dykj.rpg.util.spring.BeanFactory;

/**
 * @author jyb
 * @date 2020/12/24 19:15
 * @Description
 */
public class StartBattleHandler extends GameHandler<StartBattleRq> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Logger gameLogger = LoggerFactory.getLogger("game");

    @Override
    public void doHandler(StartBattleRq challengeMissionRq, Player player) {
        PlayerMissionModel playerMissionModel = player.cache().getPlayerMissionModel();
        if (playerMissionModel == null) {
            logger.error("ChallengeMissionHandler error  playerMissionModel is not exist  playerId: {}", player.getPlayerId());
            sendError(player, ErrorCodeEnum.DATA_ERROR);
            return;
        }

        MisBasicModel misBasicModel = BeanFactory.getBean(MisBasicDao.class).getConfigByKey(playerMissionModel.getMissionId());
        if (misBasicModel == null) {
            logger.error("ChallengeMissionHandler error  misBasicModel is not exist  missionId: {}", playerMissionModel.getMissionId());
            sendError(player, ErrorCodeEnum.DATA_ERROR);
            return;
        }
        if (misBasicModel.getEnterLv() > player.cache().getPlayerInfoModel().getLv()) {
            sendError(player, ErrorCodeEnum.PLAYER_LEVEL_ERROR);
            return;
        }
        getBean(AttributeService.class).refresh(player);
        boolean flag = BeanFactory.getBean(BattleService.class).enterBattleServer(player, misBasicModel.getMisId(), (byte)1);
        gameLogger.info("StartBattleHandler doHandler  battleServer  return  flag {} ",flag);
        if (!flag) {
            sendError(player, ErrorCodeEnum.ENTER_BATTLE_SERVER_ERROR);
        }
    }
}