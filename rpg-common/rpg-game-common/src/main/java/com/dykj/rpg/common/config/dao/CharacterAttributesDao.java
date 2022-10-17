package com.dykj.rpg.common.config.dao;

import com.dykj.rpg.common.config.model.CharacterAttributesModel;
import com.dykj.rpg.db.dao.AbstractConfigDao;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 主角属性
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/9/29
 */
@Repository
public class CharacterAttributesDao extends AbstractConfigDao<Integer, CharacterAttributesModel> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 查找属性
     *
     * @param growClass
     * @param level
     * @return
     */
    public CharacterAttributesModel get(int growClass, int level) {
        for (CharacterAttributesModel c : getConfigs()) {
            if (c.getGrowClass() == growClass && c.getLevel().get(0) <= level && level <= c.getLevel().get(1)) {
                return c;
            }

        }
        logger.error("CharacterAttributesDao get error  growClass {} level {}", growClass, level);
        return null;
    }
}