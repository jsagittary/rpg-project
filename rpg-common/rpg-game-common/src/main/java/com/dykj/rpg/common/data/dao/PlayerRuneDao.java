package com.dykj.rpg.common.data.dao;

import com.dykj.rpg.common.data.model.PlayerRuneModel;
import com.dykj.rpg.common.data.model.PlayerTaskModel;
import com.dykj.rpg.db.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description 玩家符文
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/5/20
 */
@Repository
public class PlayerRuneDao extends BaseDao<PlayerRuneModel>
{

    /**
     * 查询某个玩家的所有符文
     * @param playerId 玩家id
     * @return 符文列表
     */
    public List<PlayerRuneModel> queryForList(int playerId)
    {
        String sql = "select player_id, skill_id, rune_id, rune_pos from player_rune where player_id = ? ";
        return super.queryForList(sql, playerId);
    }
}
