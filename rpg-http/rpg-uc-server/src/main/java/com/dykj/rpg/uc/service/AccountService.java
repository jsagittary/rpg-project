package com.dykj.rpg.uc.service;

import com.dykj.rpg.common.module.uc.model.AccountInfoModel;
import com.dykj.rpg.common.redis.cache.AccountCacheMgr;
import com.dykj.rpg.uc.dao.AccountDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Author: jyb
 * @Date: 2020/9/4 11:19
 * @Description:
 */
@Service
public class AccountService {
    @Resource
    private AccountDao accountDao;

    @Resource
    private AccountCacheMgr accountCacheMgr;

    public final static int KEEP_TIME = 1000 * 60 * 30;

    /**
     * 查询账号，如果没有创建一个，这里要优化成redis拿
     *
     * @param channel
     * @param account
     * @return
     */
    public AccountInfoModel getAccount(String channel, String account) {
        AccountInfoModel accountInfoModel = accountCacheMgr.get(account, channel);
        if (accountInfoModel != null) {
            return accountInfoModel;
        }
        accountInfoModel = accountDao.queryByAccountAndChannel(channel, account);
        if (accountInfoModel == null) {
            accountInfoModel = new AccountInfoModel();
            accountInfoModel.setAccount(account);
            accountInfoModel.setChannel(channel);
            accountInfoModel.setCreateTime(new Date());
            accountInfoModel.setLoginTime(new Date());
            int key = accountDao.insert(accountInfoModel);
            accountInfoModel.setAccountKey(key);
        }
        accountCacheMgr.setTimeOut(accountInfoModel, KEEP_TIME);
        return accountInfoModel;
    }


    public  void  updateAccount(AccountInfoModel accountInfoModel){
        accountDao.updateByPrimaykey(accountInfoModel);
    }
}
