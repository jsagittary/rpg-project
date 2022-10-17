package com.dykj.rpg.net.kafka;


import com.dykj.rpg.net.core.ISyncDataTask;
import com.dykj.rpg.net.thread.ThreadPool;
import com.dykj.rpg.util.random.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaSyncDataTask implements ISyncDataTask {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 消息体
     */
    private KafkaMsg kafkaMsg;


    public KafkaSyncDataTask(KafkaMsg kafkaMsg) {
        this.kafkaMsg = kafkaMsg;

    }

    @Override
    public int getTaskId() {
        if (kafkaMsg.getThreadKey() != 0) {
            return kafkaMsg.getThreadKey();
        }
        if (kafkaMsg.getPlayerIds() != null && kafkaMsg.getPlayerIds().size() > 0) {
            return kafkaMsg.getPlayerIds().get(0);
        }
        return RandomUtil.getRandomNumber(ThreadPool.getMaxLogicThread());
    }

    @Override
    public void run() {
        IListener listener = null;
        try {
            listener = KafkaListenerManager.getDefault().getIListener(kafkaMsg.getKafkaCmd());
            if (listener == null) {
                logger.error("Listener listener is null  kafkaCmd =" + kafkaMsg.getKafkaCmd());
                return;
            }
            listener.doListen(kafkaMsg);
        } catch (Exception e) {
        	e.printStackTrace();
            logger.error("KafkaSyncDataTask error : listener {}  kafkaMsg {}", listener.getClass().getSimpleName(), kafkaMsg.toString());
        }
    }
}
