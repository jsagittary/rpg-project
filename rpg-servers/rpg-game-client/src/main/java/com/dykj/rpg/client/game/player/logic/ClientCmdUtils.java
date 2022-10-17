package com.dykj.rpg.client.game.player.logic;

import com.dykj.rpg.client.config.SendMsgThreadManager;
import com.dykj.rpg.client.msg.CmdMsg;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jyb
 * @date 2019/7/25 17:58
 */
public class ClientCmdUtils {

    private static Logger logger = LoggerFactory.getLogger(ClientCmdUtils.class);

    public static final void sendCMD(ClientPlayer player, CmdMsg msg) {
        logger.info("s--------->c:cmd:" + "0x" + Integer.toHexString(msg.getCmd()));
        if (null != player && null != player.getSession()) {
            //player.getSession().write(msg);
            msg.setSessionId(player.getSession().getId());
            BeanFactory.getBean(SendMsgThreadManager.class).execute(msg);
        }
    }
}
