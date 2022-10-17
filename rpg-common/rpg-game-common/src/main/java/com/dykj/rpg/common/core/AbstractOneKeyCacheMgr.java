package com.dykj.rpg.common.core;

import com.dykj.rpg.common.cache.ICache1Manager;
import com.dykj.rpg.common.cache.ICachePrimaryKey;
import com.dykj.rpg.common.consts.CacheOperation;
import com.dykj.rpg.common.module.uc.model.ServerInfoModel;
import com.dykj.rpg.common.redis.cache.ServerCacheMgr;
import com.dykj.rpg.common.redis.cache.ServerNewCacheMgr;
import com.dykj.rpg.db.queue.DbQueueManager;
import com.dykj.rpg.util.JsonUtil;
import com.dykj.rpg.util.reflex.ReflexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: jyb
 * @Date: 2020/10/28 14:05
 * @Description:
 */
public abstract class AbstractOneKeyCacheMgr<T extends ICachePrimaryKey> implements ICache1Manager<T> {
    /**
     * 缓存集合
     */
    private Map<Long, T> cacheMap = new ConcurrentHashMap<>();
    @Resource
    private ServerCacheMgr serverCacheMgr;
    @Resource
    private ServerNewCacheMgr serverNewCacheMgr;


    private Logger logger = LoggerFactory.getLogger("db");

    @Override
    public void addCache(T t) {
        //dbServer 不会做这个操作
        if (DbQueueManager.getInstance().isDbSaveServer()) {
            return;
        }
        logger.info("{} local addCache {} ", this.getClass().getName(), t.toString());
        cacheMap.put(t.cachePrimaryKey(), t);
        ServerInfoModel serverInfoModel = serverCacheMgr.get(DbQueueManager.getInstance().getServerId());
        //开启了主从模式，就会远程去同步
//        if (serverNewCacheMgr.isRemoteCache()) {
//            logger.info("{} remote addCache {} ", this.getClass().getName(), t.toString());
//            DbQueueManager.getInstance().sendRemoteQueue(this.getClass().getSimpleName().hashCode(), this.getClass().getName(), t.getClass().getName(), CacheOperation.ADD, JsonUtil.toJsonString(t));
//        }
    }

    @Override
    public void removeCache(T t) {
        if (DbQueueManager.getInstance().isDbSaveServer()) {
            return;
        }
        logger.info("{} local removeCache {} ", this.getClass().getName(), t.toString());
        cacheMap.remove(t.cachePrimaryKey());
//        if (serverNewCacheMgr.isRemoteCache()) {
//            logger.info("{} remote removeCache {} ", this.getClass().getName(), t.toString());
//            DbQueueManager.getInstance().sendRemoteQueue(this.getClass().getSimpleName().hashCode(), this.getClass().getName(), t.getClass().getName(), CacheOperation.REMOVE, JsonUtil.toJsonString(t));
//        }
    }

    @Override
    public void removeCache(long key) {
        T t = getCache(key);
        if (t != null) {
            removeCache(t);
        }
    }

    @Override
    public T getCache(long key) {
        return cacheMap.get(key);
    }

    @Override
    public List<T> values() {
        return new ArrayList<>(cacheMap.values());
    }

    private T create(int key) {
        T t = null;
        try {
            t = (T) ReflexUtil.getClass(getClass(), 0).newInstance();
            t.primaryKey(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
}
