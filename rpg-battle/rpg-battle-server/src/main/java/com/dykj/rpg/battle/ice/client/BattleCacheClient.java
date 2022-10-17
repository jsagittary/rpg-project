package com.dykj.rpg.battle.ice.client;

import Ice.*;
import com.dykj.rpg.battle.constant.GameConstant;
import com.dykj.rpg.battle.ice.server.CallbackBattleServerImpl;
import com.dykj.rpg.battle.ice.service.*;
import com.dykj.rpg.battle.util.RingBuffer;
import com.dykj.rpg.net.protocol.BitArray;
import com.dykj.rpg.net.protocol.ProtocolByteArrayManager;
import com.dykj.rpg.net.protocol.ProtocolPool;
import com.dykj.rpg.protocol.game2battle.BattleFinishResultResponse;
import com.dykj.rpg.protocol.game2battle.EnterBattleSuccessResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.Exception;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BattleCacheClient {

    Logger logger = LoggerFactory.getLogger("BattleCacheClient");

    private static BattleCacheClient instance;

    private Communicator communicator;

    private ObjectPrx proxy;

    private Battle2BattleCacheServicePrx servicePrx;

    private ScheduledThreadPoolExecutor scheduled = new ScheduledThreadPoolExecutor(1);

    private RingBuffer<BattleFinishResultResponse> resultBuffer = new RingBuffer<BattleFinishResultResponse>(1000,BattleFinishResultResponse.class);

    private int serverId = 0;

    /**
     * 连接状态 0=未连接， 1=正在连接， 2=连接成功
     */
    private byte state = 0;

    /**
     * 1秒进行一次ping操作，以确保服务器状态
     */
    private int pingInterval = 1000;

    private BattleCacheClient(){
        startClient();
    }

    public static BattleCacheClient getInstance(){
        if(instance == null){
            instance = new BattleCacheClient();
        }
        return instance;
    }

    private String[] args ;

    private void startClient(){
        try{
            args = new String[]{
                    "--Ice.Config=iceclient.properties"
            };

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void start(int serverId){
        this.serverId = serverId;

        scheduled.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                pingBattleCache();
            }
        },0,pingInterval,TimeUnit.MILLISECONDS);
    }

    private void connectBattleCache(){
        try{
            state = 1;
            communicator = Util.initialize(args);
            proxy = communicator.stringToProxy("Battle2BattleCacheService@BattleCacheAdapter");
            ConnectionI conn = (ConnectionI)proxy.ice_getConnection();
            if(conn != null){
                servicePrx = Battle2BattleCacheServicePrxHelper.checkedCast(proxy);

                System.out.println("client "+conn.getEndpoint());

                //添加双工通讯
                ObjectAdapter adapter = communicator.createObjectAdapter("");
                CallbackBattleServerImpl callback = new CallbackBattleServerImpl();
                adapter.add(callback, Util.stringToIdentity("CallbackBattleServer"));

                conn.setAdapter(adapter);
                adapter.activate();

                conn.setCallback(new ConnectionCallback() {
                    @Override
                    public void heartbeat(Connection connection) {

                    }

                    @Override
                    public void closed(Connection connection) {
                        System.out.println("battle cache close !!!");
                        state = 0;
                    }
                });

                state = 2;
                //通知连接状态给CacheManager
                //CacheManager.getInstance().cacheConnectedNotify();
            }
        }catch (Exception e){
            //e.printStackTrace();
            System.out.println("battle cache server maybe not start !!!");
            state = 0;
        }

    }

    public void pingBattleCache(){

        if(state == 0){
            connectBattleCache();
        }

        if(state == 2){
            try{
                boolean result = servicePrx.pingBattleCache(serverId);
                //logger.info("pingBattleCache serverId = "+serverId+"  result = "+result);
                if(result){
                    if(resultBuffer.size() > 0){
                        while(resultBuffer.size() > 0){
                            BattleFinishResultResponse response = resultBuffer.poll();
                            try{
                                BitArray bitArray = ProtocolByteArrayManager.getInstance().getProtocolBitArray();
                                response.encode(bitArray);
                                servicePrx.battleFinishResult(bitArray.getWriteByteArray());
                                bitArray.release();
                            }catch (Exception e){
                                logger.error("send BattleFinishResultResponse to cache fail !!!");
                            }
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                servicePrx = null;
                connectBattleCache();
            }

        }
    }

    public void enterToBattleServerSuccess(int battleId,byte battleType,int missionId,int[] playerIds){

        if(state == 0){
            connectBattleCache();
        }

        if(state == 2){
            try{

                EnterBattleSuccessResponse response = (EnterBattleSuccessResponse) ProtocolPool.getInstance().borrowProtocol(EnterBattleSuccessResponse.code);
                response.setAddr(GameConstant.NETTY_SERVER_IP);
                response.setPort(GameConstant.NETTY_SERVER_PORT);
                response.setBattleId(battleId);
                response.setBattleType(battleType);
                response.setMissionId(missionId);

                logger.info(response.toString());

                BitArray bitArray = ProtocolByteArrayManager.getInstance().getProtocolBitArray();
                response.encode(bitArray);

                boolean result = servicePrx.enterToBattleServerSuccess(playerIds,bitArray.getWriteByteArray());

                bitArray.release();
                ProtocolPool.getInstance().restoreProtocol(response);

                logger.info("enterToBattleServerSuccess battle battleId = "+battleId+"  result = "+result);
            }catch (Exception e){
                e.printStackTrace();
                servicePrx = null;
                connectBattleCache();
            }

        }
    }

    public void battleFinishResult(int battleId,BattleFinishResultResponse response){
        resultBuffer.add(response);
//        if(state == 0){
//            connectBattleCache();
//        }
//
//        if(state == 2){
//            try{
//                boolean result = servicePrx.battleFinishResult(response.encode());
//                logger.info("battleFinishResult battleId = "+battleId+"  result = "+result);
//            }catch (Exception e){
//                e.printStackTrace();
//                servicePrx = null;
//                connectBattleCache();
//            }
//
//        }
    }

}
