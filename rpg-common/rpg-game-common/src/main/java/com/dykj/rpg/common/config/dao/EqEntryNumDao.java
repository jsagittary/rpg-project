package com.dykj.rpg.common.config.dao;

import com.dykj.rpg.db.dao.AbstractConfigDao;
import com.dykj.rpg.common.config.model.EqEntryNumModel;
import com.dykj.rpg.util.random.ItemRandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * @Description 装备词条数量
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/18
 */
@Repository
public class EqEntryNumDao extends AbstractConfigDao<Integer, EqEntryNumModel> {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public int getEqEntryNum(int equipEntryId) {
        EqEntryNumModel eqEntryNumModel = getConfigByKey(equipEntryId);
        if (eqEntryNumModel == null) {
            logger.error("EqEntryNumDao getEqEntryNum error  equipEntryId {}", equipEntryId);
        }

        return ItemRandomUtil.getBoxItem(eqEntryNumModel.getEqEntryNumItem()).getMarkId();
    }
}