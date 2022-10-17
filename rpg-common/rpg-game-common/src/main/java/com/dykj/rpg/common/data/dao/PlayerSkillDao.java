package com.dykj.rpg.common.data.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dykj.rpg.common.data.model.PlayerSkillModel;
import com.dykj.rpg.db.dao.BaseDao;

/**
 * @author jyb
 * @date 2020/11/11 15:16
 * @Description
 */
@Repository
public class PlayerSkillDao extends BaseDao<PlayerSkillModel>
{
    /**
     * 根据玩家id拿到玩家所有技能列表
     * @param playerId 玩家id
     * @return
     */
    public List<PlayerSkillModel> queryListByPlayer(int playerId)
    {
        String sql = "select * from player_skill where player_id = ? ";
        return queryForList(sql, playerId);
    }
}