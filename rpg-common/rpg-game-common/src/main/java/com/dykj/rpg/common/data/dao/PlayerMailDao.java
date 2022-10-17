package com.dykj.rpg.common.data.dao;
/**
 * @author CaoBing
 * @date 2021年4月20日
 * @Description:
 */

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dykj.rpg.common.data.model.PlayerMailModel;
import com.dykj.rpg.db.dao.BaseDao;

@Repository
public class PlayerMailDao extends BaseDao<PlayerMailModel>{
	
	 /**
     * 查询某个玩家的所有邮件
     * @param playerId
     * @return
     */
    public List<PlayerMailModel> getPlayerMails(int playerId)
    {
        String sql = "select * from player_mail where player_id = ? ";
        return super.queryForList(sql, playerId);
    }
}

