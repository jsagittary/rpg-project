package com.dykj.rpg.game.module.event.cache.event;

import com.dykj.rpg.common.data.dao.PlayerRuneDao;
import com.dykj.rpg.common.data.model.PlayerRuneModel;
import com.dykj.rpg.game.module.cache.PlayerRuneCache;
import com.dykj.rpg.game.module.event.cache.CacheEvent;
import com.dykj.rpg.game.module.player.logic.Player;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 玩家登陆前初始化符文缓存事件
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/5/21
 */
@Component
public class RuneCacheEvent extends CacheEvent<PlayerRuneCache>
{
    @Resource
    private PlayerRuneDao playerRuneDao;

    @Override
    public boolean createCache(Player player) {
        return super.createCache(player);
    }

    @Override
    public void loadFromDb(Player player)
    {
        PlayerRuneCache playerRuneCache = player.cache().getPlayerRuneCache();
        if (null == playerRuneCache || playerRuneCache.values().isEmpty())
        {
            List<PlayerRuneModel> runeModelList = playerRuneDao.queryForList(player.getPlayerId());
            if (null != runeModelList && !runeModelList.isEmpty())
            {
                for (PlayerRuneModel playerRuneModel : runeModelList)
                {
                    playerRuneCache.updateCache(playerRuneModel);
                }
            }
        }
        player.cache().setPlayerRuneCache(playerRuneCache);
    }

    @Override
    public void refreshCache(Player player)
    {

    }
}