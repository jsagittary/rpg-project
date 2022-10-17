package com.dykj.rpg.uc.dao;

import com.dykj.rpg.common.module.uc.model.ServerInfoModel;
import com.dykj.rpg.db.dao.AbstractBaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class ServerDao extends AbstractBaseDao<ServerInfoModel> {
    public List<ServerInfoModel> queryAll() {
        return queryForList("select * from server_info");
    }
}
