package com.dykj.rpg.battle.manager;

import com.dykj.rpg.battle.attribute.AttributeConfig;
import com.dykj.rpg.battle.attribute.RoleAttributes;
import com.dykj.rpg.battle.basic.BasicRoleSkill;
import com.dykj.rpg.battle.constant.RoleTypeConstant;
import com.dykj.rpg.battle.dictionary.StaticDictionary;
import com.dykj.rpg.battle.logic.BattleContainer;
import com.dykj.rpg.battle.logic.BuffAttributeData;
import com.dykj.rpg.battle.logic.MapLogic;
import com.dykj.rpg.battle.role.BattleRole;
import com.dykj.rpg.battle.basic.BasicSkillBuff;
import com.dykj.rpg.common.attribute.consts.AttributeBasicConstant;
import com.dykj.rpg.common.attribute.consts.BuffStateConstant;
import com.dykj.rpg.common.attribute.consts.DateFixConstant;
import com.dykj.rpg.common.config.model.SkillCharacterStateModel;
import com.dykj.rpg.net.protocol.ProtocolPool;
import com.dykj.rpg.protocol.battle.BattleSkillBuffInfo;
import com.dykj.rpg.protocol.battle.BattleSkillCarrierInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * buff管理类主要用于管理角色属性和其被buff改变的内容
 */
public class BuffManager {
    /**
     * -----------------------------状态类型------------------------------
     */
    /**
     * 元素buff
     */
    public static final int STATE_TYPE_ELEMENT_BUFF = 1;
    /**
     * 元素debuff
     */
    public static final int STATE_TYPE_ELEMENT_DEBUFF = 2;
    /**
     * 元素精通
     */
    public static final int STATE_TYPE_ELEMENT_PROFICIENT = 3;

    /**
     * -----------------------------叠加类型-------------------------------
     */
    /**
     * 独立 （independence）
     */
    public static final int SUPERPOSITION_TYPE_INDEPENDENCE = 1;
    /**
     * 绝对唯一 （Absolutely unique）
     */
    public static final int SUPERPOSITION_TYPE_ABSOLUTELY_UNIQUE = 2;
    /**
     * 分组唯一 （Group unique）
     */
    public static final int SUPERPOSITION_TYPE_GROUP_UNIQUE = 3;

    /**
     * -----------------------------buff状态-------------------------------
     */
    /**
     * buff初始化
     */
    public static final byte BUFF_STATE_INIT = 1;
    /**
     * buff存活
     */
    public static final byte BUFF_STATE_LIVE = 2;
    /**
     * buff消散
     */
    public static final byte BUFF_STATE_DEAD = 3;
    /**
     * buff对象归还到对象池
     */
    public static final byte BUFF_STATE_DESTROY = 4;

    private Logger logger = LoggerFactory.getLogger("BuffManager");

    private BattleRole battleRole;

    private BattleContainer battleContainer;

    /**
     * 元素buff(启动时加入)
     */
    private Map<Integer,SkillCharacterStateModel> elementBuffMap;
    /**
     * 元素debuff(启动时加入)
     */
    private Map<Integer,SkillCharacterStateModel> elementDeBuffMap;
    /**
     * 元素精通(启动时加入)
     */
    private Map<Integer,SkillCharacterStateModel> elementProficientMap;

    /**-----------------------以下为生效的buff和debuff-----------------------*/
    /**
     * 独立的buff集合
     */
    private List<BasicSkillBuff> independenceBuffList;
    /**
     * 绝对唯一的buff集合
     */
    private List<BasicSkillBuff> absolutelyUniqueBuffList;
    /**
     * 分组唯一的buff集合
     */
    private List<BasicSkillBuff> groupUniqueBuffList;

    /**
     * 初始化的角色属性结果
     */
    private Map<Integer,Integer> initBuffAttributeResults;
    /**
     * buff产生的对属性数据操作集合 key=attributeId*100+type1
     */
    private Map<Integer,List<BuffAttributeData>> buffAttributeDatas;
    /**
     * buff对属性操作结果缓存 key=attributeId*100+type1
     * value=新值
     * 目的是为了节省重复计算的资源消耗
     * 当buffAttributeDatas有变动时，删除对应的缓存，重新计算结果
     */
    private Map<Integer,Integer> buffAttributeResults;

