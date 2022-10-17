package com.dykj.rpg.common.data.dao;

import com.dykj.rpg.common.data.model.PlayerItemModel;
import com.dykj.rpg.db.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description 玩家道具dao
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/18
 */
@Repository
public class PlayerItemDao extends BaseDao<PlayerItemModel>
{

    /**
     * 查询某个玩家的所有物品
     * @param playerId
     * @return
     */
    public List<PlayerItemModel> queryForList(int playerId)
    {
        String sql = "select player_id, item_id, item_type, item_num, item_type_detail, " +
                "live_type, live_time, item_lock, item_daily_num, item_get_time from player_item where player_id = ? ";
        return super.queryForList(sql, playerId);
    }
}
