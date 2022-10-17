package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

import java.util.List;

/**
 * @Description 技能buff
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/15
*/
public class SkillBuffModel extends BaseConfig<Integer>
{
    private Integer buffId;//buff的id
    private String buffNameMyself;//buff名（自己看）
    private String buffDescMyself;//buff描述（自己看）
    private Integer buffVisibleType;//战斗界面显示方式
    private String buffNameTextid;//buff名textID
    private String buffDescTextid;//buff描述textID
    private Integer buffLevel;//buff等级
    private String buffIcon;//buff图标
    private Integer buffVfxMount;//buff特效挂点
    private String buffVfxPath;//buff特效路径
    private Integer buffActiveMethod;//生效条件
    private Integer buffActiveParam;//生效条件参数
    private Integer buffType;//buff类型
    private List<Integer> buffParamInt;//buff参数int数组
    private List<String> buffParamString;//buff参数字符串数组
    private Integer buffDuration;//buff持续时间毫秒
    private Integer buffEffectMethod;//生效方式
    private Integer buffEffectInterval;//离散方式生效间隔时间
    private Integer buffEffectCount;//总共触发次数
    private Integer buffAttributeEffect;//属性效果

    public Integer getBuffId()
    {
        return this.buffId;
    }

    public void setBuffId(Integer buffId)
    {
        this.buffId = buffId;
    }

    public String getBuffNameMyself()
    {
        return this.buffNameMyself;
    }

    public void setBuffNameMyself(String buffNameMyself)
    {
        this.buffNameMyself = buffNameMyself;
    }

    public String getBuffDescMyself()
    {
        return this.buffDescMyself;
    }

    public void setBuffDescMyself(String buffDescMyself)
    {
        this.buffDescMyself = buffDescMyself;
    }

    public Integer getBuffVisibleType()
    {
        return this.buffVisibleType;
    }

    public void setBuffVisibleType(Integer buffVisibleType)
    {
        this.buffVisibleType = buffVisibleType;
    }

    public String getBuffNameTextid()
    {
        return this.buffNameTextid;
    }

    public void setBuffNameTextid(String buffNameTextid)
    {
        this.buffNameTextid = buffNameTextid;
    }

    public String getBuffDescTextid()
    {
        return this.buffDescTextid;
    }

    public void setBuffDescTextid(String buffDescTextid)
    {
        this.buffDescTextid = buffDescTextid;
    }

    public Integer getBuffLevel()
    {
        return this.buffLevel;
    }

    public void setBuffLevel(Integer buffLevel)
    {
        this.buffLevel = buffLevel;
    }

    public String getBuffIcon()
    {
        return this.buffIcon;
    }

    public void setBuffIcon(String buffIcon)
    {
        this.buffIcon = buffIcon;
    }

    public Integer getBuffVfxMount()
    {
        return this.buffVfxMount;
    }

    public void setBuffVfxMount(Integer buffVfxMount)
    {
        this.buffVfxMount = buffVfxMount;
    }

    public String getBuffVfxPath()
    {
        return this.buffVfxPath;
    }

    public void setBuffVfxPath(String buffVfxPath)
    {
        this.buffVfxPath = buffVfxPath;
    }

    public Integer getBuffActiveMethod()
    {
        return this.buffActiveMethod;
    }

    public void setBuffActiveMethod(Integer buffActiveMethod)
    {
        this.buffActiveMethod = buffActiveMethod;
    }

    public Integer getBuffActiveParam()
    {
        return this.buffActiveParam;
    }

    public void setBuffActiveParam(Integer buffActiveParam)
    {
        this.buffActiveParam = buffActiveParam;
    }

    public Integer getBuffType()
    {
        return this.buffType;
    }

    public void setBuffType(Integer buffType)
    {
        this.buffType = buffType;
    }

    public List<Integer> getBuffParamInt()
    {
        return this.buffParamInt;
    }

    public void setBuffParamInt(List<Integer> buffParamInt)
    {
        this.buffParamInt = buffParamInt;
    }

    public List<String> getBuffParamString()
    {
        return this.buffParamString;
    }

    public void setBuffParamString(List<String> buffParamString)
    {
        this.buffParamString = buffParamString;
    }

    public Integer getBuffDuration()
    {
        return this.buffDuration;
    }

    public void setBuffDuration(Integer buffDuration)
    {
        this.buffDuration = buffDuration;
    }

    public Integer getBuffEffectMethod()
    {
        return this.buffEffectMethod;
    }

    public void setBuffEffectMethod(Integer buffEffectMethod)
    {
        this.buffEffectMethod = buffEffectMethod;
    }

    public Integer getBuffEffectInterval()
    {
        return this.buffEffectInterval;
    }

    public void setBuffEffectInterval(Integer buffEffectInterval)
    {
        this.buffEffectInterval = buffEffectInterval;
    }

    public Integer getBuffEffectCount()
    {
        return this.buffEffectCount;
    }

    public void setBuffEffectCount(Integer buffEffectCount)
    {
        this.buffEffectCount = buffEffectCount;
    }

    public Integer getBuffAttributeEffect()
    {
        return this.buffAttributeEffect;
    }

    public void setBuffAttributeEffect(Integer buffAttributeEffect)
    {
        this.buffAttributeEffect = buffAttributeEffect;
    }

    @Override
    public Integer getKey()
    {
        return this.buffId;
    }
}