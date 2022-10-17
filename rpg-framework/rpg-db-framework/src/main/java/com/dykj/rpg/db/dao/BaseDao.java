package com.dykj.rpg.db.dao;

import com.dykj.rpg.db.consts.DBOperation;
import com.dykj.rpg.db.data.BaseModel;
import com.dykj.rpg.db.queue.DbQueueManager;
import com.dykj.rpg.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: jyb
 * @Date: 2020/9/24 10:42
 * @Description:
 */
public class BaseDao<T extends BaseModel> extends AbstractBaseDao<T> {
    private Logger logger = LoggerFactory.getLogger("db");


    @Override
    public boolean queueInsert(T data) {
        if(DbQueueManager.getInstance().isDbSaveServer()){
            return  super.queueInsert(data);
        }
        if (DbQueueManager.getInstance().isRemote()) {
            DbQueueManager.getInstance().sendRemoteQueue(getQueueId(), this.getClass().getName(), data.getClass().getName(), DBOperation.INSERT, JsonUtil.toJsonString(data),data.KeyNum());
            logger.info("{} remote queueInsert {} ",this.getClass().getName(),data.toString());
            return true;
        } else {
            return super.queueInsert(data);
        }
    }

    @Override
    public boolean queueUpdate(T data) {
        if(DbQueueManager.getInstance().isDbSaveServer()){
            return  super.queueUpdate(data);
        }
        if (DbQueueManager.getInstance().isRemote()) {
            DbQueueManager.getInstance().sendRemoteQueue(getQueueId(), this.getClass().getName(), data.getClass().getName(), DBOperation.UPDATE, JsonUtil.toJsonString(data),data.KeyNum());
            logger.info("{} remote queueUpdate {} ",this.getClass().getName(),data.toString());
            return true;
        }
        return super.queueUpdate(data);
    }

    @Override
    public boolean queueDelete(T data) {
        if(DbQueueManager.getInstance().isDbSaveServer()){
            return  super.queueDelete(data);
        }
        if (DbQueueManager.getInstance().isRemote()) {
            DbQueueManager.getInstance().sendRemoteQueue(getQueueId(), this.getClass().getName(), data.getClass().getName(), DBOperation.DELETE, JsonUtil.toJsonString(data),data.KeyNum());
            logger.info("{} remote queueDelete {} ",this.getClass().getName(),data.toString());
            return true;
        }
        return super.queueDelete(data);
    }
}
