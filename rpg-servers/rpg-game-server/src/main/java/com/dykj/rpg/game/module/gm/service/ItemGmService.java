package com.dykj.rpg.game.module.gm.service;

import com.dykj.rpg.common.config.dao.EquipBasicDao;
import com.dykj.rpg.common.config.dao.ItemDao;
import com.dykj.rpg.common.config.model.EquipBasicModel;
import com.dykj.rpg.common.config.model.ItemModel;
import com.dykj.rpg.common.consts.ItemOperateEnum;
import com.dykj.rpg.common.consts.ItemTypeEnum;
import com.dykj.rpg.common.consts.MailId;
import com.dykj.rpg.common.data.dao.PlayerInfoDao;
import com.dykj.rpg.common.data.dao.PlayerItemDao;
import com.dykj.rpg.common.data.model.PlayerInfoModel;
import com.dykj.rpg.common.data.model.PlayerItemModel;
import com.dykj.rpg.common.data.model.PlayerMailModel;
import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.module.ai.service.AiService;
import com.dykj.rpg.game.module.attribute.service.AttributeService;
import com.dykj.rpg.game.module.equip.service.EquipService;
import com.dykj.rpg.game.module.event.level.LevelEventManager;
import com.dykj.rpg.game.module.gm.core.GmStrategy;
import com.dykj.rpg.game.module.item.consts.ItemPromp;
import com.dykj.rpg.game.module.item.response.ItemJoinModel;
import com.dykj.rpg.game.module.item.service.ItemService;
import com.dykj.rpg.game.module.mail.service.MailService;
import com.dykj.rpg.game.module.task.logic.refresh.TaskScheduleRefreshUtil;
import com.dykj.rpg.game.module.util.CmdUtil;
import com.dykj.rpg.protocol.item.ItemRs;
import com.dykj.rpg.protocol.item.UpdateItemListRs;
import com.dykj.rpg.protocol.player.RoleUpgradeRs;
import com.dykj.rpg.protocol.player.UpdPlayerInfoRs;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description gm指令道具实现
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/24
 */
