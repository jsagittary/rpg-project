package com.dykj.rpg.common.data.dao;

import com.dykj.rpg.common.data.model.PlayerEquipModel;
import com.dykj.rpg.db.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jyb
 * @date 2020/11/23 11:33
 * @Description
 */
@Repository
public class PlayerEquipDao extends BaseDao<PlayerEquipModel> {

    /**
     * 查找一个玩家的所有装备
     * @param playerId
     * @return
     */
    public List<PlayerEquipModel> getEquipEntryModels(int  playerId) {
        String sql = "select * from  player_equip  where player_id = ?";
        return queryForList(sql, playerId);
    }
}