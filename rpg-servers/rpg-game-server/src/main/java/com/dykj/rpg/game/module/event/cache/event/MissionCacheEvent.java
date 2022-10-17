package com.dykj.rpg.game.module.event.cache.event;

import com.dykj.rpg.common.data.dao.PlayerMissionDao;
import com.dykj.rpg.common.data.model.PlayerMissionModel;
import com.dykj.rpg.game.module.event.cache.CacheEvent;
import com.dykj.rpg.game.module.mission.service.MissionService;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.util.CmdUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author CaoBing
 * @date 2021/5/20 17:44
 * @Description:
 */
@Component
public class MissionCacheEvent extends CacheEvent<PlayerMissionModel> {
    @Resource
    private PlayerMissionDao playerMissionDao;
    @Resource
    private MissionService missionService;


    @Override
    public void loadFromDb(Player player) {
        PlayerMissionModel playerMissionModel = playerMissionDao.queryByPrimaykey(player.getPlayerId());
        if (null == playerMissionModel) {
            playerMissionModel = missionService.initMission(player);
        }
        player.cache().setPlayerMissionModel(playerMissionModel);
    }

    @Override
    public void send(Player player) {
        CmdUtil.sendMsg(player, missionService.missionLoginRs(player));
    }

    @Override
    public void refreshCache(Player player) {
    }
}