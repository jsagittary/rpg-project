package com.dykj.rpg.common.data.dao;

import com.dykj.rpg.common.data.model.PlayerInfoAttachedModel;
import com.dykj.rpg.db.dao.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * @author lyc
 * @date 2021/3/4
 * @Description
 */
@Repository
public class PlayerInfoAttachedDao extends BaseDao<PlayerInfoAttachedModel>
{

    /**
     * 根据玩家id从数据库查询玩家附属信息
     * @param playerId 玩家id
     * @return
     */
    public PlayerInfoAttachedModel getPlayerInfoAttached(int playerId)
    {
        String sql = "select player_id, daily_activity, week_activity, activity_reward_list, is_protector, protector_active_time, protector_last_time from player_info_attached where player_id = ?";
        return super.queryForObject(sql, playerId);
    }
}