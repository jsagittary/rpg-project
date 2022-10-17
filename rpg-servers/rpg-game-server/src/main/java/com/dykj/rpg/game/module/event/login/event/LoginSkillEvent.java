package com.dykj.rpg.game.module.event.login.event;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.dykj.rpg.common.data.dao.PlayerSkillDao;
import com.dykj.rpg.common.data.model.PlayerSkillModel;
import com.dykj.rpg.game.module.cache.PlayerSkillCache;
import com.dykj.rpg.game.module.event.core.AbstractEvent;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.skill.service.SkillService;
import com.dykj.rpg.game.module.util.CmdUtil;

/**
 * @author jyb
 * @date 2020/11/12 13:54
 * @Description
 */
@Component
public class LoginSkillEvent extends AbstractEvent {

    @Resource
    private PlayerSkillDao playerSkillDao;

    @Resource
    private SkillService skillService;

    @Override
    public void doEvent(Object... prams) throws Exception {
        Player player = (Player) prams[0];
        PlayerSkillCache playerSkillCache = player.cache().getPlayerSkillCache();
        if (playerSkillCache == null || playerSkillCache.getPlayerSkillModelMap().values().size() == 0) {
            List<PlayerSkillModel> playerSkillModels = playerSkillDao.queryListByPlayer(player.getPlayerId());
            if (playerSkillModels == null || playerSkillModels.size() < 1) {
                playerSkillCache = skillService.initSkills(player);
            } else {
                playerSkillCache = new PlayerSkillCache();
                for (PlayerSkillModel playerSkillModel : playerSkillModels) {
                    playerSkillCache.getPlayerSkillModelMap().put(playerSkillModel.getSkillId(),playerSkillModel);
                }
            }
        }
        player.cache().setPlayerSkillCache(playerSkillCache);
        CmdUtil.sendMsg(player, skillService.playerSkillRs(playerSkillCache));
    }
}