package com.dykj.rpg.game.module.event.cache.event;

import com.dykj.rpg.common.data.dao.PlayerMailDao;
import com.dykj.rpg.common.data.model.PlayerMailModel;
import com.dykj.rpg.game.module.cache.PlayerMailCache;
import com.dykj.rpg.game.module.event.cache.CacheEvent;
import com.dykj.rpg.game.module.mail.service.MailService;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.util.CmdUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author CaoBing
 * @date 2021/5/20 17:33
 * @Description:
 */
@Component
public class MailCacheEvent extends CacheEvent<PlayerMailCache> {
    @Resource
    private PlayerMailDao playerMailDao;

    @Resource
    private MailService mailService;

    @Override
    public void loadFromDb(Player player) {
        PlayerMailCache playerMailCache = player.cache().getPlayerMailCache();
        List<PlayerMailModel> playerMailModels = playerMailDao.getPlayerMails(player.getPlayerId());
        if (playerMailModels != null && playerMailModels.size() > 0) {
            for (PlayerMailModel playerMailModel : playerMailModels) {
                playerMailCache.getPlayerMailModelMap().put(playerMailModel.getInstanceId(), playerMailModel);
            }
        }
    }

    @Override
    public void send(Player player) {
        CmdUtil.sendMsg(player, mailService.playerMailRs(player.cache().getPlayerMailCache()));
    }

    @Override
    public void refreshCache(Player player) {
        player.cache().getPlayerMailCache().calculateSequence();
    }
}
