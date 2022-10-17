//package com.dykj.rpg.common.redis.core;
//
//
//
//import com.dykj.rpg.common.cache.CacheModel;
//import com.dykj.rpg.db.dao.AbstractBaseDao;
//import com.dykj.rpg.db.queue.Callback;
//import com.dykj.rpg.redis.cache.redis.AbstractCacheRedisMap;
//import com.dykj.rpg.redis.cache.redis.prefix.AbstractCacheRedisListPrefix;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.dao.DataAccessException;
//
//import java.util.Collection;
//import java.util.List;
//
///**
// * @author jyb
// * @date 2019/1/5 14:39
// */
//public abstract class AbstractDBRedisQueue<T extends CacheModel> extends AbstractBaseDao<T> {
//
//    private Logger logger = LoggerFactory.getLogger(getClass());
//
//    /**
//     * 三个操作的 prefix：insert、update、delete
//     */
//    private final static int INSERT = 1;
//    private final static int UPDATE = 2;
//    private final static int DELETE = 3;
//
//    public AbstractDBRedisQueue() {
//    }
//
//    @Value("${serverId}")
//    private int  serverId;
//    public  String prefix(){
//        return  "DB_"+serverId;
//    }
//
//    public abstract AbstractCacheRedisListPrefix<T> listPrefixService();
//
//
//    public abstract AbstractCacheRedisMap<T> dbRedisMapService();
//
//    public int redisQueueInsert(T data) {
//       // data.setOperation(INSERT);
//        listPrefixService().add(prefix(), data);
//        dbRedisMapService().put(data);
//        return 0;
//    }
//
//
//    public boolean redisQueueUpdate(T data) {
//        //data.setOperation(UPDATE);
//        listPrefixService().add(prefix(), data);
//        dbRedisMapService().put(data);
//        return true;
//    }
//
//    public boolean redisQueueDelete(T data) {
//        //data.setOperation(DELETE);
//        listPrefixService().add(prefix(), data);
//        dbRedisMapService().remove(data);
//        return true;
//    }
//
//    @Override
//    public T queryByPrimaykey(Object... args) throws DataAccessException {
//        T t = super.queryByPrimaykey(args);
//        if(t!=null){
//            dbRedisMapService().put(t);
//        }
//        return t;
//    }
//
//    public void insert(Collection<T> data) {
//        if (null != data) {
//            for (T t : data) {
//                queueInsert(t);
//            }
//        }
//    }
//
//
//    public void update(Collection<T> data) {
//        if (null != data) {
//            for (T t : data) {
//                queueUpdate(t);
//            }
//        }
//    }
//
//
//    public void delete(Collection<T> data) {
//        if (null != data) {
//            for (T t : data) {
//                queueDelete(t);
//            }
//        }
//    }
//
//    public void insert(T... data) {
//        if (null != data) {
//            for (T t : data) {
//                queueInsert(t);
//            }
//        }
//    }
//
//    public void update(T... data) {
//        if (null != data) {
//            for (T t : data) {
//                queueUpdate(t);
//            }
//        }
//    }
//
//    public void delete(T... data) {
//        if (null != data) {
//            for (T t : data) {
//                queueDelete(t);
//            }
//        }
//    }
//
//
//    /**
//     * 操作完数据库
//     * @param num
//     * @param begin
//     */
//    private void afterDBFlush(int num, long begin) {
//        try {
//            listPrefixService().removeFirstFew(prefix(), num);
//        } finally {
//            listPrefixService().unlock(lockKey);
//            logger.info("[redis queue] flush {} element cost {}", num, (System.currentTimeMillis() - begin));
//        }
//    }
//
//    public static class DBCallback implements Callback {
//
//        private int flushSize;
//        private long start;
//        private AbstractDBRedisQueue queue;
//        private Callback other;
//
//        public DBCallback(int flushSize, long start, AbstractDBRedisQueue queue, Callback other) {
//            this.flushSize = flushSize;
//            this.start = start;
//            this.queue = queue;
//            this.other = other;
//        }
//
//        public DBCallback(int flushSize, long start, AbstractDBRedisQueue queue) {
//            this.flushSize = flushSize;
//            this.start = start;
//            this.queue = queue;
//        }
//
//        @Override
//        public void callback() {
//            // 完成数据库操作
//            this.queue.afterDBFlush(flushSize, start);
//            if (null != other) {
//                other.callback();
//            }
//        }
//    }
//
//    private final int FLUSH_SIZE = 300;
//    private final String lockKey = "1";
//    @Override
//    public void flush() {
//        List<T> list = null;
//        long start = 0L;
//        boolean tryLock = false;
//        try {
//            tryLock = listPrefixService().tryLock(lockKey, 900 * 1000);
//            if (tryLock) {
//                start = System.currentTimeMillis();
//                list = listPrefixService().subList(prefix(),0, FLUSH_SIZE);
//                if (null != list && 0 < list.size()) {
//                    // 这里调用本地的数据库操作，然后在回调里删除 redis 数据
//                    for (int i=0; i<list.size(); i++) {
//                        T t = list.get(i);
//                        if (INSERT == t.getOperation()) {
//                            super.queueInsert(t);
//                        } else if (UPDATE == t.getOperation()) {
//                            super.queueUpdate(t);
//                        } else if (DELETE == t.getOperation()) {
//                            super.queueDelete(t);
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (tryLock) {
//                if (null != list && 0 < list.size()) {
//                    super.syncFlush(new DBCallback(list.size(), start, this));
//                } else {
//                    listPrefixService().unlock(lockKey);
//                }
//            }
//        }
//    }
//
//    @Override
//    public void syncFlush(Callback callback) {
//        List<T> list = null;
//        long start = 0L;
//        boolean tryLock = false;
//        try {
//            tryLock = listPrefixService().tryLock(lockKey, 900 * 1000);
//            if (tryLock) {
//                start = System.currentTimeMillis();
//                list = listPrefixService().subList(prefix(),0, -1);
//                if (null != list && 0 < list.size()) {
//                    // 这里调用本地的数据库操作，然后在回调里删除 redis 数据
//                    for (int i=0; i<list.size(); i++) {
//                        T t = list.get(i);
//                        if (INSERT == t.getOperation()) {
//                            super.queueInsert(t);
//                        } else if (UPDATE == t.getOperation()) {
//                            super.queueUpdate(t);
//                        } else if (DELETE == t.getOperation()) {
//                            super.queueDelete(t);
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (tryLock) {
//                try {
//                    if (null != list && 0 < list.size()) {
//                        super.syncFlush(new DBCallback(list.size(), start, this, callback));
//                    } else {
//                        if (null != callback) {
//                            callback.callback();
//                        }
//                        listPrefixService().unlock(lockKey);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    public int size() {
//        return listPrefixService().size(prefix());
//    }
//
//    public void clear() {
//        listPrefixService().clear(prefix());
//    }
//
//
//}
