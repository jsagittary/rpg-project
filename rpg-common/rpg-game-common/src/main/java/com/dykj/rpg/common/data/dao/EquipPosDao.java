package com.dykj.rpg.common.data.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dykj.rpg.common.data.model.EquipPosModel;
import com.dykj.rpg.db.dao.BaseDao;

/**
 * @author jyb
 * @date 2021/4/6 10:43
 * @Description
 */
@Repository
public class EquipPosDao extends BaseDao<EquipPosModel> {
    /**
     * 查找一个玩家的所有装备
     * @param playerId
     * @return
     */
    public List<EquipPosModel> getEquipEquipPosModels(int  playerId) {
        String sql = "select * from  equip_pos  where player_id = ?";
        return queryForList(sql, playerId);
    }
}