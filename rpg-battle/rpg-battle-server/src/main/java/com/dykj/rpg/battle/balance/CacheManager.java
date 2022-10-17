package com.dykj.rpg.battle.balance;

import com.dykj.rpg.battle.ice.client.BattleBalanceClient;

public class CacheManager {

    public static final byte BALANCE_TYPE_ADD = 1;
    public static final byte BALANCE_TYPE_DELETE = 2;

    /**
     * 游戏的所有本地缓存数据存储的位置
     */
    //private GameDataCache gameDataCache = GameDataCache.getInstance();

    /**
     * 默认为等待服务器，当有游戏服务器进来后修改为主服务器，当有主服务器过来时设置为从服务器
     */

    private BattleBalanceClient balanceClient;

    private BalanceWheelTimer balanceWheelTimer;

    public int tickDuration = 30000;

    public int totalTickNum = 10;

    public boolean cacheConnected = false;

    private byte state = 0;

    private static CacheManager instance;

    private CacheManager(){
        System.out.println("=========CacheManager=========");
        //startMainServer();
    }

    public static CacheManager getInstance() {
        if(instance == null){
            instance = new CacheManager();
        }
        return instance;
    }

    public boolean isRunning(){
        return this.state != 0;
    }

//    public void setGameDataCache(GameDataCache gameDataCache) {
//        this.gameDataCache = gameDataCache;
//    }
//
//    public GameDataCache getGameDataCache() {
//        return gameDataCache;
//    }

    public void addNewBattle(int battleId){
        balanceWheelTimer.addRegister(battleId,CacheManager.BALANCE_TYPE_ADD);
    }

    public void removeBattle(int battleId){
        balanceWheelTimer.addRegister(battleId,CacheManager.BALANCE_TYPE_DELETE);
    }

    public void startMainServer(){
        System.out.println("=========startMainServer=========");
        this.state = 1;
        if(balanceWheelTimer != null){
            balanceWheelTimer.release();
        }
        //balanceWheelTimer = new BalanceWheelTimer(this.gameDataCache,tickDuration,totalTickNum);

        //cacheClient = new BattleCacheClient();
    }

    /**
     * 第一次与缓存服建立连接时，直接将所有本地缓存数据同步到缓存服务器
     */
    public void cacheConnectedNotify(){
        balanceWheelTimer.syncAll();
    }

    public BattleBalanceClient getBalanceClient() {
        return balanceClient;
    }
}
