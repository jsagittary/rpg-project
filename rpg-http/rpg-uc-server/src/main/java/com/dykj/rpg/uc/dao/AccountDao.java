package com.dykj.rpg.uc.dao;

import com.dykj.rpg.common.module.uc.model.AccountInfoModel;
import com.dykj.rpg.db.dao.AbstractBaseDao;
import com.dykj.rpg.db.data.BaseModel;
import org.springframework.stereotype.Repository;

/**
 * @Author: jyb
 * @Date: 2020/9/4 11:12
 * @Description:
 */
@Repository
public class AccountDao extends AbstractBaseDao<AccountInfoModel> {

    public AccountInfoModel queryByAccountAndChannel(String channel, String account) {
        return queryForObject("select * from account_info where channel = ? and account =?", channel, account);
    }
}
