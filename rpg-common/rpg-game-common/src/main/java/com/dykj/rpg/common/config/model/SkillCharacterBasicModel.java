package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

import java.util.List;

/**
 * @Description 技能基础
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/05/20
*/
public class SkillCharacterBasicModel extends BaseConfig<Integer>
{
    private Integer skillId;//技能ID
    private Integer skillLink;//关联技能
    private List<Integer> labelType;//标签
    private List<Integer> elementType;//元素类型
    private Integer skillType;//技能类型
    private String skillCondition;//技能释放条件
    private Integer skillLimitnum;//技能等级上限
    private Integer itemQualityType;//技能品质
    private Integer skillCdTime;//技能CD时间
    private Integer useTimes;//充能次数
    private Integer skillDistanceMin;//最小距离
    private Integer skillDistanceMax;//最大距离
    private Integer energyType;//消耗类型
    private Integer energyNum;//消耗量
    private Integer skillIndicator;//指示器类型
    private List<Integer> indicatorParm;//指示器参数
    private Integer singType;//释放类型
    private Integer singTime;//施法时间
    private Integer beforeAttackTime;//技能前摇时间
    private Integer animatorTime;//完整动作播放时间
    private Integer extraProp;//全局伤害加成修正
    private List<Integer> effectId;//效果ID
    private List<List<Integer>> effectIdReserve;//养成后效果ID
    private Integer exceptionProbability;//元素异常触发基础概率
    private Integer masterProbability;//精通触发基础概率
    private Integer createSoulEnergy;//技能生成魂能量值
    private Integer useSoulEnergy;//技能消耗魂能量值
    private Integer soulType;//灵魂类型
    private Integer soulAngleOut;//灵魂角度（外）
    private Integer soulAngleIn;//灵魂角度（内）
    private List<Integer> soulDistance;//灵魂之影的距离
    private List<Integer> runesType;//符文适配类型

    public Integer getSkillId()
    {
        return this.skillId;
    }

    public void setSkillId(Integer skillId)
    {
        this.skillId = skillId;
    }

    public Integer getSkillLink()
    {
        return this.skillLink;
    }

    public void setSkillLink(Integer skillLink)
    {
        this.skillLink = skillLink;
    }

    public List<Integer> getLabelType()
    {
        return this.labelType;
    }

    public void setLabelType(List<Integer> labelType)
    {
        this.labelType = labelType;
    }

    public List<Integer> getElementType()
    {
        return this.elementType;
    }

    public void setElementType(List<Integer> elementType)
    {
        this.elementType = elementType;
    }

    public Integer getSkillType()
    {
        return this.skillType;
    }

    public void setSkillType(Integer skillType)
    {
        this.skillType = skillType;
    }

    public String getSkillCondition()
    {
        return this.skillCondition;
    }

    public void setSkillCondition(String skillCondition)
    {
        this.skillCondition = skillCondition;
    }

    public Integer getSkillLimitnum()
    {
        return this.skillLimitnum;
    }

    public void setSkillLimitnum(Integer skillLimitnum)
    {
        this.skillLimitnum = skillLimitnum;
    }

    public Integer getItemQualityType()
    {
        return this.itemQualityType;
    }

    public void setItemQualityType(Integer itemQualityType)
    {
        this.itemQualityType = itemQualityType;
    }

    public Integer getSkillCdTime()
    {
        return this.skillCdTime;
    }

    public void setSkillCdTime(Integer skillCdTime)
    {
        this.skillCdTime = skillCdTime;
    }

    public Integer getUseTimes()
    {
        return this.useTimes;
    }

    public void setUseTimes(Integer useTimes)
    {
        this.useTimes = useTimes;
    }

    public Integer getSkillDistanceMin()
    {
        return this.skillDistanceMin;
    }

    public void setSkillDistanceMin(Integer skillDistanceMin)
    {
        this.skillDistanceMin = skillDistanceMin;
    }

    public Integer getSkillDistanceMax()
    {
        return this.skillDistanceMax;
    }

