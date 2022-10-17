package com.dykj.rpg.net.handler;

import com.dykj.rpg.net.msg.CmdMsg;
import com.dykj.rpg.net.thread.CmdThreadEnum;

/**
 * @Author: jyb
 * @Date: 2018/12/24 18:55
 * @Description:
 */
public abstract class C2SHandler implements IHandler<CmdMsg> {

    @Override
    public CmdThreadEnum getThread() {
        return CmdThreadEnum.MAIN;
    }
}
