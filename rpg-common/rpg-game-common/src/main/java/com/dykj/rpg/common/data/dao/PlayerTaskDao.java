package com.dykj.rpg.common.data.dao;

import com.dykj.rpg.common.data.model.PlayerTaskModel;
import com.dykj.rpg.db.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description 玩家任务dao
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/4
 */
@Repository
public class PlayerTaskDao extends BaseDao<PlayerTaskModel>
{

    /**
     * 查询某个玩家的所有任务
     * @param playerId 玩家id
     * @return 任务列表
     */
    public List<PlayerTaskModel> queryForList(int playerId)
    {
        String sql = "select player_id, task_id, task_type, task_status, task_schedule, task_activation_time, task_reward_status, " +
                "protector_reward_status, last_refresh_time, trigger_status from player_task where player_id = ? ";
        return super.queryForList(sql, playerId);
    }
}
