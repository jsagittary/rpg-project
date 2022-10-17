package com.dykj.rpg.common.redis.cache;

import com.dykj.rpg.common.module.uc.model.AccountInfoModel;
import com.dykj.rpg.redis.cache.redis.AbstractCacheRedisValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: jyb
 * @Date: 2020/9/7 11:14
 * @Description:
 */
@Component
public class AccountCacheMgr extends AbstractCacheRedisValue<AccountInfoModel> {

    /**
     * 设置账号到redis
     *
     * @param a
     * @param millisecond
     */
    public void setTimeOut(AccountInfoModel a, long millisecond) {
        set(a.primaryKey(), a, millisecond, TimeUnit.MILLISECONDS);
    }

    @Resource
    private RedisScript<Long> ipLimitScript;

    /**
     * 通过渠道跟账号 查找账号
     * @param account
     * @param channel
     * @return
     */
    public AccountInfoModel get(String account, String channel) {
        String key = channel + ":" + account;
        return get(key);
    }

    public Long ipLimit(String ip, int time, int num) {
        Logger Logger = LoggerFactory.getLogger(getClass());
        List<String> keys = Arrays.asList(ip);
        Logger.info("GameServerService parms [" + ip + "," + time + "," + num + "], sha1 =" + ipLimitScript.getSha1());
        Long result = this.getRedisTemplate().execute(ipLimitScript, keys,String.valueOf(time) , String.valueOf(num));
        Logger.info("result =" + result);
        return result;

    }


}
