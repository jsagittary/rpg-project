package com.dykj.rpg.game.module.event.cache.event;

import com.dykj.rpg.common.data.dao.PlayerAiDao;
import com.dykj.rpg.common.data.model.PlayerAiModel;
import com.dykj.rpg.game.module.cache.PlayerAiCache;
import com.dykj.rpg.game.module.event.cache.CacheEvent;
import com.dykj.rpg.game.module.player.logic.Player;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author CaoBing
 * @date 2021/5/20 17:26
 * @Description:
 */
@Component
public class AiCacheEvent extends CacheEvent<PlayerAiCache> {
    @Resource
    private PlayerAiDao playerAiDao;

    @Override
    public void loadFromDb(Player player) {
        PlayerAiCache playerAiCache = player.cache().getPlayerAiCache();
        List<PlayerAiModel> playerAiModels = playerAiDao.getAiEntryModels(player.getPlayerId());
        if (playerAiModels != null && playerAiModels.size() > 0) {
            // 初始化技巧缓存
            for (PlayerAiModel playerAiModel : playerAiModels) {
                playerAiCache.getPlayerAiModelMap().put(playerAiModel.getAiId(), playerAiModel);
            }
        }
    }

    @Override
    public void refreshCache(Player player) {
    }

}
