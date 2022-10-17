package com.dykj.rpg.common.redis.cache;

import com.dykj.rpg.common.redis.data.GlobalPlayerData;
import com.dykj.rpg.redis.cache.base.CachePathUtil;
import com.dykj.rpg.redis.cache.redis.AbstractCacheRedisMap;
import com.dykj.rpg.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhenbiao.cai
 * @date 2018/6/20 11:56
 */
@Component
public class OnlinePlayerMgr extends AbstractCacheRedisMap<GlobalPlayerData> {

    @Override
    public String path() {
        return CachePathUtil.cachePath2String("Online",super.path());
    }

    public void online(GlobalPlayerData playerData) {
        op(playerData, 1);
    }

    public void offLine(GlobalPlayerData playerData) {
        op(playerData, 2);
    }

    public void update(GlobalPlayerData playerData) {
        op(playerData, 3);
    }

    public GlobalPlayerData get(int playerId) {
        GlobalPlayerData data = new GlobalPlayerData();
        data.setPlayerId(playerId);
        String s = op(data, 4);
        if (null != s && 0 < s.length()) {
            return JsonUtil.toInstance(s, GlobalPlayerData.class);
        }
        return null;
    }


    public GlobalPlayerData getOnline(Object key) {
        return super.get(key);
    }

    public final static int EXPIRE = 60 * 60 * 1000;
    @Resource
    private RedisScript<String> globalPlayer;
    @Resource
    private OfflinePlayerMgr offlinePlayerMgr;

    private String op(GlobalPlayerData playerData, int op) {
        List<String> keys = Arrays.asList(path(), offlinePlayerMgr.path());
        String s = getRedisTemplate().execute(globalPlayer, keys, String.valueOf(op), JsonUtil.toJsonString(playerData), String.valueOf(EXPIRE));
        return s;
    }
}
