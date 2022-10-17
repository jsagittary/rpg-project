package com.dykj.rpg.common.data.dao;

import com.dykj.rpg.common.data.model.PlayerInfoModel;
import com.dykj.rpg.db.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: jyb
 * @Date: 2020/9/24 14:18
 * @Description:
 */
@Repository
public class PlayerInfoDao extends BaseDao<PlayerInfoModel> {

    /**
     * 通过accountKey查询PlayerInfoModel
     *
     * @param accountKey
     * @return
     */

    public List<PlayerInfoModel> queryByAccountKey(int accountKey, int serverId) {
        return queryForList("select * from player_info where account_key = ? and server_id =?", accountKey, serverId);
    }

    /**
     * 查询最大的玩家id
     *
     * @return
     */
    public int getMaxId() {
        String sql = "select max(player_id) from player_info";
        int maxId = getJdbcTemplate().query(sql, rs -> {
            int i = 0;
            if (rs.next()) {
                i = rs.getInt(1);
            }
            return i;
        });
        return maxId;
    }

    public List<String> getNames() {
        String sql = "select name from player_info";
        List<String> list = getJdbcTemplate().queryForList(sql, String.class);
        return list;
    }

}
