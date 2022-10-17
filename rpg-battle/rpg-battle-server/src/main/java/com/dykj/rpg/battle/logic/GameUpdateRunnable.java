package com.dykj.rpg.battle.logic;

import com.dykj.rpg.battle.constant.GameConstant;
import com.dykj.rpg.battle.util.RingBuffer;
import com.dykj.rpg.net.core.UdpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 战斗服战斗跑帧处理
 */
public class GameUpdateRunnable implements Runnable {

    Logger logger = LoggerFactory.getLogger("GameUpdateRunnable");

    RunnableData runnableData;

    public GameUpdateRunnable(){
        runnableData = new RunnableData(GameConstant.MAX_BATTLE_RUNNING/GameConstant.DATA_UPDATE_THREAD_SIZE+1);
    }

    /**
     * FRAME_TIME 跑一次
     */
    @Override
    public void run() {

        try{
            long startTime = System.currentTimeMillis();

            runnableData.update();

            long endTime = System.currentTimeMillis();
            if(endTime-startTime > GameConstant.FRAME_TIME){
                logger.error("[GameUpdateRunnable] last frame not run finish in one frame time ["+(endTime-startTime)+" > "+GameConstant.FRAME_TIME+"] !!!");
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
            //addList.get(handlerIndex).add(battleLogic);
        }else{
            System.out.println("null battleLogic !!!");
        }
    }

    public int getRunningBattleSize(){
        return runnableData.getRunningBattleSize();
    }

}