    private Random random = new Random();

    public BuffManager(BattleRole battleRole){
        this.battleRole = battleRole;

        elementBuffMap = new HashMap<>();
        elementDeBuffMap = new HashMap<>();
        elementProficientMap = new HashMap<>();

        independenceBuffList = new ArrayList<>();
        absolutelyUniqueBuffList = new ArrayList<>();
        groupUniqueBuffList = new ArrayList<>();
        buffAttributeDatas = new ConcurrentHashMap<>();
        buffAttributeResults = new ConcurrentHashMap<>();
        initBuffAttributeResults = new HashMap<>();
    }

    public void init(BattleContainer battleContainer){

        random.setSeed(battleRole.getModelId());

        this.battleContainer = battleContainer;

        if(battleRole.getRoleType() != RoleTypeConstant.BATTLE_ROLE_PLAYER){
            StaticDictionary.getInstance().getElementSkillCharacterStateModel(elementBuffMap,elementDeBuffMap,elementProficientMap);
        }

        for(AttributeConfig attributeConfig : battleRole.attributeManager.getAllAttributeConfigs()){
            int key = attributeConfig.id*100+attributeConfig.type1;
            initBuffAttributeResults.put(key,attributeConfig.num);
            buffAttributeResults.put(key,attributeConfig.num);
        }

        int key = AttributeBasicConstant.ZHUANG_TAI_SHU_XING*100+BuffStateConstant.ZHU_DONG_JI_NENG_SHI_FANG;
        initBuffAttributeResults.put(key,1);
        buffAttributeResults.put(key,1);
        key = AttributeBasicConstant.ZHUANG_TAI_SHU_XING*100+BuffStateConstant.HUN_JI_SHI_FANG;
        initBuffAttributeResults.put(key,1);
        buffAttributeResults.put(key,1);

    }

    public void release(){

        removeAllBuff();

        elementBuffMap.clear();
        elementDeBuffMap.clear();
        elementProficientMap.clear();

        buffAttributeDatas.clear();
        buffAttributeResults.clear();
        initBuffAttributeResults.clear();

        this.battleContainer = null;
    }

    /**
     * 添加新的角色属性
     * @param attributeId
     * @param attributeType
     * @param num
     */
    public void addRoleAttribute(int attributeId,int attributeType,int num){
        int key = attributeId*100+attributeType;
        battleRole.attributeManager.coverAttribute(attributeId,attributeType,num);
        initBuffAttributeResults.put(key,num);
        buffAttributeResults.put(key,num);
    }

    public void initDefaultAttribute(){
        battleRole.attributeManager.initDefaultAttribute();
    }

    public void addBuff(MapLogic mapLogic, BasicRoleSkill hostSkill, int stateId){

        SkillCharacterStateModel stateModel = StaticDictionary.getInstance().getSkillCharacterStateModelById(stateId);

        addBuff(mapLogic,hostSkill,stateModel);

    }

    //public void addBuff(BasicSkillBuff skillBuff){
    public void addBuff(MapLogic mapLogic, BasicRoleSkill hostSkill, SkillCharacterStateModel stateModel){

        if(stateModel.getSuperpositionType() == SUPERPOSITION_TYPE_INDEPENDENCE){
            addIndependenceBuff(mapLogic,hostSkill,stateModel);
        }

        if(stateModel.getSuperpositionType() == SUPERPOSITION_TYPE_ABSOLUTELY_UNIQUE){
            addAbsolutelyUniqueBuff(mapLogic,hostSkill,stateModel);
        }

        if(stateModel.getSuperpositionType() == SUPERPOSITION_TYPE_GROUP_UNIQUE){
            addGroupUniqueBuff(mapLogic,hostSkill,stateModel);
        }

    }

    private void addIndependenceBuff(MapLogic mapLogic, BasicRoleSkill hostSkill, SkillCharacterStateModel stateModel){

        BasicSkillBuff skillBuff = battleContainer.battlePoolManager.borrowSkillBuff();

        if(skillBuff == null){
            logger.error("can not borrow skill buff !!! has no enough object in the pool !!!");
            return;
        }

        skillBuff.init(battleContainer,mapLogic,hostSkill,stateModel,battleRole);

        independenceBuffList.add(skillBuff);
        skillBuff.buffEffectRelease();

    }

