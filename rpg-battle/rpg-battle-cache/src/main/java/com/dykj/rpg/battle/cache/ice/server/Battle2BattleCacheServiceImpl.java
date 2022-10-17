package com.dykj.rpg.battle.cache.ice.server;

import Ice.*;
import com.dykj.rpg.battle.cache.manager.ClientManager;
import com.dykj.rpg.battle.ice.service.*;
import com.dykj.rpg.protocol.game2battle.BattleFinishResultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.Exception;
import java.util.Arrays;

public class Battle2BattleCacheServiceImpl extends _Battle2BattleCacheServiceDisp {

    Logger logger = LoggerFactory.getLogger("Battle2BattleCacheServiceImpl");


    @Override
    public boolean pingBattleCache(int serverId, Current __current) {
        boolean result = false;
        try{
            CallbackBattleServerPrx client = CallbackBattleServerPrxHelper.uncheckedCast(__current.con.createProxy(Util.stringToIdentity("CallbackBattleServer")));
            ClientManager.getInstance().addBattleServer(serverId,client);
            result = true;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return result;
        }
    }

    @Override
    public boolean enterToBattleServerSuccess(int[] playerIds, byte[] data, Current __current) {
        System.out.println("enterToBattleServerSuccess playerIds = "+ Arrays.toString(playerIds));
        boolean result = false;
        try{
            ClientManager.getInstance().enterToBattleServerSuccess(playerIds,data);
            result = true;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return result;
        }
    }

    @Override
    public boolean battleFinishResult(byte[] data, Current __current) {

        System.out.println("battleFinishResult !!!");
        boolean result = false;
        try{
            ClientManager.getInstance().sendBattleFinishResult(data);
            result = true;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return result;
        }
    }
}
