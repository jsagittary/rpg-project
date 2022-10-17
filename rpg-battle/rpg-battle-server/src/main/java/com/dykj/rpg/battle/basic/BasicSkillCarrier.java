package com.dykj.rpg.battle.basic;

import com.dykj.rpg.battle.carrier.*;
import com.dykj.rpg.battle.constant.GameConstant;
import com.dykj.rpg.battle.constant.RoleStateConstant;
import com.dykj.rpg.battle.constant.SkillHitShapConstant;
import com.dykj.rpg.battle.dictionary.StaticDictionary;
import com.dykj.rpg.battle.logic.BattleContainer;
import com.dykj.rpg.battle.logic.BattleObject;
import com.dykj.rpg.battle.logic.MapLogic;
import com.dykj.rpg.battle.logic.SkillReleaseData;
import com.dykj.rpg.battle.role.BattleRole;
import com.dykj.rpg.battle.util.CalculationUtil;
import com.dykj.rpg.common.config.model.SkillCharacterCarrierModel;
import com.dykj.rpg.common.config.model.SkillCharacterEffectModel;
import com.dykj.rpg.net.protocol.ProtocolPool;
import com.dykj.rpg.protocol.battle.BattlePosition;
import com.dykj.rpg.protocol.battle.BattleSkillCarrierInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class BasicSkillCarrier extends BattleObject {

    Logger logger = LoggerFactory.getLogger("BasicSkillCarrier");

    /**
     * -------------------------------载体类型--------------------------------
     */
    public static final byte CARRIER_TYPE_NULL = 0;

    public static final byte CARRIER_TYPE_TARGET_BULLET = 1;

    public static final byte CARRIER_TYPE_POSITION_BULLET = 2;

    public static final byte CARRIER_TYPE_DIRECTION_BULLET = 3;
    /**
     * 目标位移
     */
    public static final byte CARRIER_TYPE_TARGET_MOVE = 4;

    public static final byte CARRIER_TYPE_POSITION_MOVE = 5;

    public static final byte CARRIER_TYPE_DIRECTION_MOVE = 6;
    /**
     * 地点魔法
     */
    public static final byte CARRIER_TYPE_POSITION_MAGIC = 7;
    /**
     *  瞬移
     */
    public static final byte CARRIER_TYPE_FLASH_MOVE = 8;
    /**
     * 跟随魔法
     */
    public static final byte CARRIER_TYPE_FOLLOW_MAGIC = 9;

    /**
     * -------------------------------载体移动方式--------------------------------
     */
    /**
     * 载体移动方式--固定点
     */
    public final static byte CARRIER_MOVE_TYPE_NULL = 0;
    /**
     * 载体移动方式--自动
     */
    public final static byte CARRIER_MOVE_TYPE_AUTO = 1;
    /**
     * 载体移动方式--跟随
     */
    public final static byte CARRIER_MOVE_TYPE_FOLLOW = 2;


    /**
     * -------------------------------载体状态类型--------------------------------
     */
    /**
     * 初始化
     */
    public final static byte CARRIER_STATE_INIT = 0;
    /**
     * 等待
     */
    public final static byte CARRIER_STATE_MOVE = 1;
    /**
     * 产生的所有效果都销毁后归还载体到对象池
     */
    public final static byte CARRIER_STATE_DESTROY = 2;

    public BattleContainer battleContainer;

    public MapLogic mapLogic;

    private SkillCharacterCarrierModel carrierModel;

    /**
     * 载体ID
     */
    protected int carrierId;
    /**
     * 载体形态
     */
    protected byte carrierForm;
    /**
     * 载体类型
     */
    protected byte carrierType;
    /**
     * 载体参数
     */
    protected List<Integer> formParm;
    /**
     * 碰撞
     */
    public boolean canCollision;
    /**
     * 射程
     */
    public float range;
    /**
     * 轨迹
     */
    public byte heightLocus;
    /**
     * 运动速度
     */
    public float moveSpeed;
    /**
     * 对象配置组合
     */
    public List<List<Integer>> targetLocation;
    /**
     * 作用次数
     */
    public int effectTimes;
    /**
     * 够次数后消失
     */
    public boolean enoughTimesDisappear;
    /**
     * 激活时间
     */
    public int activationTime;
    /**
     * 持续时间
     */
    public int durationTime;

    /**
     * 技能的离散效果列表
     */
    protected List<BasicSkillEffect> intervalEffectList = new ArrayList<>(3);
    /**
     * 技能的连续效果列表
     */
    protected List<BasicSkillEffect> continueEffectList = new ArrayList<>(3);
    /**
     * 技能的最终效果列表
     */
    protected List<BasicSkillEffect> finalEffectList = new ArrayList<>(3);
    /**
     * 技能的持续效果列表
     */
    protected List<BasicSkillEffect> buffEffectList = new ArrayList<>(3);

    /**
     * 载体移动类型（自主移动，跟随移动）
     */
    protected byte moveType;
    /**
     *  载体上次的位置
     */
    protected float[] prePosition = new float[3];
    /**
     *  载体位置
     */
    public float[] currentPosition = new float[3];
    /**
     *
     */
    public float[] targetPosition = new float[3];
    /**
     * 载体移动方向
     */
    public short direction;

    public int modelId;

    public byte state;

    /**
     * 载体所属技能释放信息
     */
    public SkillReleaseData skillReleaseData;
    /**
     * 载体所属技能 (技能生成的载体)
     */
    public BasicRoleSkill hostSkill;
    /**
     * 载体所属角色
     */
    public BattleRole hostRole;
    /**
     * 技能所属buff (buff生成的载体)
     */
    public BasicSkillBuff skillBuff;
    /**
     * 载体的触发对象
     */
    public BattleRole triggerRole;
    /**
     * 载体的目标对象，当位移类型的载体时，targetRole有值
     */
    public BattleRole targetRole;
    /**
     * 载体的目标对象，载体的目标对象也可以是载体
     */
    public BasicSkillCarrier targetCarrier;

    /**
     * 施法指示器，只有第一层effect有效
     */
    public BattlePosition skillIndicatorPos;

    public boolean clientShow = false;

    public BasicSkillCarrier(){

    }

    /**
     * 通过效果产生载体
     * @param mapLogic
     */

    public void init(BattleContainer battleContainer, MapLogic mapLogic, SkillReleaseData skillReleaseData, byte camp, int activationTime, BattlePosition skillIndicatorPos, BattleRole triggerRole){

        this.battleContainer = battleContainer;
        this.mapLogic = mapLogic;
        this.skillReleaseData = skillReleaseData;
        this.hostSkill = skillReleaseData.roleSkill;
        this.camp = camp;
        this.activationTime = activationTime;
        this.skillIndicatorPos = skillIndicatorPos;
        this.triggerRole = triggerRole;

        this.modelId = mapLogic.createCarrierModelId();

        this.state = CARRIER_STATE_INIT;

    }

    /**
     * 设置起点位置
     */
    public void setStartPosition(float[] startPosition){

        this.prePosition[0] = startPosition[0];
        this.prePosition[1] = startPosition[1];
        this.prePosition[2] = startPosition[2];

        this.currentPosition[0] = startPosition[0];
        this.currentPosition[1] = startPosition[1];
        this.currentPosition[2] = startPosition[2];

    }

    /**
     * 设置起点角色
     */
    public void setStartRole(BattleRole startRole){

        float[] startPosition = startRole.currentPosition;

        this.prePosition[0] = startPosition[0];
        this.prePosition[1] = startPosition[1];
        this.prePosition[2] = startPosition[2];

        this.currentPosition[0] = startPosition[0];
        this.currentPosition[1] = startPosition[1];
        this.currentPosition[2] = startPosition[2];

    }

    /**
     * 设置挂载角色
     */
    public void setTriggerRole(BattleRole triggerRole){
        this.triggerRole = triggerRole;
    }

    /**
     * 设置目标地点
     */
    public void setTargetPosition(float[] position){
        this.targetPosition[0] = position[0];
        this.targetPosition[1] = position[1];
        this.targetPosition[2] = position[2];

        this.direction = (short)CalculationUtil.vectorToAngle(targetPosition[0]-currentPosition[0],targetPosition[2]-currentPosition[2]);
    }

    /**
     * 设置目标角色
     */
    public void setTargetRole(BattleRole targetRole){
        if(targetRole != null){
            this.targetRole = targetRole;
            this.direction = (short)CalculationUtil.vectorToAngle(targetRole.currentPosition[0]-currentPosition[0],targetRole.currentPosition[2]-currentPosition[2]);
        }
    }

    /**
     * 设置目标载体
     */
    public void setTargetCarrier(BasicSkillCarrier targetCarrier){
        if(targetCarrier != null){
            this.targetCarrier = targetCarrier;
            this.direction = (short)CalculationUtil.vectorToAngle(targetCarrier.currentPosition[0]-currentPosition[0],targetCarrier.currentPosition[2]-currentPosition[2]);
        }
    }

    /**
     * 设置目标方向
     */
    public void setTargetDirection(short direction){
        this.direction = direction;
    }

    /**
     * 通过 SkillCharacterCarrierModel 设置效果
     */
    public void setEffectByModel(SkillCharacterCarrierModel carrierModel,int durationTime){
        this.carrierId = carrierModel.getCarrierId();
        this.carrierForm = carrierModel.getCarrierForm().byteValue();
        this.carrierType = carrierModel.getCarrierType().byteValue();
        this.formParm = carrierModel.getFormParm();
        this.canCollision = (carrierModel.getCollision()==1);
        this.range = carrierModel.getRange()/100f;
        this.heightLocus = carrierModel.getHeightLocus().byteValue();
        this.heightLocus = heightLocus == 0 ? 1 : heightLocus;
        this.moveSpeed = carrierModel.getMoveSpeed()/100f;
        this.targetLocation = carrierModel.getTargetType();
        this.effectTimes = (carrierModel.getEffectTimes() <= 0) ? GameConstant.MAX_INTEGER : carrierModel.getEffectTimes();
        this.enoughTimesDisappear = (carrierModel.getDisappear() == 1);
        //int durationTime = carrierModel.getDurationTime();
        if(durationTime > 0){
            this.durationTime = durationTime;
        }else{
            if(carrierModel.getDurationTime() <= 0){
                if(this.range > 0 && this.moveSpeed > 0){
                    this.durationTime = (int)(this.range*1000f/this.moveSpeed);
                }else{
                    this.durationTime = 0;
                }
            }else{
                this.durationTime = carrierModel.getDurationTime()/10;
            }
        }
        //如果配置表中射程为0，根据速度和时间算射程
        if(this.range == 0 && this.moveSpeed > 0 && this.durationTime > 0){
            this.range = this.moveSpeed*this.durationTime/1000f;
        }

        this.moveType = CARRIER_MOVE_TYPE_AUTO;

        List<Integer> effectIds = carrierModel.getEffectId();
        if(effectIds != null && effectIds.size() > 0){
            for(int id : effectIds){
                //SkillCharacterEffectModel effectModel = StaticDictionary.getInstance().getSkillCharacterEffectModelById(id);
                SkillCharacterEffectModel effectModel = hostSkill.getSkillAttributes().getDevelopSkillEffect(id);
                if(effectModel != null){
                    BasicSkillEffect effect = battleContainer.battlePoolManager.borrowSkillEffect();
                    if(effect != null){
                        effect.init(battleContainer,this,effectModel);
                        if(effectModel.getEffectType() == BasicSkillEffect.EFFECT_TYPE_INTERVAL){
                            intervalEffectList.add(effect);
                        }
                        if(effectModel.getEffectType() == BasicSkillEffect.EFFECT_TYPE_CONTINUE){
                            continueEffectList.add(effect);
                        }
                        if(effectModel.getEffectType() == BasicSkillEffect.EFFECT_TYPE_FINAL){
                            finalEffectList.add(effect);
                        }
                        if(effectModel.getEffectType() == BasicSkillEffect.EFFECT_TYPE_BUFF){
                            buffEffectList.add(effect);
                        }
                    }
                }else{
                    logger.error("skill carrier config error !!! can not found effect [id="+id+"] in carrier [id="+carrierId+"]");
                }
            }
        }

        this.clientShow = true;


    }

    /**
     * 通过 集合 设置效果
     */
    public void setEffectByList(List<Integer> effectIds,int durationTime){

        this.carrierId = 0;
        this.carrierForm = SkillHitShapConstant.SKILL_HIT_SHAP_POINT;
        this.carrierType = 0;
        this.formParm = null;
        this.canCollision = false;
        this.range = 0;
        this.heightLocus = 1;
        this.moveSpeed = 0;
        this.targetLocation = null;
        this.effectTimes = -1;
        this.enoughTimesDisappear = true;
        this.durationTime = (durationTime <= 0 ? 0 : durationTime);
        this.moveType = CARRIER_MOVE_TYPE_NULL;

        if(effectIds != null && effectIds.size() > 0){
            for(int id : effectIds){
                SkillCharacterEffectModel effectModel = StaticDictionary.getInstance().getSkillCharacterEffectModelById(id);
                //SkillCharacterEffectModel effectModel = hostSkill.getSkillAttributes().getDevelopSkillEffect(id);
                if(effectModel != null){

                    BasicSkillEffect effect = battleContainer.battlePoolManager.borrowSkillEffect();
                    if(effect != null){
                        effect.init(battleContainer,this,effectModel);
                        if(effectModel.getEffectType() == BasicSkillEffect.EFFECT_TYPE_INTERVAL){
                            intervalEffectList.add(effect);
                        }
                        if(effectModel.getEffectType() == BasicSkillEffect.EFFECT_TYPE_CONTINUE){
                            continueEffectList.add(effect);
                        }
                        if(effectModel.getEffectType() == BasicSkillEffect.EFFECT_TYPE_FINAL){
                            finalEffectList.add(effect);
                        }
                        if(effectModel.getEffectType() == BasicSkillEffect.EFFECT_TYPE_BUFF){
                            buffEffectList.add(effect);
                        }
                    }
                }else{
                    logger.error("skill carrier config error !!! can not found effect [id="+id+"] in carrier [id="+carrierId+"]");
                }
            }
        }

        //System.out.println("carrier effect size = "+(intervalEffectList.size()+continueEffectList.size()+finalEffectList.size()+buffEffectList.size()));

        this.clientShow = false;
    }

    public void setClientShow(boolean clientShow){
        this.clientShow = clientShow;
    }

    @Override
    public void release() {

        int size = intervalEffectList.size();
        while(size > 0){
            size --;
            battleContainer.battlePoolManager.restoreSkillEffect(intervalEffectList.get(0));
        }
        intervalEffectList.clear();

        size = continueEffectList.size();
        while(size > 0){
            size --;
            battleContainer.battlePoolManager.restoreSkillEffect(continueEffectList.get(0));
        }
        continueEffectList.clear();

        size = finalEffectList.size();
        while(size > 0){
            size --;
            battleContainer.battlePoolManager.restoreSkillEffect(finalEffectList.get(0));
        }
        finalEffectList.clear();

        size = buffEffectList.size();
        while(size > 0){
            size --;
            battleContainer.battlePoolManager.restoreSkillEffect(buffEffectList.get(0));
        }
        buffEffectList.clear();

        battleContainer = null;
        mapLogic = null;
        carrierModel = null;

        carrierId = 0;
        carrierForm = 0;
        carrierType = 0;
        formParm = null;
        canCollision = false;
        range = 0;
        heightLocus = 0;
        moveSpeed = 0;
        targetLocation = null;
        effectTimes = 0;
        enoughTimesDisappear = false;
        activationTime = 0;
        durationTime = 0;

        moveType = 0;

        prePosition[0] = 0;
        prePosition[1] = 0;
        prePosition[2] = 0;

        currentPosition[0] = 0;
        currentPosition[1] = 0;
        currentPosition[2] = 0;

        targetPosition[0] = 0;
        targetPosition[1] = 0;
        targetPosition[2] = 0;

        direction = 0;

        modelId = 0;

        state = 0;

        skillReleaseData = null;
        hostSkill = null;
        skillBuff = null;

        triggerRole = null;
        targetRole = null;
        targetCarrier = null;

        skillIndicatorPos = null;

        clientShow = false;
    }

    @Override
    public void selfCheak() {
        if(hostRole != null && hostRole.getState() == RoleStateConstant.ROLE_STATE_DEATH){
            hostRole = null;
        }
        if(triggerRole != null && triggerRole.getState() == RoleStateConstant.ROLE_STATE_DEATH){
            triggerRole = null;
        }
        if(targetRole != null && targetRole.getState() == RoleStateConstant.ROLE_STATE_DEATH){
            targetRole = null;
        }
    }

    public void update(int frameNum){
        /**
         * 载体通用更新
         */
        updateGeneral(frameNum);

        /**
         * 载体特殊化更新
         */
        updateSpecial(frameNum);
    }

    public void updateGeneral(int frameNum){

        if(state != CARRIER_STATE_DESTROY ){
            if(activationTime > 0){
                activationTime -= GameConstant.FRAME_TIME;
                return;
            }

            if(state == CARRIER_STATE_INIT){
                //释放离散效果
                intervalEffectRelease();
                return;
            }
            if(durationTime > 0){
                durationTime -= GameConstant.FRAME_TIME;
            }

            if(durationTime <= 0){
                if(targetRole != null){
                    targetRole.setFreeState();
                }
                //释放最终效果
                finalEffectRelease();
                state = CARRIER_STATE_DESTROY;
            }

            //释放离散效果
            intervalEffectRelease();

        }else{
            removeSelf();
        }
    }

    public void updateSpecial(int frameNum){
        switch (carrierType){
            case CARRIER_TYPE_TARGET_BULLET : SkillCarrierTargetBullet.update(this,frameNum);break;
            case CARRIER_TYPE_POSITION_BULLET: SkillCarrierPositionBullet.update(this,frameNum);break;
            case CARRIER_TYPE_DIRECTION_BULLET: SkillCarrierDirectionBullet.update(this,frameNum);break;
            case CARRIER_TYPE_TARGET_MOVE: SkillCarrierTargetMove.update(this,frameNum);break;
            case CARRIER_TYPE_POSITION_MOVE: SkillCarrierPositionMove.update(this,frameNum);break;
            case CARRIER_TYPE_DIRECTION_MOVE: SkillCarrierDirectionMove.update(this,frameNum);break;
            case CARRIER_TYPE_POSITION_MAGIC: SkillCarrierPositionMagic.update(this,frameNum);break;
            case CARRIER_TYPE_FLASH_MOVE: SkillCarrierFlashMove.update(this,frameNum);break;
            case CARRIER_TYPE_FOLLOW_MAGIC: SkillCarrierFollowMagic.update(this,frameNum);break;
        }
    }

    public BattleSkillCarrierInfo getSyncData(){

        if(state == CARRIER_STATE_INIT){
            if(activationTime > 0){
                return null;
            }
            state = CARRIER_STATE_MOVE;

            if(clientShow && modelId != 0){

                int mountModelId = 0;

                if(carrierType == CARRIER_TYPE_FOLLOW_MAGIC){
                    if(targetRole == null){
                        mountModelId = -1;
                    }else{
                        mountModelId = targetRole.getModelId();
                    }
                }else{

                    if(carrierType == CARRIER_TYPE_TARGET_MOVE || carrierType == CARRIER_TYPE_POSITION_MOVE || carrierType == CARRIER_TYPE_DIRECTION_MOVE){
                        if(triggerRole == null){
                            mountModelId = -1;
                        }else{
                            mountModelId = triggerRole.getModelId();
                        }
                    }

                    if(carrierType == CARRIER_TYPE_TARGET_BULLET || carrierType == CARRIER_TYPE_TARGET_MOVE){
                        if(targetRole == null){
                            mountModelId = -1;
                        }else{
                            mountModelId = targetRole.getModelId();
                        }
                    }

                }

                if(mountModelId >= 0){

                    BattleSkillCarrierInfo skillCarrier = (BattleSkillCarrierInfo)ProtocolPool.getInstance().borrowProtocol(BattleSkillCarrierInfo.code);

                    skillCarrier.setCarrierId(carrierId);
                    skillCarrier.setModelId(modelId);
                    skillCarrier.setCarrierType(carrierType);
                    skillCarrier.setState(state);
                    skillCarrier.setShowDieEffect(false);

                    if(carrierType == CARRIER_TYPE_POSITION_BULLET){
                        BattlePosition battlePosition = (BattlePosition)ProtocolPool.getInstance().borrowProtocol(BattlePosition.code);
                        battlePosition.setMapIndex(mapLogic.getMapIndex());
                        battlePosition.setPosX((int)(targetPosition[0]*100));
                        battlePosition.setPosZ((int)(targetPosition[2]*100));
                        battlePosition.setDirection(direction);
                        skillCarrier.setTargetPos(battlePosition);
                    }

                    if(carrierType == CARRIER_TYPE_POSITION_MOVE){
                        BattlePosition battlePosition = (BattlePosition)ProtocolPool.getInstance().borrowProtocol(BattlePosition.code);
                        battlePosition.setMapIndex(mapLogic.getMapIndex());
                        battlePosition.setPosX((int)(targetPosition[0]*100));
                        battlePosition.setPosZ((int)(targetPosition[2]*100));
                        battlePosition.setDirection(triggerRole.getDirection());
                        skillCarrier.setTargetPos(battlePosition);
                    }

                    BattlePosition battlePosition = (BattlePosition)ProtocolPool.getInstance().borrowProtocol(BattlePosition.code);
                    battlePosition.setMapIndex(mapLogic.getMapIndex());
                    battlePosition.setPosX((int)(currentPosition[0]*100));
                    battlePosition.setPosZ((int)(currentPosition[2]*100));
                    battlePosition.setDirection(direction);
                    skillCarrier.setEffectPos(battlePosition);

                    skillCarrier.setMoveLocus(heightLocus);
                    skillCarrier.setSpeed((int)(moveSpeed*100));
                    skillCarrier.setDistance((int)(range*100));
                    skillCarrier.setMoveTime(durationTime);

                    skillCarrier.setMountModelId(mountModelId);
                    return skillCarrier;

                }

            }
            return null;

        }


        if(state == CARRIER_STATE_DESTROY ){
            BattleSkillCarrierInfo skillCarrier = null;
            if(clientShow && modelId != 0){
                skillCarrier = (BattleSkillCarrierInfo)ProtocolPool.getInstance().borrowProtocol(BattleSkillCarrierInfo.code);

                skillCarrier.setCarrierId(carrierId);
                skillCarrier.setModelId(modelId);
                skillCarrier.setState(state);

                skillCarrier.setShowDieEffect(true);
            }

            return skillCarrier;

        }

        return null;
    }

    /**
     * 检测碰撞
     */
    public void collisionTest(){

        List<BattleRole> hitRoles = getShapCollisionRoles(camp);

        if(hitRoles != null && hitRoles.size() >0){
            for(BattleRole role: hitRoles){

                if(effectTimes > 0){
                    if(continueEffectRelease(role)){
                        effectTimes  -- ;
                    }
                }

                //够次数后消失
                if(effectTimes <= 0 && enoughTimesDisappear){
                    state = CARRIER_STATE_DESTROY;
                }

            }
        }

    }

    /**
     *
     */
    public List<BattleRole> getShapCollisionRoles(byte camp){
        List<BattleRole> hitRoles = mapLogic.getCollisionRole(this,null, camp,carrierForm,formParm,prePosition,currentPosition);
        return hitRoles;
    }

    public BattleRole getInCarrierRole(){
        return null;
    }

    /**
     * 产生间隔效果（carrier，buf,hurt......）
     */
    public void intervalEffectRelease(){
        for(BasicSkillEffect effect : intervalEffectList){
            effect.intervalEffectRelease();
        }
    }

    /**
     * 产生持续效果（carrier，buf,hurt......）
     */
    public boolean continueEffectRelease(BattleRole triggerRole){
        boolean newTrigger = false;
        for(BasicSkillEffect effect : continueEffectList){
            if(!effect.containsTriggerRole(triggerRole)){
                newTrigger = true;
                effect.continueEffectRelease(triggerRole);
            }
        }
        return newTrigger;
    }

    /**
     * 产生最终效果（carrier，buf,hurt......）
     */
    public void finalEffectRelease(){
        for(BasicSkillEffect effect : finalEffectList){
            effect.finalEffectRelease();
        }
    }

    public void buffEffectRelease(BattleRole triggerRole){
        for(BasicSkillEffect effect : buffEffectList){
            effect.buffEffectRelease(triggerRole);
        }
    }

    public void removeSelf(){
        skillReleaseData.removeDestroyCarrier(this);
    }

    public byte getState() {
        return state;
    }

    public int getModelId() {
        return modelId;
    }

    public int getCarrierId() {
        return carrierId;
    }

    public byte getMapIndex(){
        return mapLogic.getMapIndex();
    }

    public void setCurrentPosition(float x,float z) {
        this.currentPosition[0] = x;
        this.currentPosition[1] = currentPosition[1];
        this.currentPosition[2] = z;
    }

    public float[] getCurrentPosition() {
        return currentPosition;
    }

    public void setPrePosition() {
        this.prePosition[0] = currentPosition[0];
        this.prePosition[1] = currentPosition[1];
        this.prePosition[2] = currentPosition[2];
    }

    public float[] getPrePosition() {
        return prePosition;
    }

    public float[] getTargetPosition() {
        return targetPosition;
    }

    public short getDirection() {
        return direction;
    }

    public byte getCarrierType() {
        return carrierType;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public int getDurationTime() {
        return durationTime;
    }

    public BattleRole getTargetRole() {
        return targetRole;
    }

    public BattlePosition getSkillIndicatorPos() {
        return skillIndicatorPos;
    }

    public BattleRole getTriggerRole() {
        return triggerRole;
    }

    @Override
    public String toString() {
        return "BasicSkillCarrier{" +
                "using=" + isUsing() +
                ", carrierId=" + carrierId +
                ", modelId=" + modelId +
                ", state=" + state +
                ", hostSkill=" + hostSkill +
                ", hostRole=" + hostRole +
                ", skillBuff=" + skillBuff +
                ", triggerRole=" + triggerRole +
                ", targetRole=" + targetRole +
                ", targetCarrier=" + targetCarrier +
                '}';
    }
}
