package com.dykj.rpg.uc.dao;

import com.dykj.rpg.common.module.uc.model.ServerModel;
import com.dykj.rpg.db.dao.AbstractBaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jyb
 * @date 2020/12/31 16:30
 * @Description
 */
@Repository
public class ServerNewDao extends AbstractBaseDao<ServerModel> {
    public List<ServerModel> queryAll() {
        return queryForList("select * from server");
    }
}