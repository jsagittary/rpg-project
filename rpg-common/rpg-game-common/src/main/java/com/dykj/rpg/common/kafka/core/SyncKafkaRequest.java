package com.dykj.rpg.common.kafka.core;


import cn.hutool.core.thread.ThreadUtil;
import com.dykj.rpg.common.kafka.service.KafkaSenderService;
import com.dykj.rpg.net.kafka.KafkaMsg;
import com.dykj.rpg.net.thread.MessageExecutor;
import com.dykj.rpg.net.thread.ThreadPool;
import com.dykj.rpg.util.spring.BeanFactory;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SyncKafkaRequest {

    private final Logger logger = LoggerFactory.getLogger(SyncKafkaRequest.class);
    private static final long DEFAULT_TIME_OUT = 10000l;
    public static Map<Integer, DefaultFuture<KafkaMsg>> reqMap = new ConcurrentHashMap<Integer, DefaultFuture<KafkaMsg>>();
    private ExecutorService executor = Executors.newCachedThreadPool();
    public static final SyncKafkaRequest instance = new SyncKafkaRequest();

    public static final AtomicInteger TASK_PRIMARY_KEY = new AtomicInteger(0);

    private SyncKafkaRequest() {
    }

    public void receive(KafkaMsg msg) {
        DefaultFuture<KafkaMsg> future = this.getFuture(msg.getSignKey());
        if (future != null) {
            future.onReceive(msg);
        } else {
            logger.warn("can't find reqId : {}", msg.getSignKey());
        }
    }

    /**
     * @param serverId
     * @param msg
     * @return
     */
    public KafkaMsg send(int serverId, KafkaMsg msg) {
        msg.setSignKey(TASK_PRIMARY_KEY.incrementAndGet());
        FutureTask<KafkaMsg> future = new FutureTask<KafkaMsg>(() -> send0(serverId, msg));
        MessageExecutor.exeSendMsgTask(future);
        try {
            return future.get();
        } catch (Exception e) {
            logger.error("{}", ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    private KafkaMsg send0(int serverId, KafkaMsg msg) {
        DefaultFuture<KafkaMsg> future = new DefaultFuture<KafkaMsg>(false);
        try {
            logger.info("thread {} ", Thread.currentThread().getName());
            BeanFactory.getBean(KafkaSenderService.class).sendMsg2ServerAsync$(serverId, msg);
            reqMap.put(msg.getSignKey(), future);
            KafkaMsg result = future.get(DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS);
            logger.info("=============login future.get result : {}, {}", msg.getCmd(), result);
            return result;
        } catch (Exception e) {
            logger.error("{}", ExceptionUtils.getStackTrace(e));
        } finally {
            this.remove(msg.getCmd());
        }
        return null;
    }

    private DefaultFuture<KafkaMsg> getFuture(int reqId) {
        return reqMap.get(reqId);
    }

    private void remove(int reqId) {
        reqMap.remove(reqId);
    }

}
