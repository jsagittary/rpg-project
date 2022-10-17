package com.dykj.rpg.battle.cache.ice.server;

import Ice.Communicator;
import Ice.Current;
import Ice.ObjectAdapter;
import Ice.ObjectPrx;
import IceBox.Service;
import com.dykj.rpg.battle.cache.manager.ClientManager;
import com.dykj.rpg.battle.ice.service._BattleCacheServiceDisp;
import com.dykj.rpg.battle.cache.balance.GameDataCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BattleCacheServiceImpl extends _BattleCacheServiceDisp implements Service {

    Logger logger = LoggerFactory.getLogger("BattleCacheServiceImpl");

    private ObjectAdapter _adapter;

    @Override
    public void start(String name, Communicator communicator, String[] strings) {
        System.out.println("defaultLocator = "+communicator.getDefaultLocator());
        String adapterName = communicator.getProperties().getProperty("Adapter.Name");
        System.out.println("ServiceName = "+name);
        System.out.println("AdapterName = "+adapterName);

        // 通讯器创建适配器
        _adapter = communicator.createObjectAdapter(adapterName);
        // ice对象
        Ice.Object object = this;
        // 适配器添加ice对象
        _adapter.add(object, communicator.stringToIdentity(name));

        // 激活适配器
        _adapter.activate();
        System.out.println(name + "-- adapter 激活成功");

        //Admin admin = (Admin)communicator.getAdmin();
        ObjectPrx admin = communicator.getAdmin();
        //String[] nodeNames = admin.getAllNodeNames();

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean updateBattleCache(int battleId, byte[] data, Current __current) {
        logger.info("updateBattleCache battleId = " + battleId);

        try {
            //GameDataCache.getInstance().updateBattleCache(battleId,data);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean askBattleCacheState(int battleId, byte[] data, Current __current) {
        logger.info("askBattleSlaveModelState battleId = " + battleId);
        try {
            //GameDataCache.getInstance().checkBattleSate(battleId,data);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeBattleCache(int battleId, Current __current) {
        logger.info("removeBattleSlaveCache battleId = " + battleId);
        try {
            //GameDataCache.getInstance().removeBattleCache(battleId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public byte[] getBattleCache(int battleId, Current __current) {
        logger.info("getBattleCache battleId = " + battleId);
        try {
            //byte[] bytes = GameDataCache.getInstance().getBattleCache(battleId);
            //if(bytes == null){
            //    return new byte[0];
            //}else{
            //    return bytes;
            //}
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }

    }
}
