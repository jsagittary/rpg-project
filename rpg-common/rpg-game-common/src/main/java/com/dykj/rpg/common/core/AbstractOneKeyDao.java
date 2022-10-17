package com.dykj.rpg.common.core;

import com.dykj.rpg.common.cache.DB1Model;
import com.dykj.rpg.common.cache.ICache1Manager;
import com.dykj.rpg.common.consts.CacheOperation;
import com.dykj.rpg.common.module.uc.model.ServerInfoModel;
import com.dykj.rpg.common.redis.cache.ServerCacheMgr;
import com.dykj.rpg.common.redis.cache.ServerNewCacheMgr;
import com.dykj.rpg.db.dao.BaseDao;
import com.dykj.rpg.db.queue.DbQueueManager;
import com.dykj.rpg.util.JsonUtil;
import com.dykj.rpg.util.reflex.ReflexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: jyb
 * @Date: 2020/9/23 19:26
 * @Description: queueInsert queueUpdate queueDelete 如果开启了远程同步，和主从模式，那么有db发出的同步消息去更新数据库和缓存，避免缓存跟db有重复消息发出
 * 如果没有开启远程同步，但是开启了主从，那么缓存要单独发消息去同步缓存
 * 如果如果开启了远程同步，没有开主从，那么有db发出的同步消息去同步数据库，不跟新缓存
 * 两者都没有开启，只在本地做操作，本地异步入库
 */
public abstract class AbstractOneKeyDao<T extends DB1Model> extends BaseDao<T> implements ICache1Manager<T> {

    private Logger logger = LoggerFactory.getLogger("db");

    private Map<Long, T> cacheMap = new ConcurrentHashMap<>();
//    @Resource
//    private ServerCacheMgr serverCacheMgr;
    @Resource
    private ServerNewCacheMgr serverNewCacheMgr;

    @Override
    public boolean queueInsert(T data) {
        addCache(!DbQueueManager.getInstance().isRemote(), data);
        return super.queueInsert(data);
    }

    @Override
    public int insert(T data) throws DataAccessException {
        addCache(true, data);
        return super.insert(data);
    }

    @Override
    public boolean queueUpdate(T data) {
        //只有在不是dbServer 而且不是远程入库的时候 而且是主从模式 才会addcache
        addCache(!DbQueueManager.getInstance().isRemote(), data);
        return super.queueUpdate(data);
    }

    @Override
    public int updateByPrimaykey(T data) throws DataAccessException {
        addCache(true, data);
        return super.updateByPrimaykey(data);
    }

    @Override
    public boolean queueDelete(T data) {
        removeCache(!DbQueueManager.getInstance().isRemote(), data);
        return super.queueDelete(data);
    }

    @Override
    public int deleteByPrimaykey(T data) throws DataAccessException {
        removeCache(true, data);
        return super.deleteByPrimaykey(data);
    }

    @Override
    public T queryByPrimaykey(Object... args) throws DataAccessException {
        T t = super.queryByPrimaykey(args);
        if (t != null) {
            addCache(true, t);
        }
        return t;
    }

    /**
     * 此方法调用如果配置了主从，会发消息到kafka
     *
     * @param t
     */
    @Override
    public void addCache(T t) {
        addCache(true, t);
    }

    private void addCache(boolean remote, T t) {
        //dbServer 不会做这个操作
        if (DbQueueManager.getInstance().isDbSaveServer()) {
            return;
        }
        logger.info("{} local addCache {} ",this.getClass().getName(),t.toString());
        cacheMap.put(t.cachePrimaryKey(), t);
        //只有在没有开启远程异步入库，但是开启了主从模式，对数据库的操作才由于缓存自己去同步从服务器的缓存
//        if (remote && serverNewCacheMgr.isRemoteCache()) {
//            logger.info("{} remote addCache {} ",this.getClass().getName(),t.toString());
//            DbQueueManager.getInstance().sendRemoteQueue(getQueueId(), this.getClass().getName(), t.getClass().getName(), CacheOperation.ADD, JsonUtil.toJsonString(t));
//        }
    }


    /**
     * 此方法调用如果配置了主从，会发消息到kafka
     *
     * @param t
     */
    @Override
    public void removeCache(T t) {
        removeCache(true, t);
    }


    private void removeCache(boolean remote, T t) {
        if (DbQueueManager.getInstance().isDbSaveServer()) {
            return;
        }
        logger.info("{} local removeCache {} ",this.getClass().getName(),t.toString());
        cacheMap.remove(t.cachePrimaryKey());
//        if (remote && serverNewCacheMgr.isRemoteCache()) {
//            logger.info("{} remote removeCache {} ",this.getClass().getName(),t.toString());
//            DbQueueManager.getInstance().sendRemoteQueue(getQueueId(), this.getClass().getName(), t.getClass().getName(), CacheOperation.REMOVE, JsonUtil.toJsonString(t));
//        }

    }

    /**
     * 此方法调用如果配置了主从，会发消息到kafka
     *
     * @param key
     */
    @Override
    public void removeCache(long key) {
        T t = getCache(key);
        if (t != null) {
            removeCache(t);
        }
    }


    public Map<Long, T> getCacheMap() {
        return cacheMap;
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
            t = (T) clazz.newInstance();
            t.primaryKey(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
}
