package com.dykj.rpg.battle.logic;

import com.dykj.rpg.battle.constant.BattleStateConstant;
import com.dykj.rpg.battle.constant.GameConstant;
import com.dykj.rpg.battle.manager.BattleManager;
import com.dykj.rpg.battle.util.RingBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 战斗服战斗跑帧处理
 */
public class RunnableData {

    Logger logger = LoggerFactory.getLogger("RunnableData");

    public static final int ONCE_MAX_ADD_NUM = 1;

    public static final int MAX_BATTLE_CONTAINER_SIZE = GameConstant.MAX_BATTLE_RUNNING/GameConstant.RUN_FRANE_THREAD_SIZE+1;

    public int arraySize = 0;

    public BattleContainer[] battleContainerArray;

    //public List<BattleContainer> battleContainerList = new ArrayList<BattleContainer>();

    public RingBuffer<BattleContainer> addBuffer = new RingBuffer<>(GameConstant.MAX_BATTLE_RUNNING,BattleContainer.class);


    public RunnableData(int size){
        this.arraySize = size;
        battleContainerArray = new BattleContainer[size];
    }

    public void runFrame() {
        //System.out.println("-------------RunnableData runFrame------------ "+System.currentTimeMillis());
        long runFrameTime = System.currentTimeMillis();
        try{
            /**
             * 先做添加操作
             */
            int addNum = 0;
            int selectIndex = 0;
            while(addNum < ONCE_MAX_ADD_NUM && addBuffer.size()>0){
                addNum ++ ;
                //battleContainerList.add(addBuffer.poll());
                for(;selectIndex < MAX_BATTLE_CONTAINER_SIZE;selectIndex++){
                    if(battleContainerArray[selectIndex] == null){
                        battleContainerArray[selectIndex] = addBuffer.poll();
                    }
                }
            }

            /**
             * 战斗跑帧操作
             */
            //int battleContainerListSize = battleContainerList.size();
            int i = 0;
            BattleContainer battleContainer;
            while(i < MAX_BATTLE_CONTAINER_SIZE){

                battleContainer = battleContainerArray[i];

                if(battleContainer != null){
                    /**
                     * 如果战斗已经结束，直接从任务中删除
                     */
                    if(battleContainer.state == BattleStateConstant.BATTLE_STATE_REMOVE){
                        battleContainer.state = BattleStateConstant.BATTLE_STATE_CLEAR;
                        battleContainerArray[i] = null;
                    }else{
                        if(battleContainer.state == BattleStateConstant.BATTLE_STATE_INIT || battleContainer.state == BattleStateConstant.BATTLE_STATE_RUNNING || battleContainer.state == BattleStateConstant.BATTLE_STATE_FINISH){
                            battleContainer.runFrame(runFrameTime);
                        }
                    }
                }
                i++;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void update() {
        long updateTime = System.currentTimeMillis();
        try{

            /**
             * 先做添加操作
             */
            int addNum = 0;
            int selectIndex = 0;
            while(addNum < ONCE_MAX_ADD_NUM && addBuffer.size()>0){
                addNum ++ ;
                for(;selectIndex < MAX_BATTLE_CONTAINER_SIZE;selectIndex++){
                    if(battleContainerArray[selectIndex] == null){
                        battleContainerArray[selectIndex] = addBuffer.poll();
                    }
                }
            }

            /**
             * 战斗服向客户端发送更新数据
             */
            int i = 0;
            BattleContainer battleContainer;
            while(i < MAX_BATTLE_CONTAINER_SIZE){
                battleContainer = battleContainerArray[i];
                if(battleContainer != null){
                    /**
                     * 如果战斗已经结束，直接从任务中删除
                     */
                    if(battleContainer.state == BattleStateConstant.BATTLE_STATE_CLEAR){
                        battleContainer.state = BattleStateConstant.BATTLE_STATE_DESTROY;
                        battleContainerArray[i] = null;
                    }else{
                        if(battleContainer.state == BattleStateConstant.BATTLE_STATE_INIT || battleContainer.state == BattleStateConstant.BATTLE_STATE_RUNNING || battleContainer.state == BattleStateConstant.BATTLE_STATE_FINISH){
                            battleContainer.update(updateTime);
                        }
                    }
                }
                i++;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 新增战斗数据
     * @param battleContainer
     */
    public void add(BattleContainer battleContainer){
        if(battleContainer != null){
            addBuffer.add(battleContainer);
        }else{
            System.out.println("null battleContainer !!!");
        }
    }

    public int getRunningBattleSize(){
        int count = 0;
        for(int i=0;i < MAX_BATTLE_CONTAINER_SIZE;i++){
            if(battleContainerArray[i] != null){
                count ++ ;
            }
        }
        return count;
    }

}
