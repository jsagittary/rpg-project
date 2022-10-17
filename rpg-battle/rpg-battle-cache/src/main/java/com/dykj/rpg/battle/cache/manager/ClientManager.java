package com.dykj.rpg.battle.cache.manager;

import Ice.Current;
import Ice.Util;
import com.dykj.rpg.battle.ice.service.CallbackBattleServerPrx;
import com.dykj.rpg.battle.ice.service.CallbackGameServerPrx;
import com.dykj.rpg.battle.cache.constant.HandlerConstant;
import com.dykj.rpg.battle.cache.util.RingBuffer;
import com.dykj.rpg.battle.ice.service.CallbackGameServerPrxHelper;
import com.dykj.rpg.net.protocol.BitArray;
import com.dykj.rpg.net.protocol.ProtocolByteArrayManager;
import com.dykj.rpg.net.protocol.ProtocolPool;
import com.dykj.rpg.protocol.game2battle.BattleFinishPersonalResultResponse;
import com.dykj.rpg.protocol.game2battle.BattleFinishResultResponse;
import com.dykj.rpg.protocol.game2battle.EnterBattleSuccessResponse;
import com.dykj.rpg.protocol.game2battle.PlayerEnterBattleRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ClientManager {

    Logger logger = LoggerFactory.getLogger("ClientManager");

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

                while (waitSendGameServerDatas.size() != 0) {
                    CallBackData callBackData = waitSendGameServerDatas.poll();
                    try {
                        if(gameServerClients.get(callBackData.serverId) != null){
                            if (callBackData.handlerId == HandlerConstant.GAMESERVER_ENTER_CACHE_RESULT) {
                                System.out.println("send msg to game server, serverId = " + callBackData.serverId + " handlerId = " + HandlerConstant.GAMESERVER_ENTER_CACHE_RESULT);
                                gameServerClients.get(callBackData.serverId).enterToBattleCacheResult(callBackData.datas);
                            }
                            if (callBackData.handlerId == HandlerConstant.GAMESERVER_ENTER_BATTLE_SUCCESS) {
                                System.out.println("send msg to game server, serverId = " + callBackData.serverId + " handlerId = " + HandlerConstant.GAMESERVER_ENTER_BATTLE_SUCCESS);
                                gameServerClients.get(callBackData.serverId).enterToBattleServerSuccess(callBackData.datas);
                            }
                            if (callBackData.handlerId == HandlerConstant.GAMESERVER_BATTLE_FINISH_RESULT) {
                                System.out.println("send msg to game server, serverId = " + callBackData.serverId + " handlerId = " + HandlerConstant.GAMESERVER_BATTLE_FINISH_RESULT);
                                gameServerClients.get(callBackData.serverId).battleFinishResult(callBackData.datas);
                            }
                        }else{
                            logger.info("send msg to game server error, server[" + callBackData.serverId +"] maybe is closed !!!");
                        }
                    } catch (Exception e) {
                        gameServerClients.remove(callBackData.serverId);
                        logger.info("exception ! send msg to game server error, server[" + callBackData.serverId +"] maybe is closed !!!");
                        e.printStackTrace();
                    }
                }

                while (waitSendBattleServerDatas.size() != 0) {
                    CallBackData callBackData = waitSendBattleServerDatas.poll();
                    try {
                        if(battleServerClients.get(callBackData.serverId) != null){
                            if (callBackData.handlerId == HandlerConstant.BATTLESERVER_ENTER_BATTLE_REQUEST) {
                                System.out.println("send msg to battle server, serverId = " + callBackData.serverId + " handlerId = " + HandlerConstant.BATTLESERVER_ENTER_BATTLE_REQUEST);
                                if (battleServerClients.get(callBackData.serverId) != null) {
                                    battleServerClients.get(callBackData.serverId).enterToBattle(callBackData.datas);
                                }
                            }
                        }
                    }catch(Exception e){
                        battleServerClients.remove(callBackData.serverId);
                        logger.info("exception ! send msg to battle server error, server[" + callBackData.serverId +"] maybe is closed !!!");
                        e.printStackTrace();
                    }
                }

            }
        },100,100, TimeUnit.MILLISECONDS);
    }

    public static ClientManager getInstance(){
        if(instance == null){
            instance = new ClientManager();
        }
        return instance;
    }

    /**
     *
     * @param serverId
     * @param __current
     * @param cover
     */
    public void addGameServer(int serverId,Current __current,boolean cover){
        boolean needAdd = false;
        if(cover){
            needAdd = true;
        }else{
            if(gameServerClients.get(serverId) == null){
                needAdd = true;
            }

        }

        if(needAdd){
            CallbackGameServerPrx client = CallbackGameServerPrxHelper.uncheckedCast(__current.con.createProxy(Util.stringToIdentity("CallbackGameServer")));
            if(client != null){
                gameServerClients.put(serverId,client);

                System.out.println("addGameServer serverId = "+serverId);
            }
        }
    }

    public void addGameServer(int serverId,CallbackGameServerPrx client){

        if(client != null){
            gameServerClients.put(serverId,client);

            System.out.println("addGameServer serverId = "+serverId);
        }

    }

    private void buildPlayerAndGameServer(int playerId,int serverId){
        playerIdGameServerIdMap.put(playerId,serverId);
    }

    /**
     * 由游戏服传送过来的进入战斗请求
     * @param gameServerId
     * @param playerId
     * @param data
     */
    public void enterToBattleCacheFromGameServer(int gameServerId,int playerId,byte[] data){
        buildPlayerAndGameServer(playerId,gameServerId);

        PlayerEnterBattleRequest request = (PlayerEnterBattleRequest) ProtocolPool.getInstance().borrowProtocol(PlayerEnterBattleRequest.code);
        BitArray bitArray = ProtocolByteArrayManager.getInstance().getProtocolBitArray();
        bitArray.initBytes(data,data.length);
        request.decode(bitArray);
        bitArray.release();
        TableManager.getInstance().addEnterBattleRequest(request);
        //waitSendBattleServerDatas.add(new CallBackData(playerId,battleServerId,HandlerConstant.BATTLESERVER_ENTER_BATTLE_REQUEST,data));
    }

    public void sendTableInfoToBattleServer(byte[] data){
        int battleServerId = getCanUseBattleServer();
        if(battleServerId != 0){
            waitSendBattleServerDatas.add(new CallBackData(0,battleServerId,HandlerConstant.BATTLESERVER_ENTER_BATTLE_REQUEST,data));
        }else{
            logger.error("has no can use battle server !!!");
        }
    }

    /**
     * 接收战斗服传送过来的战斗启动信息,由缓存服将其转发给游戏服
     * @param playerIds
     * @param data
     */
    public void enterToBattleServerSuccess(int[] playerIds, byte[] data){
        Map<Integer,List<Integer>> serverPlayerList = new HashMap<Integer,List<Integer>>();
        for(int playerId : playerIds){
            int serverId = playerIdGameServerIdMap.get(playerId);
            if(serverId != 0){
                List<Integer> ids = serverPlayerList.get(serverId);
                if(ids == null){
                    ids = new ArrayList<Integer>();
                    serverPlayerList.put(serverId,ids);
                }
                ids.add(playerId);
            }
        }

        for(int serverId : serverPlayerList.keySet()){
            EnterBattleSuccessResponse response = new EnterBattleSuccessResponse();
            BitArray bitArray = ProtocolByteArrayManager.getInstance().getProtocolBitArray();
            bitArray.initBytes(data,data.length);
            response.decode(bitArray);
            bitArray.release();
            response.setPlayerIds(serverPlayerList.get(serverId));

            BitArray bitArray2 = ProtocolByteArrayManager.getInstance().getProtocolBitArray();
            response.encode(bitArray2);
            System.out.println(response.toString());
            waitSendGameServerDatas.add(new CallBackData(0,serverId,HandlerConstant.GAMESERVER_ENTER_BATTLE_SUCCESS,bitArray2.getWriteByteArray()));
            bitArray2.release();
        }
    }

    public void sendBattleFinishResult(byte[] data){

        BattleFinishResultResponse finishResultResponse = new BattleFinishResultResponse();
        BitArray bitArray = ProtocolByteArrayManager.getInstance().getProtocolBitArray();
        bitArray.initBytes(data,data.length);
        finishResultResponse.decode(bitArray);
        bitArray.release();

        System.out.println(finishResultResponse.toString());
        int battleId = finishResultResponse.getBattleId();

        Map<Integer,List<BattleFinishPersonalResultResponse>> serverPlayerList = new HashMap<Integer,List<BattleFinishPersonalResultResponse>>();

        List<BattleFinishPersonalResultResponse> personalResultResponses = finishResultResponse.getResults();
        for(BattleFinishPersonalResultResponse resultResponse : personalResultResponses){
            int playerId = resultResponse.getPlayerId();
            if(playerIdGameServerIdMap.get(playerId) != null){
                int serverId = playerIdGameServerIdMap.get(playerId);
                List<BattleFinishPersonalResultResponse> responseList = serverPlayerList.get(serverId);
                if(responseList == null){
                    responseList = new ArrayList<BattleFinishPersonalResultResponse>();
                    serverPlayerList.put(serverId,responseList);
                }
                responseList.add(resultResponse);
            }
        }

        for(int serverId : serverPlayerList.keySet()){
            BattleFinishResultResponse newFinishResultResponse = new BattleFinishResultResponse();
            newFinishResultResponse.setBattleId(battleId);
            newFinishResultResponse.setResults(serverPlayerList.get(serverId));

            System.out.println(newFinishResultResponse.toString());

            BitArray bitArray2 = ProtocolByteArrayManager.getInstance().getProtocolBitArray();
            newFinishResultResponse.encode(bitArray2);
            waitSendGameServerDatas.add(new CallBackData(0,serverId,HandlerConstant.GAMESERVER_BATTLE_FINISH_RESULT,bitArray2.getWriteByteArray()));
            bitArray2.release();
        }
    }

    public void addBattleServer(int serverId,CallbackBattleServerPrx client){
        battleServerClients.put(serverId,client);
        System.out.println("addBattleServer serverId = "+serverId);
    }

    /**
     * 获取压力最小的战斗服
     */
    public int getCanUseBattleServer(){
        int battleServerId = 0;
        int count = 0;
        Set<Integer> set = battleServerClients.keySet();
        for(int serverId : set){
            try{
                int battleCount = battleServerClients.get(serverId).getRunningBattleCount();
                if(battleServerId == 0 || count > battleCount){
                    battleServerId = serverId;
                }
            }catch (Exception e){
                battleServerClients.remove(serverId);
                e.printStackTrace();
            }
        }
        return battleServerId;
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
