package com.dykj.rpg.net.netty.consts;

import com.dykj.rpg.net.core.ISession;
import io.netty.util.AttributeKey;

/**
 * @Author: jyb
 * @Date: 2018/12/22 15:37
 * @Description:
 */
public class ChannelAttrKey {

    /** session 集合*/
    public static final AttributeKey<ISession> SESSIONS = AttributeKey.valueOf("SESSION");
}