    private void addAbsolutelyUniqueBuff(MapLogic mapLogic, BasicRoleSkill hostSkill, SkillCharacterStateModel stateModel){

        for(BasicSkillBuff basicSkillBuff : absolutelyUniqueBuffList){
            if(basicSkillBuff.getStateId() == stateModel.getStateId()){
                basicSkillBuff.addSuperposition();
                return;
            }
        }

        BasicSkillBuff skillBuff = battleContainer.battlePoolManager.borrowSkillBuff();

        if(skillBuff == null){
            logger.error("can not borrow skill buff !!! has no enough object in the pool !!!");
            return;
        }

        skillBuff.init(battleContainer,mapLogic,hostSkill,stateModel,battleRole);

        absolutelyUniqueBuffList.add(skillBuff);
        skillBuff.buffEffectRelease();

    }

    private void addGroupUniqueBuff(MapLogic mapLogic, BasicRoleSkill hostSkill, SkillCharacterStateModel stateModel){
        for(BasicSkillBuff basicSkillBuff : groupUniqueBuffList){
            if(basicSkillBuff.getGroupId() == stateModel.getStateGroup()){
                basicSkillBuff.addSuperposition();
                return;
            }
        }

        BasicSkillBuff skillBuff = battleContainer.battlePoolManager.borrowSkillBuff();

        if(skillBuff == null){
            logger.error("can not borrow skill buff !!! has no enough object in the pool !!!");
            return;
        }

        skillBuff.init(battleContainer,mapLogic,hostSkill,stateModel,battleRole);

        groupUniqueBuffList.add(skillBuff);
        skillBuff.buffEffectRelease();
    }

    /**
     * 触发元素buff
     * @param mapLogic
     * @param hostSkill
     */
    public void triggerElementBuff(MapLogic mapLogic, BasicRoleSkill hostSkill){
        SkillCharacterStateModel stateModel = elementBuffMap.get(hostSkill.elementType);
        if(stateModel == null){
            return;
        }
        if(random.nextInt(10000) < stateModel.getTriggerProbability()){
            addBuff(mapLogic,hostSkill,stateModel);
        }
    }

    /**
     * 触发元素debuff
     * @param mapLogic
     * @param hostSkill
     */
    public void triggerElementDeBuff(MapLogic mapLogic, BasicRoleSkill hostSkill){
        SkillCharacterStateModel stateModel = elementDeBuffMap.get(hostSkill.elementType);
        if(stateModel == null){
            return;
        }
        int randomInt = random.nextInt(10000);
        if(randomInt < stateModel.getTriggerProbability()){
            addBuff(mapLogic,hostSkill,stateModel);

            /**
             * debuff有一定几率触发元素精通
             */
            SkillCharacterStateModel stateModel2 = elementProficientMap.get(hostSkill.elementType);
            randomInt = random.nextInt(10000);
            if(randomInt < stateModel2.getTriggerProbability()){
                addBuff(mapLogic,hostSkill,stateModel2);
            }
        }
    }

