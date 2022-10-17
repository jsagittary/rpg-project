package com.dykj.rpg.battle.basic;

import com.dykj.rpg.battle.attribute.SkillAttributes;
import com.dykj.rpg.battle.constant.GameConstant;
import com.dykj.rpg.battle.logic.BattleContainer;
import com.dykj.rpg.battle.logic.BattleObject;
import com.dykj.rpg.battle.logic.MapLogic;
import com.dykj.rpg.battle.logic.SkillReleaseData;
import com.dykj.rpg.battle.role.BattleRole;
import com.dykj.rpg.battle.skill.*;
import com.dykj.rpg.common.config.model.SkillCharacterBasicModel;
import com.dykj.rpg.protocol.battle.BattlePosition;
import com.dykj.rpg.protocol.item.ItemRs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class BasicRoleSkill extends BattleObject {

    Logger logger = LoggerFactory.getLogger("BasicRoleSkill");

    /**
     * 技能释放方式--自动
     */
    public static final byte SKILL_RELEASE_TYPE_AUTO = 0;
    /**
     * 技能释放方式--手动
     */
    public static final byte SKILL_RELEASE_TYPE_MANUAL = 1;
    /**
     * 技能释放方式--被动
     */
    public static final byte SKILL_RELEASE_TYPE_PASSIVE = 2;
    /**
     * 技能释放方式--药品
     */
    public static final byte SKILL_RELEASE_TYPE_POTION = 6;

    /**
     * 技能释放方式--元素精通
     */
    public static final byte SKILL_RELEASE_TYPE_ELEMENT = 8;

    /**
     * 技能释放方式--灵魂之影
     */
    public static final byte SKILL_RELEASE_TYPE_SOUL = 9;

    /**
     * 技能释放类型--瞬发
     */
    public static final byte SKILL_SING_TYPE_INSTANT = 1;
    /**
     * 技能释放类型--吟唱
     */
    public static final byte SKILL_SING_TYPE_SING = 2;
    /**
     * 技能释放类型--引导
     */
    public static final byte SKILL_SING_TYPE_GUIDE = 3;

    /**
     * 施法指示器类型
     */
    /**
     * 空
     */
    public static final byte SKILL_INDICATOR_TYPE_NULL = 0;
    /**
     *  圆形
     */
    public static final byte SKILL_INDICATOR_TYPE_CIRCLE = 1;
    /**
     * 矩形
     */
    public static final byte SKILL_INDICATOR_TYPE_RECTANGLE = 2;
    /**
     * 扇形
     */
    public static final byte SKILL_INDICATOR_TYPE_SECTOR = 3;
    /**
     * 环形
     */
    public static final byte SKILL_INDICATOR_TYPE_RING = 4;
    /**
     * 方向
     */
    public static final byte SKILL_INDICATOR_TYPE_DIRECTION = 5;

    public SkillAttributes skillAttributes;

    public SkillCharacterBasicModel skillBasicModel;
    /**
     * 技能的效果列表
     */
    public List<Integer> effectIdList;

    public BattleContainer battleContainer;

    public MapLogic mapLogic;

    public BattleRole hostRole;

    public int skillId;

    public int skillSeat;

    /**
     * 技能释放方式
     */
    public byte skillType;
    /**
     * 最大CD时间 (单位 ms)
     */
    public int maxCdTime;
    /**
     * 技能最小攻击距离
     */
    public float minDistance;
    /**
     * 技能最大攻击距离
     */
    public float maxDistance;
    /**
     * 技能施法方式
     */
    public byte singType;
    /**
     * 技能施法时间 (单位 ms)
     */
    public int singTime;
    /**
     * 技能施法前摇时间
     */
    public int beforeAttackTime;
    /**
     * 当前剩余CD时间
     */
    public int cdTime;

    /**
     * 施法指示器类型
     */
    public int skillIndicator;

    /**
     * 施法指示器点
     */
    public BattlePosition skillIndicatorPos;

    /**
     * 施法开始时目标角色位置 [x,y,z,state] state=0为无效
     */
    public  float[] targetRolePos = new float[]{0,0,0,0};

    public boolean ignoreIndicatorPos = false;

    public int elementType;
    /**
     * 技能释放消耗的能量类型
     */
    public int energyType;
    /**
     * 技能释放消耗的能量数量
     */
    public int energyNum;
    /**
     * 使用技能产生的魂能量
     */
    public int createSoulEnergy;
    /**
     * 使用魂技能消耗的魂能量
     */
    public int useSoulEnergy;
    /** --------------------------元素精通技能特殊属性--------------------------*/
    /**
     * 元素精通释放最大次数
     */
    public int maxElementReleaseNum;
    /**
     * 元素精通释放次数
     */
    public int curElementReleaseNum;

    /** --------------------------药水技能特殊属性--------------------------*/
    /**
     * 药水ID
     */
    public int potionId;
    /**
     * 初始化的药水数量
     */
    public int initPotionNum;
    /**
     * 实时药水数量
     */
    public int potionNum;

    /** --------------------------灵魂技能特殊属性--------------------------*/
    /**
     * 灵魂之影出现的位置 {x,y,z,direction}
     */
    public float[] soulRolePos = new float[]{0,0,0,0};
    /**
     *
     */
    public float[] releaseOffsetPos = new float[]{0,0,0};

    public BasicRoleSkill(){
    }

    public void init(BattleContainer battleContainer,BattleRole hostRole,SkillAttributes skillAttributes,int skillSeat){

        this.battleContainer = battleContainer;
        this.skillAttributes = skillAttributes;
        this.skillBasicModel = skillAttributes.getDevelopSkillBasic();
        if(skillBasicModel == null){
            logger.error("skill[skillId="+skillId+"] can not found !!!");
        }

        this.hostRole = hostRole;
        this.skillId = skillBasicModel.getSkillId();
        this.skillSeat = skillSeat;

        this.maxDistance = this.skillBasicModel.getSkillDistanceMax();
        this.minDistance = this.skillBasicModel.getSkillDistanceMin();

        this.energyType = this.skillBasicModel.getEnergyType();
        this.energyNum = this.skillBasicModel.getEnergyNum();

        this.createSoulEnergy = this.skillBasicModel.getCreateSoulEnergy();
        this.useSoulEnergy = this.skillBasicModel.getUseSoulEnergy();

        initRoleSkill();
    }

    @Override
    public void release() {
        skillAttributes = null;
        skillBasicModel = null;

        effectIdList.clear();

        mapLogic = null;
        hostRole = null;

        skillId = 0;
        skillSeat = 0;
        skillType = 0;

        maxCdTime = 0;
        minDistance = 0;
        maxDistance = 0;

        singType = 0;
        singTime = 0;
        beforeAttackTime = 0;
        cdTime = 0;

        skillIndicator = 0;

        skillIndicatorPos = null;

        targetRolePos[0] = 0;
        targetRolePos[1] = 0;
        targetRolePos[2] = 0;
        targetRolePos[3] = 0;

        ignoreIndicatorPos = false;

        elementType = 0;

        energyType = 0;
        energyNum = 0;

        maxElementReleaseNum = 0;
        curElementReleaseNum = 0;

        potionId = 0;
        initPotionNum = 0;
        potionNum = 0;

        soulRolePos[0] = 0;
        soulRolePos[1] = 0;
        soulRolePos[2] = 0;
        soulRolePos[3] = 0;

        releaseOffsetPos[0] = 0;
        releaseOffsetPos[1] = 0;
        releaseOffsetPos[2] = 0;

    }

    @Override
    public void selfCheak() {

    }

    private void initRoleSkill(){
        skillType = skillBasicModel.getSkillType().byteValue();
        if(this.skillSeat == 1){
            this.skillType = SKILL_RELEASE_TYPE_AUTO;
        }
        if(this.skillSeat >= 2 && this.skillSeat <= 4){
            this.skillType = SKILL_RELEASE_TYPE_MANUAL;
        }
        if(this.skillSeat >= 5){
            this.skillType = SKILL_RELEASE_TYPE_SOUL;
        }
        this.elementType = skillBasicModel.getElementType().get(0);
        //getSkillCdTime获取的为万分秒，需转换为千分秒（毫秒）
        maxCdTime = skillBasicModel.getSkillCdTime()/10;
        minDistance = skillBasicModel.getSkillDistanceMin()/100f;
        maxDistance = skillBasicModel.getSkillDistanceMax()/100f;
        singType = skillBasicModel.getSingType().byteValue();
        singTime = 0;
        beforeAttackTime = 0;
        skillIndicator = skillBasicModel.getSkillIndicator();

        effectIdList = new ArrayList<>();
        List<Integer> indProEffectIds = skillBasicModel.getEffectId();
        if(indProEffectIds != null && indProEffectIds.size() > 0){
            for(int id : indProEffectIds){
                effectIdList.add(id);
            }
        }

        //cdTime = maxCdTime;
        cdTime = 0;
    }

    public void setMapLogic(MapLogic mapLogic){
        this.mapLogic = mapLogic;
    }

    public void setMaxElementReleaseNum(int elementReleaseNum){
        this.maxElementReleaseNum = elementReleaseNum;
    }

    public void setPotionInfo(ItemRs itemRs){
        this.potionId = itemRs.getItemId();
        this.initPotionNum = itemRs.getItemNum();
        this.potionNum = itemRs.getItemNum();
    }

    public void setPotionInfo(int itemId,int itemNum){
        this.potionId = itemId;
        this.initPotionNum = itemNum;
        this.potionNum = itemNum;
    }

    public void update(int frame){
        if(cdTime > 0){
            cdTime -= GameConstant.FRAME_TIME;
        }

        //吟唱和引导型技能释放时间
        if(singTime > 0){
            singTime -= GameConstant.FRAME_TIME;

            if(singTime <= 0){
                //效果延迟时间
//                if(singType == SKILL_SING_TYPE_INSTANT){
//                    createEmptyCarrier();
//                }
//                if(singType == SKILL_SING_TYPE_SING){
//                    createEmptyCarrier();
//                }
                //结束技能动作
                //hostRole.endReleaseSkillAnim(this);
            }

        }

        //技能前摇时间
        if(beforeAttackTime > 0){
            beforeAttackTime -= GameConstant.FRAME_TIME;
        }
    }

    /**
     * 释放技能处理逻辑
     */
    public void releaseSkillHandler(BattleRole targetRole){
        //System.out.println("releaseSkill skillId = "+skillId);
        //瞬时释放的技能
        if(singType == SKILL_SING_TYPE_INSTANT){
            //过滤策划时间配置错误
            singTime = skillBasicModel.getSingTime()/10;
            beforeAttackTime = skillBasicModel.getBeforeAttackTime()/10;
            //if(singTime==0){
                createEmptyCarrier(targetRole,singTime);
            //}
        }
        //吟唱型技能
        if(singType == SKILL_SING_TYPE_SING){
            singTime = skillBasicModel.getSingTime()/10;
            beforeAttackTime = skillBasicModel.getBeforeAttackTime()/10;
            //if(singTime == 0){
                createEmptyCarrier(targetRole,singTime);
            //}
        }
        //引导型技能
        if(singType == SKILL_SING_TYPE_GUIDE){
            singTime = skillBasicModel.getSingTime()/10;
            beforeAttackTime = skillBasicModel.getBeforeAttackTime()/10;
            createEmptyCarrier(targetRole,singTime);
        }

    }

    /**
     * 打断技能施法
     */
    private void breakSkillHandler(){
        //吟唱型技能
        if(singType == SKILL_SING_TYPE_SING){
            singTime = 0;
        }
        //引导型技能
        if(singType == SKILL_SING_TYPE_GUIDE){
            singTime = 0;
        }
    }

    /**
     * 为技能创建一个空载体
     */
    private void createEmptyCarrier(BattleRole targetRole,int singTime){
        //System.out.println("-----------------createEmptyCarrier----------------- skillId = "+skillId);
        //判断是否有施法指示器
        if(skillIndicator != 0){

            //当客户端施法指示器点击时，服务器重新为角色释放技能选点
            if(skillIndicatorPos == null){
                double a = hostRole.getDirection()*Math.PI/180f;
                float distance = 10;
                if(targetRolePos[3] > 0){
                    distance = (float)Math.sqrt(Math.pow(hostRole.getCurrentPosition()[0]-targetRolePos[0],2)+Math.pow(hostRole.getCurrentPosition()[2]-targetRolePos[2],2));
                }
                distance = distance > 4 ? 4 : distance;

                skillIndicatorPos = new BattlePosition();
                skillIndicatorPos.setPosX((int)((hostRole.getCurrentPosition()[0]+(float)(Math.cos(a)*distance))*100));
                skillIndicatorPos.setPosZ((int)((hostRole.getCurrentPosition()[2]+(float)(Math.sin(a)*distance))*100));
                skillIndicatorPos.setDirection(hostRole.getDirection());
            }

            if(skillIndicatorPos == null){
                logger.error("the skill ["+skillId+"] indicator can not null ! ");
                return ;
            }

            //角色先做转向
            short direction = skillIndicatorPos.getDirection();
            while(direction < 0){
                direction += 360;
            }
            direction %= 360;
            skillIndicatorPos.setDirection(direction);
            hostRole.setCurrentDir(direction);

            /**
             * 圆形时，施法指示器坐标点为圆心
             */
            if(skillIndicator == SKILL_INDICATOR_TYPE_CIRCLE){

            }

            if(skillIndicator == SKILL_INDICATOR_TYPE_SECTOR){

            }

            if(skillIndicator == SKILL_INDICATOR_TYPE_RING){

            }

            if(skillIndicator == SKILL_INDICATOR_TYPE_DIRECTION){

            }

            SkillReleaseData skillReleaseData = battleContainer.battlePoolManager.borrowSkillReleaseData();
            skillReleaseData.init(battleContainer,mapLogic,this,targetRole,SkillReleaseData.SKILL_RESOURCE_TYPE_BASIC,0);

            BasicSkillCarrier carrier = battleContainer.battlePoolManager.borrowSkillCarrier();
            carrier.init(battleContainer,mapLogic,skillReleaseData,hostRole.getCamp(),singTime,skillIndicatorPos,null);
            carrier.setEffectByList(effectIdList,singTime);
            carrier.setStartPosition(hostRole.currentPosition);
            carrier.setClientShow(false);
            //System.out.println("borrowSkillCarrier carrierId = "+carrier.getCarrierId());
            //BasicSkillCarrier carrier = new SkillCarrierNull(hostRole.getMapLogic(),this,effectIdList,(short)0,singTime,skillIndicatorPos);
            skillReleaseData.addNewCarrier(carrier);
            mapLogic.addSkillReleaseData(skillReleaseData);

            //清空技能指示器信息
            skillIndicatorPos = null;

        }else{

            SkillReleaseData skillReleaseData = battleContainer.battlePoolManager.borrowSkillReleaseData();
            skillReleaseData.init(battleContainer,mapLogic,this,targetRole,SkillReleaseData.SKILL_RESOURCE_TYPE_BASIC,0);

            BasicSkillCarrier carrier = battleContainer.battlePoolManager.borrowSkillCarrier();
            carrier.init(battleContainer,mapLogic,skillReleaseData,hostRole.getCamp(),0,null,null);
            carrier.setEffectByList(effectIdList,singTime);
            carrier.setClientShow(false);
            //System.out.println("borrowSkillCarrier carrierId = "+carrier.getCarrierId());
            //BasicSkillCarrier carrier = new SkillCarrierNull(hostRole.getMapLogic(),this,effectIdList,(short)0,singTime,null);
            skillReleaseData.addNewCarrier(carrier);
            mapLogic.addSkillReleaseData(skillReleaseData);
        }

    }

    /**
     * 释放技能
     */
    public boolean releaseSkill(BattleRole targetRole,BattlePosition skillIndicatorPos,boolean ignoreCdTime){
        switch (skillType){
            case SKILL_RELEASE_TYPE_AUTO: return AutoRoleSkill.releaseSkill(this,targetRole,skillIndicatorPos,ignoreCdTime);
            case SKILL_RELEASE_TYPE_MANUAL: return ManualRoleSkill.releaseSkill(this,targetRole,skillIndicatorPos,ignoreCdTime);
            case SKILL_RELEASE_TYPE_POTION: return PotionRoleSkill.releaseSkill(this,targetRole,skillIndicatorPos,ignoreCdTime);
            case SKILL_RELEASE_TYPE_ELEMENT: return ElementRoleSkill.releaseSkill(this,targetRole,skillIndicatorPos,ignoreCdTime);
            case SKILL_RELEASE_TYPE_SOUL: return SoulRoleSkill.releaseSkill(this,targetRole,skillIndicatorPos,ignoreCdTime);
        }
        return false;
    }

    /**
     * 打断技能释放
     */
    public void breakSkill(){};

    public int getSkillId() {
        return skillId;
    }

    public int getSkillSeat() {
        return skillSeat;
    }

    public byte getSkillType(){
        return skillType;
    }

    public int getMaxCdTime() {
        return maxCdTime;
    }

    public int getCdTime() {
        return cdTime;
    }

    public float getMaxDistance() {
        return maxDistance;
    }

    public BattleRole getHostRole(){
        return hostRole;
    }

    public BattlePosition getSkillIndicatorPos(){
        return skillIndicatorPos;
    }

    public int getMaxSingTime() {
        return skillBasicModel.getSingTime()/10;
    }

    public int getAnimatorTime(){
        return skillBasicModel.getAnimatorTime()/10;
    }

    public int getSkillSourceType(){
        return skillBasicModel.getEnergyType();
    }

    public int getSkillSourceNum(){
        return skillBasicModel.getEnergyNum();
    }

    public SkillAttributes getSkillAttributes(){
        return skillAttributes;
    }

    @Override
    public String toString() {
        return "BasicRoleSkill{" +
                "using=" + isUsing() +
                ", hostRole=" + hostRole +
                ", skillId=" + skillId +
                ", skillSeat=" + skillSeat +
                ", skillType=" + skillType +
                '}';
    }
}
