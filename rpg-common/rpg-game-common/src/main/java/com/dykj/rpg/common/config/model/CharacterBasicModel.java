package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

import java.util.List;
import java.util.Map;

/**
 * @Description 主角定义
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/02/04
*/
public class CharacterBasicModel extends BaseConfig<Integer>
{
    private Integer characterId;//职业编号
    private List<String> initializeInitialequip;//初始化展示装备
    private Map<Integer, Integer> initializeManualSkills;//初始化主动（灵魂）技能
    private List<String> initializeAttributes;//初始化属性
    private Integer energyType;//战斗资源类型
    private Integer growClass;//属性职业
    private Integer creatOpen;//开放选择
    private List<List<Integer>> initializeBag;//初始背包物品
    private Integer collisionRange;//碰撞范围
    private Integer hitTimesStun;//产生硬直的受击次数
    private Integer stunTime;//硬直时长
    private Integer soulSkin; //灵魂之影默认皮肤

    public Integer getCharacterId()
    {
        return this.characterId;
    }

    public void setCharacterId(Integer characterId)
    {
        this.characterId = characterId;
    }

    public List<String> getInitializeInitialequip()
    {
        return this.initializeInitialequip;
    }

    public void setInitializeInitialequip(List<String> initializeInitialequip)
    {
        this.initializeInitialequip = initializeInitialequip;
    }

    public Map<Integer, Integer> getInitializeManualSkills()
    {
        return this.initializeManualSkills;
    }

    public void setInitializeManualSkills(Map<Integer, Integer> initializeManualSkills)
    {
        this.initializeManualSkills = initializeManualSkills;
    }

    public List<String> getInitializeAttributes()
    {
        return this.initializeAttributes;
    }

    public void setInitializeAttributes(List<String> initializeAttributes)
    {
        this.initializeAttributes = initializeAttributes;
    }

    public Integer getEnergyType()
    {
        return this.energyType;
    }

    public void setEnergyType(Integer energyType)
    {
        this.energyType = energyType;
    }

    public Integer getGrowClass()
    {
        return this.growClass;
    }

    public void setGrowClass(Integer growClass)
    {
        this.growClass = growClass;
    }

    public Integer getCreatOpen()
    {
        return this.creatOpen;
    }

    public void setCreatOpen(Integer creatOpen)
    {
        this.creatOpen = creatOpen;
    }

    public List<List<Integer>> getInitializeBag()
    {
        return this.initializeBag;
    }

    public void setInitializeBag(List<List<Integer>> initializeBag)
    {
        this.initializeBag = initializeBag;
    }

    public Integer getCollisionRange()
    {
        return this.collisionRange;
    }

    public void setCollisionRange(Integer collisionRange)
    {
        this.collisionRange = collisionRange;
    }

    public Integer getHitTimesStun()
    {
        return this.hitTimesStun;
    }

    public void setHitTimesStun(Integer hitTimesStun)
    {
        this.hitTimesStun = hitTimesStun;
    }

    public Integer getStunTime()
    {
        return this.stunTime;
    }

    public void setStunTime(Integer stunTime)
    {
        this.stunTime = stunTime;
    }

    public Integer getSoulSkin() {
        return soulSkin;
    }

    public void setSoulSkin(Integer soulSkin) {
        this.soulSkin = soulSkin;
    }

    @Override
    public Integer getKey()
    {
        return this.characterId;
    }
}