    public void setSkillDistanceMax(Integer skillDistanceMax)
    {
        this.skillDistanceMax = skillDistanceMax;
    }

    public Integer getEnergyType()
    {
        return this.energyType;
    }

    public void setEnergyType(Integer energyType)
    {
        this.energyType = energyType;
    }

    public Integer getEnergyNum()
    {
        return this.energyNum;
    }

    public void setEnergyNum(Integer energyNum)
    {
        this.energyNum = energyNum;
    }

    public Integer getSkillIndicator()
    {
        return this.skillIndicator;
    }

    public void setSkillIndicator(Integer skillIndicator)
    {
        this.skillIndicator = skillIndicator;
    }

    public List<Integer> getIndicatorParm()
    {
        return this.indicatorParm;
    }

    public void setIndicatorParm(List<Integer> indicatorParm)
    {
        this.indicatorParm = indicatorParm;
    }

    public Integer getSingType()
    {
        return this.singType;
    }

    public void setSingType(Integer singType)
    {
        this.singType = singType;
    }

    public Integer getSingTime()
    {
        return this.singTime;
    }

    public void setSingTime(Integer singTime)
    {
        this.singTime = singTime;
    }

    public Integer getBeforeAttackTime()
    {
        return this.beforeAttackTime;
    }

    public void setBeforeAttackTime(Integer beforeAttackTime)
    {
        this.beforeAttackTime = beforeAttackTime;
    }

    public Integer getAnimatorTime()
    {
        return this.animatorTime;
    }

    public void setAnimatorTime(Integer animatorTime)
    {
        this.animatorTime = animatorTime;
    }

    public Integer getExtraProp()
    {
        return this.extraProp;
    }

    public void setExtraProp(Integer extraProp)
    {
        this.extraProp = extraProp;
    }

    public List<Integer> getEffectId()
    {
        return this.effectId;
    }

    public void setEffectId(List<Integer> effectId)
    {
        this.effectId = effectId;
    }

    public List<List<Integer>> getEffectIdReserve()
    {
        return this.effectIdReserve;
    }

    public void setEffectIdReserve(List<List<Integer>> effectIdReserve)
    {
        this.effectIdReserve = effectIdReserve;
    }

    public Integer getExceptionProbability()
    {
        return this.exceptionProbability;
    }

    public void setExceptionProbability(Integer exceptionProbability)
    {
        this.exceptionProbability = exceptionProbability;
    }

    public Integer getMasterProbability()
    {
        return this.masterProbability;
    }

    public void setMasterProbability(Integer masterProbability)
    {
        this.masterProbability = masterProbability;
    }

    public Integer getCreateSoulEnergy()
    {
        return this.createSoulEnergy;
    }

    public void setCreateSoulEnergy(Integer createSoulEnergy)
    {
        this.createSoulEnergy = createSoulEnergy;
    }

    public Integer getUseSoulEnergy()
    {
        return this.useSoulEnergy;
    }

    public void setUseSoulEnergy(Integer useSoulEnergy)
    {
        this.useSoulEnergy = useSoulEnergy;
    }

    public Integer getSoulType()
    {
        return this.soulType;
    }

    public void setSoulType(Integer soulType)
    {
        this.soulType = soulType;
    }

    public Integer getSoulAngleOut()
    {
        return this.soulAngleOut;
    }

    public void setSoulAngleOut(Integer soulAngleOut)
    {
        this.soulAngleOut = soulAngleOut;
    }

    public Integer getSoulAngleIn()
    {
        return this.soulAngleIn;
    }

    public void setSoulAngleIn(Integer soulAngleIn)
    {
        this.soulAngleIn = soulAngleIn;
    }

    public List<Integer> getRunesType()
    {
        return this.runesType;
    }

    public void setRunesType(List<Integer> runesType)
    {
        this.runesType = runesType;
    }

    @Override
    public Integer getKey()
    {
        return this.skillId;
    }

    public List<Integer> getSoulDistance() {
        return soulDistance;
    }

    public void setSoulDistance(List<Integer> soulDistance) {
        this.soulDistance = soulDistance;
    }
}