    public void update(int frameNum){
        int size = independenceBuffList.size();
        if(size > 0){
            int index = 0;
            BasicSkillBuff skillBuff ;
            while(index < size){
                skillBuff = independenceBuffList.get(index);
                if(skillBuff==null){
                    independenceBuffList.remove(index);
                    size --;
                    continue;
                }
                if(skillBuff.getState() == BUFF_STATE_DESTROY){
                    battleContainer.battlePoolManager.restoreSkillBuff(skillBuff);
                    independenceBuffList.remove(index);
                    size --;
                    continue;
                }
                if(skillBuff.getState() == BUFF_STATE_LIVE){
                    skillBuff.update(frameNum);
                    index ++;
                    continue;
                }
            }
        }

        size = absolutelyUniqueBuffList.size();
        if(size > 0){
            int index = 0;
            BasicSkillBuff skillBuff ;
            while(index < size){
                skillBuff = absolutelyUniqueBuffList.get(index);

                if(skillBuff==null){
                    absolutelyUniqueBuffList.remove(index);
                    size --;
                    continue;
                }
                if(skillBuff.getState() == BUFF_STATE_DESTROY){
                    battleContainer.battlePoolManager.restoreSkillBuff(skillBuff);
                    absolutelyUniqueBuffList.remove(index);
                    size --;
                    continue;
                }
                if(skillBuff.getState() == BUFF_STATE_LIVE){
                    skillBuff.update(frameNum);
                    index ++;
                    continue;
                }
            }
        }

        size = groupUniqueBuffList.size();
        if(size > 0){
            int index = 0;
            BasicSkillBuff skillBuff ;
            while(index < size){
                skillBuff = groupUniqueBuffList.get(index);

                if(skillBuff==null){
                    groupUniqueBuffList.remove(index);
                    size --;
                    continue;
                }
                if(skillBuff.getState() == BUFF_STATE_DESTROY){
                    battleContainer.battlePoolManager.restoreSkillBuff(skillBuff);
                    groupUniqueBuffList.remove(index);
                    size --;
                    continue;
                }
                if(skillBuff.getState() == BUFF_STATE_LIVE){
                    skillBuff.update(frameNum);
                    index ++;
                    continue;
                }
            }
        }
    }

    public void clear(){
        independenceBuffList.clear();
        absolutelyUniqueBuffList.clear();
        groupUniqueBuffList.clear();
    }

    public void disappearAllBuff(){
        int size = independenceBuffList.size();
        if(size > 0){
            for(BasicSkillBuff skillBuff : independenceBuffList){
                skillBuff.releaseBuff();
            }
        }

        size = absolutelyUniqueBuffList.size();
        if(size > 0){
            for(BasicSkillBuff skillBuff : absolutelyUniqueBuffList){
                skillBuff.releaseBuff();
            }
        }

        size = groupUniqueBuffList.size();
        if(size > 0){
            for(BasicSkillBuff skillBuff : groupUniqueBuffList){
                skillBuff.releaseBuff();
            }
        }
    }

    public void disappearBuffByStateType(int stateType){
        int size = independenceBuffList.size();
        if(size > 0){
            for(BasicSkillBuff skillBuff : independenceBuffList){
                if(skillBuff.getStateType() == stateType){
                    skillBuff.releaseBuff();
                }
            }
        }

        size = absolutelyUniqueBuffList.size();
        if(size > 0){
            for(BasicSkillBuff skillBuff : absolutelyUniqueBuffList){
                if(skillBuff.getStateType() == stateType){
                    skillBuff.releaseBuff();
                }
            }
        }

        size = groupUniqueBuffList.size();
        if(size > 0){
            for(BasicSkillBuff skillBuff : groupUniqueBuffList){
                if(skillBuff.getStateType() == stateType){
                    skillBuff.releaseBuff();
                }
            }
        }
    }

    public void disappearBuffByStateId(int stateId){
        int size = independenceBuffList.size();
        if(size > 0){
            for(BasicSkillBuff skillBuff : independenceBuffList){
                if(skillBuff.getStateId() == stateId){
                    skillBuff.releaseBuff();
                }
            }
        }

        size = absolutelyUniqueBuffList.size();
        if(size > 0){
            for(BasicSkillBuff skillBuff : absolutelyUniqueBuffList){
                if(skillBuff.getStateId() == stateId){
                    skillBuff.releaseBuff();
                }
            }
        }

        size = groupUniqueBuffList.size();
        if(size > 0){
            for(BasicSkillBuff skillBuff : groupUniqueBuffList){
                if(skillBuff.getStateId() == stateId){
                    skillBuff.releaseBuff();
                }
            }
        }
    }

    public void addBuffAttribute(BuffAttributeData data){
        int key = data.attributeConfig.id*100+data.attributeConfig.type1;
        List<BuffAttributeData> datas = buffAttributeDatas.get(key);
        if(datas == null){
            datas = new ArrayList<>();
            buffAttributeDatas.put(key,datas);
        }
        datas.add(data);

        int newNum = attributeCalculation(key);
        notifyAttributeChange(data.attributeConfig.id,data.attributeConfig.type1,newNum);
    }

