package com.dykj.rpg.common.config.dao;
import com.dykj.rpg.common.config.model.ValueRangeModel;
import com.dykj.rpg.db.dao.AbstractConfigDao;
import com.dykj.rpg.util.random.BaseBoxItem;
import com.dykj.rpg.util.random.ItemRandomUtil;
import com.dykj.rpg.util.random.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.temporal.ValueRange;
import java.util.List;

/**
 * @Description 装备词条值域
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/24
 */
@Repository
public class ValueRangeDao extends AbstractConfigDao<Integer, ValueRangeModel> {

    private Logger logger = LoggerFactory.getLogger(getClass());


    /**
     * @param equip_entry_value_range_id
     * @return
     */
    public int getRandomValue(int equip_entry_value_range_id) {
        if (equip_entry_value_range_id == 0) {
            return 0;
        }
        ValueRangeModel valueRangeModel = getConfigByKey(equip_entry_value_range_id);
        if (valueRangeModel == null) {
            logger.error("ValueRangeDao  getRandomResult error  equip_entry_value_range_id  {} ", equip_entry_value_range_id);
        }
        BaseBoxItem baseBoxItem = ItemRandomUtil.getBoxItem(valueRangeModel.getValueRangeItem());
        String result = valueRangeModel.getValueRangeRate(baseBoxItem.getMarkId());
        String[] rs = result.split("\\:");
        return RandomUtil.randomBetween(Integer.valueOf(rs[0]), Integer.valueOf(rs[1]));
    }


    /**
     * 获得值域集合
     *
     * @param rangeIds
     * @return
     */
    public int[] getRandomValue(List<Integer> rangeIds) {
        int[] result = new int[rangeIds.size()];
        for (int i = 0; i < rangeIds.size(); i++) {
            result[i] = getRandomValue(rangeIds.get(i));
        }
        return result;
    }
}