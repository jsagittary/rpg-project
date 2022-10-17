package com.dykj.rpg.net.netty.consts;

/**
 * @Author: jyb
 * @Date: 2018/12/22 17:12
 * @Description:
 */
public class ProtocolConstants {
    /**加密标识*/
    public static final short ENCRYPTION= 0x5a5a;
    /**非加密标识*/
    public static final short NO_ENCRYPTION= 0x6a6a;

    /**包的标志位*/
    public static final int MSG_KEY= 1234567489;
}
