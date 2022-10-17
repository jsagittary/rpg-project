package com.dykj.rpg.game.module.event.login.event;

import com.dykj.rpg.common.config.dao.ItemDao;
import com.dykj.rpg.common.config.model.ItemModel;
import com.dykj.rpg.common.consts.ItemTypeEnum;
import com.dykj.rpg.common.data.dao.PlayerItemDao;
import com.dykj.rpg.common.data.model.PlayerItemModel;
import com.dykj.rpg.game.module.cache.PlayerItemCache;
import com.dykj.rpg.game.module.ai.service.AiService;
import com.dykj.rpg.game.module.equip.service.EquipService;
import com.dykj.rpg.game.module.event.core.AbstractEvent;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.util.CmdUtil;
import com.dykj.rpg.protocol.item.ItemListRs;
import com.dykj.rpg.protocol.item.ItemRs;
import com.dykj.rpg.util.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @Description 玩家登陆前初始化道具列表
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/23
 */
@Component
public class LoginItemEvent extends AbstractEvent
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private ItemDao itemDao;

    @Resource
    private EquipService equipService;

    @Resource
    private PlayerItemDao playerItemDao;

    @Resource
    private AiService aiService;

    @Override
    public void doEvent(Object... prams) throws Exception
    {
        Player player = (Player) prams[0];
        //组装协议
        List<ItemRs> itemRsList = new ArrayList<ItemRs>();
        //加载背包
        PlayerItemCache playerItemCache = player.cache().getPlayerItemCache();
        if (null == playerItemCache)
        {
            playerItemCache = new PlayerItemCache();
            List<PlayerItemModel> playerItemModels = playerItemDao.queryForList(player.getPlayerId());
            if (null != playerItemModels && !playerItemModels.isEmpty())
            {
                for (PlayerItemModel playerItemModel : playerItemModels)
                {
                    playerItemCache.updateCache(playerItemModel);
                }
                //初始化道具缓存如果没有绑定钻石、充值钻石、金币等基础信息则初始化
                if (playerItemModels.stream().noneMatch(e -> e.getItemType() == ItemTypeEnum.CURRENCY.getItemType() &&
                        (e.getItemTypeDetail() == ItemTypeEnum.CurrencyTypeEnum.RECHARGE_DIAMOND.getSubclassType() ||
                                e.getItemTypeDetail() == ItemTypeEnum.CurrencyTypeEnum.BIND_DIAMOND.getSubclassType() ||
                                e.getItemTypeDetail() == ItemTypeEnum.CurrencyTypeEnum.GOLD.getSubclassType())))
                {
                    this.initCurrency(player, playerItemCache);
                }
            }
            else
            {
               this.initCurrency(player, playerItemCache);
            }
            player.cache().setPlayerItemCache(playerItemCache);
        }
        if (null != playerItemCache.values() && !playerItemCache.values().isEmpty())
        {
            for (PlayerItemModel playerItemModel : playerItemCache.values())
            {
                ItemRs itemRs = new ItemRs();
                itemRs.setInstId(0L);
                itemRs.setItemId(playerItemModel.getItemId());
                itemRs.setItemType(playerItemModel.getItemType());
                itemRs.setItemNum(playerItemModel.getItemNum());
                //如果不是永久道具则计算结束时长给到客户端
                if (playerItemModel.getLiveType() != 1)
                {
                    ItemModel itemModel = itemDao.getConfigByKey(playerItemModel.getItemId());
                    LocalDateTime localDateTime = DateUtils.conversionLocalDateTime(playerItemModel.getLiveTime());
                    //如果是有时限物品则取出物品的添加时间加上道具持续时间计算出结束时间推送给客户端
                    long timestamp = localDateTime.plusSeconds(itemModel.getItemLiveTime()).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                    itemRs.setExpiration(timestamp);
                }
                else
                {
                    itemRs.setExpiration(0L);
                }
                itemRs.setIsLock(playerItemModel.getItemLock());
                itemRsList.add(itemRs);
            }
        }
        itemRsList.addAll(equipService.getEquips(player));//初始化玩家装备列表

        List<ItemRs> intellects = aiService.getAis(player);
        for (ItemRs itemRs : intellects)
        {
            System.err.println(itemRs);
        }
        itemRsList.addAll(aiService.getAis(player));//初始化文案加所有的雕纹列表

        logger.info("初始化玩家:{} 背包列表数量:{}", player.getPlayerId(), itemRsList.size());
        CmdUtil.sendMsg(player, new ItemListRs(itemRsList));
    }

    /**
     * 初始化绑定钻石、充值钻石、金币
     */
    private void initCurrency(Player player, PlayerItemCache playerItemCache)
    {
        // 初始化背包绑定钻石、充值钻石、金币道具
        Collection<ItemModel> collection = itemDao.getConfigs();
        if (null != collection && !collection.isEmpty())
        {
            for (ItemModel itemModel : collection)
            {
                if ((itemModel.getItemType() == ItemTypeEnum.CURRENCY.getItemType() &&
                        (itemModel.getItemTypeDetail() == ItemTypeEnum.CurrencyTypeEnum.RECHARGE_DIAMOND.getSubclassType() ||
                                itemModel.getItemTypeDetail() == ItemTypeEnum.CurrencyTypeEnum.BIND_DIAMOND.getSubclassType() ||
                                itemModel.getItemTypeDetail() == ItemTypeEnum.CurrencyTypeEnum.GOLD.getSubclassType())))
                {
                    PlayerItemModel playerItemModel = new PlayerItemModel();
                    playerItemModel.setPlayerId(player.getPlayerId());
                    playerItemModel.setItemId(itemModel.getItemId());
                    playerItemModel.setItemType(itemModel.getItemType());
                    playerItemModel.setItemTypeDetail(itemModel.getItemTypeDetail());
                    playerItemModel.setItemNum(0);
                    playerItemModel.setLiveType(itemModel.getItemLiveType());
                    if (itemModel.getItemLock() == 1)
                    {
                        playerItemModel.setItemLock(0);
                    }
                    else
                    {
                        playerItemModel.setItemLock(1);
                    }
                    playerItemModel.setItemGetTime(new Date());
                    playerItemCache.updateCache(playerItemModel);
                    playerItemDao.queueInsert(playerItemModel);
                }
            }
        }
    }
}
