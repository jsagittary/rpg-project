package com.dykj.rpg.client.consts;

import com.dykj.rpg.client.ClientSession;
import com.dykj.rpg.client.config.Server;
import com.dykj.rpg.net.core.ISession;
import io.netty.util.AttributeKey;

/**
 * @Author: jyb
 * @Date: 2018/12/22 15:37
 * @Description:
 */
public class ChannelAttrKey {
    /**
     * session 集合
     */
    public static final AttributeKey<ClientSession> SESSIONS = AttributeKey.valueOf("SESSION");
    /**
     * 账号
     */
    public static final AttributeKey<String> ACCOUNTS = AttributeKey.valueOf("ACCOUNT");
    /**
     * server集合
     */
    public static final AttributeKey<Server> SERVERS = AttributeKey.valueOf("SERVER");

    public static int operation = 0;

}
