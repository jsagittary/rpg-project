package com.dykj.rpg.game.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author jyb
 * @date 2020/11/30 17:07
 * @Description
 */
@Service
public class KafkaConsumerListenerService {
    /**
     * LOG.
     */
    private  final Logger LOG = LoggerFactory.getLogger("game");

    /**
     * registry.
     */
    @Resource
    private KafkaListenerEndpointRegistry registry;
    /**
     * 开启监听.
     *
     * @param listenerId 监听ID
     */
    public void startListener(String listenerId) {
        //判断监听容器是否启动，未启动则将其启动
        if (!registry.getListenerContainer(listenerId).isRunning()) {
            registry.getListenerContainer(listenerId).start();
        }
        //项目启动的时候监听容器是未启动状态，而resume是恢复的意思不是启动的意思
        registry.getListenerContainer(listenerId).resume();
        LOG.info(listenerId + "开启监听成功。");
    }

    /**
     * 停止监听.
     *
     * @param listenerId 监听ID
     */
    public void stopListener(String listenerId) {
        if(registry.getListenerContainer(listenerId)!=null){
            registry.getListenerContainer(listenerId).stop();
            LOG.info(listenerId + "停止监听成功。");
        }
    }

}