    public void removeBuffAttribute(BuffAttributeData data){
        int key = data.attributeConfig.id*100+data.attributeConfig.type1;
        List<BuffAttributeData> datas = buffAttributeDatas.get(key);
        if(datas != null){
            datas.remove(data);
        }

        int newNum = attributeCalculation(key);
        notifyAttributeChange(data.attributeConfig.id,data.attributeConfig.type1,newNum);
    }

    public int getAttributeFromBuff(int attributeId,int type){
        int key = attributeId*100+type;
        Integer value = buffAttributeResults.get(key);
        if(value == null){
            value = attributeCalculation(type);
        }
        return value;
    }


    private int attributeCalculation(int key){

        Integer initAttributeNum = initBuffAttributeResults.get(key);
        if(initAttributeNum == null){
            initAttributeNum = 0;
            initBuffAttributeResults.put(key,initAttributeNum);
        }
        List<BuffAttributeData> datas = buffAttributeDatas.get(key);
        if(datas != null){
            int dataSize = datas.size();
            if(dataSize > 0){
                int index = 0;
                while(index < dataSize){
                    BuffAttributeData data = datas.get(index);
                    initAttributeNum = dataFix(initAttributeNum,data.attributeConfig.type2,data.attributeConfig.num,null);
                    index ++ ;
                }
            }
        }
        buffAttributeResults.put(key,initAttributeNum);
        return initAttributeNum;
    }

    private void notifyAttributeChange(int attributeId,int type,int num){
        //通知属性管理器属性的变更
        battleRole.attributeManager.notifyRoleAttributeChange(attributeId,type,num);

    }
    /**
     * 客户端显示buff列表
     * @return
     */
    public List<BattleSkillBuffInfo> getAllBuffInfo(){
        List<BattleSkillBuffInfo> list = new ArrayList<>();
        if(independenceBuffList.size() > 0){
            for(BasicSkillBuff skillBuff : independenceBuffList){
                byte state = 0;
                if(skillBuff.getState() == BuffManager.BUFF_STATE_INIT){
                    skillBuff.setState(BuffManager.BUFF_STATE_LIVE);
                    state = 1;
                }
                if(skillBuff.getState() == BuffManager.BUFF_STATE_DEAD){
                    skillBuff.setState(BuffManager.BUFF_STATE_DESTROY);
                    state = 2;
                }
                if(state != 0){
                    BattleSkillBuffInfo buffInfo = (BattleSkillBuffInfo) ProtocolPool.getInstance().borrowProtocol(BattleSkillBuffInfo.code);
                    buffInfo.setStateId(skillBuff.getStateId());
                    buffInfo.setGuid(skillBuff.getGuid());
                    buffInfo.setSuperpositionNum(skillBuff.getCurrentSuperpositionNum());
                    buffInfo.setDurationTime(skillBuff.getDurationTime());
                    buffInfo.setState(state);
                    list.add(buffInfo);
                }
            }
        }

        if(absolutelyUniqueBuffList.size() > 0){
            for(BasicSkillBuff skillBuff : absolutelyUniqueBuffList){
                byte state = 0;
                if(skillBuff.getState() == BuffManager.BUFF_STATE_INIT){
                    skillBuff.setState(BuffManager.BUFF_STATE_LIVE);
                    state = 1;
                }
                if(skillBuff.getState() == BuffManager.BUFF_STATE_DEAD){
                    skillBuff.setState(BuffManager.BUFF_STATE_DESTROY);
                    state = 2;
                }
                if(state != 0){
                    BattleSkillBuffInfo buffInfo = (BattleSkillBuffInfo) ProtocolPool.getInstance().borrowProtocol(BattleSkillBuffInfo.code);
                    buffInfo.setStateId(skillBuff.getStateId());
                    buffInfo.setGuid(skillBuff.getGuid());
                    buffInfo.setSuperpositionNum(skillBuff.getCurrentSuperpositionNum());
                    buffInfo.setDurationTime(skillBuff.getDurationTime());
                    buffInfo.setState(state);
                    list.add(buffInfo);
                }
            }
        }

        if(groupUniqueBuffList.size() > 0){
            for(BasicSkillBuff skillBuff : groupUniqueBuffList){
                byte state = 0;
                if(skillBuff.getState() == BuffManager.BUFF_STATE_INIT){
                    skillBuff.setState(BuffManager.BUFF_STATE_LIVE);
                    state = 1;
                }
                if(skillBuff.getState() == BuffManager.BUFF_STATE_DEAD){
                    skillBuff.setState(BuffManager.BUFF_STATE_DESTROY);
                    state = 2;
                }
                if(state != 0){
                    BattleSkillBuffInfo buffInfo = (BattleSkillBuffInfo) ProtocolPool.getInstance().borrowProtocol(BattleSkillBuffInfo.code);
                    buffInfo.setStateId(skillBuff.getStateId());
                    buffInfo.setGuid(skillBuff.getGuid());
                    buffInfo.setSuperpositionNum(skillBuff.getCurrentSuperpositionNum());
                    buffInfo.setDurationTime(skillBuff.getDurationTime());
                    buffInfo.setState(state);
                    list.add(buffInfo);
                }
            }
        }

        return list;
    }

