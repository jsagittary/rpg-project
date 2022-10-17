package com.dykj.rpg.net.core;

import com.dykj.rpg.net.kcp.RingBuffer;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class UdpSessionWheelTimer {

    public static final byte WHEELTIMER_STATE_STOP = 0;
    public static final byte WHEELTIMER_STATE_RUNNING = 1;

    private ArrayList<UdpSession>[] udpSessions;

    private RingBuffer<UdpSession>[] addSessions;

    private RingBuffer<UdpSession>[] removeSessions;

    private ArrayList<UdpSession> waitVerifySessionList;

    private ScheduledThreadPoolExecutor executor;

    private ReentrantLock reentrantLock;

    private int tickNum;

    private byte state;

    private int totalTickNum;

    /**
     *
     * @param tickDuration 一轮的时间，单位为毫秒
     * @param totalTickNum 一轮的总刻度数
     */
    public UdpSessionWheelTimer(long tickDuration, int totalTickNum ){

        this.totalTickNum = totalTickNum;

        udpSessions = new ArrayList[totalTickNum];
        for(int i=0;i<totalTickNum;i++){
            udpSessions[i] = new ArrayList<UdpSession>();
        }

        addSessions = new RingBuffer[totalTickNum];
        for(int i=0;i<totalTickNum;i++){
            addSessions[i] = new RingBuffer<UdpSession>(1000);
        }

        removeSessions = new RingBuffer[totalTickNum];
        for(int i=0;i<totalTickNum;i++){
            removeSessions[i] = new RingBuffer<UdpSession>(1000);
        }

        waitVerifySessionList = new ArrayList<UdpSession>();

        reentrantLock = new ReentrantLock();

        executor = new ScheduledThreadPoolExecutor(3);

        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try{
                    if(state == WHEELTIMER_STATE_RUNNING){
                        //waitVerifySessionList.clear();

                        int curTickNum = tickNum;
                        tickNum = (tickNum + 1) % totalTickNum;

                        while(removeSessions[curTickNum].size() > 0){
                            UdpSession udpSession = removeSessions[curTickNum].poll();
                            if(udpSessions[curTickNum].contains(udpSession)){
                                udpSessions[curTickNum].remove(udpSession);
                            }
                        }

                        while(addSessions[curTickNum].size() > 0){
                            UdpSession udpSession = addSessions[curTickNum].poll();
                            udpSessions[curTickNum].add(udpSession);
                        }

                        for(UdpSession session:udpSessions[curTickNum]) {

                            if (session.kcpHandler != null) {
                                //if (session.kcpHandler.getState() == -1) {//有消息发送多次失败，需要和对应的游戏服进行验证玩家在线情况
                                //    waitVerifySessionList.add(session);
                                //}
                                if (session.kcpHandler.isActive()) {
                                    session.kcpHandler.handler();
                                }
                            }

                        }

//                        for(UdpSession session : waitVerifySessionList){
//                            try{
//                                if(session.gameServerPrx != null){
//                                    int result = session.gameServerPrx.verifyClientState(session.sessionId);
//                                    //如果判断客户端没有掉线，清空所有消息，重新开始
//                                    if(result == 1){
//                                        session.resetKcp();
//                                    }else{
//                                        UdpSessionManager.getInstance().fireSession(session);
//                                    }
//                                }else{
//                                    //释放掉线的玩家信息
//                                    //UdpSessionManager.getInstance().fireSession(udpSession);
//                                }
//                            }catch (Exception e){
//                                UdpSessionManager.getInstance().fireSession(session);
//                                e.printStackTrace();
//                            }
//                        }
//
//                        waitVerifySessionList.clear();



                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },0,tickDuration/totalTickNum, TimeUnit.MILLISECONDS);

        state = WHEELTIMER_STATE_RUNNING;

    }

    public void addUdpSession(UdpSession udpSession){
        int tick = Math.abs((udpSession.sessionId+"").hashCode()%this.totalTickNum);
        addSessions[tick].add(udpSession);

    }

    public void removeUdpSession(UdpSession udpSession){
        int tick = Math.abs((udpSession.sessionId+"").hashCode()%this.totalTickNum);
        removeSessions[tick].add(udpSession);
    }

    public void release(){
        this.state = WHEELTIMER_STATE_STOP;
        executor.shutdown();
    }
}
