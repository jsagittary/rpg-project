package com.dykj.rpg.battle.basic;

import com.dykj.rpg.battle.attribute.AttributeConfig;
import com.dykj.rpg.battle.constant.GameConstant;
import com.dykj.rpg.battle.logic.*;
import com.dykj.rpg.battle.manager.BuffManager;
import com.dykj.rpg.battle.role.BattleRole;
import com.dykj.rpg.battle.carrier.SkillCarrierNull;
import com.dykj.rpg.common.config.model.SkillCharacterStateModel;
import com.dykj.rpg.protocol.battle.BattleSkillCarrierInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BasicSkillBuff extends BattleObject {

    Logger logger = LoggerFactory.getLogger("BasicSkillBuff");

    /**
     * 时间结束，清除所有状态
     */
    private static final int BUFF_DISSIPATE_TYPE_ALL = 1;
    /**
     * 时间结束，清除单层状态
     */
    private static final int BUFF_DISSIPATE_TYPE_ONCE = 2;

    public BattleContainer battleContainer;

    public MapLogic mapLogic;
    /**
     * 产生buff的技能
     */
    public BasicRoleSkill hostSkill;
    /**
     * buff基础信息
     */
    private SkillCharacterStateModel stateModel;
    /**
     * buff挂载的目标对象
     */
    public BattleRole targetRole;
    /**
     *
     */
    public SkillReleaseData skillReleaseData;
    /**
     * 给buff创建一个空载体，用于控制效果的释放
     */
    private BasicSkillCarrier skillCarrier;
    /**
     * buff的效果列表
     */
    private List<BasicSkillEffect> effectList;

    /**
     * buff属性修改表 value使用list是因为如攻击值可以有全攻击值和各种元素类型的攻击值
     */
    private List<BuffAttributeData> buffAttributes;

    /**
     * buff驱散表
     */
    private Map<Integer, List<AttributeConfig>> disappearBuffs;

    private int guid;

    private int stateId;

    /**
     * 状态类型(标签)
     */
    private int stateType;

    private int superpositionType;

    private int groupId;
    /**
     * buff持续时间
     */
    private int durationTime;
    /**
     * 最大叠加层数
     */
    private int superpositionNumMax;
    /**
     * 当前叠加层数
     */
    private int currentSuperpositionNum;
    /**
     * buff消散规则
     */
    private int dissipateType;

    private byte state;

    public BasicSkillBuff(){

        this.effectList = new ArrayList<>();
        this.buffAttributes = new ArrayList<>();

    }

    public void init(BattleContainer battleContainer, MapLogic mapLogic, BasicRoleSkill hostSkill, SkillCharacterStateModel stateModel, BattleRole targetRole){
        this.battleContainer = battleContainer;
        this.mapLogic = mapLogic;
        this.hostSkill = hostSkill;
        this.stateModel = stateModel;
        this.targetRole = targetRole;

        this.guid = targetRole.createNewBuffGuid();
        this.stateId = stateModel.getStateId();
        this.stateType = stateModel.getStateType();
        this.superpositionType = stateModel.getSuperpositionType();
        this.groupId = stateModel.getStateGroup();
        this.durationTime = stateModel.getDurationTime()/10;
        this.superpositionNumMax = stateModel.getSuperpositionNumMax();
        this.currentSuperpositionNum = 1;
        this.dissipateType = stateModel.getDissipateType();

        List<Integer> effectIds = stateModel.getEffectId();
        if(effectIds != null && effectIds.size() > 0){
            skillReleaseData = battleContainer.battlePoolManager.borrowSkillReleaseData();
            skillReleaseData.init(battleContainer,mapLogic,hostSkill,hostSkill.hostRole.targetRole,SkillReleaseData.SKILL_RESOURCE_TYPE_BUFF, 0);
            //创建空载体
            skillCarrier = battleContainer.battlePoolManager.borrowSkillCarrier();
            skillCarrier.init(battleContainer,mapLogic,skillReleaseData,hostSkill.camp,0,null,null);
            skillCarrier.setEffectByList(effectIds,GameConstant.MAX_INTEGER);
            skillCarrier.setTriggerRole(targetRole);
            skillCarrier.skillBuff = this;

            skillReleaseData.addNewCarrier(skillCarrier);

            mapLogic.addSkillReleaseData(skillReleaseData);
            //skillCarrier = new SkillCarrierNull(this.mapLogic,this,effectIds,(short)0,durationTime,null);
        }


        this.state = BuffManager.BUFF_STATE_INIT;
    }

    public void release(){

        battleContainer.battlePoolManager.restoreSkillReleaseData(skillReleaseData);
        skillReleaseData = null;

        skillCarrier = null;

        battleContainer = null;
        mapLogic = null;
        hostSkill = null;
        stateModel = null;
        targetRole = null;

        effectList.clear();
        buffAttributes.clear();

        guid = 0;
        stateId = 0;
        stateType = 0;
        superpositionType = 0;
        groupId = 0;
        durationTime = 0;
        superpositionNumMax = 0;
        currentSuperpositionNum = 0;
        dissipateType = 0;
        state = 0;

    }

    @Override
    public void selfCheak() {

    }

    public void buffEffectRelease(){
        //释放一次buff持续效果
        skillCarrier.buffEffectRelease(targetRole);
    }

    public void addBuffAttribute(List<List<Integer>> _buffAttributes){
        if(_buffAttributes != null && _buffAttributes.size() > 0){
            for(List<Integer> _buffAttribute : _buffAttributes){
                if(_buffAttribute.size() >= 4){
                    BuffAttributeData buffAttributeData = new BuffAttributeData();
                    AttributeConfig config = new AttributeConfig(_buffAttribute.get(0),_buffAttribute.get(1),_buffAttribute.get(2),_buffAttribute.get(3));
                    buffAttributeData.buffId = stateId;
                    buffAttributeData.attributeConfig = config;
                    buffAttributeData.waitDestroy = false;
                    buffAttributes.add(buffAttributeData);

                    this.targetRole.getBuffManager().addBuffAttribute(buffAttributeData);
                }
            }
        }
    }

    public void addDisappearBuff(List<List<Integer>> _buffAttributes){
        if(_buffAttributes != null && _buffAttributes.size() > 0){
            for(List<Integer> _buffAttribute : _buffAttributes){
                if(_buffAttribute.size() > 0){
                    int disappearType = _buffAttribute.get(0);
                    if(disappearType == 1){//驱散所有buff
                        targetRole.getBuffManager().disappearAllBuff();
                        continue;
                    }
                }
                if(_buffAttribute.size() > 1){
                    int stateType = _buffAttribute.get(1);
                    if(stateType != 0){
                        targetRole.getBuffManager().disappearBuffByStateType(stateType);
                        continue;
                    }
                }
                if(_buffAttribute.size() > 2){
                    int stateId = _buffAttribute.get(2);
                    if(stateId != 0){
                        targetRole.getBuffManager().disappearBuffByStateId(stateId);
                        continue;
                    }
                }
            }
        }
    }

    public void addSuperposition(){
        if(currentSuperpositionNum < superpositionNumMax){
            currentSuperpositionNum ++ ;
            durationTime = stateModel.getDurationTime();
            state = BuffManager.BUFF_STATE_INIT;
        }else{
            //TODO 达到叠加层数的相关处理
        }
    }

    public int getStateId() {
        return stateId;
    }

    public int getStateType() {
        return stateType;
    }

    public int getGuid() {
        return guid;
    }

    public int getSuperpositionType() {
        return superpositionType;
    }

    public int getGroupId() {
        return groupId;
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state){
        this.state = state;
    }

    public BattleRole getTargetRole() {
        return targetRole;
    }

    public int getCurrentSuperpositionNum() {
        return currentSuperpositionNum;
    }

    public int getDurationTime() {
        return durationTime;
    }

    public void update(int frameNum){

        if(durationTime > 0){
            durationTime -= GameConstant.FRAME_TIME;
            if(durationTime <= 0){

                //默认清除所有状态
                if(dissipateType == 0 || dissipateType == BUFF_DISSIPATE_TYPE_ALL){

                    /**
                     * 释放载体最终效果
                     */
                    skillCarrier.finalEffectRelease();

                    releaseBuff();
                }

                if(dissipateType == BUFF_DISSIPATE_TYPE_ONCE){
                    currentSuperpositionNum --;
                    if(currentSuperpositionNum > 0){
                        durationTime = stateModel.getDurationTime();
                        state = BuffManager.BUFF_STATE_INIT;
                    }else{

                        /**
                         * 释放载体最终效果
                         */
                        skillCarrier.finalEffectRelease();

                        releaseBuff();
                    }
                }

            }
        }else{
            if(state != BuffManager.BUFF_STATE_DEAD){
                releaseBuff();
            }
        }
    }

    public void releaseBuff(){
        skillReleaseData.removeDestroyCarrier(skillCarrier);
        state = BuffManager.BUFF_STATE_DEAD;
        for(BuffAttributeData data : buffAttributes){
            targetRole.buffManager.removeBuffAttribute(data);
        }
    }
}
