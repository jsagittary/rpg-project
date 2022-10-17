package com.dykj.rpg.battle.balance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class BalanceWheelTimer {

    Logger logger = LoggerFactory.getLogger("BalanceWheelTimer");

    public static final byte WHEELTIMER_STATE_STOP = 0;
    public static final byte WHEELTIMER_STATE_RUNNING = 1;
    public static final byte WHEELTIMER_STATE_SYNCALL = 2;

    public GameDataCache gameDataCache;

    /**
     * 状态注册表
     */
    public Map<Integer, Byte>[] registerMap;

    private int roundNum = 0;

    private ScheduledThreadPoolExecutor scheduled ;

    private ReentrantLock reentrantLock;

    private int tickNum;

    private byte state;

    private int totalTickNum;

    /**
     *
     * @param tickDuration 一轮的时间，单位为毫秒
     * @param totalTickNum 一轮的总刻度数
     */
    public BalanceWheelTimer(GameDataCache gameDataCache,long tickDuration, int totalTickNum){

        this.gameDataCache = gameDataCache;

        this.totalTickNum = totalTickNum;

        registerMap = new Map[totalTickNum];
        for(int i=0;i<totalTickNum;i++){
            registerMap[i] = new HashMap<Integer, Byte>();
        }

        reentrantLock = new ReentrantLock();

        scheduled = new ScheduledThreadPoolExecutor(1);

        scheduled.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try{
                    if(state == WHEELTIMER_STATE_RUNNING){

                        int handlerTickNum = tickNum;

                        /**
                         * 为了避免不同线程对同一个注册表的并发读写，需要在读数据前将tickNum和roundNum做修改，改变写数据时的注册表指向
                         */
                        tickNum = (tickNum+1)%totalTickNum;

                        if((tickNum+1)%totalTickNum == 0){
                            roundNum += 1;
                        }

                        //战斗信息更新
                        Map<Integer, Byte> registers = registerMap[handlerTickNum];
                        logger.info("handlerTickNum = "+handlerTickNum);
                        for(int battleId : registers.keySet()) {

                            byte registerState = registers.get(battleId);

                            if((registerState & CacheManager.BALANCE_TYPE_DELETE) != 0){
                                sendRemoveBattleToSlave(battleId);
                                registerMap[handlerTickNum].remove(battleId);
                            }else if((registerState & CacheManager.BALANCE_TYPE_ADD) != 0){
                                registerMap[handlerTickNum].put(battleId,(byte)(registers.get(battleId) & CacheManager.BALANCE_TYPE_DELETE));
                                sendFullBattleToSlave(battleId);
                            }else{
                                sendUpdateBattleToSlave(battleId);
                                askBattleSlaveModelState(battleId);

                                CacheManager.getInstance().getBalanceClient().getBattleCache(battleId);

                                //if(GameDataCache.getInstance().getBattleMap().get(battleId).getPlayers().size() > 0){
                                //    GameDataCache.getInstance().getBattleMap().get(battleId).getPlayers().remove(0);
                                //}

                            }
                        }

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },tickDuration/totalTickNum,tickDuration/totalTickNum, TimeUnit.MILLISECONDS);

        state = WHEELTIMER_STATE_STOP;

    }

    public void addRegister(int battleId,byte balanceType){
//        if(state != WHEELTIMER_STATE_STOP){
            int tick = Math.abs((battleId+"").hashCode()%this.totalTickNum);

//            if(tickNum == tick){
//                reentrantLock.lock();
//            }

            byte registerState = 0;
            if(registerMap[tick].containsKey(battleId)){
                registerState = registerMap[tick].get(battleId);
            }
            registerState |= balanceType;

            registerMap[tick].put(battleId,registerState);

//            if(reentrantLock.isLocked()){
//                reentrantLock.unlock();
//            }
//        }
    }

    public void release(){
        this.state = WHEELTIMER_STATE_STOP;
        scheduled.shutdown();
    }

    public void syncAll(){
        this.state = WHEELTIMER_STATE_SYNCALL;
        logger.info("---------------------begin syncAll------------------");
//        Map<Integer, Battle> map = gameDataCache.getBattleMap();
//        for(int battleId : map.keySet()){
//            logger.info("sync battle ! battleId = "+battleId);
//            sendFullBattleToSlave(battleId);
//        }

        this.state = WHEELTIMER_STATE_RUNNING;
    }

    private void sendFullBattleToSlave(int battleId){
        //ice同步单场战斗的所有数据
//        byte[] bytes = gameDataCache.encodeBattle(battleId);
//
//        Battle battle = new Battle();
//        battle.decode(bytes);
//
//        if(bytes != null){
//
//            CacheManager.getInstance().getBalanceClient().updateBattleSlaveCache(battleId,bytes);
//
//        }else{
//            //gameDataCache.removeBattle(battleId);
//        }
    }

    private void sendUpdateBattleToSlave(int battleId){
//        //ice同步单场战斗的所有数据
//        byte[] bytes = gameDataCache.encodeUpdateModel(battleId);
//
//        if(bytes != null){
//            logger.info("sendUpdateBattleToSlave bytes.length = " + bytes.length);
//            CacheManager.getInstance().getBalanceClient().updateBattleSlaveCache(battleId,bytes);
//
//        }
    }

    private void askBattleSlaveModelState(int battleId){
//        byte[] bytes = gameDataCache.askAllModelState(battleId);
//        if(bytes != null){
//
//            CacheManager.getInstance().getBalanceClient().askBattleSlaveModelState(battleId,bytes);
//
//        }else{
//            //gameDataCache.removeBattle(battleId);
//        }
    }

    private void sendRemoveBattleToSlave(int battleId){

        //BalanceManager.getInstance().getSlaveClient().updateBattleSlaveCache(battleId,new byte[0]);

        CacheManager.getInstance().getBalanceClient().removeBattleSlaveCache(battleId);
    }

}
