package com.dykj.rpg.battle.basic;

import com.dykj.rpg.battle.carrier.*;
import com.dykj.rpg.battle.constant.GameConstant;
import com.dykj.rpg.battle.constant.RoleAnimConstant;
import com.dykj.rpg.battle.constant.RoleStateConstant;
import com.dykj.rpg.battle.detour.DetourHandler;
import com.dykj.rpg.battle.dictionary.StaticDictionary;
import com.dykj.rpg.battle.logic.*;
import com.dykj.rpg.battle.manager.BattlePoolManager;
import com.dykj.rpg.battle.pool.TemporaryPoolManager;
import com.dykj.rpg.battle.role.BattleRole;
import com.dykj.rpg.common.attribute.consts.ElementTypeConstant;
import com.dykj.rpg.common.config.model.SkillCharacterCarrierModel;
import com.dykj.rpg.common.config.model.SkillCharacterEffectModel;
import com.dykj.rpg.common.config.model.SkillCharacterStateModel;
import com.dykj.rpg.net.protocol.Protocol;
import com.dykj.rpg.net.protocol.ProtocolPool;
import com.dykj.rpg.protocol.battle.BattlePosition;
import com.dykj.rpg.protocol.battle.BattleSkillEffectInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class BasicSkillEffect extends BattleObject {

    Logger logger = LoggerFactory.getLogger("BasicSkillEffect");

    Random random = new Random();

    /**
     * --------------------------------效果类型--------------------------------
     */
    /**
     * 离散类型生效的效果 (载体的生命周期内，每隔一段时间主动触发的效果[载体,伤害计算等])
     */
    public final static byte EFFECT_TYPE_INTERVAL = 1;
    /**
     * 连续类型生效的效果 (载体的生命周期内，被动触发的效果)
     */
    public final static byte EFFECT_TYPE_CONTINUE = 2;
    /**
     * 最终效果 (载体死亡时触发的效果)
     */
    public final static byte EFFECT_TYPE_FINAL = 3;
    /**
     * 持续类效果 (buff对目标属性的持续修改)
     */
    public final static byte EFFECT_TYPE_BUFF = 4;
    /**
     * --------------------------------效果释放主体类型-------------------------------
     */
    /**
     * 施法指示器
     */
    private final static byte EFFECT_RELEASE_HOST_INDICATOR = 1;
    /**
     * 技能释放者
     */
    private final static byte EFFECT_RELEASE_HOST_RELEASER = 2;
    /**
     * 目标
     */
    private final static byte EFFECT_RELEASE_HOST_TARGET = 3;
    /**
     * 自身
     */
    private final static byte EFFECT_RELEASE_HOST_SELF = 4;
    /**
     * 效果触发者
     */
    private final static byte EFFECT_RELEASE_HOST_TRIGGER = 5;

    /**
     *  --------------------------------魔法操作--------------------------------
     */
    /**
     * 产生子弹载体
     */
    private final static byte EFFECT_OPERATION_CREATE_BULLET_CARRIER = 1;
    /**
     * 产生位移载体
     */
    private final static byte EFFECT_OPERATION_CREATE_MOVE_CARRIER = 2;
    /**
     * 基础元素伤害计算
     */
    private final static byte EFFECT_OPERATION_HURT_ELEMENT = 11;
    /**
     * 治疗计算
     */
    private final static byte EFFECT_OPERATION_TREATMENT = 13;
    /**
     * 安装状态魔法
     */
    private final static byte EFFECT_OPERATION_INSTALL_MAGIC_STATE = 21;
    /**
     * 清除状态魔法
     */
    private final static byte EFFECT_OPERATION_CLEAR_MAGIC_STATE = 22;
    /**
     * 改变属性
     */
    private final static byte EFFECT_OPERATION_CHANGE_ATTRIBUTE = 40;

    /**
     * --------------------------------效果状态类型--------------------------------
     */
    /**
     * 初始化
     */
    private final static byte EFFECT_STATE_INIT = 0;
    /**
     * 等待
     */
    private final static byte EFFECT_STATE_WAIT = 1;
    /**
     * 销毁
     */
    private final static byte EFFECT_STATE_DESTROY = 2;

    public BattleContainer battleContainer;

    private MapLogic mapLogic;

    private BattleRole hostRole;

    private BasicRoleSkill hostSkill;

    /**
     * 效果的父载体
     */
    private BasicSkillCarrier preSkillCarrier;

    private SkillCharacterEffectModel effectModel;

    /**
     * 效果ID
     */
    private int effectId;
    /**
     * 效果类型，效果产生的时间点
     */
    private byte effectType;
    /**
     * 效果产生作用延迟时间
     */
    private int operationDelay;
    /**
     * 效果释放间隔时间
     */
    private int effectInterval;
    /**
     * 最大释放次数
     */
    private int maxEffectTimes;
    /**
     * 效果作用对象过滤
     */
    private List<List<Integer>> filterType;
    /**
     * 效果触发后产生的作用类型 [载体，伤害计算]
     */
    private byte operation;
    /**
     * -----------------------产生的作用为载体-----------------------
     */
    /**
     *  效果产生的载体ID
     */
    private int skillCarrierId;
    /**
     * 效果产生的载体数量
     */
    private int skillCarrierNum;
    /**
     * -----------------------产生的作用为伤害-----------------------
     */
    /**
     * 元素类型
     */
    private byte elementType;
    /**
     * 技能伤害加成率
     */
    private int damageProp;
    /**
     * 技能伤害附加值
     */
    private int damageValue;


    private byte state;

    /**
     * -----------------------产生的作用为安装魔法状态-----------------------
     */
    private int skillStateId;

    /**
     * 操作参数修正
     */
    private List<List<Integer>> operationParamFixs;

    /**
     * 等待时间
     */
    private int waitTime;
    /**
     * 释放次数
     */
    private int effectTimes;

    /**
     * 中了连续效果的对象集合
     */
    protected List<BattleRole> continueEffectRoles = new ArrayList<>();

    public BasicSkillEffect(){

    }

    public void init(BattleContainer battleContainer,BasicSkillCarrier skillCarrier, SkillCharacterEffectModel effectModel){
        this.battleContainer = battleContainer;
        this.mapLogic = skillCarrier.mapLogic;
        this.hostSkill = skillCarrier.hostSkill;
        this.preSkillCarrier = skillCarrier;
        this.effectModel = effectModel;

        this.effectId = effectModel.getEffectId();
        this.effectType = effectModel.getEffectType().byteValue();
        this.filterType = effectModel.getFilterType();
        this.operationDelay = effectModel.getOperationDelay()/10;
        this.effectInterval = effectModel.getEffectInterval()/10;
        this.maxEffectTimes = (effectModel.getEffectTimes() <=0 ) ? GameConstant.MAX_INTEGER : effectModel.getEffectTimes();
        this.operation = effectModel.getOperation().byteValue();

        List<List<Integer>> operationParmB = effectModel.getOperationParmB();
        if(operationParmB != null && operationParmB.size() > 0){
            this.operationParamFixs = operationParmB;
        }

        if(this.operation == EFFECT_OPERATION_CREATE_BULLET_CARRIER || this.operation == EFFECT_OPERATION_CREATE_MOVE_CARRIER){
            this.skillCarrierId = Integer.parseInt(effectModel.getOperationParmA());
            if(this.operationParamFixs != null){
                this.skillCarrierNum = this.operationParamFixs.get(0).get(0);
            }else{
                this.skillCarrierNum = 1;
            }
        }
        if(this.operation == EFFECT_OPERATION_HURT_ELEMENT){
            this.elementType = Byte.parseByte(effectModel.getOperationParmA());
            this.damageProp = effectModel.getDamageProp();
            this.damageProp = (this.damageProp == 0) ? 10000 : this.damageProp;
            this.damageValue = effectModel.getDamageValue();
        }
        if(this.operation == EFFECT_OPERATION_TREATMENT){
            this.elementType = ElementTypeConstant.ZHI_LIAO;
            this.damageProp = effectModel.getDamageProp();
            this.damageProp = (this.damageProp == 0) ? 10000 : this.damageProp;
            this.damageValue = effectModel.getDamageValue();
        }
        if(this.operation == EFFECT_OPERATION_INSTALL_MAGIC_STATE){
            this.skillStateId = Integer.parseInt(effectModel.getOperationParmA());
        }

        this.state = EFFECT_STATE_INIT;
        this.waitTime = this.operationDelay;
        this.effectTimes = 0;

        if(preSkillCarrier.hostSkill != null){
            hostRole = preSkillCarrier.hostSkill.getHostRole();
            continueEffectRelease(hostRole);
        }

        if(preSkillCarrier.skillBuff != null){
            hostRole = preSkillCarrier.skillBuff.getTargetRole();
            continueEffectRelease(hostRole);
        }

    }

    @Override
    public void release() {

        battleContainer = null;
        mapLogic = null;
        hostRole = null;
        hostSkill = null;
        preSkillCarrier = null;
        effectModel = null;

        effectId = 0;
        effectType = 0;
        operationDelay = 0;
        effectInterval = 0;
        maxEffectTimes = 0;
        filterType = null;
        operation = 0;
        skillCarrierId = 0;
        skillCarrierNum = 0;

        elementType = 0;
        damageProp = 0;
        damageValue = 0;

        state = 0;

        skillStateId = 0;
        operationParamFixs = null;

        waitTime = 0;
        effectTimes = 0;

        continueEffectRoles.clear();
    }

    @Override
    public void selfCheak() {

    }

    /**
     * 用于处理离散类型的效果
     */
    public void intervalEffectRelease(){
        //离散类型生效的效果
        if(effectType == EFFECT_TYPE_INTERVAL){
            if(state == EFFECT_STATE_INIT){
                state = EFFECT_STATE_WAIT;
            }
            if(state == EFFECT_STATE_WAIT){
                if(waitTime > 0){
                    waitTime -= GameConstant.FRAME_TIME;
                }
                if(waitTime <= 0){
                    waitTime = effectInterval;

                    releaseEffect(null,0);

                    if(maxEffectTimes > 0){
                        effectTimes ++;

                        if(effectTimes == maxEffectTimes){
                            state = EFFECT_STATE_DESTROY;
                        }
                    }
                }

            }
        }

    }

    /**
     * 用于处理持续类型的效果 触发者
     */
    public void continueEffectRelease(BattleRole triggerRole){

        /**
         * 当有多个持续性效果时，为了防止前一个effect为伤害型效果致使触发者死亡，
         * 导致后一个effect获取死亡触发者会产生各种异常，需要再每次效果产生时做一个严格的判断
         */
        if(triggerRole == null || triggerRole.getState() == RoleStateConstant.ROLE_STATE_DEATH){
            return;
        }

        //持续性的效果有数量限制
        if(effectType == EFFECT_TYPE_CONTINUE){
            if(state == EFFECT_STATE_INIT){
                state = EFFECT_STATE_WAIT;
            }
            if(state == EFFECT_STATE_WAIT){
                if(waitTime > 0){
                    waitTime -= GameConstant.FRAME_TIME;
                }
                if(waitTime <= 0){

                }

                if(triggerRole != null){
                    //过滤掉效果不生效的对象
                    if(!couldEffectRole(triggerRole)){
                        return;
                    }
                }

                //连续类型的效果每个角色只能作用一次
                continueEffectRoles.add(triggerRole);
                //System.out.println("continueEffect hitRole size = 1");
                releaseEffect(triggerRole,operationDelay);

                if(maxEffectTimes > 0){
                    effectTimes ++;

                    if(effectTimes == maxEffectTimes){
                        state = EFFECT_STATE_DESTROY;
                    }
                }

            }
        }
    }

    /**
     * 用于处理最终类型的效果
     */
    public void finalEffectRelease(){
        //最终类型的效果没有限制
        if(effectType == EFFECT_TYPE_FINAL){
            releaseEffect(null,operationDelay);
        }
    }

    /**
     * 用于处理buff的持续类型的效果
     */
    public void buffEffectRelease(BattleRole triggerRole){
        /**
         * 当有多个持续性效果时，为了防止前一个effect为伤害型效果致使触发者死亡，
         * 导致后一个effect获取死亡触发者会产生各种异常，需要再每次效果产生时做一个严格的判断
         */
        if(triggerRole == null || triggerRole.getState() == RoleStateConstant.ROLE_STATE_DEATH){
            return;
        }

        if(effectType == EFFECT_TYPE_BUFF){
            releaseEffect(triggerRole,operationDelay);
        }
    }

    /**
     * 释放效果
     */
    private void releaseEffect(BattleRole triggerRole,int activationTime){
        //System.out.println("---------------releaseEffect---------------");
        //System.out.println("release effectId = "+effectId);

//        if(effectId == 9900101){
//            int i = 0;
//        }

        //如果施法者死亡，停止后续载体的产生
        if(hostSkill.hostRole == null){
            return;
        }

        //如果施法者锁定的目标消失，停止后续载体的产生
        //if(hostSkill.hostRole.targetRole == null){
        //    return;
        //}

        //产生载体
        if(operation == EFFECT_OPERATION_CREATE_BULLET_CARRIER || operation == EFFECT_OPERATION_CREATE_MOVE_CARRIER){
            effectCreateCarrier(triggerRole,activationTime);
            return ;
        }
        //产生伤害
        if(operation == EFFECT_OPERATION_HURT_ELEMENT){
            effectHurtElement(triggerRole);
            return ;
        }
        //治疗
        if(operation == EFFECT_OPERATION_TREATMENT){
            effectTreatment(triggerRole);
            return ;
        }
        //安装魔法状态
        if(operation == EFFECT_OPERATION_INSTALL_MAGIC_STATE){
            effectInstallMagicState(triggerRole);
            return ;
        }
        //安装魔法状态
        if(operation == EFFECT_OPERATION_CLEAR_MAGIC_STATE){
            effectClearMagicState(triggerRole);
            return ;
        }
        //改变属性
        if(operation == EFFECT_OPERATION_CHANGE_ATTRIBUTE){
            effectChangeAttribute(triggerRole);
            return ;
        }

    }

    /**
     * 产生载体
     */
    List<float[]> carrierStartPosList = new ArrayList<>();
    List<BattleRole> carrierStartRoleList = new ArrayList<>();
    private void effectCreateCarrier(BattleRole triggerRole,int activationTime){

        if(triggerRole != null){
            //过滤掉效果不生效的对象
            if(!couldEffectRole(triggerRole)){
                return;
            }
        }

        if(skillCarrierId != 0){

            //System.out.println("--------------effectCreateCarrier--------------");
            //System.out.println("skillCarrierId = "+skillCarrierId);
//            if(skillCarrierId == 260071){
//                System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
//            }
            SkillCharacterCarrierModel carrierModel = hostSkill.getSkillAttributes().getDevelopSkillCarrier(skillCarrierId);
            if(carrierModel != null){

                if(triggerRole == null){
                    triggerRole = preSkillCarrier.getTriggerRole();
                }

                if(triggerRole != null && triggerRole.getState() == RoleStateConstant.ROLE_STATE_DEATH){
                    triggerRole = null;
                }

                carrierStartPosList.clear();
                carrierStartRoleList.clear();

                if(triggerRole != null){
                    carrierStartRoleList.add(triggerRole);
                }

                float[] carrierStartPosition = ParseLocation.parsePosition(preSkillCarrier,effectModel.getTargetType(),triggerRole);

                if(carrierStartPosition != null && carrierStartPosition.length == 3){
                    carrierStartPosList.add(carrierStartPosition);
                }

                Object obj = ParseLocation.parseTargetRole(preSkillCarrier,effectModel.getTargetType(),triggerRole);
                if(obj != null){

                    if(obj instanceof List){
                        List<BattleRole> objList = (List<BattleRole>)obj;
                        if(objList.size() > 0){
                            carrierStartRoleList.clear();
                        }
                        for(BattleRole role : objList){
                            if(couldEffectRole(role)){
                                carrierStartRoleList.add(role);
                            }
                        }
                    }
                }

                if(carrierStartPosList.size() == 0 && carrierStartRoleList.size() == 0){
                    //logger.error("effect config error! can not get start position of the carrier, maybe the param target_type in the effect [id="+effectId+"] is error !!!");
                    return;
                }


                //连续效果要继承上一个载体的生命周期
                int durationTime = 0;
                if(effectType == EFFECT_TYPE_CONTINUE){
                    durationTime = preSkillCarrier.getDurationTime();
                    //carrier = new BasicSkillCarrier(mapLogic,hostSkill,carrierModel, startPos,preSkillCarrier.getDurationTime(),targetPosition,targetDirection,targetRole);
                }

                if(carrierModel.getCarrierType() == BasicSkillCarrier.CARRIER_TYPE_TARGET_BULLET){ //1
                    createTargetBulletCarrier(carrierModel,carrierStartPosList,triggerRole,activationTime,durationTime);
                }

                if(carrierModel.getCarrierType() == BasicSkillCarrier.CARRIER_TYPE_POSITION_BULLET){ //2
                    createPositionBulletCarrier(carrierModel,carrierStartPosList,triggerRole,activationTime,durationTime);
                }

                if(carrierModel.getCarrierType() == BasicSkillCarrier.CARRIER_TYPE_DIRECTION_BULLET){ //3
                    createDirectionBulletCarrier(carrierModel,carrierStartPosList,triggerRole,activationTime,durationTime);
                }

                if(carrierModel.getCarrierType() == BasicSkillCarrier.CARRIER_TYPE_TARGET_MOVE){ //4
                    createTargetMoveCarrier(carrierModel,carrierStartRoleList,activationTime,durationTime);
                }

                if(carrierModel.getCarrierType() == BasicSkillCarrier.CARRIER_TYPE_POSITION_MOVE){ //5
                    createPositionMoveCarrier(carrierModel,carrierStartRoleList,activationTime,durationTime);
                }

                if(carrierModel.getCarrierType() == BasicSkillCarrier.CARRIER_TYPE_DIRECTION_MOVE){ //6
                    createDirectionMoveCarrier(carrierModel,carrierStartRoleList,activationTime,durationTime);
                }

                if(carrierModel.getCarrierType() == BasicSkillCarrier.CARRIER_TYPE_POSITION_MAGIC){ //7
                    createPositionMagicCarrier(carrierModel,carrierStartPosList,triggerRole,activationTime,durationTime);
                }

                if(carrierModel.getCarrierType() == BasicSkillCarrier.CARRIER_TYPE_FLASH_MOVE){ //8
                    createFlashMoveCarrier(carrierModel,carrierStartRoleList,activationTime,durationTime);
                }

                if(carrierModel.getCarrierType() == BasicSkillCarrier.CARRIER_TYPE_FOLLOW_MAGIC){ //9
                    createFollowMagicCarrier(carrierModel,carrierStartRoleList,activationTime,durationTime);
                }

            }else{
                logger.error("effect config error! can not find the carrier [id="+skillCarrierId+"] in the effect [id="+effectId+"]");
            }
        }
    }

    private void createTargetBulletCarrier(SkillCharacterCarrierModel carrierModel,List<float[]> startPoss,BattleRole triggerRole,int activationTime,int durationTime){
        if(startPoss != null && startPoss.size() == 0){
            logger.error("effect config error! can not get start position of the target bullet carrier, the param target_type in the effect [id="+effectId+"] maybe is wrong !!!");
            return;
        }

        for(float[] startPos : startPoss){

            Object obj = parseTargetRole(carrierModel,triggerRole);

            List<BattleRole> targetRoles = null;
            BasicSkillCarrier targetCarrier = null;

            if(obj instanceof List){
                targetRoles = (List<BattleRole>)obj;
            }

//            if(obj instanceof BasicSkillCarrier){
//                targetCarrier = (BasicSkillCarrier)obj;
//            }

            if(targetRoles == null && targetCarrier == null){
                return;
            }

            for(BattleRole targetRole : targetRoles){
                BasicSkillCarrier carrier = battleContainer.battlePoolManager.borrowSkillCarrier();
                carrier.init(battleContainer,mapLogic,preSkillCarrier.skillReleaseData,preSkillCarrier.camp,activationTime,preSkillCarrier.skillIndicatorPos,triggerRole);
                carrier.setEffectByModel(carrierModel,durationTime);
                carrier.setStartPosition(startPos);
                carrier.setTargetRole(targetRole);
                carrier.setClientShow(true);

                preSkillCarrier.skillReleaseData.addNewCarrier(carrier);
                //添加效果信息，为推送到客户端做准备
                addSyncEffectInfo(mapLogic.getMapIndex(),carrier.getCurrentPosition(),carrier.getDirection());

            }

        }
    }

    /**
     * 创建地点子弹载体 BasicSkillCarrier.CARRIER_TYPE_POSITION_BULLET
     * @param carrierModel
     * @param startPoss
     * @param triggerRole
     * @param activationTime
     * @param durationTime
     */
    private void createPositionBulletCarrier(SkillCharacterCarrierModel carrierModel,List<float[]> startPoss,BattleRole triggerRole,int activationTime,int durationTime){
        if(startPoss != null && startPoss.size() == 0){
            logger.error("effect config error! can not get start position of the position bullet carrier, the param target_type in the effect [id="+effectId+"] maybe is wrong !!!");
            return;
        }

        for(float[] startPos : startPoss){

            float[][] poss = parseTargetPosition(carrierModel,triggerRole);

            if(poss == null || poss.length == 0){
                return;
            }

            for(float[] pos : poss){

                if(pos != null){
                    BasicSkillCarrier carrier = battleContainer.battlePoolManager.borrowSkillCarrier();

                    carrier.init(battleContainer,mapLogic,preSkillCarrier.skillReleaseData,preSkillCarrier.camp,activationTime,preSkillCarrier.skillIndicatorPos,triggerRole);
                    carrier.setEffectByModel(carrierModel,durationTime);
                    carrier.setStartPosition(startPos);
                    carrier.setTargetPosition(pos);
                    carrier.setClientShow(true);

                    preSkillCarrier.skillReleaseData.addNewCarrier(carrier);
                    //添加效果信息，为推送到客户端做准备
                    addSyncEffectInfo(mapLogic.getMapIndex(),carrier.getCurrentPosition(),carrier.getDirection());
                }

            }

        }
    }

    /**
     * 创建方向子弹载体 BasicSkillCarrier.CARRIER_TYPE_DIRECTION_BULLET
     * @param carrierModel
     * @param startPoss
     * @param triggerRole
     * @param activationTime
     * @param durationTime
     */
    private void createDirectionBulletCarrier(SkillCharacterCarrierModel carrierModel,List<float[]> startPoss,BattleRole triggerRole,int activationTime,int durationTime){
        if(startPoss != null && startPoss.size() == 0){
            logger.error("effect config error! can not get start position of the direction bullet move carrier, the param target_type in the effect [id="+effectId+"] maybe is wrong !!!");
            return;
        }

        for(float[] startPos : startPoss){

            float[][] poss = parseCarrierPosFix(startPos);

            int carrierSize = 1;
            if(poss != null){
                carrierSize = poss.length;
            }

            float[] carrierStartPos = null;
            for(int i=0;i<carrierSize;i++){
                if(poss != null){
                    carrierStartPos = poss[i];
                }else{
                    carrierStartPos = startPos;
                }

                short[] dirs = parseTargetDirection(carrierModel,triggerRole);

                if(dirs == null || dirs.length == 0){
                    return;
                }

                for(short dir : dirs){
                    BasicSkillCarrier carrier = battleContainer.battlePoolManager.borrowSkillCarrier();
                    carrier.init(battleContainer,mapLogic,preSkillCarrier.skillReleaseData,preSkillCarrier.camp,activationTime,preSkillCarrier.skillIndicatorPos,triggerRole);
                    carrier.setEffectByModel(carrierModel,durationTime);
                    carrier.setStartPosition(carrierStartPos);
                    carrier.setTargetDirection(dir);
                    carrier.setClientShow(true);

                    preSkillCarrier.skillReleaseData.addNewCarrier(carrier);
                    //添加效果信息，为推送到客户端做准备
                    addSyncEffectInfo(mapLogic.getMapIndex(),carrier.getCurrentPosition(),carrier.getDirection());

                }
            }
        }
    }

    /**
     * 创建目标位移载体 BasicSkillCarrier.CARRIER_TYPE_TARGET_MOVE
     * @param carrierModel
     * @param battleRoles
     * @param activationTime
     * @param durationTime
     */
    private void createTargetMoveCarrier(SkillCharacterCarrierModel carrierModel,List<BattleRole> battleRoles,int activationTime,int durationTime){
        if(battleRoles != null && battleRoles.size() == 0){
            logger.error("effect config error! can not get start position of the target move carrier, the param target_type in the effect [id="+effectId+"] maybe is wrong !!!");
            return;
        }

        for(BattleRole battleRole : battleRoles){

            Object obj = parseTargetRole(carrierModel,battleRole);
            BattleRole targetRole = null;
            BasicSkillCarrier targetCarrier = null;

            if(obj instanceof List){
                List<BattleRole> targetRoles = (List<BattleRole>)obj;
                if(targetRoles.size() == 1){
                    targetRole = targetRoles.get(0);
                }
                if(targetRoles.size() > 1){
                    targetRole = targetRoles.get(random.nextInt(targetRoles.size()));
                }
            }

            if(obj instanceof BasicSkillCarrier){
                targetCarrier = (BasicSkillCarrier)obj;
            }

            if(targetRole == null && targetCarrier == null){
                return;
            }

            BasicSkillCarrier carrier = battleContainer.battlePoolManager.borrowSkillCarrier();
            carrier.init(battleContainer,mapLogic,preSkillCarrier.skillReleaseData,preSkillCarrier.camp,activationTime,preSkillCarrier.skillIndicatorPos,battleRole);
            carrier.setEffectByModel(carrierModel,durationTime);
            carrier.setStartPosition(battleRole.currentPosition);
            carrier.setTargetRole(targetRole);
            carrier.setTargetCarrier(targetCarrier);
            carrier.setClientShow(false);

            preSkillCarrier.skillReleaseData.addNewCarrier(carrier);
            //添加效果信息，为推送到客户端做准备
            addSyncEffectInfo(mapLogic.getMapIndex(),carrier.getCurrentPosition(),carrier.getDirection());
        }
    }

    /**
     * 创建地点位移载体 BasicSkillCarrier.CARRIER_TYPE_POSITION_MOVE
     * @param carrierModel
     * @param battleRoles
     * @param activationTime
     * @param durationTime
     */
    private void createPositionMoveCarrier(SkillCharacterCarrierModel carrierModel,List<BattleRole> battleRoles,int activationTime,int durationTime){
        if(battleRoles != null && battleRoles.size() == 0){
            logger.error("effect config error! can not get start position of the position move carrier, the param target_type in the effect [id="+effectId+"] maybe is wrong !!!");
            return;
        }

        for(BattleRole battleRole : battleRoles){

            float[][] poss = parseTargetPosition(carrierModel,battleRole);

            if(poss == null || poss.length == 0){
                return;
            }

            float[] targetPos = null;
            int size = poss.length;
            for(int i = 0; i < size;i++){
                if(poss[i] != null){
                    targetPos = poss[i];
                }
            }

            if(targetPos == null){
                return;
            }

            float distance = (float)Math.sqrt(Math.pow(targetPos[0]-battleRole.currentPosition[0],2)+Math.pow(targetPos[2]-battleRole.currentPosition[2],2));
            durationTime = (int)Math.ceil(distance/(carrierModel.getMoveSpeed()/100f)*1000);

            BasicSkillCarrier carrier = battleContainer.battlePoolManager.borrowSkillCarrier();
            carrier.init(battleContainer,mapLogic,preSkillCarrier.skillReleaseData,preSkillCarrier.camp,activationTime,preSkillCarrier.skillIndicatorPos,battleRole);
            carrier.setEffectByModel(carrierModel,durationTime);
            carrier.setStartPosition(battleRole.currentPosition);
            carrier.setTargetPosition(targetPos);
            carrier.setClientShow(true);

            preSkillCarrier.skillReleaseData.addNewCarrier(carrier);
            //添加效果信息，为推送到客户端做准备
            addSyncEffectInfo(mapLogic.getMapIndex(),carrier.getCurrentPosition(),carrier.getDirection());

        }
    }

    /**
     * 创建方向位移载体 BasicSkillCarrier.CARRIER_TYPE_DIRECTION_MOVE
     * @param carrierModel
     * @param battleRoles
     * @param activationTime
     * @param durationTime
     */
    private void createDirectionMoveCarrier(SkillCharacterCarrierModel carrierModel,List<BattleRole> battleRoles,int activationTime,int durationTime){
        if(battleRoles != null && battleRoles.size() == 0){
            logger.error("effect config error! can not get start position of the direction move carrier, the param target_type in the effect [id="+effectId+"] maybe is wrong !!!");
            return;
        }

        BattlePosition skillIndicatorPos = preSkillCarrier.getSkillIndicatorPos();

        for(BattleRole battleRole : battleRoles){

            short[] dirs = parseTargetDirection(carrierModel,battleRole);

            if(dirs == null || dirs.length == 0){
                return;
            }

            short dir = -1;
            if(dirs.length == 1){
                dir = dirs[0];
            }
            if(dirs.length > 1){
                dir = dirs[random.nextInt(dirs.length)];
            }


            BasicSkillCarrier carrier = battleContainer.battlePoolManager.borrowSkillCarrier();
            carrier.init(battleContainer,mapLogic,preSkillCarrier.skillReleaseData,preSkillCarrier.camp,activationTime,skillIndicatorPos,battleRole);
            carrier.setEffectByModel(carrierModel,durationTime);
            carrier.setStartPosition(battleRole.currentPosition);
            carrier.setTargetDirection(dir);
            carrier.setClientShow(false);

            preSkillCarrier.skillReleaseData.addNewCarrier(carrier);
            //添加效果信息，为推送到客户端做准备
            addSyncEffectInfo(mapLogic.getMapIndex(),carrier.getCurrentPosition(),carrier.getDirection());

        }
    }

    /**
     * 创建地点魔法载体 BasicSkillCarrier.CARRIER_TYPE_POSITION_MAGIC
     * @param carrierModel
     * @param startPoss
     * @param activationTime
     * @param durationTime
     */
    private void createPositionMagicCarrier(SkillCharacterCarrierModel carrierModel,List<float[]> startPoss,BattleRole triggerRole,int activationTime,int durationTime){
        if(startPoss != null && startPoss.size() == 0){
            logger.error("effect config error! can not get start position of the position magic carrier, the param target_type in the effect [id="+effectId+"] maybe is wrong !!!");
            return;
        }

        BattlePosition skillIndicatorPos = preSkillCarrier.getSkillIndicatorPos();

        for(float[] startPos : startPoss){

            float[][] poss = parseCarrierPosFix(startPos);

            int carrierSize = 1;
            if(poss != null){
                carrierSize = poss.length;
            }

            float[] carrierStartPos = null;
            for(int i=0;i<carrierSize;i++){
                if(poss != null){
                    carrierStartPos = poss[i];
                }else{
                    carrierStartPos = startPos;
                }

                BasicSkillCarrier carrier = battleContainer.battlePoolManager.borrowSkillCarrier();
                carrier.init(battleContainer,mapLogic,preSkillCarrier.skillReleaseData,preSkillCarrier.camp,activationTime,skillIndicatorPos,triggerRole);
                carrier.setEffectByModel(carrierModel,durationTime);
                carrier.setStartPosition(carrierStartPos);
                carrier.setTargetDirection(hostSkill.getHostRole().getDirection());
                carrier.setClientShow(true);

                preSkillCarrier.skillReleaseData.addNewCarrier(carrier);
                //添加效果信息，为推送到客户端做准备
                addSyncEffectInfo(mapLogic.getMapIndex(),carrier.getCurrentPosition(),carrier.getDirection());
            }

        }
    }

    /**
     * 创建闪现载体 BasicSkillCarrier.CARRIER_TYPE_FLASH_MOVE
     * @param carrierModel
     * @param battleRoles
     * @param activationTime
     * @param durationTime
     */
    private void createFlashMoveCarrier(SkillCharacterCarrierModel carrierModel,List<BattleRole> battleRoles,int activationTime,int durationTime){
        if(battleRoles != null && battleRoles.size() == 0){
            logger.error("effect config error! can not get start position of the flash move carrier, the param target_type in the effect [id="+effectId+"] maybe is wrong !!!");
            return;
        }

        for(BattleRole battleRole : battleRoles){

            float[][] poss = parseTargetPosition(carrierModel,battleRole);

            if(poss == null || poss.length == 0){
                return;
            }

            float[] targetPos = null;
            int size = poss.length;
            for(int i = 0; i < size;i++){
                if(poss[i] != null){
                    targetPos = poss[i];
                }
            }

            if(targetPos == null){
                return;
            }


            BasicSkillCarrier carrier = battleContainer.battlePoolManager.borrowSkillCarrier();
            carrier.init(battleContainer,mapLogic,preSkillCarrier.skillReleaseData,preSkillCarrier.camp,activationTime,preSkillCarrier.skillIndicatorPos,battleRole);
            carrier.setEffectByModel(carrierModel,durationTime);
            carrier.setStartPosition(battleRole.currentPosition);
            carrier.setTargetPosition(targetPos);
            carrier.setClientShow(false);

            preSkillCarrier.skillReleaseData.addNewCarrier(carrier);
            //添加效果信息，为推送到客户端做准备
            addSyncEffectInfo(mapLogic.getMapIndex(),carrier.getCurrentPosition(),carrier.getDirection());

        }
    }

    /**
     * 创建跟随魔法载体 BasicSkillCarrier.CARRIER_TYPE_FOLLOW_MAGIC
     * @param carrierModel
     * @param battleRoles
     * @param activationTime
     * @param durationTime
     */
    private void createFollowMagicCarrier(SkillCharacterCarrierModel carrierModel,List<BattleRole> battleRoles,int activationTime,int durationTime){
        if(battleRoles != null && battleRoles.size() == 0){
            logger.error("effect config error! can not get start position of the follow magic carrier, the param target_type in the effect [id="+effectId+"] maybe is wrong !!!");
            return;
        }

        for(BattleRole battleRole : battleRoles){

            BasicSkillCarrier carrier = battleContainer.battlePoolManager.borrowSkillCarrier();
            carrier.init(battleContainer,mapLogic,preSkillCarrier.skillReleaseData,preSkillCarrier.camp,activationTime,preSkillCarrier.skillIndicatorPos,battleRole);
            carrier.setEffectByModel(carrierModel,durationTime);
            carrier.setStartPosition(battleRole.currentPosition);
            carrier.setTargetRole(battleRole);
            carrier.setClientShow(true);

            preSkillCarrier.skillReleaseData.addNewCarrier(carrier);
            //添加效果信息，为推送到客户端做准备
            addSyncEffectInfo(mapLogic.getMapIndex(),carrier.getCurrentPosition(),carrier.getDirection());

        }
    }

    /**
     * 元素伤害计算
     */
    private void effectHurtElement(BattleRole triggerRole){
        if(triggerRole != null){
            if(!couldEffectRole(triggerRole)){
                return;
            }
        }

        List<BattleRole> hitRoles = null;
        if(effectType == EFFECT_TYPE_CONTINUE){ //连续效果每次只对触发者有效
            if(triggerRole != null){
                hitRoles = new ArrayList<>();
                hitRoles.add(triggerRole);
            }
        }else{

            if(triggerRole == null){
                triggerRole = preSkillCarrier.getTriggerRole();
            }

            Object obj = ParseLocation.parseTargetRole(preSkillCarrier, effectModel.getTargetType(), triggerRole);
            if(obj instanceof List){
                hitRoles = (List<BattleRole>)obj;
            }
        }

        if(hitRoles != null && hitRoles.size() > 0){
            //System.out.println("effect hitRole effectId = "+ effectId +" hitSize = "+hitRoles.size());
            for(BattleRole hitRole : hitRoles){
                //过滤掉效果不生效的对象
                if(!couldEffectRole(hitRole)){
                    continue;
                }

                //闪避判断
                boolean isDodge = DodgeCalculation.isDodge(hostSkill.getHostRole(),hitRole);
                if(!isDodge){ //闪避失败
                    int[] hurtResult = HurtCalculation.getHurt(hostSkill.getHostRole(),hostSkill.getSkillId(),elementType,damageProp,damageValue,hitRole);
                    //if(hurtResult[0] == HurtCalculation.HURT_STATE_NORMAL){
                    if(hurtResult[1] != 0){

                        if(preSkillCarrier.skillReleaseData.skillResourceType == SkillReleaseData.SKILL_RESOURCE_TYPE_BUFF){
                            hitRole.setHitInfo(hostSkill.getHostRole(),preSkillCarrier, effectId,HurtCalculation.HURT_STATE_BUFF,-hurtResult[1]);
                        }else{
                            hitRole.setHitInfo(hostSkill.getHostRole(),preSkillCarrier, effectId,hurtResult[0],-hurtResult[1]);
                        }

                        //只有存活的对象才会触发buff
                        if(hitRole.getState() != RoleStateConstant.ROLE_STATE_DEATH){
                            //成功添加受击目标的ID到技能数据中时才会触发元素buff效果，否则判断为重复添加，不触发元素buff效果
                            if(preSkillCarrier.skillReleaseData.addHitRoleModelId(hitRole.getModelId())){
                                hitRole.buffManager.triggerElementDeBuff(mapLogic,hostSkill);
                            }
                        }

                        //添加效果信息，为推送到客户端做准备
                        addSyncEffectInfo(hitRole.mapIndex,hitRole);

                        //System.out.println("hitRole carrierModelId = "+preSkillCarrier.getModelId()+" roleModelId = "+hitRole.getModelId()+"  hurtNum = "+hurtResult[1]+"  blood = "+hitRole.getBlood());
                    }
                    //}
                }else{//闪避成功
                    hitRole.setHitInfo(hostSkill.getHostRole(), preSkillCarrier,effectId,HurtCalculation.HURT_STATE_SHANBI,0);
                    //闪避日志输出
                    HurtCalculation.showCalculationLog(hostSkill.getHostRole(),hostSkill.getSkillId(),elementType,damageProp,damageValue,hitRole,HurtCalculation.HURT_STATE_SHANBI,0);
                }

            }
        }
    }

    /**
     * 治疗计算
     */
    private void effectTreatment(BattleRole triggerRole){

        if(triggerRole != null){
            if(!couldEffectRole(triggerRole)){
                return;
            }
        }

        List<BattleRole> hitRoles = null;
        if(effectType == EFFECT_TYPE_CONTINUE){ //连续效果每次只对触发者有效
            if(triggerRole != null){
                hitRoles = new ArrayList<>();
                hitRoles.add(triggerRole);
            }
        }else{

            if(triggerRole == null){
                triggerRole = preSkillCarrier.getTriggerRole();
            }

            Object obj = ParseLocation.parseTargetRole(preSkillCarrier, effectModel.getTargetType(), triggerRole);
            if(obj instanceof List){
                hitRoles = (List<BattleRole>)obj;
            }
        }

        if(hitRoles != null && hitRoles.size() > 0){
            //System.out.println("effect treatmentRole size = "+hitRoles.size());
            for(BattleRole hitRole : hitRoles){
                //过滤掉效果不生效的对象
                if(!couldEffectRole(hitRole)){
                    continue;
                }
                int[] hurtResult = HurtCalculation.getHurt(hostSkill.getHostRole(),hostSkill.getSkillId(),elementType,damageProp,damageValue,hitRole);
                if(hurtResult[0] == HurtCalculation.HURT_STATE_ZHILIAO){
                    if(hurtResult[1] != 0){
                        hitRole.setHitInfo(hostSkill.getHostRole(),preSkillCarrier,effectId,hurtResult[0],hurtResult[1]);

                        //成功添加受击目标的ID到技能数据中时才会触发元素buff效果，否则判断为重复添加，不触发元素buff效果
                        //if(preSkillCarrier.skillReleaseData.addHitRoleModelId(hitRole.getModelId())){
                        //    hitRole.buffManager.triggerElementBuff(mapLogic,hostSkill);
                        //}
                        //添加效果信息，为推送到客户端做准备
                        addSyncEffectInfo(hitRole.mapIndex,hitRole);

                        //System.out.println("treatmentRole carrierModelId = "+preSkillCarrier.getModelId()+" roleModelId = "+hitRole.getModelId()+"  hurtNum = "+hurtResult[1]+"  blood = "+hitRole.getBlood());
                    }
                }
            }
        }
    }

    /**
     * 安装魔法状态
     */
    private void effectInstallMagicState(BattleRole triggerRole){

        List<BattleRole> hitRoles = null;
        if(effectType == EFFECT_TYPE_CONTINUE){ //连续效果每次只对触发者有效
            if(triggerRole != null){
                hitRoles = new ArrayList<>();
                hitRoles.add(triggerRole);
            }
        }else{

            Object obj = ParseLocation.parseTargetRole(preSkillCarrier, effectModel.getTargetType(), triggerRole);
            if(obj instanceof List){
                hitRoles = (List<BattleRole>)obj;
            }
            //如果没有配置目标，以下进行默认处理
            if(hitRoles.size() == 0){
                if(triggerRole == null){
                    triggerRole = preSkillCarrier.getTriggerRole();
                    if(triggerRole != null){
                        hitRoles.add(triggerRole);
                    }
                }else{
                    hitRoles.add(triggerRole);
                }
            }
        }

        if(hitRoles != null){
            for(BattleRole hitRole : hitRoles){
                if(!couldEffectRole(hitRole)){
                    continue;
                }
                hitRole.getBuffManager().addBuff(mapLogic,hostSkill,skillStateId);
            }
        }

    }

    /**
     * 清除魔法状态
     */
    private void effectClearMagicState(BattleRole triggerRole){

        if(triggerRole != null){
            if(!couldEffectRole(triggerRole)){
                return;
            }
        }

        if(preSkillCarrier.skillBuff != null){
            //将驱散的属性添加到buff的驱散表中
            preSkillCarrier.skillBuff.addDisappearBuff(operationParamFixs);
        }else{
            logger.error("effect config error! the operation[22] must be created by table skill_character_state !!!");
        }
    }

    /**
     * 改变属性
     */
    private void effectChangeAttribute(BattleRole triggerRole){

        if(preSkillCarrier.skillBuff != null){
            //将属性的修改添加到buff的属性表中
            preSkillCarrier.skillBuff.addBuffAttribute(operationParamFixs);
        }else{
            logger.error("effect config error! the operation[40] must be created by table skill_character_state !!!");
        }
    }

    /**
     * 解析载体目标对象配置
     */
    private Object parseTargetRole(SkillCharacterCarrierModel carrierModel,BattleRole triggerRole){
        return ParseLocation.parseTargetRole(preSkillCarrier,carrierModel.getTargetType(),triggerRole);
    }

    /**
     * 解析载体目标地点配置
     */
    private float[][] parseStartPosition(SkillCharacterCarrierModel carrierModel,BattleRole triggerRole){

        float[] basicPos = ParseLocation.parsePosition(preSkillCarrier,effectModel.getTargetType(),triggerRole);
        if(basicPos == null){
            logger.error("effect config error! can not get target position for carrier, the param target_type in the effect [id="+carrierModel.getCarrierId()+"] must be valid !!!");
            return null;
        }

        //优先处理效果表中对载体初始位置和角度的修正
        float[][] results = parseCarrierPosFix(basicPos);
        if(results != null && results.length > 0){
            int size = results.length;
            for(int i=0;i<size;i++){
                //将不在地图上的点删除
                if(!mapLogic.isPointOnMesh(results[i][0],results[i][2])){
                    results[i] = null;
                }
            }
            return results;
        }

        results = new float[skillCarrierNum][3];
        for(int i=0;i<skillCarrierNum;i++){
            results[i] = basicPos;
        }

        return results;

    }

    /**
     * 解析载体目标地点配置
     */
    private float[][] parseTargetPosition(SkillCharacterCarrierModel carrierModel,BattleRole triggerRole){

        float[] basicPos = ParseLocation.parsePosition(preSkillCarrier,carrierModel.getTargetType(),triggerRole);
        if(basicPos == null){
            logger.error("effect config error! can not get target position for carrier, the param target_type in the carrier [id="+carrierModel.getCarrierId()+"] must be valid !!!");
            return null;
        }

        //优先处理效果表中对载体初始位置和角度的修正
        float[][] results = parseCarrierPosFix(basicPos);
        if(results != null && results.length > 0){
            int size = results.length;
            for(int i=0;i<size;i++){
                //将不在地图上的点删除
                if(!mapLogic.isPointOnMesh(results[i][0],results[i][2])){
                    results[i] = null;
                }
            }
            return results;
        }

        results = new float[skillCarrierNum][3];
        for(int i=0;i<skillCarrierNum;i++){
            results[i] = basicPos;
        }

        return results;

    }

    /**
     * 获取载体的相对角度
     * @return
     */
    public float[][] parseCarrierPosFix(float[] basicStartPos){

        if(operationParamFixs != null && operationParamFixs.size() > 0){

            for(List<Integer> skillCarrierFix : operationParamFixs){

                if(skillCarrierFix.size()>=3){
                    int fixType = skillCarrierFix.get(1);

                    if(fixType == 2){ //中心左右偏移

                        float[][] startPoss = new float[skillCarrierNum][];

                        float distance = skillCarrierFix.get(2)/100f;

                        short newDir = (short)((hostSkill.getHostRole().getDirection()+90)%360);

                        double a = newDir*Math.PI/180f;

                        float disX = (float)(Math.cos(a)*distance);
                        float disZ = (float)(Math.sin(a)*distance);

                        float[] leftPos = null;
                        if(skillCarrierNum % 2 == 0){
                            leftPos = new float[]{
                                    basicStartPos[0]- (skillCarrierNum/2-0.5f)*disX,
                                    0,
                                    basicStartPos[2]- (skillCarrierNum/2-0.5f)*disZ,
                            };
                        }else{
                            leftPos = new float[]{
                                    basicStartPos[0]- (skillCarrierNum/2)*disX,
                                    0,
                                    basicStartPos[2]- (skillCarrierNum/2)*disZ,
                            };
                        }

                        for(int i=0;i<skillCarrierNum;i++){
                            startPoss[i] = new float[]{
                                    leftPos[0] + disX*i,
                                    0,
                                    leftPos[2] + disZ*i
                            };
                        }

                        return startPoss;

                    }
                }
            }

        }

        return null;
    }


    /**
     * 解析载体方向配置
     */
    private short[] parseTargetDirection(SkillCharacterCarrierModel carrierModel,BattleRole triggerRole){
        //优先处理效果表中对载体初始位置和角度的修正
        short[] results = parseCarrierDirFix();
        if(results != null && results.length > 0){
            return results;
        }

        short direction = ParseLocation.parseDirection(this,carrierModel.getTargetType(),triggerRole);

        if(direction >= 0){
            short[] dirFixs = new short[skillCarrierNum];
            for(int i=0;i<skillCarrierNum;i++){
                dirFixs[i] = direction;
            }
            return dirFixs;
        }else{
            short basicDir = hostSkill.getHostRole().getDirection();
            short[] dirFixs = new short[skillCarrierNum];
            for(int i=0;i<skillCarrierNum;i++){
                dirFixs[i] = basicDir;
            }
            return dirFixs;
        }

    }

    /**
     * 获取载体的相对角度
     * @return
     */
    public short[] parseCarrierDirFix(){

        if(operationParamFixs != null && operationParamFixs.size() > 0){

            for(List<Integer> skillCarrierFix : operationParamFixs){

                if(skillCarrierFix.size()>=3){
                    int fixType = skillCarrierFix.get(1);

                    if(fixType == 1){ //均分角度（两两间隔夹角）

                        short[] dirs = new short[skillCarrierNum];

                        int disDir = skillCarrierFix.get(2);

                        short basicDir = hostSkill.getHostRole().getDirection();


                        short leftDir = 0;
                        if(skillCarrierNum % 2 == 0){
                            leftDir = (short)(basicDir-(skillCarrierNum/2-0.5f)*disDir);
                        }else{
                            leftDir = (short)(basicDir-(skillCarrierNum/2)*disDir);
                        }

                        for(int i=0;i<skillCarrierNum;i++){
                            dirs[i] = (short)(leftDir + i*disDir);
                            while(dirs[i] < 0){
                                dirs[i] += 360;
                            }

                            dirs[i] %= 360;
                        }

                        return dirs;

                    }
                }
            }

        }

        return null;
    }

    /**
     * 过滤作用对象
     * @param role
     * @return
     */
    public boolean couldEffectRole(BattleRole role){
        if(role == null){
            return false;
        }
        if(filterType != null && filterType.size() > 0){
            for(List<Integer> filter : filterType){
                if(filter != null && filter.size() > 0){
                    if(filter.get(0) == 1){ //关系过滤
                        if(filter.get(1) == 1){ //敌方单位
                            if(hostRole.getCamp() == role.getCamp()){//非敌方，返回false
                                return false;
                            }
                        }
                        if(filter.get(1) == 2){ //友方单位
                            if(hostRole.getCamp() != role.getCamp()){ //非友方，返回false
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    public void addSyncEffectInfo(byte mapIndex,float[] pos,short direction){
        BattleSkillEffectInfo effectInfo = (BattleSkillEffectInfo)ProtocolPool.getInstance().borrowProtocol(BattleSkillEffectInfo.code);
        effectInfo.setEffectId(effectId);

        BattlePosition battlePosition = (BattlePosition)ProtocolPool.getInstance().borrowProtocol(BattlePosition.code);
        battlePosition.setMapIndex(mapIndex);
        battlePosition.setPosX((int)(pos[0]*100));
        battlePosition.setPosZ((int)(pos[2]*100));
        battlePosition.setDirection(direction);
        effectInfo.setEffectPos(battlePosition);

        mapLogic.addSyncEffect(effectInfo);
    }

    public void addSyncEffectInfo(byte mapIndex,BattleRole targetRole){
        BattleSkillEffectInfo effectInfo = (BattleSkillEffectInfo)ProtocolPool.getInstance().borrowProtocol(BattleSkillEffectInfo.code);
        effectInfo.setEffectId(effectId);
        effectInfo.setModelId(targetRole.getModelId());

        mapLogic.addSyncEffect(effectInfo);
    }

    public BasicRoleSkill getHostSkill() {
        return hostSkill;
    }

    public BasicSkillCarrier getPreSkillCarrier() {
        return preSkillCarrier;
    }


    public boolean containsTriggerRole(BattleRole triggerRole){
        return continueEffectRoles.contains(triggerRole);
    }

    @Override
    public String toString() {
        return "BasicSkillEffect{" +
                "using=" + isUsing() +
                ", hostRole=" + hostRole +
                ", hostSkill=" + hostSkill +
                ", effectId=" + effectId +
                '}';
    }
}