@Service
public class ItemGmService extends GmStrategy
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private ItemDao itemDao;

    @Resource
    private EquipBasicDao equipBasicDao;

    @Resource
    private PlayerItemDao playerItemDao;

    @Resource
    private PlayerInfoDao playerInfoDao;

    @Resource
    private ItemService itemService;

    @Resource
    private EquipService equipService;

    @Resource
    private AttributeService attributeService;

    @Resource
    private AiService aiService;
    @Resource
    private MailService mailService;

    @Override
    public String serviceName()
    {
        return "item";
    }

    /**
     * 执行指令根据道具id增加道具数量
     *
     * @param paramsMap 参数map(key-道具id, value-道具数量)
     * @return
     */
    public void add(Map<String, String> paramsMap)
    {
        UpdateItemListRs updateItemListRs = new UpdateItemListRs(ItemOperateEnum.GM.getType(), ItemPromp.GENERIC.getType(), true, null);
        if (null == paramsMap || paramsMap.isEmpty())
        {
            logger.error("使用GM指令添加道具时参数为空!");
            CmdUtil.sendErrorMsg(player.getSession(), updateItemListRs.getCode(), ErrorCodeEnum.CLIENT_PRAMS_ERROR);
            return;
        }
        // 存储同类型的参数 key-itemType value-Map(key-itemId value-itemNum)
        Map<Integer, Map<String, String>> integerListMap = new HashMap<Integer, Map<String, String>>();
        for (String key : paramsMap.keySet())
        {
            ItemModel itemModel = itemDao.getConfigByKey(Integer.valueOf(key));// 道具基础信息
            // 为空则表示道具基础配置表没有则去装备、技能书等配置表里去找
            if (null == itemModel)
            {
                EquipBasicModel equipBasicModel = equipBasicDao.getConfigByKey(Integer.valueOf(key));
                if (null == equipBasicModel)
                {
                    // TODO 注意后续如果在装备配置基础表没找到则去技能书、材料、宝石等基础表找

                    logger.error("通过gm指令执行添加道具操作, 参数为:{}, 执行失败，没有匹配到对应道具id!", key);
                    CmdUtil.sendErrorMsg(player.getSession(), updateItemListRs.getCode(), ErrorCodeEnum.GM_ADD_ITEM_ERROR);
                    return;
                }
                else
                {
                    this.sameTypeCache(integerListMap, ItemTypeEnum.EQUIPMENT.getItemType(), key, paramsMap);
                }
            }
            else
            {
                this.sameTypeCache(integerListMap, itemModel.getItemType(), key, paramsMap);
            }
        }

        // 获取升级前的玩家等级和经验
        int beforeLv = player.cache().getPlayerInfoModel().getLv();
        PlayerInfoModel afterPlayerInfo = null;// 触发升级后玩家信息
        List<PlayerItemModel> insPlayerItemModelList = new ArrayList<PlayerItemModel>();// 最终新增list
        List<PlayerItemModel> updPlayerItemModelList = new ArrayList<PlayerItemModel>();// 最终更新list
        List<PlayerItemModel> delPlayerItemModelList = new ArrayList<PlayerItemModel>();// 最终删除list
        List<ItemJoinModel> spendGoldList = new ArrayList<>();// 存储任务花费多少金币list
        List<ItemJoinModel> diamondList = new ArrayList<>();// 存储任务花费多少钻石list
        List<ItemJoinModel> mailSendItemList = new ArrayList<>();//存储添加的道具达到总上限后进行邮件发送list
        List<ItemRs> protocolList = new ArrayList<ItemRs>();// 存储返回协议
        for (Map.Entry<Integer, Map<String, String>> entry : integerListMap.entrySet())
        {
            for (String key : entry.getValue().keySet())
            {
                int itemId = Integer.parseInt(key);// 道具id
                int itemNum = Integer.parseInt(paramsMap.get(key));// 道具数量
                // 如果是装备类型
                if (entry.getKey() == ItemTypeEnum.EQUIPMENT.getItemType())
                {
                    for (int i = 0; i < itemNum; i++)
                    {
                        ItemRs itemRs = equipService.addEquip(itemId, super.getPlayer(), ItemOperateEnum.GM);
                        protocolList.add(itemRs);
                    }
                }
                else if (entry.getKey() == ItemTypeEnum.AI.getItemType())
                {
                    for (int i = 0; i < itemNum; i++)
                    {
                        ItemRs itemRs = aiService.addAi(itemId, super.getPlayer(), ItemOperateEnum.GM);
                        if (null != itemRs)
                        {
                            protocolList.add(itemRs);
                        }
                    }
                }
                else
                {
                    ItemModel itemModel = itemDao.getConfigByKey(itemId);// 道具基础信息
                    if (null == itemModel)
                    {
                        logger.error("玩家id:{}, 道具id:{}, 对应道具基础配置表数据为空!", player.getPlayerId(), itemModel.getItemId());
                        CmdUtil.sendErrorMsg(player.getSession(), updateItemListRs.getCode(), ErrorCodeEnum.CONFIG_ERROR);
                        return;
                    }
                    // 如果是经验类型则走角色升级逻辑
                    if (entry.getKey() == ItemTypeEnum.EXPERIENCE.getItemType() && itemModel.getItemTypeDetail() == ItemTypeEnum.ExperienceTypeEnum.ROLE_EXPERIENCE.getSubclassType())
                    {
                        Map<ErrorCodeEnum, PlayerInfoModel> rolesUpgradeMap = itemService.rolesUpgrade(player, itemNum);
                        if (rolesUpgradeMap.containsKey(ErrorCodeEnum.SUCCESS))
                        {
                            afterPlayerInfo = rolesUpgradeMap.get(ErrorCodeEnum.SUCCESS);
                        }
                        else
                        {
                            CmdUtil.sendErrorMsg(player.getSession(), updateItemListRs.getCode(), rolesUpgradeMap.keySet().stream().findFirst().get());
                            return;
                        }
                        continue;
                    }
                    //如果添加普通道具
                    int itemType = ItemTypeEnum.GENERIC.getItemType();
                    for (ItemTypeEnum itemTypeEnum : ItemTypeEnum.values())
                    {
                        if (itemModel.getItemType().equals(itemTypeEnum.getItemType()))
                        {
                            itemType = itemTypeEnum.getItemType();
                            break;
                        }
                    }
                    ItemJoinModel itemJoinModel = new ItemJoinModel(itemId, itemNum, itemType);
                    Map<ErrorCodeEnum, PlayerItemModel> resultMap = itemService.updateItem(super.getPlayer(), itemJoinModel, itemModel);
                    for (ErrorCodeEnum errorCodeEnum : resultMap.keySet())
                    {
                        if (errorCodeEnum.equals(ErrorCodeEnum.SUCCESS))
                        {
                            // 拿到更新后的数据
                            PlayerItemModel playerItemModel = resultMap.get(ErrorCodeEnum.SUCCESS);
                            // 判断当前道具是否存在如不存在则新增
                            PlayerItemModel frontPlayerItem = itemService.getItemInfo(player, itemId);
                            if (null == frontPlayerItem)
                            {
                                insPlayerItemModelList.add(playerItemModel);
                            }
                            else
                            {
                                // 判断更新后如果当前道具数量为0则删除并且如果不是货币类型则把为0的数据删除
                                if (playerItemModel.getItemNum() == 0 && playerItemModel.getItemType() != ItemTypeEnum.CURRENCY.getItemType())
                                {
                                    delPlayerItemModelList.add(playerItemModel);
                                }
                                else
                                {
                                    updPlayerItemModelList.add(playerItemModel);
                                }
                            }
                            // 组装协议返回最新变更数量
                            ItemRs itemRs = itemService.itemRs(playerItemModel, itemModel);
                            itemRs.setItemNum(itemNum);// 返回输入的道具数量
                            if (resultMap.containsKey(ErrorCodeEnum.ITEM_NUM_TOTAL_UPPERLIMIT_SEND_MAIL))
                            {
                                //如果达到邮件上限则计算推送给客户端的道具数量供客户端计算
                                itemRs.setItemNum(playerItemModel.getItemNum() - (null == frontPlayerItem ? 0 : frontPlayerItem.getItemNum()));
                            }
                            protocolList.add(itemRs);
                            // 如果当前道具类型为货币类型
                            if (itemModel.getItemType() == ItemTypeEnum.CURRENCY.getItemType())
                            {
                                // 如果道具子类型为金币并且数量小于0则触发【激活期间】花费X金币和【累计期间】花费X金币
                                if (itemModel.getItemTypeDetail() == ItemTypeEnum.CurrencyTypeEnum.GOLD.getSubclassType() && itemJoinModel.getItemNum() < 0)
                                {
                                    spendGoldList.add(itemJoinModel);
                                }
                                // 如果道具子类型为钻石并且数量小于0则触发【激活期间】花费X钻石和【累计期间】花费X钻石
                                else if (itemModel.getItemTypeDetail() == ItemTypeEnum.CurrencyTypeEnum.RECHARGE_DIAMOND.getSubclassType() && itemJoinModel.getItemNum() < 0)
                                {
                                    diamondList.add(itemJoinModel);
                                }
                            }
                        }
                        else if (errorCodeEnum.equals(ErrorCodeEnum.ITEM_NUM_TOTAL_UPPERLIMIT_SEND_MAIL))
                        {
                            //如果达到道具总上限则发送邮件
                            PlayerItemModel assItemModel = resultMap.get(ErrorCodeEnum.ITEM_NUM_TOTAL_UPPERLIMIT_SEND_MAIL);
                            mailSendItemList.add(new ItemJoinModel(assItemModel.getItemId(), assItemModel.getItemNum(), assItemModel.getItemType()));
                        }
                        else
                        {
                            CmdUtil.sendErrorMsg(player.getSession(), updateItemListRs.getCode(), errorCodeEnum);
                            return;
                        }
                    }
                }
            }
        }

        // 批量异步新增入库
        if (!insPlayerItemModelList.isEmpty())
        {
            insPlayerItemModelList.forEach(playerItemModel ->
            {
                player.cache().getPlayerItemCache().updateCache(playerItemModel);
                playerItemDao.queueInsert(playerItemModel);
            });
        }
        // 批量异步更新入库
        if (!updPlayerItemModelList.isEmpty())
        {
            updPlayerItemModelList.forEach(playerItemModel ->
            {
                player.cache().getPlayerItemCache().updateCache(playerItemModel);
                playerItemDao.queueUpdate(playerItemModel);
            });
        }
        // 批量异步删除入库
        if (!delPlayerItemModelList.isEmpty())
        {
            delPlayerItemModelList.forEach(playerItemModel ->
            {
                player.cache().getPlayerItemCache().deleteCache(playerItemModel);
                playerItemDao.queueDelete(playerItemModel);
            });
        }
        //道具数量达到上限进行邮件发送
        if (!mailSendItemList.isEmpty())
        {
            mailService.addMail(player, MailId.PACK_MIAL, mailSendItemList, null);
        }
        if (!protocolList.isEmpty())
        {
            updateItemListRs.setItemArr(protocolList);
            CmdUtil.sendMsg(player, updateItemListRs);
            logger.debug("根据玩家id:{}, 参数列表:{} 执行GM指令添加物品操作后返回:{}", super.getPlayer().getPlayerId(), paramsMap.toString(), updateItemListRs.toString());
        }
        // 判断当前如果有消耗金币的行为则触发
        if (!spendGoldList.isEmpty())
        {
            // 【激活期间】或【累计期间】花费X金币
            TaskScheduleRefreshUtil.goldSchedule(player, spendGoldList.stream().mapToInt(e -> Math.abs(e.getItemNum())).sum());
        }
        // 判断当前如果有消耗钻石的行为则触发
        if (!diamondList.isEmpty())
        {
            // 【激活期间】或【累计期间】花费X钻石
            TaskScheduleRefreshUtil.diamondSchedule(player, diamondList.stream().mapToInt(e -> Math.abs(e.getItemNum())).sum());
        }
        // 如果有经验值则触发角色升级操作
        if (null != afterPlayerInfo)
        {
            player.cache().setPlayerInfoModel(afterPlayerInfo);
            playerInfoDao.queueUpdate(afterPlayerInfo);
            // 如果触发角色升级后的玩家等级大于之前的则推送RoleUpgradeRs协议否则推送UpdPlayerInfoRs玩家信息变更协议
            if (afterPlayerInfo.getLv() > beforeLv)
            {
                RoleUpgradeRs roleUpgradeRs = new RoleUpgradeRs(afterPlayerInfo.getLv(), afterPlayerInfo.getExp());
                CmdUtil.sendMsg(player, roleUpgradeRs);
                attributeService.refresh(player);
                logger.debug("玩家id:{}, 角色升级完成后刷新属性值: {}", player.getPlayerId(), attributeService.attributeString(player.cache().getAttributeCache().getAttributes()));
                // 等级提升后触发事件监听
                BeanFactory.getBean(LevelEventManager.class).doEvents(player);
                logger.debug("玩家id:{}, 触发角色升级操作, 返回协议:{}", player.getPlayerId(), roleUpgradeRs.toString());
            }
            else
            {
                UpdPlayerInfoRs updPlayerInfoRs = new UpdPlayerInfoRs();
                updPlayerInfoRs.setLevel(afterPlayerInfo.getLv());
                updPlayerInfoRs.setExp(afterPlayerInfo.getExp());
                CmdUtil.sendMsg(player, updPlayerInfoRs);
                logger.debug("玩家id:{}, 触发角色升级操作, 返回协议:{}", player.getPlayerId(), updPlayerInfoRs.toString());
            }
        }
    }

    /**
     * 根据大类型存储所有道具信息
     *
     * @param integerListMap 存储同类型的参数 key-itemType value-Map(key-itemId value-itemNum)
     * @param itemType       道具类型
     * @param key            道具id
     * @param paramsMap      gm指令列表
     */
    private void sameTypeCache(Map<Integer, Map<String, String>> integerListMap, Integer itemType, String key, Map<String, String> paramsMap)
    {
        if (integerListMap.containsKey(itemType))
        {
            Map<String, String> stringMap = integerListMap.get(itemType);
            stringMap.put(key, paramsMap.get(key));
        }
        else
        {
            Map<String, String> stringMap = new HashMap<String, String>();
            stringMap.put(key, paramsMap.get(key));
            integerListMap.put(itemType, stringMap);
        }
    }
}