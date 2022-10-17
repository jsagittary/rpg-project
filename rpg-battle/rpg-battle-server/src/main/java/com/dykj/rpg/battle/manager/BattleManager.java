package com.dykj.rpg.battle.manager;

/**
 * 战斗数据和逻辑的管理者
 */

import com.dykj.rpg.battle.constant.BattleStateConstant;
import com.dykj.rpg.battle.constant.BattleTypeConstant;
import com.dykj.rpg.battle.constant.GameConstant;
import com.dykj.rpg.battle.logic.*;
import com.dykj.rpg.battle.role.BattlePlayer;
import com.dykj.rpg.net.core.UdpSessionManager;
import com.dykj.rpg.net.protocol.ProtocolPool;
import com.dykj.rpg.protocol.battle.BattleMapInfo;
import com.dykj.rpg.protocol.game2battle.PlayerEnterBattleRequest;
import com.dykj.rpg.protocol.game2battle.PlayerEnterBattleRequestList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class BattleManager {

    Logger logger = LoggerFactory.getLogger("BattleManager");

    private static BattleManager instance;

    private BattleObjectPool<BattleContainer> battleContainerPool;

    /**
     * 每场战斗有一个BattleLogic进行逻辑处理
     */
    private Map<Integer, BattleContainer> battleContainerMap;

    private Map<Integer,Integer> playerIdBattleIdMap;

    private GameRunFrameRunnable[] runFrameRunnables;

    private GameUpdateRunnable[] dataUpdateRunnables;

    private int nextBattleId = 1;

    private BattleManager(){

        battleContainerPool = new BattleObjectPool<>(BattleContainer.class,GameConstant.MAX_BATTLE_RUNNING,1200000); //20分钟

        battleContainerMap = new ConcurrentHashMap<>();

        playerIdBattleIdMap = new ConcurrentHashMap<>();

        //线程工厂，用于设置线程池中线程相对于其他线程拥有较高的执行优先级
        ThreadFactory runFrameThreadFactory = new ThreadFactory() {
            public int index = 0;
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                //设置线程优先级[0-10],默认为5
                thread.setName("run-frame-thread-"+(++index));
                thread.setPriority(10);
                return thread;
            }
        };
        //定时任务，每帧进行一次战斗计算
        ScheduledThreadPoolExecutor runFrameExecutor = new ScheduledThreadPoolExecutor(GameConstant.RUN_FRANE_THREAD_SIZE,runFrameThreadFactory);
        runFrameRunnables = new GameRunFrameRunnable[GameConstant.RUN_FRANE_THREAD_SIZE];
//
        for(int i=0;i<GameConstant.RUN_FRANE_THREAD_SIZE;i++){
            GameRunFrameRunnable runFrameRunnable = new GameRunFrameRunnable();
            runFrameRunnables[i] = runFrameRunnable;
            runFrameExecutor.scheduleAtFixedRate(runFrameRunnable, GameConstant.FRAME_TIME*i/GameConstant.RUN_FRANE_THREAD_SIZE, GameConstant.FRAME_TIME, TimeUnit.MILLISECONDS);
        }

        //线程工厂，用于设置线程池中线程相对于其他线程拥有较高的执行优先级
        ThreadFactory updateDataThreadFactory = new ThreadFactory() {
            public int index = 0;
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                //设置线程优先级[0-10],默认为5
                thread.setName("update-data-thread-"+(++index));
                thread.setPriority(10);
                return thread;
            }
        };
        //定时任务，与客户端每帧同步一次数据，延迟一帧
        ScheduledThreadPoolExecutor dataUpdateExecutor = new ScheduledThreadPoolExecutor(GameConstant.DATA_UPDATE_THREAD_SIZE,updateDataThreadFactory);
        dataUpdateRunnables = new GameUpdateRunnable[GameConstant.DATA_UPDATE_THREAD_SIZE];
        for(int i=0;i<GameConstant.DATA_UPDATE_THREAD_SIZE;i++){
            GameUpdateRunnable updateRunnable = new GameUpdateRunnable();
            dataUpdateRunnables[i] = updateRunnable;
            //updateExecutor.scheduleAtFixedRate(updateRunnable,GameConstant.FRAME_TIME/GameConstant.ONE_FRAME_TICKNUM*i/GameConstant.DATA_UPDATE_THREAD_SIZE, GameConstant.FRAME_TIME/GameConstant.ONE_FRAME_TICKNUM, TimeUnit.MILLISECONDS);
            dataUpdateExecutor.scheduleAtFixedRate(updateRunnable,GameConstant.FRAME_TIME*i/GameConstant.DATA_UPDATE_THREAD_SIZE/GameConstant.ONE_FRAME_TICKNUM+GameConstant.FRAME_TIME, GameConstant.FRAME_TIME/GameConstant.ONE_FRAME_TICKNUM, TimeUnit.MILLISECONDS);
        }

        ScheduledThreadPoolExecutor managerUpdateExecutor = new ScheduledThreadPoolExecutor(2);

        Runnable managerUpdateRunnable = new Runnable() {
            @Override
            public void run() {
                BattleManager.getInstance().update();
            }
        };
        managerUpdateExecutor.scheduleAtFixedRate(managerUpdateRunnable,GameConstant.FRAME_TIME, GameConstant.FRAME_TIME, TimeUnit.MILLISECONDS);

        ScheduledThreadPoolExecutor poolScanExecutor = new ScheduledThreadPoolExecutor(1);

        Runnable poolScanRunnable = new Runnable() {
            @Override
            public void run() {
                for(int key : battleContainerMap.keySet()){
                    battleContainerMap.get(key).battlePoolManager.scanBattlePool();
                }
                //ProtocolPool.getInstance().scanProtocolPool();
            }
        };
        poolScanExecutor.scheduleAtFixedRate(poolScanRunnable,10000, 10000, TimeUnit.MILLISECONDS);

    }

    public static BattleManager getInstance() {
        if(instance == null) {
            instance = new BattleManager();
        }
        return instance;
    }

    /**
     * 创建一场战斗(测试环境使用)
     * @param playerId
     */
    public BattleContainer createOneBattle(int playerId){
        List<BattleMapInfo> results = null;

        if(playerIdBattleIdMap.get(playerId)==null){
            BattleContainer battleContainer = battleContainerPool.borrow();
            if(battleContainer != null){
                List<Integer> list = new ArrayList<>();
                list.add(playerId);
                battleContainer.initBattle(createBattleId(),0);
                battleContainer.initBattlePlayers(list);
                battleContainerMap.put(battleContainer.battleId,battleContainer);

                playerIdBattleIdMap.put(playerId,battleContainer.battleId);

                addBattleToRunFrameRunnable(battleContainer);

                //logger.info("player["+playerId+"] enter battle["+battleContainer.battleId+"] success!!!");
                return battleContainer;
            }

        }else{
            BattleContainer battleContainer = battleContainerMap.get(playerIdBattleIdMap.get(playerId));
            logger.info("player["+playerId+"] is battling in battle["+battleContainer.battleId+"]!!!");
            return battleContainer;
        }

        return null;

    }

    /**
     * 创建一场战斗(正式环境使用)
     * @param requestList
     */
    public BattleContainer createOneBattle(PlayerEnterBattleRequestList requestList){
        System.out.println(requestList);
        List<BattleMapInfo> results = null;


        PlayerEnterBattleRequest request = requestList.getRequests().get(0);
        int playerId = request.getPlayerId();
        if(playerIdBattleIdMap.get(playerId)==null){
            BattleContainer battleContainer = battleContainerPool.borrow();
            if(battleContainer != null){
                //给角色注册连接信息
                UdpSessionManager.getInstance().register(playerId,playerId,null);

                battleContainer.initBattle(createBattleId(),request.getMissionId());
                //战斗添加角色，务必放在创建session后
                battleContainer.initBattlePlayers(requestList);
                battleContainerMap.put(battleContainer.battleId,battleContainer);

                playerIdBattleIdMap.put(playerId,battleContainer.battleId);

                addBattleToRunFrameRunnable(battleContainer);

                logger.info("player["+playerId+"] enter battle["+battleContainer.battleId+"] success!!!");

                return battleContainer;
            }

        }else{
            BattleContainer battleContainer = battleContainerMap.get(playerIdBattleIdMap.get(playerId));
            logger.info("player["+playerId+"] is battling in battle["+battleContainer.battleId+"]!!!");
            return battleContainer;
        }

        return null;

    }

    public BattleContainer getBattleContainerByPlayerId(int playerId){
        if(playerIdBattleIdMap == null || playerIdBattleIdMap.size() == 0){
            return null;
        }

        if(playerIdBattleIdMap.get(playerId) == null){
            return null;
        }

        int battleId = playerIdBattleIdMap.get(playerId);
        return battleContainerMap.get(battleId);

    }

    private void addBattleToRunFrameRunnable(BattleContainer battleContainer){
        int size = 0;
        GameRunFrameRunnable frameRunnable = null;
        for(GameRunFrameRunnable runnable : runFrameRunnables){
            int runningBattleSize = runnable.getRunningBattleSize();
            if(frameRunnable == null || runningBattleSize < size){
                frameRunnable = runnable;
                size = runningBattleSize;
            }
        }
        frameRunnable.add(battleContainer);

        size = 0;
        GameUpdateRunnable updateRunnable = null;
        for(GameUpdateRunnable runnable : dataUpdateRunnables){
            int runningBattleSize = runnable.getRunningBattleSize();
            if(updateRunnable == null || runningBattleSize < size){
                updateRunnable = runnable;
                size = runningBattleSize;
            }
        }
        updateRunnable.add(battleContainer);
    }

    public int createBattleId(){
        if(nextBattleId == Integer.MAX_VALUE/2){
            nextBattleId = 1;
        }
        return nextBattleId++;
    }

    /**
     *  对应游戏每帧进行更新,由定时任务进行驱动
     */
    public void update() {

        if(battleContainerMap.size() == 0) {
            return;
        }

        Set<Integer> battleIds = battleContainerMap.keySet();
        for(Integer battleId : battleIds){
            BattleContainer battleContainer = battleContainerMap.get(battleId);
            //战斗完成做清除操作
            if(battleContainer.state == BattleStateConstant.BATTLE_STATE_DESTROY){
                System.out.println("删除战斗 battleId = "+battleContainer.battleId);
                for(BattlePlayer player : battleContainer.players){
                    int playerId = player.getPlayerId();
                    playerIdBattleIdMap.remove(playerId);
                    UdpSessionManager.getInstance().fireSession(playerId);
                }
                battleContainerPool.restore(battleContainer);
                battleContainerMap.remove(battleId);
            }
        }

        addRobotBattle();
    }

    public int robotId = 0;
    public void addRobotBattle(){
        for(int i=0;i<3;i++){
            if(battleContainerMap.size() < GameConstant.MAX_ROBOT_BATTLE_NUM && battleContainerMap.size() < GameConstant.MAX_BATTLE_RUNNING){
                robotId ++ ;
                //UdpSessionManager.getInstance().register(robotId,robotId,null);
                //BattleContainer battleContainer = createOneBattle(robotId);
                BattleContainer battleContainer = createOneBattle(robotId);
                battleContainer.startBattle();
                battleContainer.isRobetBattle = true;
                //System.out.println("add robotId = "+robotId+"  battle size = "+battleContainerMap.size());
            }
        }
    }

}
