package com.dykj.rpg.common.config.dao;

import com.dykj.rpg.common.config.model.CharacterBasicModel;
import com.dykj.rpg.db.dao.AbstractConfigDao;
import org.springframework.stereotype.Repository;

/**
 * @Description 主角定义
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/9/29
 */
@Repository
public class CharacterBasicDao extends AbstractConfigDao<Integer, CharacterBasicModel>
{
    /**
     * 是否存在该职业
     * @param characterId
     * @return
     */
    public boolean isExist(int characterId) {
        return getConfigByKey(characterId) == null ? false : true;
    }
}