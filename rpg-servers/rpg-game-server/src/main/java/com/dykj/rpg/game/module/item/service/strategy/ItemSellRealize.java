package com.dykj.rpg.game.module.item.service.strategy;

import com.dykj.rpg.common.config.dao.ItemDao;
import com.dykj.rpg.common.config.model.ItemModel;
import com.dykj.rpg.common.consts.ItemOperateEnum;
import com.dykj.rpg.common.data.dao.PlayerItemDao;
import com.dykj.rpg.common.data.model.PlayerItemModel;
import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.module.item.consts.ItemOperateTypeEnum;
import com.dykj.rpg.game.module.item.consts.ItemPromp;
import com.dykj.rpg.game.module.item.response.ItemJoinModel;
import com.dykj.rpg.game.module.item.service.ItemService;
import com.dykj.rpg.game.module.item.service.strategy.core.ItemOperateRealize;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.protocol.item.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Description 道具出售
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/19
 */
@Service
public class ItemSellRealize implements ItemOperateRealize
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private ItemDao itemDao;

    @Resource
    private PlayerItemDao playerItemDao;

    @Resource
    private ItemService itemService;

    @Override
    public ItemOperateTypeEnum itemOperating()
    {
        return ItemOperateTypeEnum.SELL;
    }

    /**
     * 实现道具出售逻辑
     * @param player 玩家信息
     * @param itemUniversalListRq 请求协议参数
     */
    @Override
    public void realize(Player player, ItemUniversalListRq itemUniversalListRq)
    {
        ErrorCodeEnum codeEnum = ErrorCodeEnum.SUCCESS;
        List<ItemJoinModel> joinModelList = new ArrayList<>();
        for (ItemUniversalRq itemUniversalRq : itemUniversalListRq.getItemUniversalArr())
        {
            if (0 == itemUniversalRq.getItemId() || 0 == itemUniversalRq.getItemNum())
            {
                logger.error("玩家id:{}, 协议号:{}, 道具id或道具数量为空!", player.getPlayerId(), itemUniversalListRq.getCode());
                codeEnum = ErrorCodeEnum.CLIENT_PRAMS_ERROR;
                break;
            }
            ItemModel itemModel = itemDao.getConfigByKey(itemUniversalRq.getItemId());
            //1-为不可出售
            if (itemModel.getItemCanSell() == 1)
            {
                logger.error("玩家id:{}, 协议号:{}, 背包操作类型:{}, 道具id:{}, 当前道具不可出售!", player.getPlayerId(), itemUniversalListRq.getCode(), ItemOperateTypeEnum.SELL.getItemOperateDesc(), itemUniversalRq.getItemId());
                codeEnum = ErrorCodeEnum.ITEM_NOT_SELL;
                break;
            }
            //如果当前道具配置是可出售的且出售类型或出售金额为空
            if (null == itemModel.getItemSellItem() || null == itemModel.getItemSellCurrencyPrice())
            {
                logger.error("玩家id:{}, 协议号:{}, 背包操作类型:{}, 道具id:{}, 当前道具配置表出售货币ID或道具出售价格配置错误!", player.getPlayerId(), itemUniversalListRq.getCode(), ItemOperateTypeEnum.SELL.getItemOperateDesc(), itemUniversalRq.getItemId());
                codeEnum = ErrorCodeEnum.ITEM_SELLTYPE_SELLPRICE_ERROR;
                break;
            }
            //拿到对应道具信息
            PlayerItemModel playerItemModel = itemService.getItemInfo(player, itemUniversalRq.getItemId());
            if (null == playerItemModel)
            {
                logger.error("玩家id:{}, 协议号:{}, 道具id:{}, 背包操作类型:{}, 当前道具不存在!", player.getPlayerId(), itemUniversalListRq.getCode(), itemUniversalRq.getItemId(), ItemOperateTypeEnum.SELL.getItemOperateDesc());
                codeEnum = ErrorCodeEnum.CLIENT_PRAMS_ERROR;
                break;
            }
            //当前道具已上锁无法出售
            if (playerItemModel.getItemLock() == 2)
            {
                logger.error("玩家id:{}, 协议号:{}, 道具id:{}, 背包操作类型:{}, 道具数量: {}, 当前道具已上锁!", player.getPlayerId(), itemUniversalListRq.getCode(), itemUniversalRq.getItemId(), ItemOperateTypeEnum.SELL.getItemOperateDesc(), playerItemModel.getItemNum());
                codeEnum = ErrorCodeEnum.ITEM_LOCKED;
                break;
            }
            //如果背包里的道具数量小于消耗的数量
            if (playerItemModel.getItemNum() < itemUniversalRq.getItemNum())
            {
                logger.error("玩家id:{}, 协议号:{}, 道具id:{}, 背包操作类型:{}, 道具数量: {}, 持有的道具数量不足!", player.getPlayerId(), itemUniversalListRq.getCode(), itemUniversalRq.getItemId(), ItemOperateTypeEnum.SELL.getItemOperateDesc(), playerItemModel.getItemNum());
                codeEnum = ErrorCodeEnum.ITEM_QUANTITY_NOT_ENOUGH;
                break;
            }
            ItemJoinModel joinModel = new ItemJoinModel();
            joinModel.setItemId(itemUniversalRq.getItemId());
            joinModel.setItemNum(-itemUniversalRq.getItemNum());
            joinModel.setItemType(itemModel.getItemType());
            joinModelList.add(joinModel);

            //根据道具配置表的道具出售货币ID拿到对应出售后因增加的道具model
            PlayerItemModel playerCurrencyItemModel = player.cache().getPlayerItemCache().values().stream().filter(pl -> pl.getItemId() == itemModel.getItemSellItem()).findFirst().get();
            BigDecimal playerCurrencyBig = new BigDecimal(playerCurrencyItemModel.getItemNum());
            //因是批量出售则需把每个道具的出售价格剩数量
            BigDecimal sellCurrencyPriceBig = new BigDecimal(itemModel.getItemSellCurrencyPrice() * itemUniversalRq.getItemNum());
            //出售的道具数量增加对应金额结果
            int itemAddNumResult = playerCurrencyBig.add(sellCurrencyPriceBig).intValue();
            ItemJoinModel currencyJoinModel = new ItemJoinModel();
            currencyJoinModel.setItemId(playerCurrencyItemModel.getItemId());
            currencyJoinModel.setItemNum(itemAddNumResult);
            currencyJoinModel.setItemType(playerCurrencyItemModel.getItemType());
            joinModelList.add(currencyJoinModel);
        }
        itemService.batchUpdateItemPush(player, joinModelList, ItemOperateEnum.SELL, ItemPromp.GENERIC);
    }
}