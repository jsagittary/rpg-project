package com.dykj.rpg.net.thread;

import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.net.handler.IHandler;
import com.dykj.rpg.net.msg.CmdMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 执行消息的任务
 */
public class MsgTask implements Runnable {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private IHandler handler;

    private ISession session;

    private CmdMsg cmdMsg;

    public MsgTask(IHandler handler, ISession session, CmdMsg cmdMsg) {
        this.cmdMsg = cmdMsg;
        this.session = session;
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            exceuteMsg();
        } catch (Throwable e) {
            e.printStackTrace();
            logger.error(handler.getClass() + " error :sessionId=" + session.getId() + "; cmd =" + cmdMsg.getCmd());
        }
    }


    /**
     * 消息执行
     *
     * @throws Throwable
     */
    public void exceuteMsg() throws Throwable {
        // logger.info("c--------->s:cmd:" + "0x" +
        // Integer.toHexString(message.getCommandId()) + ";hander=" +
        // handler.getClass());
        long time = System.currentTimeMillis();
        try {
            //logger.info("THREAD:"+Thread.currentThread().getName());
            handler.handler(cmdMsg, session);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            long p = System.currentTimeMillis() - time;
            if (p >= 100) {
                logger.error(handler.getClass() + ":work too long : time =" + p);
            }
        }

    }
}
