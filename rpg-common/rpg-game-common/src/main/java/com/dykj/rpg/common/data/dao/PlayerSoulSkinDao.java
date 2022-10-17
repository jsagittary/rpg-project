package com.dykj.rpg.common.data.dao;

import com.dykj.rpg.common.data.model.PlayerSoulSkinModel;
import com.dykj.rpg.db.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jyb
 * @date 2021/4/26 11:33
 * @Description
 */
@Repository
public class PlayerSoulSkinDao extends BaseDao<PlayerSoulSkinModel> {

    public List<PlayerSoulSkinModel> getPlayerSoulSkins(int playerId) {
        String sql = "select * from  player_soul_skin  where player_id = ?";
        return queryForList(sql, playerId);
    }

}