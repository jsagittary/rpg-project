package com.dykj.rpg.common.kafka.consts;

public interface KafkaCmdConsts {
	/**
	 * 监听服务器消息
	 */
	final short REGIST_SERVER_BROAST = 1000;
	/**
	 * 监听世界消息
	 */
	final short REGIST_SERVER_ALL_BROAST = 1002;
	// ************************************1050以上是注册消息的指令号********************************************//
	/**
	 * 玩家转发，广播(无逻辑)
	 */
	final short ONLINE_BROADCAST = 1050;
	/**
	 * 玩家转发，广播(世界)
	 */
	final short WORLD_SERVER_BROADCAST = 1051;


	final short KAFKA_SYN_TEST = 1052;


	final short KAFKA_ASYNC_TEST = 1053;
}
