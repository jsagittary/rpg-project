package com.dykj.rpg.game.module.card.service;

import com.dykj.rpg.game.module.cache.PlayerCardCache;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.protocol.card.CardListRq;
import com.dykj.rpg.protocol.card.CardRq;

/**
 * @Description 抽卡系统接口
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/29
 */
public interface CardService
{

    /**
     * 获取玩家抽卡缓存
     * @param player 玩家信息
     * @return 玩家抽卡缓存
     */
    public PlayerCardCache getCardCache(Player player);

    /**
     * 打开抽卡界面加载数据
     * @param player 玩家信息
     * @param cardListRq 协议
     */
    public void loadCardData(Player player, CardListRq cardListRq);

    /**
     * 实现抽卡逻辑
     * @param player 玩家信息
     * @param cardRq 请求协议
     */
    public void cardLogic(Player player, CardRq cardRq);
}