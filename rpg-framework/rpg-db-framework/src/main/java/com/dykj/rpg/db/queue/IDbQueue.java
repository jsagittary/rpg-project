package com.dykj.rpg.db.queue;

/**
 * @Author: jyb
 * @Date: 2018/3/6 18:22
 * @Description:
 */
public interface IDbQueue<T> extends Runnable {
    /**
     * 从缓存同步到数据库
     */
    void flush();

    /**
     * 同步刷新数据库之后执行操作
     *
     * @param callback
     */
    void syncFlush(Callback callback);

    /**
     * 插入到缓存
     */
    boolean queueInsert( T data);
    /**
     * 更新到缓存
     */
    boolean queueUpdate(T data);

    /**
     * 删除到缓存
     */
    boolean queueDelete(T data);

    /**
     * getQueueId
     * @return
     */
    int  getQueueId();

}
