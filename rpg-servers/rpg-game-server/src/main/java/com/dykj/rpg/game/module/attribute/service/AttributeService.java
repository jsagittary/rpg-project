package com.dykj.rpg.game.module.attribute.service;

import com.dykj.rpg.common.attribute.AttributeArray;
import com.dykj.rpg.common.attribute.AttributeUnit;
import com.dykj.rpg.common.attribute.AttributeUtil;
import com.dykj.rpg.common.attribute.consts.*;
import com.dykj.rpg.common.config.dao.AttributesBasicDao;
import com.dykj.rpg.common.config.dao.CharacterAttributesDao;
import com.dykj.rpg.common.config.dao.CharacterBasicDao;
import com.dykj.rpg.common.config.model.AttributesBasicModel;
import com.dykj.rpg.common.config.model.CharacterAttributesModel;
import com.dykj.rpg.common.config.model.CharacterBasicModel;
import com.dykj.rpg.common.config.model.SkillAttrBasicModel;
import com.dykj.rpg.common.consts.CommonConsts;
import com.dykj.rpg.game.module.attribute.logic.AttributeCache;
import com.dykj.rpg.game.module.equip.service.EquipService;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.util.reflex.ReflexUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jyb
 * @date 2020/11/9 20:52
 * @Description
 */
@Service
public class AttributeService {
    @Resource
    private AttributesBasicDao attributesBasicDao;
    @Resource
    private CharacterBasicDao characterBasicDao;
    @Resource
    private CharacterAttributesDao characterAttributesDao;
    @Resource
    private EquipService equipService;

    /**
     * 创建
     *
     * @return
     */
    public AttributeArray create() {
        return new AttributeArray(attributesBasicDao.getConfigs().size());
    }

    /**
     * 初始化玩家的基础属性
     *
     * @param player
     * @return
     */
    private Map<String, Integer> basicAttribute(Player player) {
        CharacterBasicModel characterBasicModel = characterBasicDao.getConfigByKey(player.cache().getPlayerInfoModel().getProfession());

        //初始化属性
        List<AttributeUnit> initializeAttributes = AttributeUtil.coverToAttributeUnits(characterBasicModel.getInitializeAttributes());
        CharacterAttributesModel characterAttributesModel = characterAttributesDao.get(characterBasicModel.getGrowClass(), player.cache().getPlayerInfoModel().getLv());
        //等级属性 主属性
        List<AttributeUnit> mainattributes = AttributeUtil.coverToAttributeUnits(characterAttributesModel.getMainattributes());
        //等级属性 普通属性
        List<AttributeUnit> generalattributes = AttributeUtil.coverToAttributeUnits(characterAttributesModel.getGeneralattributes());
        return flush(initializeAttributes, mainattributes, generalattributes);
    }

    /**
     * 刷新属性
     *
     * @param player
     * @return
     */
    public void refresh(Player player) {
        AttributeCache attributeCache = player.cache().getAttributeCache();
        if (attributeCache == null) {
            attributeCache = new AttributeCache();
            player.cache().setAttributeCache(attributeCache);
        }

        //玩家的基础属性
        Map<String, Integer> basicMap = basicAttribute(player);
        //玩家的装备属性
        Map<String, Integer> equipMap = equipService.equipAttribute(player);
        //玩家装备栏属性
        Map<String, Integer> equipPosMap = flush(equipService.getEquipPosAttribute(player));

        //TODO 后续还有很多养成
        Map<String, Integer> attributeMap = sumAttributeMap(basicMap, equipMap, equipPosMap);

        attributeCache.setAttributes(attributeMap);
    }


