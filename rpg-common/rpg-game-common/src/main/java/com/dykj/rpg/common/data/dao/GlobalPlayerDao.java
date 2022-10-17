package com.dykj.rpg.common.data.dao;

import com.dykj.rpg.common.core.GlobalBaseDao;
import com.dykj.rpg.common.data.model.GlobalPlayerModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: jyb
 * @Date: 2020/10/9 15:21
 * @Description:
 */
@Repository
public class GlobalPlayerDao extends GlobalBaseDao<GlobalPlayerModel> {


    /**
     * 根据accountKey 查询账号列表
     *
     * @param accountKey
     * @param
     * @return
     */
    public List<GlobalPlayerModel> queryList(int accountKey) {
        String sql = "select * from global_player where account_key = ? ";
        return queryForList(sql, accountKey);
    }

}
