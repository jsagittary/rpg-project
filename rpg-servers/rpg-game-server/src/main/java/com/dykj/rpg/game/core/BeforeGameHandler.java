package com.dykj.rpg.game.core;

import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.module.util.CmdUtil;
import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.net.handler.core.AbstractClientHandler;
import com.dykj.rpg.net.protocol.Protocol;
import com.dykj.rpg.net.thread.CmdThreadEnum;

/**
 * @Author: jyb
 * @Date: 2020/10/12 11:23
 * @Description:
 */
public abstract class BeforeGameHandler<T extends Protocol> extends AbstractClientHandler<T> {



    public void sendError(ISession session, ErrorCodeEnum errorCode) {
        CmdUtil.sendErrorMsg(session,getCode(),errorCode);
    }

    public void sendError(ISession session,ErrorCodeEnum errorCode, String... prams) {
        CmdUtil.sendErrorMsg(session,getCode(),errorCode,prams);
    }

    @Override
    public CmdThreadEnum getThread() {
        return CmdThreadEnum.LOGIN;
    }
}
