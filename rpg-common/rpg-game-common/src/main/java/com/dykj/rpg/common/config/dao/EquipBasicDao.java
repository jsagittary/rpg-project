package com.dykj.rpg.common.config.dao;

import com.dykj.rpg.db.dao.AbstractConfigDao;
import com.dykj.rpg.common.config.model.EquipBasicModel;
import com.dykj.rpg.util.random.BaseBoxItem;
import com.dykj.rpg.util.random.ItemRandomUtil;
import com.dykj.rpg.util.random.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * @Description 装备基础
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/12
 */
@Repository
public class EquipBasicDao extends AbstractConfigDao<Integer, EquipBasicModel> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 拿出一件装备，effect_box的集合
     * <p>
     * 1、先随机出该装备的最大词条数量
     * <p>
     * 2、取出必出的词条
     * <p>
     * 3、剩余的数量取equip_entry_effect_box_id_group 去随机 补充数量
     *
     * @param equipBasicModel
     * @return
     */
    public List<Integer> getEntryEffectBox(EquipBasicModel equipBasicModel, int num) {
        List<Integer> result = new ArrayList<>();
        List<BaseBoxItem> baseBoxItems = equipBasicModel.getEquipEntryEffectBox();
        Map<Integer, Integer> valueMap = new HashMap<>();
        if (baseBoxItems == null || baseBoxItems.size() < 1) {
            return result;
        }
        for (int i = 0; i < num; i++) {
            refreshItem(baseBoxItems, valueMap);
            if (baseBoxItems.size() < 1) {
                logger.error("EquipBasicDao getEntryEffectBox {} ", equipBasicModel.getEquipId());
                break;
            }
            BaseBoxItem baseBoxItem = ItemRandomUtil.getBoxItem(baseBoxItems);
            result.add(baseBoxItem.getMarkId());
            Integer value = valueMap.get(baseBoxItem.getMarkId());
            valueMap.put(baseBoxItem.getMarkId(), value == null ? 1 : value + 1);

        }
        return result;

    }

    /**
     * 随机到最大数量的时候需要移除
     *
     * @param baseBoxItems
     * @param valueMap
     */
    private void refreshItem(List<BaseBoxItem> baseBoxItems, Map<Integer, Integer> valueMap) {
        Iterator<BaseBoxItem> itemIterator = baseBoxItems.iterator();
        while (itemIterator.hasNext()) {
            BaseBoxItem baseBoxItem = itemIterator.next();
            int value = valueMap.get(baseBoxItem.getMarkId()) == null ? 0 : valueMap.get(baseBoxItem.getMarkId());
            if (baseBoxItem.getValue() <= value) {
                itemIterator.remove();
            }
        }
    }

}