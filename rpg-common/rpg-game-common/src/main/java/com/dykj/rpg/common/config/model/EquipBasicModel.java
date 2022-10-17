package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;
import com.dykj.rpg.util.random.BaseBoxItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description 装备基础
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/27
*/
public class EquipBasicModel extends BaseConfig<Integer>
{
    private Integer equipId;//装备编号
    private List<Integer> equipCharacterId;//装备适合职业
    private Integer equipLevel;//装备等级
    private List<List<Integer>> equipWearNeedAttribute;//装备穿戴需求属性
    private Integer equipMainType;//装备大类型
    private Integer equipWeaponType;//武器类型
    private Integer equipArmorType;//防具护甲类型
    private Integer equipPartType;//装备部位类型
    private String equipPartDetailType;//装备部位下细分类型
    private Integer itemQualityType;//装备品质类型
    private Integer equipEntryId;//装备词条生成数量
    private List<Integer> equipNecessaryEntry;//装备对应的必出效果词条
    private List<String> equipEntryEffectBoxIdGroup;//装备对应的词条效果库组
    private Integer equipMainattribute;//装备主属性
    private Integer equipLock;//道具是否能上锁

    public Integer getEquipId()
    {
        return this.equipId;
    }

    public void setEquipId(Integer equipId)
    {
        this.equipId = equipId;
    }

    public List<Integer> getEquipCharacterId()
    {
        return this.equipCharacterId;
    }

    public void setEquipCharacterId(List<Integer> equipCharacterId)
    {
        this.equipCharacterId = equipCharacterId;
    }

    public Integer getEquipLevel()
    {
        return this.equipLevel;
    }

    public void setEquipLevel(Integer equipLevel)
    {
        this.equipLevel = equipLevel;
    }

    public List<List<Integer>> getEquipWearNeedAttribute()
    {
        return this.equipWearNeedAttribute;
    }

    public void setEquipWearNeedAttribute(List<List<Integer>> equipWearNeedAttribute)
    {
        this.equipWearNeedAttribute = equipWearNeedAttribute;
    }

    public Integer getEquipMainType()
    {
        return this.equipMainType;
    }

    public void setEquipMainType(Integer equipMainType)
    {
        this.equipMainType = equipMainType;
    }

    public Integer getEquipWeaponType()
    {
        return this.equipWeaponType;
    }

    public void setEquipWeaponType(Integer equipWeaponType)
    {
        this.equipWeaponType = equipWeaponType;
    }

    public Integer getEquipArmorType()
    {
        return this.equipArmorType;
    }

    public void setEquipArmorType(Integer equipArmorType)
    {
        this.equipArmorType = equipArmorType;
    }

    public Integer getEquipPartType()
    {
        return this.equipPartType;
    }

    public void setEquipPartType(Integer equipPartType)
    {
        this.equipPartType = equipPartType;
    }

    public String getEquipPartDetailType()
    {
        return this.equipPartDetailType;
    }

    public void setEquipPartDetailType(String equipPartDetailType)
    {
        this.equipPartDetailType = equipPartDetailType;
    }

    public Integer getItemQualityType()
    {
        return this.itemQualityType;
    }

    public void setItemQualityType(Integer itemQualityType)
    {
        this.itemQualityType = itemQualityType;
    }

    public Integer getEquipEntryId()
    {
        return this.equipEntryId;
    }

    public void setEquipEntryId(Integer equipEntryId)
    {
        this.equipEntryId = equipEntryId;
    }

    public List<Integer> getEquipNecessaryEntry()
    {
        return this.equipNecessaryEntry;
    }

    public void setEquipNecessaryEntry(List<Integer> equipNecessaryEntry)
    {
        this.equipNecessaryEntry = equipNecessaryEntry;
    }

    public List<String> getEquipEntryEffectBoxIdGroup()
    {
        return this.equipEntryEffectBoxIdGroup;
    }

    public void setEquipEntryEffectBoxIdGroup(List<String> equipEntryEffectBoxIdGroup)
    {
        this.equipEntryEffectBoxIdGroup = equipEntryEffectBoxIdGroup;
    }

    public Integer getEquipMainattribute()
    {
        return this.equipMainattribute;
    }

    public void setEquipMainattribute(Integer equipMainattribute)
    {
        this.equipMainattribute = equipMainattribute;
    }

    public Integer getEquipLock()
    {
        return this.equipLock;
    }

    public void setEquipLock(Integer equipLock)
    {
        this.equipLock = equipLock;
    }

    @Override
    public Integer getKey()
    {
        return this.equipId;
    }

    public List<BaseBoxItem> getEquipEntryEffectBox() {
        List<BaseBoxItem> baseBoxItems = new ArrayList<>();
        for (String rate : equipEntryEffectBoxIdGroup) {
            String[] result = rate.split("\\:");
            //101:2:200  id:最大次数:权重
            BaseBoxItem baseBoxItem = new BaseBoxItem(Integer.valueOf(result[0]),Integer.valueOf(result[1]), Integer.valueOf(result[2]));
            baseBoxItems.add(baseBoxItem);
        }
        return baseBoxItems;
    }
}