package com.dykj.rpg.common.core;

import com.dykj.rpg.common.cache.Db2Model;
import com.dykj.rpg.common.cache.ICache;
import com.dykj.rpg.common.cache.ICache2Manager;
import com.dykj.rpg.common.consts.CacheOperation;
import com.dykj.rpg.common.module.uc.model.ServerInfoModel;
import com.dykj.rpg.common.redis.cache.ServerCacheMgr;
import com.dykj.rpg.common.redis.cache.ServerNewCacheMgr;
import com.dykj.rpg.db.consts.DBOperation;
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
public abstract class AbstractTwoKeyDao<V extends Db2Model, T extends ICache> extends BaseDao<V> implements ICache2Manager<V, T> {

    private Map<Long, T> cacheMap = new ConcurrentHashMap<>();

//    @Resource
//    private ServerCacheMgr serverCacheMgr;
    @Resource
    private ServerNewCacheMgr serverNewCacheMgr;


    private Logger logger = LoggerFactory.getLogger("db");

    @Override
    public boolean queueInsert(V data) {
        updateValue(!DbQueueManager.getInstance().isRemote(), data);
        return super.queueInsert(data);
    }

    @Override
    public int insert(V data) throws DataAccessException {
        updateValue(true, data);
        return super.insert(data);
    }

    @Override
    public boolean queueUpdate(V data) {
        updateValue(!DbQueueManager.getInstance().isRemote(), data);
        return super.queueUpdate(data);
    }

    @Override
    public int updateByPrimaykey(V data) throws DataAccessException {
        updateValue(true, data);
        return super.updateByPrimaykey(data);
    }

    @Override
    public boolean queueDelete(V data) {
        removeValue(!DbQueueManager.getInstance().isRemote(), data);
        return super.queueDelete(data);
    }

    @Override
    public int deleteByPrimaykey(V data) throws DataAccessException {
        removeValue(true, data);
        return super.deleteByPrimaykey(data);
    }

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
//        if (remote &&serverNewCacheMgr.isRemoteCache()) {
//            logger.info("{} local updateValue {} ", this.getClass().getName(), v.toString());
//            DbQueueManager.getInstance().sendRemoteQueue(getQueueId(), this.getClass().getName(), v.getClass().getName(), DBOperation.UPDATE, JsonUtil.toJsonString(v), v.KeyNum());
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
//            DbQueueManager.getInstance().sendRemoteQueue(getQueueId(), this.getClass().getName(), v.getClass().getName(), DBOperation.DELETE, JsonUtil.toJsonString(v),v.KeyNum());
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
//            DbQueueManager.getInstance().sendRemoteQueue(getQueueId(), this.getClass().getName(), t.getClass().getName(), CacheOperation.ADD, JsonUtil.toJsonString(t), 2);
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
//            //System.out.println(JsonUtil.toJsonString(t));
//            DbQueueManager.getInstance().sendRemoteQueue(getQueueId(), this.getClass().getName(), t.getClass().getName(), CacheOperation.REMOVE, JsonUtil.toJsonString(t), 2);
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

    public T create(long key) {
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
