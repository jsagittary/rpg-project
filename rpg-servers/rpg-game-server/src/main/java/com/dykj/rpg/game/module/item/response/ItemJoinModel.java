package com.dykj.rpg.game.module.item.response;

import java.util.ArrayList;
import java.util.List;

import com.dykj.rpg.common.consts.CalculationEnum;
import com.dykj.rpg.protocol.item.ItemRs;
import com.dykj.rpg.util.random.BaseBoxItem;

/**
 * @Description
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/22
 */
public class ItemJoinModel extends BaseBoxItem {
    private Integer itemId;//道具id
    private Integer itemNum;//道具数量
    private Integer itemType;//道具类型

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getItemNum() {
        return itemNum;
    }

    public void setItemNum(Integer itemNum) {
        this.itemNum = itemNum;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public ItemJoinModel(Integer itemId, Integer itemNum, Integer itemType, int probability) {
        super(itemId, probability);
        this.itemId = itemId;
        this.itemNum = itemNum;
        this.itemType = itemType;
    }

    public ItemJoinModel() {
    }

    public ItemJoinModel(List<Integer> prams, CalculationEnum calculationEnum) {
        this.itemType = prams.get(0);
        if (calculationEnum.equals(CalculationEnum.ADDITION)) {
            this.itemNum = prams.get(2);
        } else {
            this.itemNum = -prams.get(2);
        }
        this.itemId = prams.get(1);


    }

    public ItemJoinModel(Integer itemId, Integer itemNum, Integer itemType) {
        this.itemId = itemId;
        this.itemNum = itemNum;
        this.itemType = itemType;
    }

    /**
     * @param itemStr type:id:num
     */
    public ItemJoinModel(String itemStr) {
        String item[] = itemStr.split("\\:");
        this.itemType = Integer.valueOf(item[0]);
        this.itemId = Integer.valueOf(item[1]);
        this.itemNum = Integer.valueOf(item[2]);
    }

    /**
     * @param itemStr type:id:num
     */
    public ItemJoinModel(String itemStr, CalculationEnum calculationEnum) {
        String item[] = itemStr.split("\\:");
        this.itemType = Integer.valueOf(item[0]);
        this.itemId = Integer.valueOf(item[1]);
        if (calculationEnum == CalculationEnum.ADDITION) {
            this.itemNum = Integer.valueOf(item[2]);
        } else {
            this.itemNum = -Integer.valueOf(item[2]);
        }

    }

    public ItemRs covertItemRs() {
        ItemRs itemRs = new ItemRs();
        itemRs.setItemId(itemId);
        itemRs.setItemType(itemType);
        itemRs.setItemNum(itemNum);
        return itemRs;
    }

    /**
     * type:id:num;type:id:num
     *
     * @param itemsStr
     * @return
     */
    public static List<ItemJoinModel> items(String itemsStr) {
        List<ItemJoinModel> result = new ArrayList<>();
        String[] itemStr = itemsStr.split("\\,");
        for (String item : itemStr) {
            result.add(new ItemJoinModel(item));
        }
        return result;
    }


    /**
     * type:id:num;type:id:num
     *
     * @param itemsStr
     * @return
     */
    public static List<ItemJoinModel> items(String itemsStr, CalculationEnum calculationEnum) {
        List<ItemJoinModel> result = new ArrayList<>();
        String[] itemStr = itemsStr.split("\\,");
        for (String item : itemStr) {
            result.add(new ItemJoinModel(item, calculationEnum));
        }
        return result;
    }


    public static List<ItemJoinModel> items(List<List<Integer>> prams, CalculationEnum calculationEnum) {
        List<ItemJoinModel> result = new ArrayList<>();
        prams.forEach((pram)-> result.add(new ItemJoinModel(pram,calculationEnum)));
        return result;
    }

    public ItemJoinModel(ItemRs itemRs) {
        this.itemId = itemRs.getItemId();
        this.itemNum = itemRs.getItemNum();
        this.itemType = itemRs.getItemType();
    }

    public ItemJoinModel copy() {
        ItemJoinModel itemJoinModel = new ItemJoinModel();
        itemJoinModel.setItemId(itemId);
        itemJoinModel.setItemNum(itemNum);
        itemJoinModel.setItemType(itemType);
        return itemJoinModel;
    }

    @Override
    public String toString() {
        return "ItemJoinModel{" + "itemId=" + itemId + ", itemNum=" + itemNum + ", itemType=" + itemType + '}';
    }
}