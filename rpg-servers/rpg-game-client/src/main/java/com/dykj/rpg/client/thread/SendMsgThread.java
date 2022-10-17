package com.dykj.rpg.client.thread;

import com.dykj.rpg.client.msg.CmdMsg;
import com.dykj.rpg.net.core.ISession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Author: jyb
 * @Date: 2019/1/8 11:48
 * @Description:
 */
public class SendMsgThread extends AbstractSendMsgThread<CmdMsg, ISession> {

    private Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 将指令消息放到队列排队处理
     *
     * @param msg
     * @throws Exception
     */
    protected final BlockingQueue<CmdMsg> queue = new LinkedBlockingQueue<>();

    @Override
    public void execute(CmdMsg msg) {
        queue.add(msg);
    }

    @Override
    public void run() {
        while (true) {
            CmdMsg msg = null;
            try {
                msg = queue.take();
                //logger.info("msg {}", msg.toString());
                if (msg.getSessionId() != null) {
                    ISession session = sessions.get(msg.getSessionId());
                    if (session != null) {
                        session.write(msg.getProtocol());
                    }
                } else {
                    Iterator<Map.Entry<Number, ISession>> it = sessions.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<Number, ISession> entry = it.next();
                        entry.getValue().write(msg.getProtocol());
                    }
                }
            } catch (Exception e) {
                logger.error("SendMsgThread error {}", msg.toString());
            }
        }
    }

    /**
     * 下线
     */
    public void close(){
        Iterator<Map.Entry<Number,ISession>> it = sessions.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<Number,ISession> entry = it.next();
            entry.getValue().sessionClosed();
            it.remove();
        }

    }
}
