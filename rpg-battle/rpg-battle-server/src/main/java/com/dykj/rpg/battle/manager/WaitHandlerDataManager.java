package com.dykj.rpg.battle.manager;

import com.dykj.rpg.battle.constant.BattleStateConstant;
import com.dykj.rpg.battle.constant.MapLogicStateConstant;
import com.dykj.rpg.battle.constant.RoleTypeConstant;
import com.dykj.rpg.battle.data.WaitEventData;
import com.dykj.rpg.battle.data.WaitReleaseSkillData;
import com.dykj.rpg.battle.data.WaitUsePotionData;
import com.dykj.rpg.battle.logic.BattleContainer;
import com.dykj.rpg.battle.role.BattlePlayer;
import com.dykj.rpg.battle.role.BattleRole;
import com.dykj.rpg.battle.util.RingBuffer;
import com.dykj.rpg.protocol.battle.BattlePosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WaitHandlerDataManager {

    Logger logger = LoggerFactory.getLogger("WaitHandlerDataManager");

    BattleContainer battleContainer;

    /**
     * 等待释放的玩家手动释放技能列表
     * key 为 playerId
     */
    public Map<Integer, RingBuffer<WaitReleaseSkillData>> waitReleaseSkillMap = new ConcurrentHashMap<>(10);

    /**
     * 等待使用的玩家手动使用药品列表
     * key 为 playerId
     */
    public Map<Integer,RingBuffer<WaitUsePotionData>> waitUsePotionMap = new ConcurrentHashMap<>(10);

    /**
     * 等待处理的玩家事件
     */
    public Map<Integer,WaitEventData> waitEventDataMap = new ConcurrentHashMap<>(10);


    public WaitHandlerDataManager(){
    }

    public void init(BattleContainer battleContainer){
        this.battleContainer = battleContainer;
    }

    public void release(){
        waitReleaseSkillMap.clear();
        waitUsePotionMap.clear();
        waitEventDataMap.clear();
    }

    public void addPlayer(int playerId){
        waitReleaseSkillMap.put(playerId,new RingBuffer<WaitReleaseSkillData>(10,WaitReleaseSkillData.class));
        waitUsePotionMap.put(playerId,new RingBuffer<WaitUsePotionData>(10,WaitUsePotionData.class));
        waitEventDataMap.put(playerId,new WaitEventData());
    }

    public boolean addReleaseSkillToWaitMap(int playerId, int frameNum, int skillId, BattlePosition position){
        if(battleContainer.state != BattleStateConstant.BATTLE_STATE_RUNNING){
            return false;
        }
        //十帧以内的数据生效
        if(frameNum <= battleContainer.frameNum && frameNum >= battleContainer.frameNum-10){
            RingBuffer<WaitReleaseSkillData> rb = waitReleaseSkillMap.get(playerId);
            if(rb != null){
                WaitReleaseSkillData waitReleaseSkillData = rb.getAddObj();
                waitReleaseSkillData.frameNum = frameNum;
                waitReleaseSkillData.skillId = skillId;
                waitReleaseSkillData.pos = position;

                return true;
            }
        }

        return false;
    }

    /**
     * 处理手动释放技能逻辑，单个角色每一帧默认只处理一个技能
     * @param playerId
     * @return
     */
    public WaitReleaseSkillData getFirstReleaseSkill(int playerId){
        RingBuffer<WaitReleaseSkillData> rb = waitReleaseSkillMap.get(playerId);
        if(rb != null){
            return rb.poll();
        }
        return null;
    }

    public boolean addUsePotionToWaitMap(int playerId,int pointId,boolean ignoreCdTime){
        if(battleContainer.state != BattleStateConstant.BATTLE_STATE_RUNNING){
            return false;
        }

        RingBuffer<WaitUsePotionData> rb = waitUsePotionMap.get(playerId);
        if(rb != null){
            WaitUsePotionData data = rb.getAddObj();
            data.potionId = pointId;
            data.ignoreCdTime = ignoreCdTime;
            return true;
        }

        return false;
    }

    /**
     * 处理使用药品逻辑，单个角色每一帧默认只处理一个药品
     * @param playerId
     * @return
     */
    public WaitUsePotionData getFirstUsePotion(int playerId){
        RingBuffer<WaitUsePotionData> rb = waitUsePotionMap.get(playerId);
        if(rb != null){
            return rb.poll();
        }
        return null;
    }

    public boolean addEventData(int playerId,int eventType,int[] eventDataParams,float[] eventPosParams){
        WaitEventData waitEventData = waitEventDataMap.get(playerId);
        if(waitEventData != null){
            if(eventType > waitEventData.eventType){
                waitEventData.eventType = eventType;
                waitEventData.eventDataParams = eventDataParams;
                waitEventData.eventPosParams = eventPosParams;
                return true;
            }
        }

        return false;
    }

    /**
     *  每帧操作事件
     */
    public void eventHandler(BattleRole battleRole){
        if(battleRole.getRoleType() == RoleTypeConstant.BATTLE_ROLE_PLAYER){
            WaitEventData waitEventData = waitEventDataMap.get(battleRole.getPlayerId());
            if(waitEventData != null){
                switch (waitEventData.eventType){
                    case WaitEventData.EVENT_DATA_TYPE_FIND_TARGET: playerFindTarget((BattlePlayer) battleRole);break;
                    case WaitEventData.EVENT_DATA_TYPE_SET_TARGET_POSITION: playerSetTargetPosition((BattlePlayer) battleRole,waitEventData.eventPosParams);break;
                }
            }
        }
    }

    /**
     * 寻找新目标
     */
    private void playerFindTarget(BattlePlayer player){
        //地图正常运行，寻找一个距离最近的敌人
        if(player.mapLogic.getState() == MapLogicStateConstant.MAPLOGIC_STATE_RUN){
            BattleRole enemy = player.mapLogic.findNearestEnemy();
            if(enemy != null){
                player.setPathTargetRole(enemy);
                //setCrowdTargetRole(enemy);
            }else{
                //查看是否有等待出生的怪物
                if(!player.mapLogic.hasWaitBornNpc()){
                    if(player.nextRoadPoint[3] < 0){
                        player.mapLogic.peekPlayerNextRoadPoint(player.nextRoadPoint);
                    }
                    if(player.nextRoadPoint[3] >= 0){
                        player.setPathTargetPosition(player.nextRoadPoint);
                    }else{
                        //没有新目标（敌人和路点）
                        playerRunToMapExitPosition(player);
                    }
                }
            }
        }
    }

    /**
     * 寻找地图出口
     */
    private void playerRunToMapExitPosition(BattlePlayer player){
        System.out.println("---------runToMapExitPosition-----------");
        float[] exitPos = player.mapLogic.getExitPos();
        if(exitPos == null){
            //没有下一个出口，说明地图已经跑完，结束战斗
            logger.info("the map of player on has no exit position !!!");
            battleContainer.finishBattle(BattleContainer.BATTLE_FINISH_TYPE_NORMAL,true,10);
        }else{
            if(exitPos.length == 2){
                player.setExitTargetPosition(exitPos[0],exitPos[1]);
            }
        }

    }

    /**
     * 设置目标地点
     */
    private void playerSetTargetPosition(BattlePlayer player,float[] eventPosParams){

    }

    /**
     * 设置目标角色
     */
    private void playerSetTargetRole(BattlePlayer player,float[] eventPos){

    }

}
