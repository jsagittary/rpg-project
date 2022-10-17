package com.dykj.rpg.battle.cache.balance;


//import com.dykj.rpg.cache.Battle;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GameDataCache {

//    private Map<Integer, Battle> battleMap;
//
//    private Random random = new Random();
//
//    private static GameDataCache instance;
//
//    private ScheduledThreadPoolExecutor scheduled ;
//
//    public static GameDataCache getInstance() {
//        if(instance == null){
//            instance = new GameDataCache();
//        }
//        return instance;
//    }
//
//    private GameDataCache(){
//        battleMap = new ConcurrentHashMap<>();
//
//        scheduled = new ScheduledThreadPoolExecutor(1);
//
//        scheduled.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                //失效时间设为60分钟
//                long now = System.currentTimeMillis();
//                Set<Integer> set = battleMap.keySet();
//                for(Integer key : set){
//                    if(now - battleMap.get(key).getRefreshTime() > 1*60000){
//                        System.out.println("battle over time ! battleId = "+battleMap.get(key).getBattleId());
//                        battleMap.remove(key);
//                    }
//                }
//            }
//        },60000,60000, TimeUnit.MILLISECONDS); //每分钟更新一次
//
//    }
//
//    /**
//     * 从服务器处理主服务器发送过来的更新战斗缓存数据
//     * @param battleId
//     * @param bytes
//     */
//    public void updateBattleCache(int battleId,byte[] bytes){
//        Battle battle = battleMap.get(battleId);
//        if(battle == null){
//            battle = new Battle();
//            battle.setBattleId(battleId);
//            battleMap.put(battleId,battle);
//        }
//        battle.decodeUpdateModel(bytes);
//
//        System.out.println("battle = "+battle);
//    }
//
//    /**
//     * 从服务器处理主服务器的modelId
//     * @param battleId
//     * @param bytes
//     */
//    public void checkBattleSate(int battleId,byte[] bytes){
//        Battle battle = battleMap.get(battleId);
//
//        if(battle != null){
//            if(bytes == null || bytes.length == 0){//删除操作
//                battleMap.remove(battleId);
//            }else{
//                battle.checkAllModelState(bytes);
//            }
//        }
//    }
//
//    /**
//     * 从服务器删除Battle
//     * @param battleId
//     */
//    public void removeBattleCache(int battleId){
//        battleMap.remove(battleId);
//    }
//
//    public byte[] getBattleCache(int battleId){
//        Battle battle = battleMap.get(battleId);
//        if(battle == null){
//            return null;
//        }
//        return battle.encode();
//    }
}
