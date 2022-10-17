package com.dykj.rpg.game.module.util;

import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.player.service.PlayerCacheService;
import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.net.protocol.Protocol;
import com.dykj.rpg.protocol.common.ErrorMsg;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

public class CmdUtil {

    private static Logger logger = LoggerFactory.getLogger(CmdUtil.class);

    /**
     * 像某个session推送消息
     *
     * @param session
     * @param protocol
     */
    public static void sendMsg(ISession session, Protocol protocol) {
        session.write(protocol);
    }

    /**
     * 像某个session推送消息
     *
     * @param session
     */
    public static void sendMsg(ISession session, byte[] msg) {
        session.write(msg);
    }


    /**
     * 像某个人推送消息
     *
     * @param player
     * @param protocol
     */
    public static void sendMsg(Player player, Protocol protocol) {
        if (player.getSession() != null && player.getSession().isActive()) {
            sendMsg(player.getSession(), protocol);
        } else {
            String des = "NULL";
            if (player.getSession() != null) {
                des = player.getSession().toString();
            }
            logger.error("CmdUtil sendMsg error session is time out {} ", des);
        }
    }


    /**
     * 像某个人推送消息
     *
     * @param player
     */
    public static void sendMsg(Player player, byte[] bytes) {
        if (player.getSession() != null && player.getSession().isActive()) {
            sendMsg(player.getSession(), bytes);
        } else {
            String des = "NULL";
            if (player.getSession() != null) {
                des = player.getSession().toString();
            }
            logger.error("CmdUtil sendMsg error session is time out {} ", des);
        }
    }

    /**
     * 像世界推送消息
     *
     * @param protocol
     */
    public static void sendWorldMsg(Protocol protocol) {
        PlayerCacheService cacheService = BeanFactory.getBean(PlayerCacheService.class);
        Iterator<Map.Entry<Integer, Player>> iterator = cacheService.getPlayers().entrySet().iterator();
        if (iterator.hasNext()) {
            Map.Entry<Integer, Player> entry = iterator.next();
            sendMsg(entry.getValue(), protocol);
        }

    }

    public static void sendErrorMsg(ISession session, int handlerId, ErrorCodeEnum errorCode) {
        ErrorMsg errorMsg = new ErrorMsg(errorCode.getCode(), handlerId, null);
        sendMsg(session, errorMsg);
    }


    public static void sendErrorMsg(ISession session, int handlerId, ErrorCodeEnum errorCode, String... prams) {
        ErrorMsg errorMsg = new ErrorMsg(errorCode.getCode(), handlerId, Arrays.asList(prams));
        sendMsg(session, errorMsg);
    }
}
