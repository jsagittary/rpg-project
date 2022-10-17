package com.dykj.rpg.game.module.event.cache.event;

import com.dykj.rpg.common.data.dao.PlayerSkillDao;
import com.dykj.rpg.common.data.model.PlayerRuneModel;
import com.dykj.rpg.common.data.model.PlayerSkillModel;
import com.dykj.rpg.game.module.cache.PlayerRuneCache;
import com.dykj.rpg.game.module.cache.PlayerSkillCache;
import com.dykj.rpg.game.module.event.cache.CacheEvent;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.rune.service.RuneService;
import com.dykj.rpg.game.module.skill.service.SkillService;
import com.dykj.rpg.game.module.util.CmdUtil;
import com.dykj.rpg.protocol.rune.RuneRs;
import com.dykj.rpg.protocol.skill.PlayerSkillRs;
import com.dykj.rpg.protocol.skill.SkillRs;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jyb
 * @date 2021/5/8 15:32
 * @Description
 */
@Component
public class SkillCacheEvent extends CacheEvent<PlayerSkillCache> {

    @Resource
    private PlayerSkillDao playerSkillDao;
    @Resource
    private SkillService skillService;

    @Resource
    private RuneService runeService;


    @Override
    public void loadFromDb(Player player) {
        PlayerSkillCache playerSkillCache = player.cache().getPlayerSkillCache();
        if (playerSkillCache.getPlayerSkillModelMap().values().size() == 0) {
            List<PlayerSkillModel> playerSkillModels = playerSkillDao.queryListByPlayer(player.getPlayerId());
            for (PlayerSkillModel playerSkillModel : playerSkillModels) {
                playerSkillCache.getPlayerSkillModelMap().put(playerSkillModel.getSkillId(), playerSkillModel);
            }
        }
    }


    @Override
    public void refreshCache(Player player) {
        if (player.cache().getPlayerSkillCache().getPlayerSkillModelMap().size() < 1) {
            skillService.initSkills1(player);
        }
    }

    @Override
    public void send(Player player) {
        PlayerSkillRs playerSkillRs = skillService.playerSkillRs(player.cache().getPlayerSkillCache());
        //加载符文
        PlayerRuneCache playerRuneCache = player.cache().getPlayerRuneCache();
        if (null != playerRuneCache && !playerRuneCache.getRuneCacheMap().isEmpty())
        {
            for (SkillRs skillRs : playerSkillRs.getSkills())
            {
                List<PlayerRuneModel> runeModelList = playerRuneCache.getRuneList(skillRs.getSkillId());
                if (null != runeModelList && !runeModelList.isEmpty())
                {
                    List<RuneRs> runeRsList = runeModelList.stream().map(e -> runeService.runeRs(e)).collect(Collectors.toList());
                    skillRs.setRuneList(runeRsList);
                }
            }
        }
        CmdUtil.sendMsg(player, playerSkillRs);
    }
}