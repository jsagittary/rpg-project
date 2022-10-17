package com.dykj.rpg.battle.logic;

import com.dykj.rpg.battle.constant.GameConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 战斗服战斗跑帧处理
 */
public class GameRunFrameRunnable implements Runnable {

    Logger logger = LoggerFactory.getLogger("GameRunFrameRunnable");

    private RunnableData runnableData;


    public GameRunFrameRunnable(){
        runnableData = new RunnableData(GameConstant.MAX_BATTLE_RUNNING/GameConstant.RUN_FRANE_THREAD_SIZE+1);
    }

    /**
     * FRAME_TIME 跑一次
     */
    @Override
    public void run() {

        try{
            long startTime = System.currentTimeMillis();
            //System.out.println("GameRunFrameRunnable start time = "+startTime);

            //Thread.sleep(100);

            runnableData.runFrame();

            long endTime = System.currentTimeMillis();
            if(endTime-startTime > GameConstant.FRAME_TIME){
                logger.error("[GameRunFrameRunnable "+Thread.currentThread().getName()+"] last frame not run finish in one frame time ["+(endTime-startTime)+" > "+GameConstant.FRAME_TIME+"] !!!");
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
            runnableData.add(battleContainer);
        }else{
            System.out.println("null battleLogic !!!");
        }
    }

    public int getRunningBattleSize(){
        return runnableData.getRunningBattleSize();
    }

}
