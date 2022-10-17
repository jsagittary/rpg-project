package com.dykj.rpg.common.redis.cache;

import com.dykj.rpg.common.redis.data.GlobalPlayerData;
import com.dykj.rpg.redis.cache.base.CachePathUtil;
import com.dykj.rpg.redis.cache.redis.AbstractCacheRedisValue;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 不可以直接由这个类操作下线 redis，相关逻辑由 OnlinePlayerService.offLine 完成
* @author Yu.Liang
* @version 创建时间：2018年7月5日 下午4:40:10
*/
@Component
public class OfflinePlayerMgr extends AbstractCacheRedisValue<GlobalPlayerData> {
	@Override
	public String path() {
		return CachePathUtil.cachePath2String("offline",super.path());
	}
	public void setTimeOut(GlobalPlayerData g, long millisecond) {
	  	set(g.primaryKey(), g, millisecond, TimeUnit.MILLISECONDS);
	}
}
