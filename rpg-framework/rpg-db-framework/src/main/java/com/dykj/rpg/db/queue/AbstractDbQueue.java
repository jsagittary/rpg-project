package com.dykj.rpg.db.queue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;

/**
 * @Author: jyb
 * @Date: 2018/3/6 18:23
 * @Description:
 */
public abstract class AbstractDbQueue<T> implements IDbQueue<T> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 插入队列
     */
    protected List<T> insertQueue = new ArrayList<T>();

    /**
     * 更新队列
     */
    protected List<T> updateQueue = new ArrayList<T>();
    /***
     * 删除队列
     */
    protected List<T> deleteQueue = new ArrayList<T>();
    /**
     * 同步锁
     */
    private Lock lock = new ReentrantLock();
    /**
     * 最大长度
     */
    private int capacity = 1024;
    /**
     * 最大同步时间
     */
    private int maxTimeWait = 600 * 1000;
    /**
     * 最后一次刷新数据系统时间
     */
    private long flushTimeMillis = System.currentTimeMillis();
    /**
     * 同步刷新
     */
    private AtomicBoolean aynsFlushMark = new AtomicBoolean(false);
    /***
     * 队列id
     */
    private final int queueId;

    public AbstractDbQueue() {
        queueId = DbQueueManager.getInstance().getQueueId();
        DbQueueManager.getInstance().addQueue(this.getClass());
    }

    private boolean doInsert(T data) {
        boolean result = false;
        if (deleteQueue != null) {
            result = deleteQueue.remove(data);
        }
        if (result) {
            result = updateQueue.add(data);
        } else {
            result = insertQueue.add(data);
        }
        return result;
    }

    private boolean doUpdate(T data) {
        if (deleteQueue != null) {
            deleteQueue.remove(data);
        }
        if (insertQueue != null) {
            if (insertQueue.contains(data)) {
                // 由于dubbo在序列化过来的时候 同一条数据内存地址不一样 放在内存中也无法改变更改的值
                insertQueue.remove(data);
                insertQueue.add(data);
                return false;
            }
        }
        return updateQueue.add(data);
    }

    private boolean doDelete(T data) {
        if (insertQueue != null) {
            if (insertQueue.remove(data)) {
                return false;
            }
        }
        if (updateQueue != null) {
            updateQueue.remove(data);
        }
        return deleteQueue.add(data);
    }

    @Override
    public boolean queueInsert(T data) {
        boolean result = false;
        lock.lock();
        try {
            result = doInsert(data);
        } finally {
            lock.unlock();
            tryFlush();
        }
        return result;
    }

    @Override
    public boolean queueUpdate(T data) {
        lock.lock();
        try {
            return doUpdate(data);
        } finally {
            lock.unlock();
            tryFlush();
        }
    }

    @Override
    public boolean queueDelete(T data) {
        lock.lock();
        try {
            return doDelete(data);
        } finally {
            lock.unlock();
            tryFlush();
        }
    }

    protected void tryFlush() {
        boolean needFlush = isNeedFlush();
        if (!needFlush) {
            return;
        }
        aynsFlush();
    }

    /**
     * 异步刷新数据到数据库
     */
    protected void aynsFlush() {
        if (aynsFlushMark.get()) {
            return;
        }
        aynsFlushMark.set(true);
        flush();
        aynsFlushMark.set(false);
    }

    /**
     * 验证是否达到了写入数据库的条件
     *
     * @return 如果已经达到了写入数据库条件返回true
     */
    private boolean isNeedFlush() {
        int size = size();
        if (size >= capacity) {
            return true;
        }
        if ((System.currentTimeMillis() - flushTimeMillis) > maxTimeWait) {
            return true;
        }
        return false;
    }

    private int size() {
        int size = 0;
        if (insertQueue != null) {
            size += insertQueue.size();
        }
        if (updateQueue != null) {
            size += updateQueue.size();
        }
        if (deleteQueue != null) {
            size += deleteQueue.size();
        }
        return size;
    }

    //@Scheduled(initialDelay = 600, fixedRate = 5000)
    public void flush() {
        //System.out.println(this.getClass());
        List<T> insertList = null;
        List<T> updateList = null;
        List<T> deleteList = null;
        lock.lock();
        try {
            // 后面异步写数据库这里设置为true，flush方法有些时候数据不会刷新到数据库，导致顺序错乱
            flushTimeMillis = System.currentTimeMillis();
            if (insertQueue != null && !insertQueue.isEmpty()) {
                insertList = new ArrayList<T>(insertQueue.size());
                insertList.addAll(insertQueue);
                insertQueue.clear();
            }
            if (updateQueue != null && !updateQueue.isEmpty()) {
                updateList = new ArrayList<T>(updateQueue.size());
                updateList.addAll(updateQueue);
                updateQueue.clear();
            }
            if (deleteQueue != null && !deleteQueue.isEmpty()) {
                deleteList = new ArrayList<T>(deleteQueue.size());
                deleteList.addAll(deleteQueue);
                deleteQueue.clear();
            }
            if (insertList == null && updateList == null && deleteList == null) {
                return;
            }
        } catch (Throwable ex) {
            logger.error("", ex);
        } finally {
            lock.unlock();
        }
        try {
            // for (T t : insertList) {
            // System.out.println(this.getClass() + ";" + t.toString());
            // }
            DbQueueManager.getInstance().getExecutor(queueId).execute(new DBSaveTask(this, insertList, updateList, deleteList));
        } catch (Exception e) {
            logger.error("AbstractDBQueue {}  executor error  {} ", this.getClass().getSimpleName(), e);
        }

    }

    public void doFlush(List<T> insertList, List<T> updateList, List<T> deleteList) {
        try {
            if (deleteList != null) {
                if (!deleteList.isEmpty()) {
                    if (deleteList.size() > 4096) {
                        List<T> limitNumList = new ArrayList<T>();
                        for (T data : deleteList) {
                            limitNumList.add(data);
                            if (limitNumList.size() >= 4096) {
                                flushDelete(limitNumList);
                                limitNumList.clear();
                            }
                        }
                        if (!limitNumList.isEmpty()) {
                            flushDelete(limitNumList);
                        }
                    } else {
                        flushDelete(deleteList);
                    }
                    deleteList.clear();
                }
            }
        } catch (Throwable ex) {
            logger.error("", ex);
            // 以防在出现错误时所有数据都不会更新
            if (deleteList != null && !deleteList.isEmpty()) {
                for (T data : deleteList) {
                    try {
                        singleDelete(data);
                    } catch (Throwable e) {
                        logger.error("{}", data, e);
                    }
                }
                deleteList.clear();
            }
        }
        try {
            if (updateList != null) {
                if (!updateList.isEmpty()) {
                    if (updateList.size() > 4096) {
                        List<T> limitNumList = new ArrayList<T>();
                        for (T data : updateList) {
                            limitNumList.add(data);
                            if (limitNumList.size() >= 4096) {
                                flushUpdate(limitNumList);
                                limitNumList.clear();
                            }
                        }
                        if (!limitNumList.isEmpty()) {
                            flushUpdate(limitNumList);
                        }
                    } else {
                        flushUpdate(updateList);
                    }
                    updateList.clear();
                }
            }
        } catch (Throwable ex) {
            logger.error("", ex);
            // 以防在出现错误时所有数据都不会更新
            if (updateList != null && !updateList.isEmpty()) {
                for (T data : updateList) {
                    try {
                        singleUpdate(data);
                    } catch (Throwable e) {
                        logger.error("{}", data, e);
                    }
                }
                updateList.clear();
            }
        }
        try {
            if (insertList != null) {
                if (!insertList.isEmpty()) {
                    if (insertList.size() > 4096) {
                        List<T> limitNumList = new ArrayList<T>();
                        for (T data : insertList) {
                            limitNumList.add(data);
                            if (limitNumList.size() >= 4096) {
                                flushInsert(limitNumList);
                                limitNumList.clear();
                            }
                        }
                        if (!limitNumList.isEmpty()) {
                            flushInsert(limitNumList);
                        }
                    } else {
                        flushInsert(insertList);
                    }
                    insertList.clear();
                }
            }
        } catch (Throwable ex) {
            logger.error("", ex);
            // 以防在出现错误时所有数据都不会更新
            if (insertList != null && !insertList.isEmpty()) {
                for (T data : insertList) {
                    try {
                        singleInsert(data);
                    } catch (DuplicateKeyException e) {
                        duplicateKey(data);
                        logger.error("{}", data, e);
                    } catch (Throwable e) {
                        logger.error("{}", data, e);
                    }
                }
                insertList.clear();
            }
        }
    }

    /**
     * 主键冲突
     *
     * @param data
     */
    private void duplicateKey(T data) {
        singleUpdate(data);
    }

    public void syncFlush(Callback callback) {
        List<T> insertList = null;
        List<T> updateList = null;
        List<T> deleteList = null;
        lock.lock();
        try {
            if (insertQueue != null && !insertQueue.isEmpty()) {
                insertList = new ArrayList<T>(insertQueue.size());
                insertList.addAll(insertQueue);
                insertQueue.clear();
            }
            if (updateQueue != null && !updateQueue.isEmpty()) {
                updateList = new ArrayList<T>(updateQueue.size());
                updateList.addAll(updateQueue);
                updateQueue.clear();
            }
            if (deleteQueue != null && !deleteQueue.isEmpty()) {
                deleteList = new ArrayList<T>(deleteQueue.size());
                deleteList.addAll(deleteQueue);
                deleteQueue.clear();
            }
        } catch (Throwable ex) {
            logger.error("", ex);
        } finally {
            lock.unlock();
        }
        DbQueueManager.getInstance().getExecutor(queueId)
                .execute(new DBSaveTask<T>(this, insertList, updateList, deleteList, callback));
    }

    /**
     * 刷新插入数据到数据库
     *
     * @param list
     */
    protected abstract void flushInsert(Collection<T> list);

    /**
     * 刷新更新数据到数据库
     *
     * @param list
     */
    protected abstract void flushUpdate(Collection<T> list);

    /**
     * 刷新删除数据到数据库
     *
     * @param list
     */
    protected abstract void flushDelete(Collection<T> list);

    /**
     * 单个数据插入
     *
     * @param data 数据对象
     */
    protected abstract void singleInsert(T data);

    /**
     * 单个数据更新
     *
     * @param data 数据对象
     */
    protected abstract void singleUpdate(T data);

    /**
     * 单个数据删除
     *
     * @param data 数据对象
     */
    protected abstract void singleDelete(T data);


    @Override
    public int getQueueId() {
        return queueId;
    }

    @Override
    public void run() {
        flush();
    }
}
