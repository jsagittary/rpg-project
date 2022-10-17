package com.dykj.rpg.client.handler;

import com.dykj.rpg.client.game.player.logic.ClientPlayer;
import com.dykj.rpg.client.msg.CmdMsg;
import com.dykj.rpg.net.core.ISession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: jyb
 * @Date: 2019/1/8 18:30
 * @Description:
 */
public abstract class AfterLoginHandler<T> implements IHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void handler(CmdMsg cmdMsg, ISession session) throws Exception {
        ClientPlayer player = (ClientPlayer) session.getSessionHolder();
        if (player == null) {
            logger.error("AfterLoginHandler  player{} is not exist {}",session.toString(),cmdMsg.toString());
            return;
        }
        doHandler(player,decode(cmdMsg),cmdMsg);
    }

    public abstract void doHandler(ClientPlayer player, T t, CmdMsg cmdMsg);

    public abstract  T decode (CmdMsg cmdMsg) throws Exception;

    @Override
    public short registerSendCmd() {
        return 0;
    }


}
