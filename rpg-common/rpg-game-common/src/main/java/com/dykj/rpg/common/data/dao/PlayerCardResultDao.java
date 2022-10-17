package com.dykj.rpg.common.data.dao;

import com.dykj.rpg.common.data.model.PlayerCardResultModel;
import com.dykj.rpg.db.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description 玩家抽卡结果dao
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/4
 */
@Repository
public class PlayerCardResultDao extends BaseDao<PlayerCardResultModel>
{
    /**
     * 查询某个玩家的所有抽卡结果
     * @param playerId 玩家id
     * @return 抽卡结果
     */
    public List<PlayerCardResultModel> queryForList(int playerId)
    {
        String sql = "select player_id, card_id, card_result from player_card_result where player_id = ? ";
        return super.queryForList(sql, playerId);
    }
}
