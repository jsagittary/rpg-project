package com.dykj.rpg.battle.logic;

import com.dykj.rpg.battle.constant.GameConstant;
import com.dykj.rpg.common.attribute.consts.AttributeBasicConstant;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

public class SkillSourceData {
    /**
     * 技能资源类型
     */
    public int skillSourceType;
    /**
     * 最大技能资源
     */
    public int maxSkillSource;
    /**
     * 当前技能资源
     */
    public int curSkillSource;
    /**
     * 技能资源值回复值
     */
    public int skillSourcesRecover;
    /**
     * 技能资源值回复延迟值
     */
    public int skillSourcesDelay;
    /**
     * 技能资源值回复间隔值
     */
    public int skillSourcesInterval;
    /**
     * 技能资源值回复剩余时间
     */
    public int skillSourcesRecoverTime;

    public void addSkillSource(int num){
        curSkillSource += num ;
        if(curSkillSource < 0){
            curSkillSource = 0;
        }
        if(curSkillSource > maxSkillSource){
            curSkillSource = maxSkillSource;
        }

        //释放技能
        if(num < 0){
            skillSourcesRecoverTime = skillSourcesDelay;
        }

        //自动回复
        if(num > 0){
            skillSourcesRecoverTime = skillSourcesInterval;
        }
    }

    public void changeAttribute(int attributeId,int num){
        if(attributeId == AttributeBasicConstant.ZUI_DA_JI_NENG_ZI_YUAN_ZHI){
            maxSkillSource = num;
        }
        if(attributeId == AttributeBasicConstant.JI_NENG_ZI_YUAN_ZHI_HUI_FU_ZHI){
            skillSourcesRecover = num;
        }
        if(attributeId == AttributeBasicConstant.JI_NENG_ZI_YUAN_ZHI_HUI_FU_YAN_CHI_ZHI){
            skillSourcesDelay = num/10;
        }
        if(attributeId == AttributeBasicConstant.JI_NENG_ZI_YUAN_ZHI_HUI_FU_JIAN_GE_ZHI){
            skillSourcesInterval = num/10;
        }
    }

    /**
     * 技能资源更新
     */
    public void update(){
        if(skillSourcesRecoverTime - GameConstant.FRAME_TIME > 0){
            skillSourcesRecoverTime -= GameConstant.FRAME_TIME;
        }else{
            //int sourceRecover = buffManager.getAttributeFromBuff(AttributeBasicConstant.JI_NENG_ZI_YUAN_ZHI_HUI_FU_ZHI,key,skillSourcesRecover.get(key));
            addSkillSource(skillSourcesRecover);
        }
    }

    @Override
    public String toString() {
        return "SkillSourceData{" +
                "skillSourceType=" + skillSourceType +
                ", maxSkillSource=" + maxSkillSource +
                ", curSkillSource=" + curSkillSource +
                ", skillSourcesRecover=" + skillSourcesRecover +
                ", skillSourcesDelay=" + skillSourcesDelay +
                ", skillSourcesInterval=" + skillSourcesInterval +
                ", skillSourcesRecoverTime=" + skillSourcesRecoverTime +
                '}';
    }
}
