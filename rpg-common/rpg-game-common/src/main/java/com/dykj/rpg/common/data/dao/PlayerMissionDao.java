package com.dykj.rpg.common.data.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dykj.rpg.common.data.model.PlayerMissionModel;
import com.dykj.rpg.db.dao.BaseDao;

/**
 * @author jyb
 * @date 2020/12/21 14:32
 * @Description
 */
@Repository
public class PlayerMissionDao  extends BaseDao<PlayerMissionModel> {

    @Override
    public int insert(PlayerMissionModel data) throws DataAccessException {
        return super.insert(data);
    }

    @Override
    public boolean queueUpdate(PlayerMissionModel data) {
        return super.queueUpdate(data);
    }

    @Override
    public int updateByPrimaykey(PlayerMissionModel data) throws DataAccessException {
        return super.updateByPrimaykey(data);
    }

    @Override
    public boolean queueDelete(PlayerMissionModel data) {
        return super.queueDelete(data);
    }

    @Override
    public int deleteByPrimaykey(PlayerMissionModel data) throws DataAccessException {
        return super.deleteByPrimaykey(data);
    }

    @Override
    public PlayerMissionModel queryByPrimaykey(Object... args) throws DataAccessException {
        return super.queryByPrimaykey(args);
    }
}