    /**
     * 通过 List<AttributeUnit>... 计算出 属性key =id:属性子类型   value=值
     *
     * @param lists
     * @return
     */
    public Map<String, Integer> flush(List<AttributeUnit>... lists) {
        Map<String, Integer> result = AttributeUtil.sumAttributeList(lists);
        Map<String, Integer> _result = new HashMap<>();
        for (AttributesBasicModel attributesBasicModel : attributesBasicDao.getConfigs()) {
            int attrSubType = attributesBasicModel.getAttrSubType();
            if (attrSubType == AttrSubType.ELEMENT) {
                for (ElementTypeEnum elementTypeEnum : ElementTypeEnum.values()) {
                    attrSubType = elementTypeEnum.id;

                    String valueKey = attributesBasicModel.getId() + ":" + attrSubType + ":" + DateFixConstant.ZHI_XIU_ZHENG;
                    String increaseKey = attributesBasicModel.getId() + ":" + attrSubType + ":" + DateFixConstant.ZENG_BI_XIU_ZHENG;
                    String reductionKey = attributesBasicModel.getId() + ":" + attrSubType + ":" + DateFixConstant.JIAN_BI_XIU_ZHENG;
                    int value = result.get(valueKey) == null ? 0 : result.get(valueKey);
                    int increase = result.get(increaseKey) == null ? 0 : result.get(increaseKey);
                    int reduction = result.get(reductionKey) == null ? 0 : result.get(reductionKey);


                    //把最终值放入集合key=属性id:属性子类型  value =值;
                    value = (int) Math.floor(value * ((1 + increase / CommonConsts.THOUSAND) / (1 + reduction / CommonConsts.THOUSAND)));
                    String resultKey = new StringBuffer().append(attributesBasicModel.getId()).append(":").append(attrSubType).toString();
                    _result.put(resultKey, value);
                }
            } else if (attributesBasicModel.getAttrSubType() == AttrSubType.SKILL_RES) {
                for (SkillSourceEnum skillSourceEnum : SkillSourceEnum.values()) {
                    attrSubType = skillSourceEnum.id;

                    String valueKey = attributesBasicModel.getId() + ":" + attrSubType + ":" + DateFixConstant.ZHI_XIU_ZHENG;
                    String increaseKey = attributesBasicModel.getId() + ":" + attrSubType + ":" + DateFixConstant.ZENG_BI_XIU_ZHENG;
                    String reductionKey = attributesBasicModel.getId() + ":" + attrSubType + ":" + DateFixConstant.JIAN_BI_XIU_ZHENG;
                    int value = result.get(valueKey) == null ? 0 : result.get(valueKey);
                    int increase = result.get(increaseKey) == null ? 0 : result.get(increaseKey);
                    int reduction = result.get(reductionKey) == null ? 0 : result.get(reductionKey);

                    //把最终值放入集合key=属性id:属性子类型  value =值;
                    value = (int) Math.floor(value * ((1 + increase / CommonConsts.THOUSAND) / (1 + reduction / CommonConsts.THOUSAND)));
                    String resultKey = new StringBuffer().append(attributesBasicModel.getId()).append(":").append(attrSubType).toString();
                    _result.put(resultKey, value);
                }
            } else {
                String valueKey = attributesBasicModel.getId() + ":" + attrSubType + ":" + DateFixConstant.ZHI_XIU_ZHENG;
                String increaseKey = attributesBasicModel.getId() + ":" + attrSubType + ":" + DateFixConstant.ZENG_BI_XIU_ZHENG;
                String reductionKey = attributesBasicModel.getId() + ":" + attrSubType + ":" + DateFixConstant.JIAN_BI_XIU_ZHENG;
                int value = result.get(valueKey) == null ? 0 : result.get(valueKey);
                int increase = result.get(increaseKey) == null ? 0 : result.get(increaseKey);
                int reduction = result.get(reductionKey) == null ? 0 : result.get(reductionKey);

                //把最终值放入集合key=属性id:属性子类型  value =值;
                value = (int) Math.floor(value * ((1 + increase / CommonConsts.THOUSAND) / (1 + reduction / CommonConsts.THOUSAND)));
                String resultKey = new StringBuffer().append(attributesBasicModel.getId()).append(":").append(attrSubType).toString();
                _result.put(resultKey, value);
            }
        }
        return _result;
    }

    /**
     * 属性求和
     *
     * @param values 各个模块的属性相加
     * @return
     */
    public Map<String, Integer> sumAttributeMap(Map<String, Integer>... values) {
        Map<String, Integer> resultMap = new HashMap<>();
        for (AttributesBasicModel attributesBasicModel : attributesBasicDao.getConfigs()) {
            for (Map<String, Integer> value : values) {
                int attrSubType = attributesBasicModel.getAttrSubType();
                if (attrSubType == AttrSubType.ELEMENT) {
                    for (ElementTypeEnum elementTypeEnum : ElementTypeEnum.values()) {
                        attrSubType = elementTypeEnum.id;
                        String valueKey = attributesBasicModel.getId() + ":" + attrSubType;
                        int result = resultMap.get(valueKey) == null ? 0 : resultMap.get(valueKey);
                        int _result = value.get(valueKey) == null ? 0 : value.get(valueKey);
                        resultMap.put(valueKey, result + _result);
                    }
                } else if (attributesBasicModel.getAttrSubType() == AttrSubType.SKILL_RES) {
                    for (SkillSourceEnum skillSourceEnum : SkillSourceEnum.values()) {
                        attrSubType = skillSourceEnum.id;
                        String valueKey = attributesBasicModel.getId() + ":" + attrSubType;
                        int result = resultMap.get(valueKey) == null ? 0 : resultMap.get(valueKey);
                        int _result = value.get(valueKey) == null ? 0 : value.get(valueKey);
                        resultMap.put(valueKey, result + _result);

                    }
                } else {
                    String valueKey = attributesBasicModel.getId() + ":" + attrSubType;
                    int result = resultMap.get(valueKey) == null ? 0 : resultMap.get(valueKey);
                    int _result = value.get(valueKey) == null ? 0 : value.get(valueKey);
                    resultMap.put(valueKey, result + _result);
                }
            }
        }
        return resultMap;
    }


    /**
     * 玩家属性转化成一个字符串id:type:value;id:type:value
     *
     * @param values
     * @return
     */
    public String attributeString(Map<String, Integer> values) {
        StringBuffer stringBuffer = new StringBuffer();
        int i = 0;
        for (Map.Entry<String, Integer> entry : values.entrySet()) {
            if (entry.getValue() == null || entry.getValue() == 0) {
                continue;
            }
            stringBuffer.append(entry.getKey()).append(":").append(entry.getValue());
            if (i < values.size() - 1) {
                stringBuffer.append(",");
            }
        }
        return stringBuffer.toString();
    }


}
