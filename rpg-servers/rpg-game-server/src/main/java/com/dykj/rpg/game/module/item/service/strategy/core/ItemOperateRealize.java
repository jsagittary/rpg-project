package com.dykj.rpg.game.module.item.service.strategy.core;

import com.dykj.rpg.game.module.item.consts.ItemOperateTypeEnum;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.protocol.item.ItemUniversalListRq;

/**
 * @Description 背包操作类型具体实现
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/19
 */
public interface ItemOperateRealize
{

    /**
     * 背包操作类型定义
     * @return
     */
    public ItemOperateTypeEnum itemOperating();
    
    /**
     * 背包操作(出售、兑换、分解、使用、丢弃)
     * @param player 玩家信息
     * @param itemUniversalListRq 请求协议参数
     */
    public void realize(Player player, ItemUniversalListRq itemUniversalListRq);
}
