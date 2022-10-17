package com.dykj.rpg.game.module.item.service.impl;

import com.dykj.rpg.common.config.constant.ConfigEnum;
import com.dykj.rpg.common.config.dao.ConfigDao;
import com.dykj.rpg.common.config.dao.EquipBasicDao;
import com.dykj.rpg.common.config.dao.ItemDao;
import com.dykj.rpg.common.config.dao.LevelExperienceDao;
import com.dykj.rpg.common.config.model.ConfigModel;
import com.dykj.rpg.common.config.model.EquipBasicModel;
import com.dykj.rpg.common.config.model.ItemModel;
import com.dykj.rpg.common.config.model.LevelExperienceModel;
import com.dykj.rpg.common.consts.*;
import com.dykj.rpg.common.data.dao.PlayerEquipDao;
import com.dykj.rpg.common.data.dao.PlayerInfoDao;
import com.dykj.rpg.common.data.dao.PlayerItemDao;
import com.dykj.rpg.common.data.model.PlayerEquipModel;
import com.dykj.rpg.common.data.model.PlayerInfoModel;
import com.dykj.rpg.common.data.model.PlayerItemModel;
import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.module.attribute.service.AttributeService;
import com.dykj.rpg.game.module.cache.PlayerItemCache;
import com.dykj.rpg.game.module.cache.logic.EquipCache;
import com.dykj.rpg.game.module.equip.service.EquipService;
import com.dykj.rpg.game.module.event.level.LevelEventManager;
import com.dykj.rpg.game.module.item.consts.ItemPromp;
import com.dykj.rpg.game.module.item.response.ItemJoinModel;
import com.dykj.rpg.game.module.item.response.ItemResponse;
import com.dykj.rpg.game.module.item.service.ItemService;
import com.dykj.rpg.game.module.item.service.strategy.core.ItemOperateRealizeManage;
import com.dykj.rpg.game.module.mail.service.MailService;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.task.logic.refresh.TaskScheduleRefreshUtil;
import com.dykj.rpg.game.module.util.CmdUtil;
import com.dykj.rpg.protocol.item.*;
import com.dykj.rpg.protocol.player.RoleUpgradeRs;
import com.dykj.rpg.protocol.player.UpdPlayerInfoRs;
import com.dykj.rpg.util.date.DateUtils;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
/**
 * @Description 背包service
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/21
 */
