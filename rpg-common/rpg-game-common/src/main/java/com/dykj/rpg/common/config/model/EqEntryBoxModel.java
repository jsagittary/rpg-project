package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;
import com.dykj.rpg.util.random.BaseBoxItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 装备词条类型库
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/27
*/
public class EqEntryBoxModel extends BaseConfig<Integer>
{
    private Integer id;//自定义id
    private Integer equipEntryEffectBoxId;//装备词条效果库id
    private List<Integer> equipCharacterId;//当前装备对应的职业id
    private List<String> equipEntryBoxTypeRate;//词条库内词条id之间的随机权重

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getEquipEntryEffectBoxId()
    {
        return this.equipEntryEffectBoxId;
    }

    public void setEquipEntryEffectBoxId(Integer equipEntryEffectBoxId)
    {
        this.equipEntryEffectBoxId = equipEntryEffectBoxId;
    }

    public List<Integer> getEquipCharacterId()
    {
        return this.equipCharacterId;
    }

    public void setEquipCharacterId(List<Integer> equipCharacterId)
    {
        this.equipCharacterId = equipCharacterId;
    }

    public List<String> getEquipEntryBoxTypeRate()
    {
        return this.equipEntryBoxTypeRate;
    }

    public void setEquipEntryBoxTypeRate(List<String> equipEntryBoxTypeRate)
    {
        this.equipEntryBoxTypeRate = equipEntryBoxTypeRate;
    }

    @Override
    public Integer getKey()
    {
        return this.id;
    }


    public List<BaseBoxItem> getEqEntryBoxItem() {
        List<BaseBoxItem> baseBoxItems = new ArrayList<>();
        for (String rate : equipEntryBoxTypeRate) {
            String[] result = rate.split("\\:");
            //id:权重 index =1 为权重
            BaseBoxItem baseBoxItem = new BaseBoxItem(Integer.valueOf(result[0]), Integer.valueOf(result[1]));
            baseBoxItems.add(baseBoxItem);
        }
        return baseBoxItems;
    }
}