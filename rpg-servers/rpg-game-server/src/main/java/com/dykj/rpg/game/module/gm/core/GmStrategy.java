package com.dykj.rpg.game.module.gm.core;

import com.dykj.rpg.game.module.player.logic.Player;

/**
 * @Description gm指令策略
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/24
 */
public abstract class GmStrategy
{

    protected Player player;//玩家信息

    /**
     * 服务名
     * @return
     */
    public abstract String serviceName();

    public Player getPlayer()
    {
        return player;
    }

    public void setPlayer(Player player)
    {
        this.player = player;
    }
}
