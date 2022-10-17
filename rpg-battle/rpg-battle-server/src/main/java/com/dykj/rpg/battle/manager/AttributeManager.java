package com.dykj.rpg.battle.manager;

import com.dykj.rpg.battle.attribute.AttributeConfig;
import com.dykj.rpg.battle.attribute.RoleAttributes;
import com.dykj.rpg.battle.basic.BasicSkillCarrier;
import com.dykj.rpg.battle.constant.GameConstant;
import com.dykj.rpg.battle.logic.SkillSourceData;
import com.dykj.rpg.battle.role.BattleRole;
import com.dykj.rpg.common.attribute.consts.AttributeBasicConstant;
import com.dykj.rpg.common.attribute.consts.SkillSourceEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AttributeManager {

    Logger logger = LoggerFactory.getLogger("AttributeManager");

    private BattleRole battleRole;

    private RoleAttributes roleAttributes;
    /**
     * 最大血量值
     */
    public int maxBlood;
    /**
     * 生命值回复值
     */
    private int bloodCover;
    /**
     * 生命值回复延迟值
     */
    private int bloodDelay;
    /**
     * 生命值回复间隔值
     */
    private int bloodInterval;
    /**
     * 角色当前血量
     */
    public int curBlood;
    /**
     * 角色当前血量回复剩余时间
     */
    private int bloodCoverTime;

    /**
     * 最大护盾值
     */
    public int maxProtect;
    /**
     * 护盾值回复值
     */
    private int protectCover;
    /**
     * 护盾值回复延迟值
     */
    private int protectDelay;
    /**
     * 护盾值回复间隔值
     */
    private int protectInterval;
    /**
     * 当前护盾值
     */
    public int curProtect;
    /**
     * 护盾回复剩余时间
     */
    private int protectCoverTime;

    /**
     * 最大魂能量值
     */
    public int maxSoulEnergy;
    /**
     * 魂能量值回复值
     */
    private int soulEnergyCover;
    /**
     * 魂能量值回复延迟值
     */
    private int soulEnergyDelay;
    /**
     * 魂能量回复间隔值
     */
    private int soulEnergyInterval;
    /**
     * 当前魂能量值
     */
    public int curSoulEnergy;
    /**
     * 魂能量回复剩余时间
     */
    private int soulEnergyCoverTime;

    /**
     * 技能资源
     */
    private SkillSourceData[] skillSources = new SkillSourceData[10];

    /**
     * 碰撞盒大小
     */
    public float radius = 0.3f;

    /**
     * 视野范围
     */
    public float vision;

    /**
     * 最远攻击距离
     */
    public float maxAttackDistance = 0;
    /**
     * 最近攻击距离
     */
    public float minAttackDistance = 0;

    public AttributeManager(BattleRole battleRole){
        this.battleRole = battleRole;
    }

    public void init(RoleAttributes roleAttributes){
        this.roleAttributes = roleAttributes;
        initAttribute();
    }

    private void initAttribute(){
        maxBlood = roleAttributes.getZuiDaShengMingZhi();
        bloodCover = roleAttributes.getShengMingZhiHuiFuZhi();
        bloodDelay = roleAttributes.getShengMingZhiHuiFuYanChiZhi();
        bloodInterval = roleAttributes.getShengMingZhiHuiFuJianGeZhi();
        bloodCoverTime = bloodInterval;

        maxProtect = roleAttributes.getZuiDaHuDunZhi();
        protectCover = roleAttributes.getHuDunZhiHuiFuZhi();
        protectDelay = roleAttributes.getHuDunZhiHuiFuYanChiZhi();
        protectInterval = roleAttributes.getHuDunZhiHuiFuJianGeZhi();
        protectCoverTime = protectInterval;

        maxSoulEnergy = roleAttributes.getZuiDaHunNengLiangZhi();
        soulEnergyCover = roleAttributes.getHunNengLiangZhiHuiFuZhi();
        soulEnergyDelay = roleAttributes.getHunNengLiangZhiHuiFuYanChiZhi();
        soulEnergyInterval = roleAttributes.getHunNengLiangZhiHuiFuJianGeZhi();
        soulEnergyCoverTime = soulEnergyInterval;

        SkillSourceEnum[] skillSourceEnums = SkillSourceEnum.values();
        for(SkillSourceEnum skillSourceEnum : skillSourceEnums){
            SkillSourceData skillSourceData = new SkillSourceData();
            skillSourceData.skillSourceType = skillSourceEnum.id;
            skillSourceData.maxSkillSource = roleAttributes.getZuiDaJiNengZiYuanZhi(skillSourceEnum.id);
            skillSourceData.skillSourcesRecover = roleAttributes.getJiNengZiYuanZhiHuiFuZhi(skillSourceEnum.id);
            skillSourceData.skillSourcesDelay = roleAttributes.getJiNengZiYuanZhiHuiFuYanChiZhi(skillSourceEnum.id)/10;
            skillSourceData.skillSourcesInterval = roleAttributes.getJiNengZiYuanZhiHuiFuJianGeZhi(skillSourceEnum.id)/10;
            skillSourceData.curSkillSource = skillSourceData.maxSkillSource*skillSourceEnum.initType;

            skillSources[skillSourceEnum.id-1] = skillSourceData;
        }
    }

    /**
     * 添加或覆盖新的属性
     */
    public void coverAttribute(int attributeId,int attributeType,int num){
        roleAttributes.coverAttribute(attributeId,attributeType,num);
        notifyRoleAttributeChange(attributeId,attributeType,num);
    }

    /**
     * 初始化默认属性
     */
    public void initDefaultAttribute(){
        curBlood = maxBlood;
        curProtect = maxProtect;
        curSoulEnergy = 0; //默认为0
        SkillSourceEnum[] skillSourceEnums = SkillSourceEnum.values();
        for(SkillSourceEnum skillSourceEnum : skillSourceEnums){
            skillSources[skillSourceEnum.id-1].curSkillSource = skillSources[skillSourceEnum.id-1].maxSkillSource*skillSourceEnum.initType;
        }
    }

    public List<AttributeConfig> getAllAttributeConfigs(){
        return roleAttributes.getAllAttributeConfigs();
    }

    public void notifyRoleAttributeChange(int attributeId,int attributeType,int num){
        if(attributeId == AttributeBasicConstant.ZUI_DA_SHENG_MING_ZHI){
            maxBlood = num;
        }
        if(attributeId == AttributeBasicConstant.SHENG_MING_ZHI_HUI_FU_ZHI){
            bloodCover = num;
        }
        if(attributeId == AttributeBasicConstant.SHENG_MING_ZHI_HUI_FU_YAN_CHI_ZHI){
            bloodDelay = num/10;
        }
        if(attributeId == AttributeBasicConstant.SHENG_MING_ZHI_HUI_FU_JIAN_GE_ZHI){
            bloodInterval = num/10;
        }

        if(attributeId == AttributeBasicConstant.ZUI_DA_HU_DUN_ZHI){
            maxProtect = num;
        }
        if(attributeId == AttributeBasicConstant.HU_DUN_ZHI_HUI_FU_ZHI){
            protectCover = num;
        }
        if(attributeId == AttributeBasicConstant.HU_DUN_ZHI_HUI_FU_YAN_CHI_ZHI){
            protectDelay = num/10;
        }
        if(attributeId == AttributeBasicConstant.HU_DUN_ZHI_HUI_FU_JIAN_GE_ZHI){
            protectInterval = num/10;
        }

        if(attributeId == AttributeBasicConstant.ZUI_DA_HUN_NENG_LIANG_ZHI){
            maxSoulEnergy = num;
        }
        if(attributeId == AttributeBasicConstant.HUN_NENG_LIANG_ZHI_HUI_FU_ZHI){
            soulEnergyCover = num;
        }
        if(attributeId == AttributeBasicConstant.HUN_NENG_LIANG_ZHI_HUI_FU_YAN_CHI_ZHI){
            soulEnergyDelay = num/10;
        }
        if(attributeId == AttributeBasicConstant.HUN_NENG_LIANG_ZHI_HUI_FU_JIAN_GE_ZHI){
            soulEnergyInterval = num/10;
        }

        if(attributeId == AttributeBasicConstant.ZUI_DA_JI_NENG_ZI_YUAN_ZHI ||
                attributeId == AttributeBasicConstant.JI_NENG_ZI_YUAN_ZHI_HUI_FU_ZHI ||
                attributeId == AttributeBasicConstant.JI_NENG_ZI_YUAN_ZHI_HUI_FU_YAN_CHI_ZHI ||
                attributeId == AttributeBasicConstant.JI_NENG_ZI_YUAN_ZHI_HUI_FU_JIAN_GE_ZHI){
            if(attributeType > 0){
                //System.out.println("attributeId = "+attributeId+"  attributeType = "+attributeType+"  num = "+num);
                skillSources[attributeType-1].changeAttribute(attributeId,num);
            }
        }

        if(attributeId == AttributeBasicConstant.SHI_YE_FAN_WEI_ZHI){
            vision = num/100;
        }
    }

    /**
     * 修改生命值
     * @param hostRole
     * @param bloodNum
     */
    public void addBlood(BattleRole hostRole, int bloodNum){

        curBlood += bloodNum;
        if(curBlood > maxBlood){
            curBlood = maxBlood;
        }
        if(curBlood < 0){
            curBlood = 0;
            battleRole.setDeadState(hostRole);
        }

        //只有减血时才会触发血量值回复延迟值
        if(bloodNum < 0){
            bloodCoverTime = bloodDelay;
        }

    }

    /**
     * 修改护盾值
     * @param protectNum
     */
    public void addProtect(int protectNum){
        curProtect += protectNum;
        if(curProtect > maxProtect){
            curProtect = maxProtect;
        }
        if(curProtect < 0){
            curProtect = 0;
        }

        //只有减护盾时才会触发护盾值回复延迟值
        if(protectNum < 0){
            protectCoverTime = protectDelay;
        }

    }

    /**
     * 修改魂能量值
     * @param soulEnergy
     */
    public void addSoulEnergy(int soulEnergy){
        curSoulEnergy += soulEnergy;
        if(curSoulEnergy > maxSoulEnergy){
            curSoulEnergy = maxSoulEnergy;
        }
        if(curSoulEnergy < 0){
            curSoulEnergy = 0;
        }

        //只有减魂能量时才会触发魂能量值回复延迟值
        if(soulEnergy < 0){
            soulEnergyCoverTime = soulEnergyDelay;
        }

    }

    public int getMaxSkillSourceNumByType(int skillSourceType){
        if(skillSourceType <= 0){
            return 0;
        }
        if(skillSources[skillSourceType-1] != null){
            return skillSources[skillSourceType-1].maxSkillSource;
        }
        return 0;
    }

    public int getSkillSourceNumByType(int skillSourceType){
        if(skillSourceType <= 0){
            return 0;
        }
        if(skillSources[skillSourceType-1] != null){
            return skillSources[skillSourceType-1].curSkillSource;
        }
        return 0;
    }

    public String getSkillSourceStrByType(int skillSourceType){
        if(skillSourceType <= 0){
            return "";
        }
        if(skillSources[skillSourceType-1] != null){
            return skillSources[skillSourceType-1].toString();
        }
        return "";
    }

    public boolean addSkillSourceNumByType(int skillSourceType,int num){
        if(skillSourceType <= 0){
            return false;
        }
        if(skillSources[skillSourceType-1] != null){
            skillSources[skillSourceType-1].addSkillSource(num);
            return true;
        }
        return false;
    }


    public void update(int frameNum){
        /**
        * 角色生命值回复
        */
        bloodCoverTime -= GameConstant.FRAME_TIME;
        if(bloodCoverTime < 0){
            curBlood += bloodCover;
            if(curBlood > maxBlood){
                curBlood = maxBlood;
            }
            bloodCoverTime = bloodInterval;
        }

        /**
         * 角色护盾值回复
         */
        protectCoverTime -= GameConstant.FRAME_TIME;
        if(protectCoverTime < 0){
            curProtect += protectCover;
            if(curProtect > maxProtect){
                curProtect = maxProtect;
            }
            protectCoverTime = protectInterval;
        }

        /**
         * 魂能量值回复
         */
        soulEnergyCoverTime -= GameConstant.FRAME_TIME;
        if(soulEnergyCoverTime < 0){
            curSoulEnergy += soulEnergyCover;
            if(curSoulEnergy > maxSoulEnergy){
                curSoulEnergy = maxSoulEnergy;
            }
            soulEnergyCoverTime = soulEnergyInterval;
        }

        /**
         * 技能资源值回复
         */
        for(SkillSourceData skillSourceData : skillSources){
            if(skillSourceData != null){
                skillSourceData.update();
            }
        }
    }

}
