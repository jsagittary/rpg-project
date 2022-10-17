package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;
import com.dykj.rpg.util.random.BaseBoxItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description 装备词条数量
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/18
*/
public class EqEntryNumModel extends BaseConfig<Integer>
{
    private Integer equipEntryId;//自定义id
    private Map<Integer, Integer> entryFinallyNumber;//对应品质下，实际词条数量出现的权重

    public Integer getEquipEntryId()
    {
        return this.equipEntryId;
    }

    public void setEquipEntryId(Integer equipEntryId)
    {
        this.equipEntryId = equipEntryId;
    }

    public Map<Integer, Integer> getEntryFinallyNumber()
    {
        return this.entryFinallyNumber;
    }

    public void setEntryFinallyNumber(Map<Integer, Integer> entryFinallyNumber)
    {
        this.entryFinallyNumber = entryFinallyNumber;
    }

    @Override
    public Integer getKey()
    {
        return this.equipEntryId;
    }


    public List<BaseBoxItem> getEqEntryNumItem() {
        List<BaseBoxItem> baseBoxItems = new ArrayList<>();
        for (Map.Entry<Integer,Integer> entry : entryFinallyNumber.entrySet()) {
            //id:权重 index =1 为权重
            BaseBoxItem baseBoxItem = new BaseBoxItem(entry.getKey(), entry.getValue());
            baseBoxItems.add(baseBoxItem);
        }
        return baseBoxItems;
    }
}