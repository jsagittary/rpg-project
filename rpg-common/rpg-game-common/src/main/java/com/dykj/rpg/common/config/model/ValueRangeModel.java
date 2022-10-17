
package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;
import com.dykj.rpg.util.random.BaseBoxItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 装备词条值域
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/24
*/
public class ValueRangeModel extends BaseConfig<Integer>
{
    private Integer equipEntryValueRangeId;//装备词条值域id
    private Integer entryNumRangeType;//属性值域类型
    private List<String> entryNumRangeRate;//属性值域权重
    private Integer reduceValue;//缩小倍数
    private Integer skillReduceValue;//技能参数缩小参数

    public Integer getEquipEntryValueRangeId()
    {
        return this.equipEntryValueRangeId;
    }

    public void setEquipEntryValueRangeId(Integer equipEntryValueRangeId)
    {
        this.equipEntryValueRangeId = equipEntryValueRangeId;
    }

    public Integer getEntryNumRangeType()
    {
        return this.entryNumRangeType;
    }

    public void setEntryNumRangeType(Integer entryNumRangeType)
    {
        this.entryNumRangeType = entryNumRangeType;
    }

    public List<String> getEntryNumRangeRate()
    {
        return this.entryNumRangeRate;
    }

    public void setEntryNumRangeRate(List<String> entryNumRangeRate)
    {
        this.entryNumRangeRate = entryNumRangeRate;
    }

    public Integer getReduceValue()
    {
        return this.reduceValue;
    }

    public void setReduceValue(Integer reduceValue)
    {
        this.reduceValue = reduceValue;
    }

    public Integer getSkillReduceValue()
    {
        return this.skillReduceValue;
    }

    public void setSkillReduceValue(Integer skillReduceValue)
    {
        this.skillReduceValue = skillReduceValue;
    }

    @Override
    public Integer getKey()
    {
        return this.equipEntryValueRangeId;
    }

    public String getValueRangeRate(int index) {
        return entryNumRangeRate.get(index);
    }

    public List<BaseBoxItem> getValueRangeItem() {
        List<BaseBoxItem> baseBoxItems = new ArrayList<>();
        int i = 0;
        for (String rate : entryNumRangeRate) {
            String[] result = rate.split("\\:");
            //100:150:800 index =2 为权重
            BaseBoxItem baseBoxItem = new BaseBoxItem(i, Integer.valueOf(result[2]));
            baseBoxItems.add(baseBoxItem);
            i++;
        }
        return baseBoxItems;
    }
}