    private int dataFix(int oldData,int type,int newData,List<Integer> relativeList){
        if(type == DateFixConstant.ZHI_XIU_ZHENG){ //值修正
            return oldData + newData;
        }
        if(type == DateFixConstant.ZENG_BI_XIU_ZHENG){ //增比修正
            return (int)(oldData * (10000f + newData)/10000f);
        }
        if(type == DateFixConstant.JIAN_BI_XIU_ZHENG){ //减比修正
            return (int)(oldData / (10000f + newData)/10000f);
        }
        if(type == DateFixConstant.LIN_SHI_FU_GAI_XIU_ZHENG){ //临时覆盖修正
            return newData;
        }
        if(type == DateFixConstant.FU_GAI_XIU_ZHENG){ //覆盖修正
            return newData;
        }
        if(type == DateFixConstant.DIAN_LIANG_XIU_ZHENG){ //点亮式修正
            relativeList.add(newData);
            return 0;
        }
        return oldData;
    }

    public void removeAllBuff(){
        int size = independenceBuffList.size();
        if(size > 0){
            for(BasicSkillBuff skillBuff : independenceBuffList){
                battleContainer.battlePoolManager.restoreSkillBuff(skillBuff);
            }
        }
        independenceBuffList.clear();

        size = absolutelyUniqueBuffList.size();
        if(size > 0){
            for(BasicSkillBuff skillBuff : absolutelyUniqueBuffList){
                battleContainer.battlePoolManager.restoreSkillBuff(skillBuff);
            }
        }
        absolutelyUniqueBuffList.clear();

        size = groupUniqueBuffList.size();
        if(size > 0){
            for(BasicSkillBuff skillBuff : groupUniqueBuffList){
                battleContainer.battlePoolManager.restoreSkillBuff(skillBuff);
            }
        }
        groupUniqueBuffList.clear();
    }

    /**
     * 清除buff,主要用于提前通知客户端销毁状态效果
     */
    public void stopAllBuff(){
        int size = independenceBuffList.size();
        if(size > 0){
            for(BasicSkillBuff skillBuff : independenceBuffList){
                skillBuff.setState(BuffManager.BUFF_STATE_DEAD);
            }
        }

        size = absolutelyUniqueBuffList.size();
        if(size > 0){
            for(BasicSkillBuff skillBuff : absolutelyUniqueBuffList){
                skillBuff.setState(BuffManager.BUFF_STATE_DEAD);
            }
        }

        size = groupUniqueBuffList.size();
        if(size > 0){
            for(BasicSkillBuff skillBuff : groupUniqueBuffList){
                skillBuff.setState(BuffManager.BUFF_STATE_DEAD);
            }
        }
    }

    /**
     * 是否可以释放角色技能
     * @return
     */
    public boolean canReleaseRoleSkill(){
        return getAttributeFromBuff(AttributeBasicConstant.ZHUANG_TAI_SHU_XING, BuffStateConstant.ZHU_DONG_JI_NENG_SHI_FANG)==1;
    }

    /**
     * 是否可以释放灵魂技能
     * @return
     */
    public boolean canReleaseSoulSkill(){
        return getAttributeFromBuff(AttributeBasicConstant.ZHUANG_TAI_SHU_XING, BuffStateConstant.HUN_JI_SHI_FANG)==1;
    }

}
