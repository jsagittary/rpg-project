package com.dykj.rpg.battle.cache.ice.server;

import com.dykj.rpg.battle.ice.service.CallbackBattleServerPrx;
import com.dykj.rpg.battle.ice.service.CallbackGameServerPrx;
import com.dykj.rpg.battle.cache.constant.HandlerConstant;
import com.dykj.rpg.battle.cache.util.RingBuffer;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ClientManager {

    private static ClientManager instance;

    Map<Integer, CallbackGameServerPrx> gameServerClients;

    Map<Integer,Integer> playerIdGameServerIdMap;

    RingBuffer<CallBackData> waitSendGameServerDatas;

    Map<Integer, CallbackBattleServerPrx> battleServerClients;

    Map<Integer,Integer> battleServerIdRunningBattleCountMap;

    RingBuffer<CallBackData> waitSendBattleServerDatas;

    private ScheduledThreadPoolExecutor scheduled = new ScheduledThreadPoolExecutor(1);

    private ClientManager(){
        gameServerClients = new ConcurrentHashMap<>(1000);
        playerIdGameServerIdMap = new ConcurrentHashMap<>(100000);
        waitSendGameServerDatas = new RingBuffer<>(100000);

        battleServerClients = new ConcurrentHashMap<>(1000);
        battleServerIdRunningBattleCountMap = new ConcurrentHashMap<>(100000);
        waitSendBattleServerDatas = new RingBuffer<>(100000);

        scheduled.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                while(waitSendGameServerDatas.size()!=0){
                    CallBackData callBackData = waitSendGameServerDatas.poll();
                    if(callBackData.handlerId == HandlerConstant.GAMESERVER_ENTER_CACHE_RESULT){
                        gameServerClients.get(callBackData.serverId).enterToBattleCacheResult(callBackData.datas);
                    }
                    if(callBackData.handlerId == HandlerConstant.GAMESERVER_ENTER_BATTLE_SUCCESS){
                        gameServerClients.get(callBackData.serverId).enterToBattleServerSuccess(callBackData.datas);
                    }
                    if(callBackData.handlerId == HandlerConstant.GAMESERVER_ENTER_CACHE_RESULT){
                        gameServerClients.get(callBackData.serverId).battleFinishResult(callBackData.datas);
                    }
                }
                while(waitSendBattleServerDatas.size()!=0){
                    CallBackData callBackData = waitSendBattleServerDatas.poll();
                    if(callBackData.handlerId == HandlerConstant.BATTLESERVER_ENTER_BATTLE_REQUEST){
                        battleServerClients.get(callBackData.serverId).enterToBattle(callBackData.datas);
                    }
                    if(callBackData.handlerId == HandlerConstant.BATTLESERVER_ENTER_BATTLE_REQUEST){
                        battleServerClients.get(callBackData.serverId).enterToBattle(callBackData.datas);
                    }
                }
            }
        },1000,1000, TimeUnit.MILLISECONDS);
    }

    public static ClientManager getInstance(){
        if(instance == null){
            instance = new ClientManager();
        }
        return instance;
    }

    public void addGameServer(int serverId,CallbackGameServerPrx client){
        gameServerClients.put(serverId,client);
        System.out.println("addGameServer serverId = "+serverId);
    }

    private void buildPlayerAndGameServer(int playerId,int serverId){
        playerIdGameServerIdMap.put(playerId,serverId);
    }

    /**
     * 由游戏服传送过来的进入战斗请求
     * @param gameServerId
     * @param battleServerId
     * @param playerId
     * @param data
     */
    public void enterToBattleCache(int gameServerId,int battleServerId,int playerId,byte[] data){
        buildPlayerAndGameServer(playerId,gameServerId);
        waitSendBattleServerDatas.add(new CallBackData(playerId,battleServerId,HandlerConstant.BATTLESERVER_ENTER_BATTLE_REQUEST,data));
    }

    /**
     * 接收战斗服传送过来的战斗启动信息,由缓存服将其转发给游戏服
     * @param playerIds
     * @param data
     */
    public void enterToBattleServerSuccess(int[] playerIds, byte[] data){
        for(int playerId : playerIds){
            int serverId = playerIdGameServerIdMap.get(playerId);
            if(serverId != 0){
                waitSendGameServerDatas.add(new CallBackData(playerId,serverId,HandlerConstant.GAMESERVER_ENTER_BATTLE_SUCCESS,data));
            }
        }
    }

    public void addBattleServer(int serverId,CallbackBattleServerPrx client){
        battleServerClients.put(serverId,client);
    }

    /**
     * 获取压力最小的战斗服
     */
    public void getCanUseBattleServer(){
        int battleServerId = 0;
        int count = 0;
        Set<Integer> set = battleServerClients.keySet();
        for(int serverId : set){
            int battleCount = battleServerClients.get(serverId).getRunningBattleCount();
            if(battleServerId == 0 || count > battleCount){
                battleServerId = serverId;

            }
        }
    }

    class CallBackData{
        private int playerId;
        private int serverId;
        private int handlerId;
        private byte[] datas;

        public CallBackData(int playerId, int serverId, int handlerId, byte[] datas) {
            this.playerId = playerId;
            this.serverId = serverId;
            this.handlerId = handlerId;
            this.datas = datas;
        }
    }
}
