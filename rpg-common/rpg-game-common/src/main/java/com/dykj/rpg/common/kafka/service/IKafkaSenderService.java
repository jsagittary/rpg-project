package com.dykj.rpg.common.kafka.service;

import java.util.Collection;

/**
 * @author jyb
 * @date 2019/2/11 17:56
 */
public interface IKafkaSenderService {

    /**
     * 转发到所有服的所有在线玩家（没有其他逻辑，就是转发）
     * @param cmd
     * @param bytes
     */
    void send2AllPlayers(short cmd, byte[] bytes);

    /**
     * 转发消息给 playerIds 玩家集合（没有其他逻辑，就是转发）
     * @param playerIds
     * @param bytes
     */
    void send2Players(Collection<Integer> playerIds, byte[] bytes);

    /**
     * 广播消息给 playerIds 玩家集合（传输 json 格式数据）
     * @param playerIds
     * @param kafkaCmd
     * @param object
     */
    void send2Players(Collection<Integer> playerIds, short kafkaCmd, Object object);

    /**
     * 广播消息给 playerIds 玩家集合（传输字节数组格式数据）
     * @param playerIds
     * @param kafkaCmd
     * @param bytes
     */
    void send2Players(Collection<Integer> playerIds, short kafkaCmd,  byte[] bytes);

    /**
     * 广播到所有服
     * @param kafkaCmd
     * @param object
     */
    void send2AllServers(short kafkaCmd, Object object);

    /**
     * 广播到所以服务器，byte数组
     * @param kafkaCmd
     * @param bytes
     */
    void send2AllServers(short kafkaCmd, byte[] bytes);
}
