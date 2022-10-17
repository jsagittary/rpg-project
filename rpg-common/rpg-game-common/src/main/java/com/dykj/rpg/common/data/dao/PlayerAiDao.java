package com.dykj.rpg.common.data.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dykj.rpg.common.data.model.PlayerAiModel;
import com.dykj.rpg.db.dao.BaseDao;

/**
 * @author CaoBing
 * @date 2021/4/7 19:04
 * @Description:
 */
@Repository
public class PlayerAiDao extends BaseDao<PlayerAiModel> {

    /**
     * 根据玩家id拿到玩家所有技巧列表
     * @param playerId 玩家id
     * @return
     */
    public List<PlayerAiModel> getAiEntryModels(int playerId)
    {
        String sql = "select * from player_ai where player_id = ? ";
        return queryForList(sql, playerId);
    }
}