@Service
public class ItemServiceImpl implements ItemService
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private ItemDao itemDao;

    @Resource
    private ConfigDao configDao;

    @Resource
    private PlayerItemDao playerItemDao;

    @Resource
    private PlayerInfoDao playerInfoDao;

    @Resource
    private LevelExperienceDao levelExperienceDao;

    @Resource
    private EquipBasicDao equipBasicDao;

    @Resource
    private PlayerEquipDao playerEquipDao;

    @Resource
    private EquipService equipService;

    @Resource
    private ItemOperateRealizeManage itemOperateRealizeManage;

    @Resource
    private AttributeService attributeService;

    @Resource
    private MailService mailService;

    /**
     * 获取指定道具信息
     * @param player 玩家信息
     * @param itemId 物品id
     * @return 物品信息
     */
    @Override
    public PlayerItemModel getItemInfo(Player player, Integer itemId)
    {
        return player.cache().getPlayerItemCache().getCache(itemId.longValue());
    }

    /**
     * 判断指定道具数量是否足够
     * @param player 玩家信息
     * @param itemId 道具id
     * @param itemNum 道具数量
     * @return true-是 false-否
     */
    @Override
    public boolean isQuantityEnough(Player player, Integer itemId, Integer itemNum)
    {
        PlayerItemModel playerItemModel = this.getItemInfo(player, itemId);
        if (null == playerItemModel)
            return false;

        return itemNum <= playerItemModel.getItemNum();
    }

    /**
     * 获取指定货币类型的总数量
     * @param player 玩家信息
     * @param currencyType 货币子类型
     * @return 具体货币类型的总数量
     */
    @Override
    public int getCurrencyQuantity(Player player, ItemTypeEnum.CurrencyTypeEnum currencyType)
    {
        int quantity = 0;
        PlayerItemCache playerItemCache = player.cache().getPlayerItemCache();
        if (null != playerItemCache)
        {
            Map<Long, PlayerItemModel> playerItemModelMap = playerItemCache.getItemCacheMap();
            if (null != playerItemModelMap && !playerItemModelMap.isEmpty())
            {
                //根据货币大类和传入子类统计玩家货币总数
                quantity = playerItemModelMap.values().stream()
                        .filter(e -> e.getItemType() == ItemTypeEnum.CURRENCY.getItemType()
                                && e.getItemTypeDetail() == currencyType.getSubclassType())
                        .mapToInt(PlayerItemModel::getItemNum)
                        .sum();
            }
        }
        return quantity;
    }

    /**
     * 获取指定大类道具列表
     * @param player 玩家信息
     * @param itemTypeEnum 道具大类枚举
     * @return 道具协议列表
     */
    @Override
    public List<ItemRs> getSpecifyItems(Player player, ItemTypeEnum itemTypeEnum)
    {
        List<ItemRs> list = null;
        PlayerItemCache playerItemCache = player.cache().getPlayerItemCache();
        if (null != playerItemCache)
        {
            Collection<PlayerItemModel> collect = null;
            if (null == itemTypeEnum || itemTypeEnum.getItemType() == ItemTypeEnum.GENERIC.getItemType())
            {
                collect = playerItemCache.values();
            }
            else
            {
                collect = playerItemCache.values().stream().filter(e -> e.getItemType() == itemTypeEnum.getItemType()).collect(Collectors.toList());
            }

            list = new ArrayList<>();
            for (PlayerItemModel playerItemModel : collect)
            {
                ItemModel itemModel = itemDao.getConfigByKey(playerItemModel.getItemId());
                if (null == itemModel)
                {
                    return null;
                }
                list.add(this.itemRs(playerItemModel, itemModel));
            }
        }
        return list;
    }

    /**
     * 获取指定道具子类型列表
     * @param player 玩家信息
     * @param itemTypeEnum 道具大类枚举
     * @param itemType 道具子类枚举
     * @return 道具协议列表
     */
    @Override
    public List<ItemRs> getChildItems(Player player, ItemTypeEnum itemTypeEnum, ItemType itemType)
    {
        List<ItemRs> list = null;
        PlayerItemCache playerItemCache = player.cache().getPlayerItemCache();
        if (null != playerItemCache)
        {
            List<PlayerItemModel> collect = playerItemCache.values().stream()
                    .filter(e -> e.getItemType() == itemTypeEnum.getItemType() &&
                            e.getItemTypeDetail() == itemTypeEnum.getSubclassTypeEnum(itemType)).collect(Collectors.toList());
            list = new ArrayList<>();
            for (PlayerItemModel playerItemModel : collect)
            {
                ItemModel itemModel = itemDao.getConfigByKey(playerItemModel.getItemId());
                if (null == itemModel)
                {
                    return null;
                }
                list.add(this.itemRs(playerItemModel, itemModel));
            }
        }
        return list;
    }

    /**
     * 更新
     */
    private void upd(Player player, PlayerItemModel playerItemModel, ItemJoinModel itemJoinModel,
                     ItemModel itemModel, Map<ErrorCodeEnum, PlayerItemModel> resultMap)
    {
        BigDecimal originalBig = new BigDecimal(playerItemModel.getItemNum());
        int itemUpdNum = 0;//更新后的数量
        //为负数则减少
        if (itemJoinModel.getItemNum() < 0)
        {
            int amount = Math.abs(itemJoinModel.getItemNum());
            //如果背包里的道具数量小于消耗的数量
            if (playerItemModel.getItemNum() < amount)
            {
                logger.error("玩家id:{}, 道具id:{}, 待扣减的道具数量: {}, 持有的道具数量不足!", player.getPlayerId(), itemJoinModel.getItemId(), itemJoinModel.getItemNum());
                resultMap.put(ErrorCodeEnum.ITEM_QUANTITY_NOT_ENOUGH, playerItemModel);
                return;
            }
            itemUpdNum = originalBig.subtract(new BigDecimal(amount)).intValue();
        }
        else
        {
            BigDecimal currentBig = new BigDecimal(itemJoinModel.getItemNum());
            itemUpdNum = originalBig.add(currentBig).intValue();
            //如果当前不是经验类型则需要校验道具总上限和每日上限
            if(playerItemModel.getItemType() != ItemTypeEnum.EXPERIENCE.getItemType())
            {
                //当前时间
                LocalDate currentDate = LocalDate.now();
                //道具获取时间
                LocalDate itemGetDate = playerItemModel.getItemGetTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                BigDecimal itemDailyNumBig = new BigDecimal(playerItemModel.getItemDailyNum());//每日获取道具数量
                int itemDailyNum = itemDailyNumBig.add(currentBig).intValue();
                //判断计算后的每日获取道具数是否达到上限且是否为当天
                if ((null != itemModel.getItemGetMaxDay() && itemModel.getItemGetMaxDay() > 0) && itemDailyNum > itemModel.getItemGetMaxDay() && itemGetDate.equals(currentDate))
                {
                    logger.error("玩家id:{}, 道具id:{}, 道具数量达到每日上限!", player.getPlayerId(), playerItemModel.getItemId());
                    resultMap.put(ErrorCodeEnum.ITEM_NUM_DAILY_UPPERLIMIT, playerItemModel);
                    return;
                }
                //如果道具获取时间不等于当前时间则表示已经跨天需要把每日道具获取数量设置为当前添加数
                if (!itemGetDate.equals(currentDate))
                {
                    playerItemModel.setItemDailyNum(currentBig.intValue());
                }
                else
                {
                    playerItemModel.setItemDailyNum(itemDailyNum);
                }
                playerItemModel.setItemGetTime(new Date());
                //判断道具是否到达道具数量总上限(道具总数为空则表示没有上限) 如果达到上限则走邮件系统
                if (playerItemModel.getItemType() != ItemTypeEnum.CURRENCY.getItemType() && (null != itemModel.getItemMaxNum() && itemModel.getItemMaxNum() > 0) && itemUpdNum > itemModel.getItemMaxNum())
                {
                    logger.debug("玩家id:{}, 道具id:{}, 道具数量达到总上限则进行邮件发送!", player.getPlayerId(), playerItemModel.getItemId());
                    int tempNum = itemUpdNum - itemModel.getItemMaxNum();
                    PlayerItemModel assItemModel = playerItemModel.copy();
                    assItemModel.setItemNum(tempNum);
                    resultMap.put(ErrorCodeEnum.ITEM_NUM_TOTAL_UPPERLIMIT_SEND_MAIL, assItemModel);
                    //重新对itemUpdNum赋值为最大道具上限
                    itemUpdNum = itemModel.getItemMaxNum();
                }
            }
        }
        playerItemModel.setItemNum(itemUpdNum);
        resultMap.put(ErrorCodeEnum.SUCCESS, playerItemModel);
    }

    /**
     * 更新道具(不入库不生成协议;如果返回的错误码为ErrorCodeEnum.ITEM_NUM_DAILY_UPPERLIMIT, 表示道具添加已达到上限则需要手动组装进行邮件发送)
     * @param player 玩家信息
     * @param itemJoinModel 道具更新对象(itemNum道具数量, 整数增加, 负数减少))
     * @param itemModel 当前道具的基础配置信息
     * @return key-错误码 value-道具信息
     */
    @Override
    public Map<ErrorCodeEnum, PlayerItemModel> updateItem(Player player, ItemJoinModel itemJoinModel, ItemModel itemModel)
    {
        //返回更新结果  key - ErrorCodeEnum错误码  value - 具体更新道具对象
        Map<ErrorCodeEnum, PlayerItemModel> resultMap = new HashMap<ErrorCodeEnum, PlayerItemModel>();
        PlayerItemModel playerItemModel = this.getItemInfo(player, itemJoinModel.getItemId());
        //如果根据道具id没有拿到所持有的道具则表示玩家没有该道具进行添加操作
        if (null == playerItemModel)
        {
            playerItemModel = new PlayerItemModel();
            //如果传入的道具数量小于0表示当前道具背包中没有且做的是扣减操作需提示错误码
            if (itemJoinModel.getItemNum() < 0)
            {
                logger.error("玩家id:{}, 道具id:{}, 待扣减的道具数量: {}, 当前道具背包中不存在!", player.getPlayerId(), itemJoinModel.getItemId(), itemJoinModel.getItemNum());
                resultMap.put(ErrorCodeEnum.ITEM_NOT_EXIST, playerItemModel);
                return resultMap;
            }
            playerItemModel.setPlayerId(player.getPlayerId());//玩家id
            playerItemModel.setItemId(itemJoinModel.getItemId());//道具id
            playerItemModel.setItemType(itemModel.getItemType());//道具类型
            playerItemModel.setItemNum(itemJoinModel.getItemNum());//道具数量
            playerItemModel.setItemTypeDetail(itemModel.getItemTypeDetail());//道具子类型
            playerItemModel.setLiveType(itemModel.getItemLiveType());//道具限时类型
            if (itemModel.getItemLiveType() != 1)
            {
                playerItemModel.setLiveTime(new Date());//道具开始持续时间
            }
            //因数据库该字段取值范围为:0-不可上锁 1-已解锁 2-已上锁  所以当配置表里ItemLock为2时表示支持上锁(2种状态:1=解锁 2=上锁)
            int itemLock = 0;
            if (null != itemModel.getItemLock() && itemModel.getItemLock() == 2)
            {
                itemLock = 1;
            }
            playerItemModel.setItemLock(itemLock);//道具是否上锁
            playerItemModel.setItemDailyNum(itemJoinModel.getItemNum());//道具每日获取数量
            playerItemModel.setItemGetTime(new Date());//道具获取时间
            resultMap.put(ErrorCodeEnum.SUCCESS, playerItemModel);
        }
        else
        {
            //克隆当前玩家道具避免当前赋值道具值变更时内存对象也一并更新
            this.upd(player, playerItemModel.copy(), itemJoinModel, itemModel, resultMap);
        }
        return resultMap;
    }

    /**
     * 更新道具(入库并生成协议UpdateItemListRs并触发角色升级)
     * 如果itemType是装备类型则新增单个装备
     * @param player 玩家信息
     * @param itemJoinModel 道具更新对象(itemNum道具数量, 整数增加, 负数减少))
     * @param itemOperateEnum 道具操作类型
     * @param itemPromp 弹出类型枚举(0-不弹窗, 1-弹窗)
     * @return 道具处理响应包装类
     */
    @Override
    public ItemResponse updateItem(Player player, ItemJoinModel itemJoinModel, ItemOperateEnum itemOperateEnum, ItemPromp itemPromp)
    {
        ItemResponse itemResponse = new ItemResponse(ErrorCodeEnum.SUCCESS);
        if (null == itemJoinModel.getItemId() || null == itemJoinModel.getItemNum() || null == itemJoinModel.getItemType())
        {
            logger.error("玩家id:{}, 更新道具时道具id、道具数量或道具类型为空!", player.getPlayerId());
            itemResponse.setCodeEnum(ErrorCodeEnum.CLIENT_PRAMS_ERROR);
            return itemResponse;
        }
        //获取升级前的玩家等级和经验
        int beforeLv = player.cache().getPlayerInfoModel().getLv();
        //如果是经验类型则走角色升级逻辑
        if (itemJoinModel.getItemType() == ItemTypeEnum.EXPERIENCE.getItemType())
        {
            PlayerInfoModel playerInfoModel = this.roleUpgradeLogic(player, itemJoinModel, itemResponse);
            if (null != playerInfoModel)
            {
                this.roleUpgradeUpd(player, playerInfoModel, beforeLv, itemResponse);
            }
        }
        else
        {
            List<ItemRs> itemArr = new ArrayList<>();
            //如果添加的道具是装备则只新增单个装备
            if(itemJoinModel.getItemType() == ItemTypeEnum.EQUIPMENT.getItemType())
            {
                ItemRs itemRs = equipService.addEquip(itemJoinModel.getItemId(), player, itemOperateEnum);
                itemArr.add(itemRs);
            }
            else
            {
                ItemModel itemModel = itemDao.getConfigByKey(itemJoinModel.getItemId());//拿到道具基础配置表信息
                if (null == itemModel)
                {
                    logger.error("玩家id:{}, 道具id:{}, 对应道具基础配置表数据为空!", player.getPlayerId(), itemJoinModel.getItemId());
                    itemResponse.setCodeEnum(ErrorCodeEnum.CONFIG_ERROR);
                    return itemResponse;
                }
                Map<ErrorCodeEnum, PlayerItemModel> resultMap = this.updateItem(player, itemJoinModel, itemModel);
                for (ErrorCodeEnum errorCodeEnum : resultMap.keySet())
                {
                    if (errorCodeEnum.equals(ErrorCodeEnum.SUCCESS))
                    {
                        PlayerItemCache playerItemCache = player.cache().getPlayerItemCache();
                        //拿到更新后的道具信息
                        PlayerItemModel playerItemModel = resultMap.get(ErrorCodeEnum.SUCCESS);
                        //判断当前道具是否存在如不存在则新增
                        PlayerItemModel frontPlayerItem = this.getItemInfo(player, itemJoinModel.getItemId());
                        if (null == frontPlayerItem)
                        {
                            playerItemCache.updateCache(playerItemModel);
                            playerItemDao.queueInsert(playerItemModel);
                        }
                        else
                        {
                            //判断更新后如果当前道具数量为0并且当前道具类型不为货币则删除
                            if (playerItemModel.getItemNum() == 0 && playerItemModel.getItemType() != ItemTypeEnum.CURRENCY.getItemType())
                            {
                                playerItemCache.deleteCache(playerItemModel);
                                playerItemDao.queueDelete(playerItemModel);
                            }
                            else
                            {
                                playerItemCache.updateCache(playerItemModel);
                                playerItemDao.queueUpdate(playerItemModel);
                            }
                        }
                        //组装协议返回最新变更数量
                        ItemRs itemRs = this.itemRs(playerItemModel, itemModel);
                        itemRs.setItemNum(itemJoinModel.getItemNum());//返回传入的道具数量
                        if (resultMap.containsKey(ErrorCodeEnum.ITEM_NUM_TOTAL_UPPERLIMIT_SEND_MAIL))
                        {
                            //如果达到邮件上限则计算推送给客户端的道具数量供客户端计算
                            itemRs.setItemNum(playerItemModel.getItemNum() - (null == frontPlayerItem ? 0 : frontPlayerItem.getItemNum()));
                        }
                        itemArr.add(itemRs);
                        //刷新货币类任务进度
                        TaskScheduleRefreshUtil.currencySchedule(player, itemModel, itemJoinModel.getItemNum());
                    }
                    else if (errorCodeEnum.equals(ErrorCodeEnum.ITEM_NUM_TOTAL_UPPERLIMIT_SEND_MAIL))
                    {
                        //如果达到道具总上限则发送邮件
                        PlayerItemModel assItemModel = resultMap.get(ErrorCodeEnum.ITEM_NUM_TOTAL_UPPERLIMIT_SEND_MAIL);
                        List<ItemJoinModel> award = new ArrayList<>();
                        award.add(new ItemJoinModel(assItemModel.getItemId(), assItemModel.getItemNum(), assItemModel.getItemType()));
                        mailService.addMail(player, MailId.PACK_MIAL, award, null);
                    }
                    else
                    {
                        itemResponse.setCodeEnum(errorCodeEnum);
                        return itemResponse;
                    }
                }
            }
            UpdateItemListRs updateItemListRs = new UpdateItemListRs(itemOperateEnum.getType(), itemPromp.getType(), true, itemArr);
            itemResponse.setUpdateItemListRs(updateItemListRs);
            itemResponse.setItemRsList(itemArr);
        }
        logger.debug("玩家id:{}, 道具id:{}, 道具数量:{}, 道具操作类型:{}, 道具类型:{}, 操作完成后返回: {}", player.getPlayerId(), itemJoinModel.getItemId(),
                itemJoinModel.getItemNum(), itemOperateEnum.getType(), itemJoinModel.getItemType(), itemResponse.toString());
        return itemResponse;
    }

    /**
     * 角色升级
     */
    private PlayerInfoModel roleUpgradeLogic(Player player, ItemJoinModel itemJoinModel, ItemResponse itemResponse)
    {
        ItemModel itemModel = itemDao.getConfigByKey(itemJoinModel.getItemId());//拿到道具基础配置表信息
        if (null == itemModel)
        {
            logger.error("玩家id:{}, 道具id:{}, 对应道具基础配置表数据为空!", player.getPlayerId(), itemJoinModel.getItemId());
            itemResponse.setCodeEnum(ErrorCodeEnum.CONFIG_ERROR);
            return null;
        }
        //如果子类型不为角色经验
        if (itemModel.getItemTypeDetail() != ItemTypeEnum.ExperienceTypeEnum.ROLE_EXPERIENCE.getSubclassType())
        {
            logger.error("玩家id:{}, 道具id:{}, 经验值道具子类型配置错误!", player.getPlayerId(), itemModel.getItemId());
            itemResponse.setCodeEnum(ErrorCodeEnum.CONFIG_ERROR);
            return null;
        }
        Map<ErrorCodeEnum, PlayerInfoModel> rolesUpgradeMap = this.rolesUpgrade(player, itemJoinModel.getItemNum());
        if (!rolesUpgradeMap.containsKey(ErrorCodeEnum.SUCCESS))
        {
            itemResponse.setCodeEnum(rolesUpgradeMap.keySet().stream().findFirst().get());
            return null;
        }
        return rolesUpgradeMap.get(ErrorCodeEnum.SUCCESS);
    }

    /**
     * 角色升级
     */
    private void roleUpgradeUpd(Player player, PlayerInfoModel afterPlayerInfo, int beforeLv, ItemResponse itemResponse)
    {
        player.cache().setPlayerInfoModel(afterPlayerInfo);
        playerInfoDao.queueUpdate(afterPlayerInfo);
        //如果触发角色升级后的玩家等级大于之前的则推送RoleUpgradeRs协议否则推送UpdPlayerInfoRs玩家信息变更协议
        if (afterPlayerInfo.getLv() > beforeLv)
        {
            itemResponse.setRoleUpgradeRs(new RoleUpgradeRs(afterPlayerInfo.getLv(), afterPlayerInfo.getExp()));
            attributeService.refresh(player);
            logger.debug("玩家id:{}, 角色升级完成后刷新属性值: {}", player.getPlayerId(), attributeService.attributeString(player.cache().getAttributeCache().getAttributes()));
            //等级提升后触发事件监听
            BeanFactory.getBean(LevelEventManager.class).doEvents(player);
            logger.debug("玩家id:{}, 触发角色升级操作, 返回协议:{}", player.getPlayerId(), itemResponse.getRoleUpgradeRs().toString());
        }
        else
        {
            UpdPlayerInfoRs updPlayerInfoRs = new UpdPlayerInfoRs();
            updPlayerInfoRs.setLevel(afterPlayerInfo.getLv());
            updPlayerInfoRs.setExp(afterPlayerInfo.getExp());
            itemResponse.setUpdPlayerInfoRs(updPlayerInfoRs);
            logger.debug("玩家id:{}, 触发角色升级操作, 返回协议:{}", player.getPlayerId(), itemResponse.getUpdPlayerInfoRs().toString());
        }
    }

    /**
     * 批量更新道具(入库并生成协议UpdateItemListRs并触发角色升级; 如果itemType是装备类型则新增单个装备)
     * @param player 玩家信息
     * @param itemJoinModels 道具更新列表(每个ItemJoinModel的itemNum字段值传正数则增加, 负数减少)
     * @param itemOperateEnum 道具操作类型
     * @param itemPromp 弹出类型枚举(0-不弹窗, 1-弹窗)
     * @return 道具处理响应包装类
     */
    @Override
    public ItemResponse batchUpdateItem(Player player, List<ItemJoinModel> itemJoinModels, ItemOperateEnum itemOperateEnum, ItemPromp itemPromp)
    {
        ItemResponse itemResponse = new ItemResponse(ErrorCodeEnum.SUCCESS);
        List<PlayerItemModel> insPlayerItemModelList = new ArrayList<PlayerItemModel>();//最终新增list
        List<PlayerItemModel> updPlayerItemModelList = new ArrayList<PlayerItemModel>();//最终更新list
        List<PlayerItemModel> delPlayerItemModelList = new ArrayList <PlayerItemModel>();//最终删除list
        List<ItemJoinModel> spendGoldList = new ArrayList<>();//存储任务花费多少金币list
        List<ItemJoinModel> diamondList = new ArrayList<>();//存储任务花费多少钻石list
        List<ItemJoinModel> mailSendItemList = new ArrayList<>();//存储添加的道具达到总上限后进行邮件发送list
        List<ItemRs> itemRsList = new ArrayList<ItemRs>();//存储协议
        //获取升级前的玩家等级和经验
        int beforeLv = player.cache().getPlayerInfoModel().getLv();
        PlayerInfoModel afterPlayerInfo = null;//触发升级后玩家信息
        for (ItemJoinModel itemJoinModel : itemJoinModels)
        {
            if (null == itemJoinModel.getItemId() || null == itemJoinModel.getItemNum() || null == itemJoinModel.getItemType())
            {
                logger.error("玩家id:{}, 更新道具时道具id、道具数量或道具类型为空!", player.getPlayerId());
                itemResponse.setCodeEnum(ErrorCodeEnum.CLIENT_PRAMS_ERROR);
                return itemResponse;
            }
            //如果是经验类型则走角色升级逻辑
            if (itemJoinModel.getItemType() == ItemTypeEnum.EXPERIENCE.getItemType())
            {
                afterPlayerInfo = this.roleUpgradeLogic(player, itemJoinModel, itemResponse);
                continue;
            }
            //如果添加的道具是装备
            if (itemJoinModel.getItemType() == ItemTypeEnum.EQUIPMENT.getItemType())
            {
                for (int i = 0; i < itemJoinModel.getItemNum(); i++)
                {
                    ItemRs itemRs = equipService.addEquip(itemJoinModel.getItemId(), player, itemOperateEnum);
                    itemRsList.add(itemRs);
                }
                continue;
            }
            //如果添加的是普通道具
            ItemModel itemModel = itemDao.getConfigByKey(itemJoinModel.getItemId());//拿到道具基础配置表信息
            if (null == itemModel)
            {
                logger.error("玩家id:{}, 道具id:{}, 对应道具基础配置表数据为空!", player.getPlayerId(), itemJoinModel.getItemId());
                itemResponse.setCodeEnum(ErrorCodeEnum.CONFIG_ERROR);
                return itemResponse;
            }
            Map<ErrorCodeEnum, PlayerItemModel> itemModelMap = this.updateItem(player, itemJoinModel, itemModel);
            for (ErrorCodeEnum errorCodeEnum : itemModelMap.keySet())
            {
                if (errorCodeEnum.equals(ErrorCodeEnum.SUCCESS))
                {
                    //拿到更新后的道具信息
                    PlayerItemModel playerItemModel = itemModelMap.get(ErrorCodeEnum.SUCCESS);
                    //判断当前道具是否存在如不存在则新增
                    PlayerItemModel frontPlayerItem = this.getItemInfo(player, itemJoinModel.getItemId());
                    if (null == frontPlayerItem)
                    {
                        insPlayerItemModelList.add(playerItemModel);
                    }
                    else
                    {
                        //判断更新后如果当前道具数量为0则删除并且如果不是货币类型则把为0的数据删除
                        if (playerItemModel.getItemNum() == 0 && playerItemModel.getItemType() != ItemTypeEnum.CURRENCY.getItemType())
                        {
                            delPlayerItemModelList.add(playerItemModel);
                        }
                        else
                        {
                            updPlayerItemModelList.add(playerItemModel);
                        }
                    }
                    //组装协议返回最新变更数量
                    ItemRs itemRs = this.itemRs(playerItemModel, itemModel);
                    itemRs.setItemNum(itemJoinModel.getItemNum());//返回输入的道具数量
                    if (itemModelMap.containsKey(ErrorCodeEnum.ITEM_NUM_TOTAL_UPPERLIMIT_SEND_MAIL))
                    {
                        //如果达到邮件上限则计算推送给客户端的道具数量供客户端计算
                        itemRs.setItemNum(playerItemModel.getItemNum() - (null == frontPlayerItem ? 0 : frontPlayerItem.getItemNum()));
                    }
                    itemRsList.add(itemRs);
                    //如果当前道具类型为货币类型
                    if (itemModel.getItemType() == ItemTypeEnum.CURRENCY.getItemType())
                    {
                        //如果道具子类型为金币并且数量小于0则触发【激活期间】花费X金币和【累计期间】花费X金币
                        if (itemModel.getItemTypeDetail() == ItemTypeEnum.CurrencyTypeEnum.GOLD.getSubclassType() && itemJoinModel.getItemNum() < 0)
                        {
                            spendGoldList.add(itemJoinModel);
                        }
                        //如果道具子类型为钻石并且数量小于0则触发【激活期间】花费X钻石和【累计期间】花费X钻石
                        else if (itemModel.getItemTypeDetail() == ItemTypeEnum.CurrencyTypeEnum.RECHARGE_DIAMOND.getSubclassType() && itemJoinModel.getItemNum() < 0)
                        {
                            diamondList.add(itemJoinModel);
                        }
                    }
                }
                else if (errorCodeEnum.equals(ErrorCodeEnum.ITEM_NUM_TOTAL_UPPERLIMIT_SEND_MAIL))
                {
                    //如果达到道具总上限则发送邮件
                    PlayerItemModel assItemModel = itemModelMap.get(ErrorCodeEnum.ITEM_NUM_TOTAL_UPPERLIMIT_SEND_MAIL);
                    mailSendItemList.add(new ItemJoinModel(assItemModel.getItemId(), assItemModel.getItemNum(), assItemModel.getItemType()));
                }
                else
                {
                    itemResponse.setCodeEnum(errorCodeEnum);
                    return itemResponse;
                }
            }
        }

        UpdateItemListRs updateItemListRs = new UpdateItemListRs(itemOperateEnum.getType(), itemPromp.getType(), true, null);//更新物品列表协议
        if (itemResponse.getCodeEnum().equals(ErrorCodeEnum.SUCCESS))
        {
            PlayerItemCache playerItemCache = player.cache().getPlayerItemCache();
            //批量异步新增入库
            if (!insPlayerItemModelList.isEmpty())
            {
                insPlayerItemModelList.forEach(playerItemModel ->
                {
                    playerItemCache.updateCache(playerItemModel);
                    playerItemDao.queueInsert(playerItemModel);
                });
            }
            //批量异步更新入库
            if (!updPlayerItemModelList.isEmpty())
            {
                updPlayerItemModelList.forEach(playerItemModel ->
                {
                    playerItemCache.updateCache(playerItemModel);
                    playerItemDao.queueUpdate(playerItemModel);
                });
            }
            //批量异步删除入库
            if (!delPlayerItemModelList.isEmpty())
            {
                delPlayerItemModelList.forEach(playerItemModel ->
                {
                    playerItemCache.deleteCache(playerItemModel);
                    playerItemDao.queueDelete(playerItemModel);
                });
            }
            //道具数量达到上限进行邮件发送
            if (!mailSendItemList.isEmpty())
            {
                mailService.addMail(player, MailId.PACK_MIAL, mailSendItemList, null);
            }
            if (!itemRsList.isEmpty())
            {
                updateItemListRs.setItemArr(itemRsList);
                itemResponse.setItemRsList(itemRsList);
            }
            //判断当前如果有消耗金币的行为则触发
            if (!spendGoldList.isEmpty())
            {
                /*
                 * 触发任务进度刷新
                 * 任务完成条件类型:
                 *      【激活期间】花费X金币
                 *      【累计期间】花费X金币
                 */
                TaskScheduleRefreshUtil.goldSchedule(player, spendGoldList.stream().mapToInt(e -> Math.abs(e.getItemNum())).sum());
            }
            //判断当前如果有消耗钻石的行为则触发
            if (!diamondList.isEmpty())
            {
                /*
                 * 触发任务进度刷新
                 * 任务完成条件类型:
                 *      【激活期间】花费X钻石
                 *      【累计期间】花费X钻石
                 */
                TaskScheduleRefreshUtil.diamondSchedule(player, diamondList.stream().mapToInt(e -> Math.abs(e.getItemNum())).sum());
            }
            //如果有经验值则触发角色升级操作
            if (null != afterPlayerInfo)
            {
                this.roleUpgradeUpd(player, afterPlayerInfo, beforeLv, itemResponse);
            }
        }
        itemResponse.setUpdateItemListRs(updateItemListRs);
        logger.debug("玩家id:{}, 道具列表: {}, 道具操作类型:{}, 操作完成后返回: {}", player.getPlayerId(), itemJoinModels.toString(), itemOperateEnum.getType(), itemResponse.toString());
        return itemResponse;
    }

    /**
     * 更新道具并推送客户端(推送协议UpdateItemListRs并触发角色升级)
     * 如果itemType是装备类型则新增单个装备
     * @param player 玩家信息
     * @param itemJoinModel 道具更新对象(itemNum道具数量, 整数增加, 负数减少))
     * @param itemOperateEnum 道具操作类型
     * @param itemPromp 弹出类型枚举
     */
    @Override
    public ItemResponse updateItemPush(Player player, ItemJoinModel itemJoinModel, ItemOperateEnum itemOperateEnum, ItemPromp itemPromp)
    {
        ItemResponse itemResponse = this.updateItem(player, itemJoinModel, itemOperateEnum, itemPromp);
        this.pushProtocol(player, itemResponse);
        return itemResponse;
    }

    /**
     * 批量更新道具并推送客户端(推送协议UpdateItemListRs, 默认type=1(弹窗))并触发角色升级; 如果itemType是装备类型则新增单个装备
     * @param player 玩家信息
     * @param itemJoinModels 道具更新列表(每个ItemJoinModel的itemNum字段值传正数则增加, 负数减少)
     * @param itemOperateEnum 道具操作类型
     * @param itemPromp 弹出类型枚举
     */
    @Override
    public ItemResponse batchUpdateItemPush(Player player, List<ItemJoinModel> itemJoinModels, ItemOperateEnum itemOperateEnum, ItemPromp itemPromp)
    {
        ItemResponse itemResponse = this.batchUpdateItem(player, itemJoinModels, itemOperateEnum, itemPromp);
        this.pushProtocol(player, itemResponse);
        return itemResponse;
    }

    private void pushProtocol(Player player, ItemResponse itemResponse)
    {
        if (itemResponse.getCodeEnum().equals(ErrorCodeEnum.SUCCESS))
        {
            if (null != itemResponse.getUpdateItemListRs())
            {
                CmdUtil.sendMsg(player, itemResponse.getUpdateItemListRs());
            }
            if (null != itemResponse.getRoleUpgradeRs())
            {
                CmdUtil.sendMsg(player, itemResponse.getRoleUpgradeRs());
            }
            if (null != itemResponse.getUpdPlayerInfoRs())
            {
                CmdUtil.sendMsg(player, itemResponse.getUpdPlayerInfoRs());
            }
        }
        else
        {
            CmdUtil.sendErrorMsg(player.getSession(), new UpdateItemListRs().getCode(), itemResponse.getCodeEnum());
        }
    }

    /**
     * 背包扩展
     * @param player 玩家信息
     * @param itemExpandRq 扩展背包协议
     */
    @Override
    public void handleItemExpand(Player player, ItemExpandRq itemExpandRq)
    {
        ItemExpandRs itemExpandRs = new ItemExpandRs();
        PlayerInfoModel playerInfoModel = player.cache().getPlayerInfoModel();//拿到玩家信息
        if (null == playerInfoModel)
        {
            logger.error("玩家id: {}, 协议号: {}, 查询玩家信息为空!", player.getPlayerId(), itemExpandRq.getCode());
            CmdUtil.sendErrorMsg(player.getSession(), itemExpandRq.getCode(), ErrorCodeEnum.DATA_ERROR);
            return;
        }
        ConfigModel bagAddCostConfig = configDao.getConfigByKey(ConfigEnum.BAGADDCOST.getConfigType());//拿到玩家背包格子单价（钻石）
        ConfigModel bagexAddNumConfig = configDao.getConfigByKey(ConfigEnum.BAGEXADDNUM.getConfigType());//快速购买背包格子数量
        if (null == bagAddCostConfig || null == bagexAddNumConfig)
        {
            logger.error("玩家id:{}, 从config基础配置表查询\"玩家背包格子单价\"属性或\"快速购买背包格子数量\"属性为空!", player.getPlayerId());
            CmdUtil.sendErrorMsg(player.getSession(), itemExpandRq.getCode(), ErrorCodeEnum.CONFIG_ERROR);
            return;
        }
        PlayerItemCache playerItemCache = player.cache().getPlayerItemCache();//玩家背包
        if (null != playerItemCache)
        {
            int price = 0;//用于刷新消耗钻石任务
            //玩家背包格子单价（钻石）	[71000:10]   key-道具id, value-价格
            int[] arr = Arrays.stream(bagAddCostConfig.getValue().split(CommonConsts.STR_COLON)).mapToInt(Integer::parseInt).toArray();
            ItemModel dinItemModel = itemDao.getConfigByKey(arr[0]);//拿到道具基础配置表信息
            if (dinItemModel == null)
            {
                logger.error("玩家id:{}, 道具id:{}, 对应道具基础配置表数据为空!", player.getPlayerId(), arr[0]);
                CmdUtil.sendErrorMsg(player.getSession(), itemExpandRq.getCode(), ErrorCodeEnum.CONFIG_ERROR);
                return;
            }
            ItemJoinModel itemJoinModel = new ItemJoinModel();
            itemJoinModel.setItemId(arr[0]);
            itemJoinModel.setItemNum(-arr[1]);
            itemJoinModel.setItemType(dinItemModel.getItemType());
            price = price + arr[1];//累计待消耗的钻石
            ItemResponse response = this.updateItemPush(player, itemJoinModel, ItemOperateEnum.BACKPACK_EXTENSION, ItemPromp.GENERIC);
            if (!response.getCodeEnum().equals(ErrorCodeEnum.SUCCESS))
            {
                return;
            }
            //如果当前道具类型为货币类型且道具子类型为钻石并且数量小于0则触发
            if (dinItemModel.getItemType() == ItemTypeEnum.CURRENCY.getItemType() && dinItemModel.getItemTypeDetail() == ItemTypeEnum.CurrencyTypeEnum.RECHARGE_DIAMOND.getSubclassType())
            {
                /*
                 * 触发任务进度刷新
                 * 任务完成条件类型:
                 *      【激活期间】花费X钻石
                 *      【累计期间】花费X钻石
                 */
                TaskScheduleRefreshUtil.diamondSchedule(player, -price);
            }
            //添加当前玩家背包容量
            BigDecimal originalBig = new BigDecimal(playerInfoModel.getBackCapacity());
            BigDecimal currentBig = new BigDecimal(bagexAddNumConfig.getValue());
            playerInfoModel.setBackCapacity(originalBig.add(currentBig).intValue());
            //异步更新玩家基本信息
            player.cache().setPlayerInfoModel(playerInfoModel);
            playerInfoDao.queueUpdate(playerInfoModel);
            itemExpandRs.setStatus(true);
            CmdUtil.sendMsg(player, itemExpandRs);
            logger.debug("玩家id:{}, 协议号:{}, 背包扩展操作完成后返回:{}", player.getPlayerId(), itemExpandRq.getCode(), itemExpandRs.toString());
        }
    }

    /**
     * 过期道具
     * @param player 玩家信息
     * @param expiredItemRq 过期道具协议
     */
    @Override
    public void handleItemExpired(Player player, ItemExpiredRq expiredItemRq)
    {
        ItemExpiredItemRs itemExpiredItemRs = new ItemExpiredItemRs(true);
        LocalDateTime currentDateTime = LocalDateTime.now();//当前时间
        PlayerItemCache playerItemCache = player.cache().getPlayerItemCache();
        if (null != playerItemCache)
        {
            for (PlayerItemModel playerItemModel : playerItemCache.values())
            {
                //如果当前不是货币和经验类型则需要进行清理
                if(playerItemModel.getItemType() != ItemTypeEnum.CURRENCY.getItemType() && playerItemModel.getItemType() != ItemTypeEnum.EXPERIENCE.getItemType())
                {
                    //道具不为永久时判断是否到达结束时间
                    if (playerItemModel.getLiveType() != 1)
                    {
                        ItemModel itemModel = itemDao.getConfigByKey(playerItemModel.getItemId());
                        //道具持续开始时间并增加基础道具配置表里对应项的持续时间(秒)得到最终结束时间
                        LocalDateTime endDateTime = playerItemModel.getLiveTime().toInstant()
                                .atZone(ZoneId.systemDefault()).toLocalDateTime().plusSeconds(itemModel.getItemLiveTime() <= 0 ? 0 : itemModel.getItemLiveTime());
                        //结束时间在当前时间之前则表示道具以及过期
                        if (endDateTime.isBefore(currentDateTime))
                        {
                            //移除缓存及异步执行数据库删除这条记录
                            playerItemCache.updateCache(playerItemModel);
                            playerItemDao.queueDelete(playerItemModel);
                            logger.debug("玩家id:{}, 协议号:{}, 道具id:{}, 清除过期道具!", player.getPlayerId(), expiredItemRq.getCode(), playerItemModel.getItemId());
                        }
                    }
                }
            }
            //TODO  注意后续需加上处理过期装备

            CmdUtil.sendMsg(player, itemExpiredItemRs);
            logger.debug("玩家id:{}, 清理过期物品执行完毕!", player.getPlayerId());
        }
    }

    /**
     * 道具上锁
     * @param player 玩家信息
     * @param itemLockRq 物品上锁协议
     */
    @Override
    public void handleItemLock(Player player, ItemLockRq itemLockRq)
    {
        ItemLockRs itemLockRs = new ItemLockRs(itemLockRq.getId(), itemLockRq.getType(), true);
        if (itemLockRq.getId() == 0 || itemLockRq.getType() == 0)
        {
            logger.error("玩家id:{}, 道具上锁请求协议:{}, 参数错误!", player.getPlayerId(), itemLockRq.toString());
            CmdUtil.sendErrorMsg(player.getSession(), itemLockRq.getCode(), ErrorCodeEnum.CLIENT_PRAMS_ERROR);
            return;
        }
        boolean flag = true;
        //装备
        if (itemLockRq.getType() == 1)
        {
            EquipCache equipCache = player.cache().getPlayerEquipCache().getEquipCacheMap().get(itemLockRq.getId());
            if (null == equipCache)
            {
                logger.error("玩家id:{}, 装备id:{}, 当前装备不存在!", player.getPlayerId(), itemLockRq.getId());
                CmdUtil.sendErrorMsg(player.getSession(), itemLockRq.getCode(), ErrorCodeEnum.ITEM_NOT_EXIST);
                return;
            }
            PlayerEquipModel playerEquipModel = equipCache.getPlayerEquipModel();
            EquipBasicModel equipBasicModel = equipBasicDao.getConfigByKey(playerEquipModel.getEquipId());
            //如果equip_lock字段不为空并且等于2时才可以进行上锁操作
            if (null != equipBasicModel.getEquipLock() && equipBasicModel.getEquipLock() == 2)
            {
                if (itemLockRq.getIsLock() != 1)
                {
                    playerEquipModel.setEquipLock(2);//装备上锁
                }
                else
                {
                    playerEquipModel.setEquipLock(1);//装备解锁
                }
                equipCache.setPlayerEquipModel(playerEquipModel);
                playerEquipDao.queueUpdate(playerEquipModel);
            }
            else
            {
                flag = false;
            }
        }
        //道具
        else if (itemLockRq.getType() == 2)
        {
            PlayerItemModel playerItemModel = this.getItemInfo(player, (int) itemLockRq.getId());
            if (null == playerItemModel)
            {
                logger.error("玩家id:{}, 道具id:{}, 当前道具不存在!", player.getPlayerId(), itemLockRq.getId());
                CmdUtil.sendErrorMsg(player.getSession(), itemLockRq.getCode(), ErrorCodeEnum.ITEM_NOT_EXIST);
                return;
            }
            ItemModel itemModel = itemDao.getConfigByKey(playerItemModel.getItemId());
            //如果item_lock字段不为空并且等于2时才可以进行上锁操作
            if (null != itemModel.getItemLock() && itemModel.getItemLock() == 2)
            {
                if (itemLockRq.getIsLock() != 1)
                {
                    playerItemModel.setItemLock(2);//道具上锁
                }
                else
                {
                    playerItemModel.setItemLock(1);//道具解锁
                }
                player.cache().getPlayerItemCache().updateCache(playerItemModel);
                playerItemDao.queueUpdate(playerItemModel);
            }
            else
            {
                flag = false;
            }
        }

        if (flag)
        {
            CmdUtil.sendMsg(player, itemLockRs);
            logger.debug("玩家id:{}, 协议号:{}, 物品上锁操作完成后返回:{}", player.getPlayerId(), itemLockRq.getCode(), itemLockRs.toString());
        }
        else
        {
            logger.error("玩家id:{}, 协议号:{}, 物品请求协议: {} 当前道具不支持上锁！", player.getPlayerId(), itemLockRq.getCode(), itemLockRq.toString());
            CmdUtil.sendErrorMsg(player.getSession(), itemLockRq.getCode(), ErrorCodeEnum.ITEM_NOT_SUPPORT_LOCKED);
        }
    }

    /**
     * 背包操作
     * @param player 玩家信息
     * @param itemUniversalListRq 协议参数
     */
    @Override
    public void handleItemOperate(Player player, ItemUniversalListRq itemUniversalListRq)
    {
        itemOperateRealizeManage.execute(player, itemUniversalListRq);
    }

    /**
     * 单个道具协议
     * @param playerItemModel 玩家持有道具
     * @param itemModel 道具基础配置信息
     * @return ItemRs 道具基础协议
     */
    @Override
    public ItemRs itemRs(PlayerItemModel playerItemModel, ItemModel itemModel)
    {
        ItemRs itemRs = new ItemRs();;
        itemRs.setInstId(0L);
        itemRs.setItemId(playerItemModel.getItemId());
        itemRs.setItemType(playerItemModel.getItemType());
        itemRs.setItemNum(playerItemModel.getItemNum());
        //如果不是永久道具则计算结束时长给到客户端
        if (playerItemModel.getLiveType() != 1)
        {
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
        return itemRs;
    }

    /**
     * 角色升级(不入库、不生成RolesUpgrade协议且不刷新属性)
     * @param player 玩家信息
     * @param exp    经验值
     * @return ErrorCodeEnum key-错误码, value-玩家信息
     */
    @Override
    public Map<ErrorCodeEnum, PlayerInfoModel> rolesUpgrade(Player player, Integer exp)
    {
        Map<ErrorCodeEnum, PlayerInfoModel> rolesUpgradeMap = new HashMap<>();
        //玩家角色信息
        PlayerInfoModel playerInfoModel = player.cache().getPlayerInfoModel();
        Collection<LevelExperienceModel> collection = levelExperienceDao.getConfigs();
        if (null == collection || collection.isEmpty())
        {
            logger.error("玩家id:{}, 查询角色等级经验值基础配置表为空!", player.getPlayerId());
            rolesUpgradeMap.put(ErrorCodeEnum.CONFIG_ERROR, playerInfoModel);
            return rolesUpgradeMap;
        }
        ConfigModel configModel = configDao.getConfigByKey(ConfigEnum.LEVELLIMIT.getConfigType());//玩家等级限制
        if (null == configModel)
        {
            logger.error("玩家id:{}, 从config基础配置表查询\"玩家等级限制\"属性为空!", player.getPlayerId());
            rolesUpgradeMap.put(ErrorCodeEnum.CONFIG_ERROR, playerInfoModel);
            return rolesUpgradeMap;
        }
        //拿到最高等级对应的升级基础配置信息
        int maxLevel = collection.stream().map(LevelExperienceModel::getLevel).max(Integer::compareTo).get();
        LevelExperienceModel maxLevelModel = collection.stream().filter(e -> e.getLevel() == maxLevel).findFirst().get();
        //如果config配置表的玩家等级限制属性不等于level_experience配置表里最高等级
        if (!Integer.valueOf(configModel.getValue()).equals(maxLevelModel.getLevel()))
        {
            logger.error("玩家id:{}, config基础配置表的\"玩家等级限制\"属性与level_experience配置表最高等级不一致!", player.getPlayerId());
            rolesUpgradeMap.put(ErrorCodeEnum.CONFIG_ERROR, playerInfoModel);
            return rolesUpgradeMap;
        }
        //判断是否达到升级上限
        if (playerInfoModel.getLv() >= maxLevelModel.getLevel())
        {
            logger.error("玩家id:{}, 当前等级:{} 已达到上限!", player.getPlayerId(), playerInfoModel.getLv());
            rolesUpgradeMap.put(ErrorCodeEnum.ROLES_LEVEL_UPPER_LIMIT, playerInfoModel);
            return rolesUpgradeMap;
        }

        //计算经验值
        PlayerInfoModel clonePlayerInfo = playerInfoModel.copy(playerInfoModel);
        int resultExp = clonePlayerInfo.getExp() + exp;
        int tempExp = 0;
        //在经验值足够的情况下支持跨等级提升
        for (LevelExperienceModel levelExperienceModel : collection)
        {
            if (levelExperienceModel.getLevel() > clonePlayerInfo.getLv())
            {
                //因等级升级规则配置方式是错位的所以遍历当前的等级时则取上一个等级的经验值
                Optional<LevelExperienceModel> first = collection.stream().filter(e -> e.getLevel() == (levelExperienceModel.getLevel() - 1)).findFirst();
                if (!first.isPresent())
                {
                    logger.error("玩家id:{}, 角色等级经验配置表数据配置错误!", player.getPlayerId());
                    rolesUpgradeMap.put(ErrorCodeEnum.CONFIG_ERROR, clonePlayerInfo);
                    return rolesUpgradeMap;
                }
                Integer experience = first.get().getExperience();
                if (resultExp >= experience)
                {
                    clonePlayerInfo.setLv(levelExperienceModel.getLevel());
                    //如果达到最高等级则把经验值赋值为最高等级的经验值
                    if (clonePlayerInfo.getLv() == maxLevelModel.getLevel())
                    {
                        tempExp = maxLevelModel.getExperience();
                        break;
                    }
                    resultExp = resultExp - experience;//每次等级成功提升后传入的经验值减去配置每级需要提升的经验值作为消耗
                }
                else
                {
                    tempExp = resultExp;
                }
            }
        }
        clonePlayerInfo.setExp(tempExp);
        rolesUpgradeMap.put(ErrorCodeEnum.SUCCESS, clonePlayerInfo);
        return rolesUpgradeMap;
    }
}