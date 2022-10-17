package com.dykj.rpg.common.core;

import com.dykj.rpg.common.cache.ICache0Manager;
import com.dykj.rpg.common.consts.CacheOperation;
import com.dykj.rpg.common.module.uc.model.ServerInfoModel;
import com.dykj.rpg.common.redis.cache.ServerCacheMgr;
import com.dykj.rpg.common.redis.cache.ServerNewCacheMgr;
import com.dykj.rpg.db.queue.DbQueueManager;
import com.dykj.rpg.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author jyb
 * @date 2020/11/9 14:02
 * @Description 注意 线程不安全 需要自己加锁
 */
public abstract class AbstractListCacheMgr<T> implements ICache0Manager<T> {

    /**
     * 集合
     */
    private List<T> ts = new ArrayList<>();

    private Logger logger = LoggerFactory.getLogger("db");

    @Resource
    private ServerNewCacheMgr serverNewCacheMgr;

    @Override
    public void addCache(T t) {
        //dbServer 不会做这个操作
        if (DbQueueManager.getInstance().isDbSaveServer()) {
            return;
        }
        ts.add(t);
        //开启了主从模式，就会远程去同步
        if (serverNewCacheMgr.isRemoteCache()) {
            logger.info("{} remote addCache {} ", this.getClass().getName(), t.toString());
            DbQueueManager.getInstance().sendRemoteQueue(t.getClass().hashCode(), this.getClass().getName(), t.getClass().getName(), CacheOperation.ADD, JsonUtil.toJsonString(t), -1);
        }


    }

    @Override
    public void removeCache(T t) {
        //dbServer 不会做这个操作
        if (DbQueueManager.getInstance().isDbSaveServer()) {
            return;
        }
        Iterator<T> iterator = ts.iterator();
        while (iterator.hasNext()) {
            T t1 = iterator.next();
            if (t1.equals(t)) {
                iterator.remove();
            }
        }
        //开启了主从模式，就会远程去同步
//        if (serverNewCacheMgr.isRemoteCache()) {
//            logger.info("{} remote addCache {} ", this.getClass().getName(), t.toString());
//            DbQueueManager.getInstance().sendRemoteQueue(t.getClass().hashCode(), this.getClass().getName(), t.getClass().getName(), CacheOperation.REMOVE, JsonUtil.toJsonString(t), -1);
//        }

    }

    @Override
    public void updateCache(T t) {
        //dbServer 不会做这个操作
        if (DbQueueManager.getInstance().isDbSaveServer()) {
            return;
        }
        Iterator<T> iterator = ts.iterator();
        while (iterator.hasNext()) {
            T t1 = iterator.next();
            if (t1.equals(t)) {
                t1 = t;
            }
        }
        //开启了主从模式，就会远程去同步
//        if (serverNewCacheMgr.isRemoteCache()) {
//            logger.info("{} remote addCache {} ", this.getClass().getName(), t.toString());
//            DbQueueManager.getInstance().sendRemoteQueue(t.getClass().hashCode(), this.getClass().getName(), t.getClass().getName(), CacheOperation.UPDATE, JsonUtil.toJsonString(t), -1);
//        }

    }

    @Override
    public List<T> values() {
        return ts;
    }

    @Override
    public void addAll(List<T> ts) {
        values().addAll(ts);
    }
}