package com.dykj.rpg.battle.ice.client;

import Ice.Communicator;
import Ice.ConnectionI;
import Ice.ObjectPrx;
import Ice.Util;
import com.dykj.rpg.battle.balance.CacheManager;
import com.dykj.rpg.battle.ice.service.BattleCacheServicePrx;
import com.dykj.rpg.battle.ice.service.BattleCacheServicePrxHelper;
//import com.dykj.rpg.cache.Battle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BattleBalanceClient {

    Logger logger = LoggerFactory.getLogger("BattleCacheClient");

    private ObjectPrx proxy;

    private BattleCacheServicePrx balancePrx;

    private ScheduledThreadPoolExecutor scheduled = new ScheduledThreadPoolExecutor(1);

    public BattleBalanceClient(){
        startClient();
    }

    private void startClient(){
        try{
            String[] args = new String[]{
                    "--Ice.Config=iceclient.properties"
            };

            Communicator communicator = Util.initialize(args);

            proxy = communicator.stringToProxy("Battle2BattleCacheService@BattleCacheAdapter");

            connectSlave();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void connectSlave(){
        try{
            ConnectionI conn = (ConnectionI)proxy.ice_getConnection();
            if(conn != null){
                BattleCacheServicePrx balancePrx = BattleCacheServicePrxHelper.checkedCast(proxy);
                this.balancePrx = balancePrx;

                System.out.println("client "+conn.getEndpoint());
                //通知连接状态给CacheManager
                CacheManager.getInstance().cacheConnectedNotify();
            }
        }catch (Exception e){
            e.printStackTrace();

            System.out.println("battle server connecting cache ...");

            scheduled.schedule(new Runnable(){

                @Override
                public void run() {
                    connectSlave();
                }
            },10000, TimeUnit.MILLISECONDS);
        }

    }

    public void updateBattleSlaveCache(int battleId,byte[] data){
        if(balancePrx != null){
            try{
                boolean result = balancePrx.updateBattleCache(battleId,data);
                logger.info("add or update battle battleId = "+battleId+"  result = "+result);
            }catch (Exception e){
                e.printStackTrace();
                balancePrx = null;
                connectSlave();
            }

        }
    }

    public void askBattleSlaveModelState(int battleId,byte[] data){
        if(balancePrx != null){
            try{
                boolean result = balancePrx.askBattleCacheState(battleId,data);
                logger.info("askModelState battle battleId = "+battleId+"  result = "+result);
            }catch (Exception e){
                e.printStackTrace();
                balancePrx = null;
                connectSlave();
            }

        }
    }

    public void removeBattleSlaveCache(int battleId){
        if(balancePrx != null){
            try{
                boolean result = balancePrx.removeBattleCache(battleId);
                logger.info("remove battle battleId = "+battleId+"  result = "+result);
            }catch (Exception e){
                e.printStackTrace();
                balancePrx = null;
                connectSlave();
            }

        }
    }

    public void getBattleCache(int battleId){
        if(balancePrx != null){
            try{
                byte[] bytes = balancePrx.getBattleCache(battleId);
                if(bytes.length == 0){

                }else{
//                    Battle battle = new Battle();
//                    battle.decode(bytes);
//                    logger.info("get battle battleId = "+battleId+"  battle = "+battle.toString());
                }

            }catch (Exception e){
                e.printStackTrace();
                balancePrx = null;
                connectSlave();
            }

        }
    }

}
