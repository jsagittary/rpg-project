package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

/**
 * @Description 道具基础表
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/02/19
 */
public class ItemModel extends BaseConfig<Integer>
{
    private Integer itemId;//道具的id
    private Integer itemType;//道具的类型
    private Integer displayName;//道具名称
    private Integer itemTypeDetail;//道具子类型
    private Integer itemQualityType;//道具品质
    private Integer displayDesc;//道具描述
    private Integer itemCanPile;//道具是否可堆叠
    private Integer itemOnePileNum;//道具单堆上限数
    private Integer itemMaxNum;//道具总数量上限
    private Integer itemGetMaxDay;//道具每日获取上限数量
    private Integer itemCanSell;//道具是否可出售
    private Integer itemSellItem;//道具出售货币ID
    private Integer itemSellCurrencyPrice;//道具出售价格
    private String itemUseResult;//道具使用效果
    private String itemChestContent;//宝箱内容显示
    private Integer itemLiveType;//道具时限类型
    private Integer itemLiveTime;//道具持续时间
    private Integer itemLock;//道具是否能上锁
    private String itemTipsbutton;//道具tips信息的功能按钮

    public Integer getItemId()
    {
        return this.itemId;
    }

    public void setItemId(Integer itemId)
    {
        this.itemId = itemId;
    }

    public Integer getItemType()
    {
        return this.itemType;
    }

    public void setItemType(Integer itemType)
    {
        this.itemType = itemType;
    }

    public Integer getDisplayName()
    {
        return this.displayName;
    }

    public void setDisplayName(Integer displayName)
    {
        this.displayName = displayName;
    }

    public Integer getItemTypeDetail()
    {
        return this.itemTypeDetail;
    }

    public void setItemTypeDetail(Integer itemTypeDetail)
    {
        this.itemTypeDetail = itemTypeDetail;
    }

    public Integer getItemQualityType()
    {
        return this.itemQualityType;
    }

    public void setItemQualityType(Integer itemQualityType)
    {
        this.itemQualityType = itemQualityType;
    }

    public Integer getDisplayDesc()
    {
        return this.displayDesc;
    }

    public void setDisplayDesc(Integer displayDesc)
    {
        this.displayDesc = displayDesc;
    }

    public Integer getItemCanPile()
    {
        return this.itemCanPile;
    }

    public void setItemCanPile(Integer itemCanPile)
    {
        this.itemCanPile = itemCanPile;
    }

    public Integer getItemOnePileNum()
    {
        return this.itemOnePileNum;
    }

    public void setItemOnePileNum(Integer itemOnePileNum)
    {
        this.itemOnePileNum = itemOnePileNum;
    }

    public Integer getItemMaxNum()
    {
        return this.itemMaxNum;
    }

    public void setItemMaxNum(Integer itemMaxNum)
    {
        this.itemMaxNum = itemMaxNum;
    }

    public Integer getItemGetMaxDay()
    {
        return this.itemGetMaxDay;
    }

    public void setItemGetMaxDay(Integer itemGetMaxDay)
    {
        this.itemGetMaxDay = itemGetMaxDay;
    }

    public Integer getItemCanSell()
    {
        return this.itemCanSell;
    }

    public void setItemCanSell(Integer itemCanSell)
    {
        this.itemCanSell = itemCanSell;
    }

    public Integer getItemSellItem()
    {
        return this.itemSellItem;
    }

    public void setItemSellItem(Integer itemSellItem)
    {
        this.itemSellItem = itemSellItem;
    }

    public Integer getItemSellCurrencyPrice()
    {
        return this.itemSellCurrencyPrice;
    }

    public void setItemSellCurrencyPrice(Integer itemSellCurrencyPrice)
    {
        this.itemSellCurrencyPrice = itemSellCurrencyPrice;
    }

    public String getItemUseResult()
    {
        return this.itemUseResult;
    }

    public void setItemUseResult(String itemUseResult)
    {
        this.itemUseResult = itemUseResult;
    }

    public String getItemChestContent()
    {
        return this.itemChestContent;
    }

    public void setItemChestContent(String itemChestContent)
    {
        this.itemChestContent = itemChestContent;
    }

    public Integer getItemLiveType()
    {
        return this.itemLiveType;
    }

    public void setItemLiveType(Integer itemLiveType)
    {
        this.itemLiveType = itemLiveType;
    }

    public Integer getItemLiveTime()
    {
        return this.itemLiveTime;
    }

    public void setItemLiveTime(Integer itemLiveTime)
    {
        this.itemLiveTime = itemLiveTime;
    }

    public Integer getItemLock()
    {
        return this.itemLock;
    }

    public void setItemLock(Integer itemLock)
    {
        this.itemLock = itemLock;
    }

    public String getItemTipsbutton()
    {
        return this.itemTipsbutton;
    }

    public void setItemTipsbutton(String itemTipsbutton)
    {
        this.itemTipsbutton = itemTipsbutton;
    }

    @Override
    public Integer getKey()
    {
        return this.itemId;
    }
}