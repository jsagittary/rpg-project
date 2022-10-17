package com.dykj.rpg.common.config.dao;

import com.dykj.rpg.db.dao.AbstractConfigDao;
import com.dykj.rpg.common.config.model.LootDropteamModel;
import org.springframework.stereotype.Repository;

/**
 * @Description 掉落组
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/31
*/
@Repository
public class LootDropteamDao extends AbstractConfigDao<Integer, LootDropteamModel>
{
    /**
     * 通过掉落组id 和职业查找掉落数据
     * @param dropteamId
     * @param character
     * @return
     */
    public LootDropteamModel getLootDropteamModel(int dropteamId, int character) {
        for (LootDropteamModel lootDropteamModel : getConfigs()) {
            if (lootDropteamModel.getDropteamId() == dropteamId) {
                if (lootDropteamModel.getDropteamCharacter().contains(0) || lootDropteamModel.getDropteamCharacter().contains(character)) {
                    return lootDropteamModel;
                }
            }
        }
        return null;
    }
}