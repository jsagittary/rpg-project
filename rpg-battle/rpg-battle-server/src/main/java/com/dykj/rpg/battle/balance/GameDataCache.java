package com.dykj.rpg.battle.balance;


//import com.dykj.rpg.cache.Battle;
//import com.dykj.rpg.cache.RoleBasicModel;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class GameDataCache {

//    private Map<Integer, Battle> battleMap;
//
//    private Map<Integer,Battle> userIdBattleMap;
//
//    private Random random = new Random();
//
//    private static GameDataCache instance;
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
//        userIdBattleMap = new ConcurrentHashMap<>();
//    }
//
//    /**
//     * 主服务器添加战斗，并且加入注册列表
//     * @param battle
//     */
//    public void masterAddBattle(Battle battle){
//
//        List<RoleBasicModel> rbms = battle.getRoleBasicModels();
//        for(RoleBasicModel rbm : rbms){
//            if(userIdBattleMap.get(rbm.getModelId()) != null){
//                //TODO 此处创建战斗失败
//                return ;
//            }else{
//                userIdBattleMap.put(rbm.getModelId(),battle);
//            }
//        }
//        battle.setBattleId(createBattleId());
//        battleMap.put(battle.getBattleId(),battle);
//
//        if(CacheManager.getInstance().isRunning()){
//            CacheManager.getInstance().addNewBattle(battle.getBattleId());
//        }
//
//    }
//
//    /**
//     * 主服务器删除战斗
//     * @param battleId
//     */
//    public void masterRemoveBattle(int battleId){
//        Battle battle = battleMap.get(battleId);
//        if(battle != null){
//            List<RoleBasicModel> rbms = battle.getRoleBasicModels();
//            for(RoleBasicModel bpm : rbms){
//                userIdBattleMap.remove(bpm.getModelId());
//            }
//            battleMap.remove(battleId);
//        }
//    }
//
//    /**
//     * 主服务器将本地缓存的所有modelId上传到从服务器
//     * 从服务用于删除和添加动作
//     * @param battleId
//     */
//    public byte[] masterAskAllModelState(int battleId){
//        Battle battle = battleMap.get(battleId);
//        if(battle != null){
//            byte[] bytes = battle.askAllModelState();
//            return bytes;
//        }
//        return null;
//    }
//
//    /**
//     * 从服务器处理主服务器发送过来的更新战斗缓存数据
//     * @param battleId
//     * @param bytes
//     */
//    public void slaveUpdateBattle(int battleId,byte[] bytes){
//        Battle battle = battleMap.get(battleId);
//        if(battle == null){
//            battle = new Battle();
//            battle.setBattleId(battleId);
//            battleMap.put(battleId,battle);
//
//            List<RoleBasicModel> rbms = battle.getRoleBasicModels();
//            for(RoleBasicModel rbm : rbms){
//                userIdBattleMap.put(rbm.getModelId(),battle);
//            }
//        }
//        battle.decodeUpdateModel(bytes);
//    }
//
//    /**
//     * 从服务器处理主服务器的modelId
//     * @param battleId
//     * @param bytes
//     */
//    public void slaveCheckBattleSate(int battleId,byte[] bytes){
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
//    public void slaveRemoveBattle(int battleId){
//        Battle battle = battleMap.get(battleId);
//        List<RoleBasicModel> rbms = battle.getRoleBasicModels();
//        for(RoleBasicModel rbm : rbms){
//            userIdBattleMap.remove(rbm.getModelId());
//        }
//        battleMap.remove(battleId);
//    }
//
//
//    public byte[] encodeBattle(int battleId){
//        Battle battle = battleMap.get(battleId);
//        if(battle != null){
//            System.out.println("encodeBattle freshTime = " + System.currentTimeMillis());
//            battle.setRefreshTime(System.currentTimeMillis());
//            return battle.encode();
//        }
//        return null;
//    }
//
//    public byte[] encodeUpdateModel(int battleId){
//        Battle battle = battleMap.get(battleId);
//        if(battle != null){
//            System.out.println("encodeUpdateModel freshTime = " + System.currentTimeMillis());
//            battle.setRefreshTime(System.currentTimeMillis());
//            return battle.encodeUpdateModel();
//        }
//        return null;
//    }
//
//    public byte[] askAllModelState(int battleId){
//        Battle battle = battleMap.get(battleId);
//        if(battle != null){
//            return battle.askAllModelState();
//        }
//        return null;
//    }
//
//    public Map<Integer, Battle> getBattleMap() {
//        return battleMap;
//    }
//
//    public Battle getBattleByBattleId(int battleId) {
//        return battleMap.get(battleId);
//    }
//
//    public Battle getBattleByPlayerId(int playerId) {
//        return userIdBattleMap.get(playerId);
//    }
//
//    private int createBattleId(){
//        int battleId = (int)(System.currentTimeMillis() & 0xffff);
//        battleId = (battleId << 16 + new Random().nextInt(0xffff));
//        return battleId;
//    }
}
