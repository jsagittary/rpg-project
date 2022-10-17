package com.dykj.rpg.common.core;

import com.dykj.rpg.common.cache.ICache;
import com.dykj.rpg.common.cache.ICache2Manager;
import com.dykj.rpg.common.cache.ICache2PrimaryKey;
import com.dykj.rpg.common.consts.CacheOperation;
import com.dykj.rpg.common.module.uc.model.ServerInfoModel;
import com.dykj.rpg.common.redis.cache.ServerCacheMgr;
import com.dykj.rpg.common.redis.cache.ServerNewCacheMgr;
import com.dykj.rpg.db.consts.DBOperation;
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
 * @Date: 2020/10/28 14:16
 * @Description:
 */
public abstract class AbstractTwoKeyCacheMgr<V extends ICache2PrimaryKey, T extends ICache> implements ICache2Manager<V, T> {

    /**
     * 缓存集合
     */
    private Map<Long, T> cacheMap = new ConcurrentHashMap<>();
//    @Resource
//    private ServerCacheMgr serverCacheMgr;
    @Resource
    private ServerNewCacheMgr serverNewCacheMgr;


    private Logger logger = LoggerFactory.getLogger("db");


    @Override
    public void updateValue(V v) {
        updateValue(true, v);
    }


    private void updateValue(boolean remote, V v) {
        if (DbQueueManager.getInstance().isDbSaveServer()) {
            return;
        }
        T t = cacheMap.get(v.primaryKey());
        if (t == null) {
            t = create(v.primaryKey());
            cacheMap.put(t.cachePrimaryKey(), t);
        }
        t.update(v);
        logger.info("{} local updateValue {} ", this.getClass().getName(), v.toString());
//        if (remote && serverNewCacheMgr.isRemoteCache()) {
//            logger.info("{} local updateValue {} ", this.getClass().getName(), v.toString());
//            DbQueueManager.getInstance().sendRemoteQueue(this.getClass().hashCode(), this.getClass().getName(), v.getClass().getName(), DBOperation.UPDATE, JsonUtil.toJsonString(v),2);
//        }
    }


    public void removeValue(boolean remote, V v) {
        if (DbQueueManager.getInstance().isDbSaveServer()) {
            return;
        }
        T t = cacheMap.get(v.primaryKey());
        if (t != null) {
            t.remove(v);
        }
        logger.info("{} local removeValue {} ", this.getClass().getName(), v.toString());
//        if (remote && serverNewCacheMgr.isRemoteCache()) {
//            logger.info("{} remote removeValue {} ", this.getClass().getName(), v.toString());
//            DbQueueManager.getInstance().sendRemoteQueue(this.getClass().hashCode(), this.getClass().getName(), v.getClass().getName(), DBOperation.DELETE, JsonUtil.toJsonString(t),2);
//        }
    }

    @Override
    public void removeValue(V v) {
        removeValue(true, v);
    }


    @Override
    public void addCache(T t) {
        if (DbQueueManager.getInstance().isDbSaveServer()) {
            return;
        }
        logger.info("{} local addCache {} ", this.getClass().getName(), t.toString());
        cacheMap.put(t.cachePrimaryKey(), t);
//        if (serverNewCacheMgr.isRemoteCache()) {
//            logger.info("{} remote addCache {} ", this.getClass().getName(), t.toString());
//            DbQueueManager.getInstance().sendRemoteQueue(this.getClass().hashCode(), this.getClass().getName(), t.getClass().getName(), CacheOperation.ADD, JsonUtil.toJsonString(t), 2);
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
//            DbQueueManager.getInstance().sendRemoteQueue(this.getClass().hashCode(), this.getClass().getName(), t.getClass().getName(), CacheOperation.REMOVE,  JsonUtil.toJsonString(t), 2);
//        }
    }

    @Override
    public void removeCache(Long key) {
        T t = getCache(key);
        if (t != null) {
            removeCache(t);
        }
    }


    @Override
    public T getCache(Long key) {
        return cacheMap.get(key);
    }

    private T create(long key) {
        T t = null;
        try {
            t = (T) ReflexUtil.getClass(getClass(), 1).newInstance();
            t.primaryKey(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    @Override
    public List<T> values() {
        return new ArrayList<>(cacheMap.values());
    }

    @Override
    public V getValue(Long key1, Long key2) {
        T t = cacheMap.get(key1);
        if (t != null) {
            return (V) t.selectByKey2(key2);
        }
        return null;
    }
}
