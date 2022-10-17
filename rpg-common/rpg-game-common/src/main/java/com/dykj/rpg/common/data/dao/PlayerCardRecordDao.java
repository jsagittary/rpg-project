package com.dykj.rpg.common.data.dao;

import com.dykj.rpg.common.data.model.PlayerCardRecordModel;
import com.dykj.rpg.db.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description 玩家抽卡记录dao
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/29
 */
@Repository
public class PlayerCardRecordDao extends BaseDao<PlayerCardRecordModel>
{
    /**
     * 查询某个玩家的所有抽卡记录
     * @param playerId 玩家id
     * @return 抽卡记录列表
     */
    public List<PlayerCardRecordModel> queryForList(int playerId)
    {
        String sql = "select player_id, card_id, card_status, button_extract_number, button_id, button_last_extract_time from player_card_record where player_id = ? ";
        return super.queryForList(sql, playerId);
    }
}
