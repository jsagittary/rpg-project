package com.dykj.rpg.battle.cache.ice.server;

import Ice.*;
import IceBox.Service;
import com.dykj.rpg.battle.cache.manager.ClientManager;
import com.dykj.rpg.battle.ice.service.CallbackGameServerPrx;
import com.dykj.rpg.battle.ice.service.CallbackGameServerPrxHelper;
import com.dykj.rpg.battle.ice.service._Game2BattleCacheServiceDisp;
import com.dykj.rpg.mapping.ProtocolMapping;
import com.dykj.rpg.net.protocol.BitArray;
import com.dykj.rpg.net.protocol.ProtocolByteArrayManager;
import com.dykj.rpg.net.protocol.ProtocolPool;
import com.dykj.rpg.protocol.game2battle.PlayerEnterBattleRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.Exception;

public class Game2BattleCacheServiceImpl extends _Game2BattleCacheServiceDisp implements Service {

    Logger logger = LoggerFactory.getLogger("Game2BattleCacheServiceImpl");

    private ObjectAdapter _adapter;
    @Override
    public void start(String service1Name, Communicator communicator, String[] strings) {
        long now =System.currentTimeMillis();
        System.out.println("defaultLocator = "+communicator.getDefaultLocator());
        String service2Name = communicator.getProperties().getProperty("Service2.Name");
        String adapterName = communicator.getProperties().getProperty("Adapter.Name");
        System.out.println("Service1Name = "+service1Name);
        System.out.println("Service2Name = "+service2Name);
        System.out.println("AdapterName = "+adapterName);

        // 通讯器创建适配器
        _adapter = communicator.createObjectAdapter(adapterName);
        // ice对象
        Ice.Object game2BattleCacheService = this;
        // 适配器添加ice对象
        _adapter.add(game2BattleCacheService, communicator.stringToIdentity(service1Name));

        // ice对象
        Ice.Object battle2BattleCacheService = new Battle2BattleCacheServiceImpl();
        // 适配器添加ice对象
        _adapter.add(battle2BattleCacheService, communicator.stringToIdentity(service2Name));

        // 激活适配器
        _adapter.activate();
        System.out.println(adapterName + " 激活成功");

        //Admin admin = (Admin)communicator.getAdmin();
        ObjectPrx admin = communicator.getAdmin();
        //String[] nodeNames = admin.getAllNodeNames();

        //开启协议对象池
        ProtocolPool.getInstance().init(ProtocolMapping.getClassMap());

        //启动消息转发器
        ClientManager.getInstance();
        logger.info("BattleCache START SUCCESS %% STARTTIME=" + (System.currentTimeMillis() - now));
        logger.info("BattleCache START COMPLETE");
        logger.info("LINUX LOG SUCCESS");
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean pingBattleCache(int serverId, Current __current) {
        boolean result = false;
        try{
            Ice.ObjectPrx callBack = __current.con.createProxy(Util.stringToIdentity("CallbackGameServer"));
            CallbackGameServerPrx client = CallbackGameServerPrxHelper.checkedCast(callBack);
            ClientManager.getInstance().addGameServer(serverId,client);
            result = true;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return result;
        }

    }

    @Override
    public boolean enterToBattleCache(int serverId, int playerId, byte[] data, Current __current) {
        //TODO 先实现单人模式
        boolean result = false;
        try{

            System.out.println("enterToBattleCache serverId = "+serverId+"  playerId = "+playerId);

            PlayerEnterBattleRequest request = new PlayerEnterBattleRequest();

            BitArray bitArray = ProtocolByteArrayManager.getInstance().getProtocolBitArray();
            bitArray.initBytes(data,data.length);
            request.decode(bitArray);
            bitArray.release();

            System.out.println(request.toString());

            ClientManager.getInstance().enterToBattleCacheFromGameServer(serverId,playerId,data);

            //int battleServerId = 1;
            //ClientManager.getInstance().enterToBattleCache(serverId,battleServerId,playerId,data);
            result = true;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return result;
        }

    }

}
