package com.dykj.rpg.client.config;

import com.dykj.rpg.client.msg.CmdMsg;
import com.dykj.rpg.client.consts.CmdThreadEnum;
import com.dykj.rpg.client.consts.InitOrder;
import com.dykj.rpg.client.thread.SendMsgThread;

import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.protocol.common.HeartBeatRq;
import io.netty.channel.Channel;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @Author: jyb
 * @Date: 2019/1/8 15:31
 * @Description:
 */
@Configuration
@Order(value = InitOrder.THREAD)
@Component
public class SendMsgThreadManager {

    private SendMsgThread[] sendMsgThreads;

    private final int THREAD_NUM = Runtime.getRuntime().availableProcessors()*2;

    private final int MAX_LOGIN_THREAD = Runtime.getRuntime().availableProcessors()*2;

    /** 定时任务线程 */
    private  ScheduledThreadPoolExecutor TIMING_THREAD;

    /**
     * 登录线程
     */
    private  ExecutorService LOGIN_THREAD_POOL;


    /** 重连任务线程 */
    private  ScheduledThreadPoolExecutor RECONNECT_THREAD;


    private Map<Channel,LoginMsgPram> loginMsgPramMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void run() throws Exception {
        sendMsgThreads = new SendMsgThread[THREAD_NUM];
        for (int i = 0; i < THREAD_NUM; i++) {
            SendMsgThread sendMsgThread = new SendMsgThread();
            new Thread(sendMsgThread, "SendMsgThread" + "_" + i).start();
            sendMsgThreads[i] = sendMsgThread;
        }
        TIMING_THREAD =new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
            private int count = 0;
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, CmdThreadEnum.TIMER_TASK.getCode() + "-" + (++count));
            }
        });

        RECONNECT_THREAD = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
            private int count = 0;
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, CmdThreadEnum.RECONNECT_THREAD.getCode() + "-" + (++count));
            }
        });

        //注册心跳
        TIMING_THREAD.scheduleWithFixedDelay(new HeartBeatTask(), 1, 20, TimeUnit.SECONDS);



        /** 登录线程 */
        LOGIN_THREAD_POOL = Executors.newFixedThreadPool(MAX_LOGIN_THREAD, new ThreadFactory() {
            private int count = 0;

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, CmdThreadEnum.LOGIN.getCode() + "-" + (++count));
            }
        });
    }

    public void execute(CmdMsg cmdMsg) {
        if (cmdMsg.getSessionId() != null) {
            sendMsgThreads[cmdMsg.getSessionId().intValue() % THREAD_NUM].execute(cmdMsg);
        } else {
            for (SendMsgThread s : sendMsgThreads) {
                s.execute(cmdMsg);
            }
        }
    }




    /**
     * 添加session
     *
     * @param session
     */
    public void addSession(ISession session) {
        sendMsgThreads[session.getId() % THREAD_NUM].addSession(session);
    }

    /**
     * 移除session
     *
     * @param session
     */
    public void removeSession(ISession session) {
        sendMsgThreads[session.getId() % THREAD_NUM].removeSession(session.getId());
    }

    public void closeConnect() {
        for (SendMsgThread sendMsgThread : sendMsgThreads) {
                sendMsgThread.close();
        }
    }


    class  HeartBeatTask implements  Runnable{
        @Override
        public void run() {
            CmdMsg cmdMsg  = new CmdMsg(new  HeartBeatRq());
            execute(cmdMsg);
        }
    }

    public ExecutorService getLoginThreadPoll() {
        return LOGIN_THREAD_POOL;
    }

    public ScheduledThreadPoolExecutor getReconnectThread() {
        return RECONNECT_THREAD;
    }

    public void setRECONNECT_THREAD(ScheduledThreadPoolExecutor RECONNECT_THREAD) {
        this.RECONNECT_THREAD = RECONNECT_THREAD;
    }

    public Map<Channel, LoginMsgPram> getLoginMsgPramMap() {
        return loginMsgPramMap;
    }
}
