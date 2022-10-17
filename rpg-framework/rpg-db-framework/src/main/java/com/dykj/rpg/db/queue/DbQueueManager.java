package com.dykj.rpg.db.queue;

import com.dykj.rpg.db.consts.DBTopicConsts;
import com.dykj.rpg.db.dao.DBKafkaMsg;
import com.dykj.rpg.util.JsonUtil;
import com.dykj.rpg.util.spring.BeanFactory;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: jyb
 * @Date: 2018/3/7 15:43
 * @Description:
 */
public class DbQueueManager {
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;

    private static Logger logger = LoggerFactory.getLogger(DbQueueManager.class);

    private static DbQueueManager ourInstance = new DbQueueManager();

    public static DbQueueManager getInstance() {
        return ourInstance;
    }

    private ExecutorService[] es;

    private int initialDelay = 60;

    private int period = 300;

    private ScheduledExecutorService scheduledThreadPool;

    private List<Class<? extends AbstractDbQueue>> queueList = new ArrayList<Class<? extends AbstractDbQueue>>();

    private AtomicInteger QUEUE_SQL = new AtomicInteger(-1);
    /**
     * 远程入库
     */
    private boolean remote = false;

//    /**
//     * 1 单机模式
//     * 2 主从模式
//     */
//    private int serverModel = 1;
//    /**
//     * 远程通讯地址（暂时为dubbo地址）
//     */
//    private String address;
    /**
     * 当前服务器ID
     */
    private int serverId;
    /**
     * 是否是入库rpg-db-server
     * 如果是的话直接进行异步批量操作
     * 默认不是
     */
    private boolean dbSaveServer = false;

    private DbQueueManager() {

    }

