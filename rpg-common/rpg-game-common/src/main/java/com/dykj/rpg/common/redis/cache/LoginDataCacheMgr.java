package com.dykj.rpg.common.redis.cache;

import com.dykj.rpg.common.redis.data.LoginData;
import com.dykj.rpg.redis.cache.redis.AbstractCacheRedisValue;
import org.springframework.stereotype.Component;

/**
 * @Author: jyb
 * @Date: 2020/10/12 10:10
 * @Description:
 */
@Component
public class LoginDataCacheMgr extends AbstractCacheRedisValue<LoginData> {
}
