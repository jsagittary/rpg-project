package com.dykj.rpg.game.ice.client;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import com.dykj.rpg.battle.ice.service.Game2BattleCacheServicePrx;
import com.dykj.rpg.battle.ice.service.Game2BattleCacheServicePrxHelper;
import com.dykj.rpg.game.ice.server.CallbackGameServerImpl;
import com.dykj.rpg.net.protocol.BitArray;
import com.dykj.rpg.net.protocol.ProtocolByteArrayManager;
import com.dykj.rpg.protocol.game2battle.PlayerEnterBattleRequest;

import Ice.Communicator;
import Ice.Connection;
import Ice.ConnectionCallback;
import Ice.ConnectionI;
import Ice.Identity;
import Ice.ObjectAdapter;
import Ice.ObjectPrx;
import Ice.Util;

public class BattleClient {

    private static BattleClient instance;

    private Communicator communicator;

    private ObjectPrx proxy;

    private Game2BattleCacheServicePrx servicePrx;

    private ScheduledThreadPoolExecutor scheduled = new ScheduledThreadPoolExecutor(1);

    private int serverId;

    /**
     * 连接状态 0=未连接， 1=正在连接， 2=连接成功
     */
    private byte state = 0;

    /**
     * 30秒进行一次ping操作，以确保服务器状态
     */
    private int pingInterval = 30000;

    private int reconnectTime = 0;

    private int maxReconnectTime = 6;

    public static BattleClient getInstance(){
        if(instance == null){
            instance = new BattleClient();
        }
        return instance;
    }

    private BattleClient(){
        createClient();
    }

    private String[] args;

    private void createClient(){
        args = new String[]{
                "--Ice.Config=iceclient.properties"
        };
    }

    public void start(int serverId){
        this.serverId = serverId;
        connectBattleCache();
    }

    private void connectBattleCache(){
        try{
            state = 1;

            communicator = Util.initialize(args);

            proxy = communicator.stringToProxy("Game2BattleCacheService@BattleCacheAdapter");

            ConnectionI conn = (ConnectionI)proxy.ice_getConnection();

            if(conn != null){
                servicePrx = Game2BattleCacheServicePrxHelper.checkedCast(proxy);

                System.out.println("client "+conn.getEndpoint());

                Identity identity = Util.stringToIdentity("CallbackGameServer");

                //添加双工通讯
                ObjectAdapter adapter = communicator.createObjectAdapter("");
                CallbackGameServerImpl callback = new CallbackGameServerImpl();

                adapter.add(callback, identity);

                adapter.activate();
                conn.setAdapter(adapter);

                conn.setCallback(new ConnectionCallback() {
                    @Override
                    public void heartbeat(Connection connection) {

                    }

                    @Override
                    public void closed(Connection connection) {
                        System.out.println("battle cache close !!!");
                        //proxy.ice_getConnection().close(true);
                        state = 0;
                    }
                });

                state = 2;
                reconnectTime = 0;

                servicePrx.pingBattleCache(serverId);

                //通知连接状态给CacheManager
                //CacheManager.getInstance().cacheConnectedNotify();
            }
        }catch (Exception e){
            state = 0;
        }

    }

    /**
     * 请求进入战斗服
     * @param request
     */
    public boolean enterToBattleCache (int serverId,int playerId,PlayerEnterBattleRequest request){
        try{
            if(state == 0){
                connectBattleCache();
            }
            if(state == 1){
                System.out.println("connecting to battle cache !!! please wait !!!");
            }
            if(state == 2){
                BitArray bitArray = ProtocolByteArrayManager.getInstance().getProtocolBitArray();
                request.encode(bitArray);
                boolean result = servicePrx.enterToBattleCache(serverId,playerId,bitArray.getWriteByteArray());
                bitArray.release();
                return result;
            }
            return false;
        }catch (Exception e){
            //e.printStackTrace();

            return false;
        }

    }

    public void kickOut (int sessionId){
        try{
            //servicePrx.kickOut(sessionId);
        }catch (Exception e){
            //e.printStackTrace();
        }
    }

}
