package com.dykj.rpg.game.kafka.core;

import com.dykj.rpg.common.kafka.consts.KafkaCmdConsts;
import com.dykj.rpg.common.consts.TopicConsts;
import com.dykj.rpg.net.kafka.KafkaListenerManager;
import com.dykj.rpg.net.kafka.KafkaMsg;
import com.dykj.rpg.util.JsonUtil;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: jianbo.gong
 * @version: 2018年6月8日 下午3:56:25
 * @describe:
 */
@Configuration
@EnableKafka
@Order(1)
public class KafkaProducerConfig {

	@Value("${kafka.host}")
	private String bootstrap_servers_config;

	@Value("${serverId}")
	private String serverId;

	public Map<String, Object> producerConfigs() {
		Map<String, Object> props = new HashMap<>();
		//kafka链接的服务器
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap_servers_config);
		//props.put(ProducerConfig.RETRIES_CONFIG, 0);

		//多少数据发送一次，默认是16k
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, 4096);

		//批量发送的等待时间(毫秒)
		props.put(ProducerConfig.LINGER_MS_CONFIG, 1);

		//客户端缓冲区的大小，默认是32m，满了也会发送消息
		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 40960);

		//key序列化的形式
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

		//value  序列化的形式
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

		// 0 发出去确认，1 leader确认， all（-1）所有follower同步完才确认
		props.put(ProducerConfig.ACKS_CONFIG,"1");

		//获取元数据时 生产者的阻塞时间，超时后抛出异常
		props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG,3000);
		return props;
	}

	@Bean
	public ProducerFactory<String, String> producerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}

	@PostConstruct
	public void initListener() {
		KafkaListenerManager.getDefault().setPackeagePath("com.dykj.rpg.game.kafka.listener");
		KafkaListenerManager.getDefault().initHandler();
	}

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<String, String>(producerFactory());
		//注册单服topic
 		KafkaMsg kafkaMsg = new KafkaMsg(KafkaCmdConsts.REGIST_SERVER_BROAST, JsonUtil.toJsonString(serverId));
		kafkaTemplate.send(TopicConsts.SERVER_BROADCAST + "_" + serverId, JsonUtil.toJsonString(kafkaMsg));
		//注册全服topic
		kafkaMsg = new KafkaMsg(KafkaCmdConsts.REGIST_SERVER_ALL_BROAST, JsonUtil.toJsonString(serverId));
		kafkaTemplate.send(TopicConsts.SERVER_ALL_TOPIC , JsonUtil.toJsonString(kafkaMsg));
		return kafkaTemplate;
	}
}