    public void init() {
        es = new ExecutorService[THREAD_POOL_SIZE];
        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            es[i] = Executors
                    .newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("db_save_thread_" + i).build());
        }
        scheduledThreadPool = Executors.newScheduledThreadPool(1);
        registerDBQueueQuartz();
        if (remote && null == getTopic() || "".equals(getTopic())) {
            logger.error("DbQueueManager topic is not exist ");
            System.exit(-1);
        }
    }

    public ExecutorService getExecutor(int id) {
        ExecutorService service = es[id % THREAD_POOL_SIZE];
        if (!service.isShutdown()) {
            return es[id % THREAD_POOL_SIZE];
        } else {
            logger.error("ExecutorService is shutDown db_save_thread_index:" + id % THREAD_POOL_SIZE);
            es[id % THREAD_POOL_SIZE] = Executors.newSingleThreadExecutor(
                    new ThreadFactoryBuilder().setNameFormat("db_save_thread_" + id % THREAD_POOL_SIZE).build());
            return es[id % THREAD_POOL_SIZE];
        }
    }

    public synchronized void flush() {
        logger.info("[Start] -> flush all queue to database....");
        for (Class<?> queue : queueList) {
            if (queue == null)
                continue;
            try {
                IDbQueue<?> dbQueue = (IDbQueue) BeanFactory.getBean(queue);
                dbQueue.flush();
            } catch (Throwable ex) {
                logger.error("", ex);
            }
        }
        logger.info("[End] -> flush all queue to database....");
    }


    public int getQueueId() {
        return QUEUE_SQL.incrementAndGet();
    }


    public void registerDBQueueQuartz() {
        try {
            for (Class<?> queue : queueList) {
                if (queue == null)
                    continue;
                try {
                    IDbQueue<?> dbQueue = (IDbQueue) BeanFactory.getBean(queue);
                    int queueId = dbQueue.getQueueId();
                    logger.info("{} register success  queueId {} initialDelay {}  ", dbQueue.getClass().getSimpleName(), queueId, queueId * initialDelay);
                    scheduledThreadPool.scheduleAtFixedRate(dbQueue, queueId * initialDelay, period, TimeUnit.SECONDS);
                } catch (Throwable ex) {
                    logger.error("DbQueueManager registerDBQueueQuartz error", ex);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param cl
     */
    public void addQueue(Class<? extends AbstractDbQueue> cl) {
        queueList.add(cl);
    }

    public List<Class<? extends AbstractDbQueue>> getQueueList() {
        return queueList;
    }

    public int getInitialDelay() {
        return initialDelay;
    }

    public void setInitialDelay(int initialDelay) {
        this.initialDelay = initialDelay;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public boolean isRemote() {
        if (dbSaveServer == true) {
            return false;
        }
        return remote;
    }

    public void setRemote(boolean remote) {
        this.remote = remote;
    }

    public String getTopic() {
        return DBTopicConsts.DB_QUEUE_TOPIC + "_" + serverId;
    }

    public void sendRemoteQueue(int queueId, String queueName, String modelName, int operation, String data) {
        KafkaTemplate<String, String> kafkaTemplate = BeanFactory.getBean(KafkaTemplate.class);
        if (kafkaTemplate == null) {
            logger.error("***queue {}*********************kafkaTemplate is null************************************ ", queueName);
            logger.error("***queue {}*********************kafkaTemplate is null************************************ ", queueName);
            logger.error("***queue {}*********************kafkaTemplate is null************************************ ", queueName);
            return;
        }
        getExecutor(queueId).execute(() -> {
            try {
                DBKafkaMsg dbKafkaMsg = new DBKafkaMsg();
                dbKafkaMsg.setData(data);
                dbKafkaMsg.setModelName(modelName);
                dbKafkaMsg.setOperation(operation);
                dbKafkaMsg.setQueueName(queueName);
                kafkaTemplate.send(DbQueueManager.getInstance().getTopic(), JsonUtil.toJsonString(dbKafkaMsg));
                //logger.error("DbQueueManager error :queue {} flush remote error  data {}", queueName, data);
            } catch (Exception e) {
                logger.error("DbQueueManager error :queue {} flush remote error  data {}", queueName, data);
                logger.error("DbQueueManager error {} ", e);
            }
        });

    }

    public void sendRemoteQueue(int queueId, String queueName, String modelName, int operation, String data, int keyNum) {
        KafkaTemplate<String, String> kafkaTemplate = BeanFactory.getBean(KafkaTemplate.class);
        if (kafkaTemplate == null) {
            logger.error("***queue {}*********************kafkaTemplate is null************************************ ", queueName);
            logger.error("***queue {}*********************kafkaTemplate is null************************************ ", queueName);
            logger.error("***queue {}*********************kafkaTemplate is null************************************ ", queueName);
            return;
        }
        getExecutor(queueId).execute(() -> {
            try {
                DBKafkaMsg dbKafkaMsg = new DBKafkaMsg();
                dbKafkaMsg.setData(data);
                dbKafkaMsg.setModelName(modelName);
                dbKafkaMsg.setOperation(operation);
                dbKafkaMsg.setQueueName(queueName);
                dbKafkaMsg.setKeyNum(keyNum);
                kafkaTemplate.send(DbQueueManager.getInstance().getTopic(), JsonUtil.toJsonString(dbKafkaMsg));
               // logger.error("DbQueueManager error :queue {} flush remote error  data {} {} ", queueName, data,keyNum);
            } catch (Exception e) {
                logger.error("DbQueueManager error :queue {} flush remote error  data {}", queueName, data);
                logger.error("DbQueueManager error {} ", e);
            }
        });

    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

/*
    public int getServerModel() {
        return serverModel;
    }

    public void setServerModel(int serverModel) {
        this.serverModel = serverModel;
    }
*/

    /**
     * 是否需要远程同步cache
     * 1 首先需要是主从模式 serverModel ==2
     * 2 当前主机是master 正在通信的address 正好是本机的address address指的dubbo通讯的接口
     *
     * @param address
     * @return
     */
//    public boolean isRemoteCache(String address) {
//        if (serverModel == 2) {
//            return address.equals(this.address);
//        }
//        return false;
//    }

    public boolean isDbSaveServer() {
        return dbSaveServer;
    }

    public void setDbSaveServer(boolean dbSaveServer) {
        this.dbSaveServer = dbSaveServer;
    }
}
