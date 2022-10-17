package com.dykj.rpg.game.module.event.login.event;

import com.dykj.rpg.common.data.dao.PlayerMissionDao;
import com.dykj.rpg.common.data.model.PlayerMissionModel;
import com.dykj.rpg.game.module.event.core.AbstractEvent;
import com.dykj.rpg.game.module.mission.service.MissionService;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.util.CmdUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author jyb
 * @date 2020/12/21 15:53
 * @Description
 */
@Component
public class LoginMissionEvent extends AbstractEvent {
    @Resource
    private PlayerMissionDao playerMissionDao;

    @Resource
    private MissionService missionService;

    @Override
    public void doEvent(Object... prams) throws Exception {
        Player player = (Player) prams[0];
        PlayerMissionModel playerMissionModel = player.cache().getPlayerMissionModel();
        if (playerMissionModel == null) {
            playerMissionModel = playerMissionDao.queryByPrimaykey(player.getPlayerId());
            if (playerMissionModel == null) {
                playerMissionModel = missionService.initMission(player);
            }
        }
        player.cache().setPlayerMissionModel(playerMissionModel);
        CmdUtil.sendMsg(player,missionService.missionLoginRs(player));
    }
}