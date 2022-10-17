package com.dykj.rpg.game.module.quartz.job;

import com.dykj.rpg.game.module.event.destroy.PlayerDestroyManager;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.player.service.PlayerCacheService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author: jyb
 * @Date: 2020/9/5 10:20
 * @Description: 回收失效的player 玩家下线之后玩家缓存会在内存保存三十分钟
 */
@Component
public class PlayerCacheJob {

    @Resource
    private PlayerCacheService playerCacheService;

    @Resource
    private PlayerDestroyManager playerDestroyManager;

    public void doJob() {
        Iterator<Map.Entry<Integer, Player>> iterator = playerCacheService.getPlayers().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Player> entry = iterator.next();
            if (entry.getValue().isTimeOut()) {
                iterator.remove();
                playerDestroyManager.doEvents(entry.getValue());
            }
        }
    }
}
