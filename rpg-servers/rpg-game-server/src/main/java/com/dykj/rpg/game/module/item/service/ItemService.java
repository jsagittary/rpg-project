package com.dykj.rpg.game.module.item.service;

import com.dykj.rpg.common.config.model.ItemModel;
import com.dykj.rpg.common.consts.ItemOperateEnum;
import com.dykj.rpg.common.consts.ItemType;
import com.dykj.rpg.common.consts.ItemTypeEnum;
import com.dykj.rpg.common.data.model.PlayerInfoModel;
import com.dykj.rpg.common.data.model.PlayerItemModel;
import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.module.item.consts.ItemPromp;
import com.dykj.rpg.game.module.item.response.ItemJoinModel;
import com.dykj.rpg.game.module.item.response.ItemResponse;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.protocol.item.*;

import java.util.*;
/**
 * @Description 背包service
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/21
 */
public interface ItemService
{
    /**
     * 获取指定道具信息
     * @param player 玩家信息
     * @param itemId 物品id
     * @return 物品信息
     */
    public PlayerItemModel getItemInfo(Player player, Integer itemId);

    /**
     * 判断指定道具数量是否足够
     * @param player 玩家信息
     * @param itemId 道具id
     * @param itemNum 道具数量
     * @return true-是 false-否
     */
    public boolean isQuantityEnough(Player player, Integer itemId, Integer itemNum);

    /**
     * 获取指定货币类型的总数量
     * @param player 玩家信息
     * @param currencyType 货币子类
     * @return 具体货币类型的总数量
     */
    public int getCurrencyQuantity(Player player, ItemTypeEnum.CurrencyTypeEnum currencyType);

    /**
     * 获取指定大类道具列表
     * @param player 玩家信息
     * @param itemTypeEnum 道具大类枚举
     * @return 道具协议列表
     */
    public List<ItemRs> getSpecifyItems(Player player, ItemTypeEnum itemTypeEnum);

    /**
     * 获取指定道具子类型列表
     * @param player 玩家信息
     * @param itemTypeEnum 道具大类枚举
     * @param itemType 道具子类枚举
     * @return 道具协议列表
     */
    public List<ItemRs> getChildItems(Player player, ItemTypeEnum itemTypeEnum, ItemType itemType);

    /**
     * 更新道具(不入库不生成协议;如果返回的错误码为ErrorCodeEnum.ITEM_NUM_DAILY_UPPERLIMIT, 表示道具添加已达到上限则需要手动组装进行邮件发送)
     * @param player 玩家信息
     * @param itemJoinModel 道具更新对象(itemNum道具数量, 整数增加, 负数减少))
     * @param itemModel 当前道具的基础配置信息
     * @return key-错误码 value-道具信息
     */
    public Map<ErrorCodeEnum, PlayerItemModel> updateItem(Player player, ItemJoinModel itemJoinModel, ItemModel itemModel);

    /**
     * 更新道具(入库并生成协议UpdateItemListRs并触发角色升t级)
     * 如果itemType是装备类型则新增单个装备
     * @param player 玩家信息
     * @param itemJoinModel 道具更新对象(itemNum道具数量, 整数增加, 负数减少))
     * @param itemOperateEnum 道具操作类型
     * @param itemPromp 弹出类型枚举(0-不弹窗, 1-弹窗)
     * @return 道具处理响应包装类
     */
    public ItemResponse updateItem(Player player, ItemJoinModel itemJoinModel, ItemOperateEnum itemOperateEnum, ItemPromp itemPromp);

    /**
     * 批量更新道具(入库并生成协议UpdateItemListRs并触发角色升级; 如果itemType是装备类型则新增单个装备)
     * @param player 玩家信息
     * @param itemJoinModels 道具更新列表(每个ItemJoinModel的itemNum字段值传正数则增加, 负数减少)
     * @param itemOperateEnum 道具操作类型
     * @param itemPromp 弹出类型枚举(0-不弹窗, 1-弹窗)
     * @return 道具处理响应包装类
     */
    public ItemResponse batchUpdateItem(Player player, List<ItemJoinModel> itemJoinModels, ItemOperateEnum itemOperateEnum, ItemPromp itemPromp);

    /**
     * 更新道具并推送客户端(推送协议UpdateItemListRs并触发角色升级)
     * 如果itemType是装备类型则新增单个装备
     * @param player 玩家信息
     * @param itemJoinModel 道具更新对象(itemNum道具数量, 整数增加, 负数减少))
     * @param itemOperateEnum 道具操作类型
     * @param itemPromp 弹出类型枚举
     */
    public ItemResponse updateItemPush(Player player, ItemJoinModel itemJoinModel, ItemOperateEnum itemOperateEnum, ItemPromp itemPromp);

    /**
     * 批量更新道具并推送客户端(推送协议UpdateItemListRs, 默认type=1(弹窗))并触发角色升级; 如果itemType是装备类型则新增单个装备
     * @param player 玩家信息
     * @param itemJoinModels 道具更新列表(每个ItemJoinModel的itemNum字段值传正数则增加, 负数减少)
     * @param itemOperateEnum 道具操作类型
     * @param itemPromp 弹出类型枚举
     */
    public ItemResponse batchUpdateItemPush(Player player, List<ItemJoinModel> itemJoinModels, ItemOperateEnum itemOperateEnum, ItemPromp itemPromp);

    /**
     * 背包扩展
     * @param player 玩家信息
     * @param itemExpandRq 扩展背包协议
     */
    public void handleItemExpand(Player player, ItemExpandRq itemExpandRq);

    /**
     * 过期道具
     * @param player 玩家信息
     * @param expiredItemRq 过期道具协议
     */
    public void handleItemExpired(Player player, ItemExpiredRq expiredItemRq);

    /**
     * 道具上锁
     * @param player 玩家信息
     * @param itemLockRq 物品上锁协议
     */
    public void handleItemLock(Player player, ItemLockRq itemLockRq);

    /**
     * 背包操作
     * @param player 玩家信息
     * @param itemUniversalListRq 协议参数
     */
    public void handleItemOperate(Player player, ItemUniversalListRq itemUniversalListRq);

    /**
     * 组装单个道具协议
     * @param playerItemModel 玩家持有道具
     * @param itemModel 道具基础配置信息
     * @return ItemRs 道具基础协议
     */
    public ItemRs itemRs(PlayerItemModel playerItemModel, ItemModel itemModel);

    /**
     * 角色升级(不入库、不生成RolesUpgrade协议且不刷新属性)
     * @param player 玩家信息
     * @param exp    经验值
     * @return ErrorCodeEnum key-错误码, value-玩家信息
     */
    public Map<ErrorCodeEnum, PlayerInfoModel> rolesUpgrade(Player player, Integer exp);
}