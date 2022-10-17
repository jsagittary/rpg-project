package com.dykj.rpg.db.kafka;

import com.dykj.rpg.db.consts.DBOperation;
import com.dykj.rpg.db.consts.DBTopicConsts;
import com.dykj.rpg.db.dao.DBKafkaMsg;
import com.dykj.rpg.db.data.BaseModel;
import com.dykj.rpg.db.queue.IDbQueue;
import com.dykj.rpg.util.JsonUtil;
import com.dykj.rpg.util.spring.BeanFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Optional;

/**
 * @Author: jyb
 * @Date: 2020/9/23 20:34
 * @Description:
 */
public class Listener {
    @KafkaListener(topics = {DBTopicConsts.DB_QUEUE_TOPIC + "_" + "${serverId}"})
    public void listenServer(ConsumerRecord<?, ?> record) {
        listen(record);
    }

    private Logger logger = LoggerFactory.getLogger(getClass());
    private void listen(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            String context = message.toString();
            if (context != null) {
                DBKafkaMsg msg = JsonUtil.toInstance(context, DBKafkaMsg.class);
                if (msg.getOperation() > 3) {
                    return;
                }
                Class queue = null;
                try {
                    queue = Class.forName(msg.getQueueName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    Class  model = Class.forName(msg.getModelName());
                    BaseModel baseModel = (BaseModel) JsonUtil.toInstance(msg.getData(), model);
                    IDbQueue dbQueue = (IDbQueue) BeanFactory.getBean(queue);
                    if (msg.getOperation() == DBOperation.INSERT) {
                        dbQueue.queueInsert(baseModel);
                    } else if (msg.getOperation() == DBOperation.UPDATE) {
                        dbQueue.queueUpdate(baseModel);
                    } else if (msg.getOperation() == DBOperation.DELETE) {
                        dbQueue.queueDelete(baseModel);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    logger.info("Listener {} ",context);
                }

            }
        }
    }
}
