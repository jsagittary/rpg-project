package com.dykj.rpg.battle.attribute;

import com.dykj.rpg.battle.dictionary.StaticDictionary;
import com.dykj.rpg.common.attribute.consts.DateFixConstant;
import com.dykj.rpg.common.config.model.SkillCharacterBasicModel;
import com.dykj.rpg.common.config.model.SkillCharacterCarrierModel;
import com.dykj.rpg.common.config.model.SkillCharacterEffectModel;
import com.dykj.rpg.common.config.model.SkillCharacterStateModel;
import com.dykj.rpg.protocol.game2battle.SkillAttribute;
import org.apache.commons.lang.SerializationUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillAttributes {

    int skillId;

    /**
     * key = 各个表的主键id
     */
    Map<Integer, List<AttributeConfig>> attrs = new HashMap<>();

    SkillCharacterBasicModel skillCharacterBasicModel;

    Map<Integer,SkillCharacterCarrierModel> skillCharacterCarrierModelMap = new HashMap<>();

    Map<Integer,SkillCharacterEffectModel> skillCharacterEffectModelMap = new HashMap<>();

    Map<Integer,SkillCharacterStateModel> skillCharacterStateModelMap = new HashMap<>();

    public SkillAttributes(int skillId,List<SkillAttribute> attributeList){
        this.skillId = skillId;
        addAttribute(attributeList);
    }

    /**
     * 覆盖属性
     */
    public void addAttribute(List<SkillAttribute> attributeList){

        if(attributeList != null && attributeList.size() > 0){
            for(SkillAttribute attribute : attributeList){
                List<AttributeConfig> list = attrs.get(attribute.getTableKey());
                if(list == null){
                    list = new ArrayList<>();
                    attrs.put(attribute.getTableKey(),list);
                }

                list.add(new AttributeConfig(attribute.getSkillAttrId(),attribute.getPrams(),attribute.getValue()));

            }
        }
    }

    /**
     * 获取养成后的SkillCharacterBasicModel
     */
    public SkillCharacterBasicModel getDevelopSkillBasic(){
        if(skillCharacterBasicModel == null){
            SkillCharacterBasicModel model = StaticDictionary.getInstance().getSkillCharacterBasicModelById(skillId);
            skillCharacterBasicModel = (SkillCharacterBasicModel) SerializationUtils.clone(model);

            List<AttributeConfig> configList = attrs.get(skillId);
            if(configList != null && configList.size() >0){
                for(AttributeConfig config : configList){
                    setSkillCharacterBasicModelCoverAttribute(skillCharacterBasicModel,config);
                }
            }
        }

        return skillCharacterBasicModel;
    }

    private void setSkillCharacterBasicModelCoverAttribute(SkillCharacterBasicModel model,AttributeConfig config){
        if(config.id == 1){//skill_cd_time
            model.setSkillCdTime(dataFix(model.getSkillCdTime(),config.type1,config.num,null));
        }
        if(config.id == 2){//use_times
            model.setUseTimes(dataFix(model.getUseTimes(),config.type1,config.num,null));
        }
        if(config.id == 3){//skill_distance_max
            model.setSkillDistanceMax(dataFix(model.getSkillDistanceMax(),config.type1,config.num,null));
        }
        if(config.id == 4){//energy_num
            model.setEnergyNum(dataFix(model.getEnergyNum(),config.type1,config.num,null));
        }
        if(config.id == 5){//sing_type
            model.setSingType(dataFix(model.getSingType(),config.type1,config.num,null));
        }
        if(config.id == 6){//sing_time
            model.setSingTime(dataFix(model.getSingTime(),config.type1,config.num,null));
        }

        if(config.id == 9){//final_effect_reserve
            //TODO 特殊处理
            dataFix(0,DateFixConstant.DIAN_LIANG_XIU_ZHENG,config.num,model.getEffectId());
        }

        if(config.id == 11){//skill_limitnum
            model.setSkillLimitnum(dataFix(model.getSkillLimitnum(),config.type1,config.num,null));
        }
        if(config.id == 12){//item_quality_type
            model.setItemQualityType(dataFix(model.getItemQualityType(),config.type1,config.num,null));
        }
        if(config.id == 13){//extra_prop
            model.setExtraProp(dataFix(model.getExtraProp(),config.type1,config.num,null));
        }

        if(config.id == 54){//skill_type
            model.setSkillType(dataFix(model.getSkillType(),config.type1,config.num,null));
        }
    }

    /**
     * 获取养成后的SkillCharacterCarrierModel
     */
    public SkillCharacterCarrierModel getDevelopSkillCarrier(int carrierId){
        SkillCharacterCarrierModel carrierModel = skillCharacterCarrierModelMap.get(carrierId);
        if(carrierModel == null){
            SkillCharacterCarrierModel model = StaticDictionary.getInstance().getSkillCharacterCarrierModelById(carrierId);

            if(model != null){

                carrierModel = (SkillCharacterCarrierModel) SerializationUtils.clone(model);

                List<AttributeConfig> configList = attrs.get(carrierId);
                if(configList != null && configList.size() >0){
                    for(AttributeConfig config : configList){
                        setSkillCharacterCarrierModelCoverAttribute(carrierModel,config);
                    }
                }

                skillCharacterCarrierModelMap.put(carrierId,carrierModel);

            }
        }
        return carrierModel;

    }

    private void setSkillCharacterCarrierModelCoverAttribute(SkillCharacterCarrierModel model,AttributeConfig config) {
        if (config.id == 14) {//carrier_form
            model.setCarrierForm(dataFix(model.getCarrierForm(),config.type1,config.num,null));
        }
        if (config.id == 15) {//carrier_type
            model.setCarrierType(dataFix(model.getCarrierType(),config.type1,config.num,null));
        }
        if (config.id == 16) {//special_type
            model.setSpecialType(dataFix(model.getSpecialType(),config.type1,config.num,null));
        }
        //if (config.id == 17) {//form_parm
        //    model.setFormParm(config.num);
        //}
        if (config.id == 18) {//collision
            model.setCollision(dataFix(model.getCollision(),config.type1,config.num,null));
        }
        if (config.id == 19) {//range
            model.setRange(dataFix(model.getRange(),config.type1,config.num,null));
        }
        //if (config.id == 20) {//carrier_model
        //    model.setCarrierModel(config.num);
        //}
        //if (config.id == 21) {//carrier_colour
        //    model.setCarrierColour(config.num);
        //}
        //if (config.id == 22) {//carrier_vfx
        //    model.setCarrierVfx(config.num);
        //}
        if (config.id == 23) {//height_locus
            model.setHeightLocus(dataFix(model.getHeightLocus(),config.type1,config.num,null));
        }
        //if (config.id == 24) {//target_type
        //    model.setTargetType(config.num);
        //}
        //if (config.id == 25) {//filter_type
        //    model.setFilterType(config.num);
        //}
        if (config.id == 26) {//move_speed
            model.setMoveSpeed(dataFix(model.getMoveSpeed(),config.type1,config.num,null));
        }
        if (config.id == 27) {//effect_times
            model.setEffectTimes(dataFix(model.getEffectTimes(),config.type1,config.num,null));
        }
        if (config.id == 28) {//disappear
            model.setDisappear(dataFix(model.getDisappear(),config.type1,config.num,null));
        }
        if (config.id == 29) {//duration_time
            model.setDurationTime(dataFix(model.getDurationTime(),config.type1,config.num,null));
        }
        //if (config.id == 30) {//effect_id
        //    model.setEffectId(config.num);
        //}
        if (config.id == 31) {//effect_id_reserve
            //TODO 特殊处理
            dataFix(0,DateFixConstant.DIAN_LIANG_XIU_ZHENG,config.num,model.getEffectId());
        }
    }

    /**
     * 获取养成后的SkillCharacterEffectModel
     */
    public SkillCharacterEffectModel getDevelopSkillEffect(int effectId){

        SkillCharacterEffectModel effectModel = skillCharacterEffectModelMap.get(effectId);
        if(effectModel == null){
            SkillCharacterEffectModel model = StaticDictionary.getInstance().getSkillCharacterEffectModelById(effectId);

            if(model != null){
                effectModel = (SkillCharacterEffectModel) SerializationUtils.clone(model);

                List<AttributeConfig> configList = attrs.get(effectId);
                if(configList != null && configList.size() >0){
                    for(AttributeConfig config : configList){
                        setSkillCharacterEffectModelCoverAttribute(effectModel,config);
                    }
                }

                skillCharacterEffectModelMap.put(effectId,effectModel);
            }

        }

        return effectModel;
    }

    private void setSkillCharacterEffectModelCoverAttribute(SkillCharacterEffectModel model,AttributeConfig config) {
        if (config.id == 35) {//effect_type
            model.setEffectType(dataFix(model.getEffectType(),config.type1,config.num,null));
        }
        //if (config.id == 36) {//target_type
        //    model.setTargetType(config.num);
        //}
        //if (config.id == 37) {//filter_type
        //    model.setFilterType(config.num);
        //}
        if (config.id == 38) {//operation_delay
            model.setOperationDelay(dataFix(model.getOperationDelay(),config.type1,config.num,null));
        }
        if (config.id == 39) {//effect_interval
            model.setEffectInterval(dataFix(model.getEffectInterval(),config.type1,config.num,null));
        }
        if (config.id == 40) {//effect_times
            model.setEffectTimes(dataFix(model.getEffectTimes(),config.type1,config.num,null));
        }
        if (config.id == 41) {//operation
            model.setOperation(dataFix(model.getOperation(),config.type1,config.num,null));
        }
        //if (config.id == 42) {//operation_parm_a
        //    model.setOperationParmA(config.num);
        //}
        //if (config.id == 43) {//operation_parm_b
        //    model.setOperationParmB(config.num);
        //    model.setOperationParmB(dataFix(model.getOperationParmB(),config.type1,config.num,null));
        //}
        //if (config.id == 44) {//operation_parm_c
        //    model.setOperationParmC(config.num);
        //}
        if (config.id == 45) {//damage_prop
            model.setDamageProp(dataFix(model.getDamageProp(),config.type1,config.num,null));
        }
        if (config.id == 46) {//damage_value
            model.setDamageValue(dataFix(model.getDamageValue(),config.type1,config.num,null));
        }
    }

    /**
     * 获取养成后的SkillCharacterStateModel
     */
    public SkillCharacterStateModel getDevelopSkillState(int stateId){
        SkillCharacterStateModel stateModel = skillCharacterStateModelMap.get(stateId);
        if(stateModel == null){
            SkillCharacterStateModel model = StaticDictionary.getInstance().getSkillCharacterStateModelById(stateId);

            if(model != null){
                stateModel = (SkillCharacterStateModel) SerializationUtils.clone(model);

                List<AttributeConfig> configList = attrs.get(stateId);
                if(configList != null && configList.size() >0){
                    for(AttributeConfig config : configList){
                        setSkillCharacterStateModelCoverAttribute(stateModel,config);
                    }
                }

                skillCharacterStateModelMap.put(stateId,stateModel);
            }

        }

        return stateModel;
    }

    private void setSkillCharacterStateModelCoverAttribute(SkillCharacterStateModel model,AttributeConfig config) {
        if (config.id == 47) {//state_type
            model.setStateType(dataFix(model.getStateType(),config.type1,config.num,null));
        }
        if (config.id == 48) {//disappear_die
            model.setDisappearDie(dataFix(model.getDisappearDie(),config.type1,config.num,null));
        }
        if (config.id == 49) {//disappear_effect
            model.setDisappearEffect(dataFix(model.getDisappearEffect(),config.type1,config.num,null));
        }
        if (config.id == 50) {//replace
            model.setReplace(dataFix(model.getReplace(),config.type1,config.num,null));
        }
        if (config.id == 51) {//superposition_type
            model.setSuperpositionType(dataFix(model.getSuperpositionType(),config.type1,config.num,null));
        }
        if (config.id == 52) {//superposition_num_max
            model.setSuperpositionNumMax(dataFix(model.getSuperpositionNumMax(),config.type1,config.num,null));
        }
        if (config.id == 53) {//duration_time
            model.setDurationTime(dataFix(model.getDurationTime(),config.type1,config.num,null));
        }
    }

    public int dataFix(int oldData,int type,int newData,List<Integer> relativeList){
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

}
