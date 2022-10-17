package com.dykj.rpg.db.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DBSaveTask<T> implements Runnable {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private AbstractDbQueue<T> dbQueue;
    private List<T> insertList;
    private List<T> updateList;
    private List<T> deleteList;
    private Callback callback;

    public DBSaveTask(AbstractDbQueue<T> dbQueue, List<T> insertList, List<T> updateList, List<T> deleteList) {
        this(dbQueue, insertList, updateList, deleteList, null);
    }

    public DBSaveTask(AbstractDbQueue<T> dbQueue, List<T> insertList,
                      List<T> updateList, List<T> deleteList, Callback callback) {
        super();
        this.dbQueue = dbQueue;
        this.insertList = insertList;
        this.updateList = updateList;
        this.deleteList = deleteList;
        this.callback = callback;
    }

    public void run() {
        dbQueue.doFlush(insertList, updateList, deleteList);
        logger.debug("DBSaveTask dbQueue {} insertList {}  updateList {} deleteList {} ", dbQueue.getClass().getSimpleName(), getSize(insertList), getSize(updateList), getSize(deleteList));
        if (callback != null) {
            callback.callback();
        }
    }

    public int getSize(List<T> info) {
        if (info == null) {
            return 0;
        }
        return info.size();
    }
}
