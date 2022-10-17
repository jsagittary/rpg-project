package com.dykj.rpg.battle.basic;

/**
 * 战斗逻辑处理类
 */

import com.dykj.rpg.battle.config.*;
import com.dykj.rpg.battle.constant.GameConstant;
import com.dykj.rpg.battle.logic.BattleContainer;
import com.dykj.rpg.battle.logic.MapLogic;
import com.dykj.rpg.battle.role.BattleBoss;
import com.dykj.rpg.battle.role.BattleMonster;
import com.dykj.rpg.battle.role.BattlePlayer;
import com.dykj.rpg.battle.role.BattleRole;
import com.dykj.rpg.protocol.battle.BattleNpcRoundWarning;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class BasicBattleLogic {

    Logger logger = LoggerFactory.getLogger("BasicBattleLogic");

    protected Random random = new Random(System.currentTimeMillis());

    protected byte battleType;

    protected BattleContainer battleContainer;

    public BasicBattleLogic(BattleContainer battleContainer){
        this.battleContainer = battleContainer;
    }

    public abstract boolean playerRunLogic(int frameNum,BattlePlayer player);

    public abstract boolean monsterRunLogic(int frameNum,BattleMonster monster);

    public abstract boolean bossRunLogic(int frameNum,BattleBoss boss);

    public boolean refreshMonsterLogic(MapLogic mapLogic, MapTileConfig tileConfig, BattlePlayer player) {

        if(tileConfig.listenerConfigList.size() > 0){
            int listSize = tileConfig.listenerConfigList.size();
            int index = 0;
            a:while(index < listSize){
                TileListenerConfig listenerConfig = tileConfig.listenerConfigList.get(index);
                boolean listenerResult = false;
                if(listenerConfig.effects.size() > 0 && listenerConfig.listenerConditions.size() > 0){
                    b:for(TileListenerCondition condition : listenerConfig.listenerConditions){
                        boolean conditionResult = false;
                        if(condition.type == TileListenerCondition.CONDITION_TYPE_TIME){
                            if(condition.time > 0){
                                condition.time -= GameConstant.FRAME_TIME;
                                conditionResult = false;
                            }else{
                                conditionResult = true;
                            }
                        }

                        if(condition.type == TileListenerCondition.CONDITION_TYPE_DISTANCE){
                            double distance = Math.sqrt(Math.pow(player.currentPosition[0]- condition.x,2)+Math.pow(player.currentPosition[2]- condition.z,2));
                            if(distance > condition.distance){
                                conditionResult = false;
                            }else{
                                conditionResult = true;
                            }
                        }

                        if(condition.type == TileListenerCondition.CONDITION_TYPE_KILL){
                            if(tileConfig.containsGroupConfig(condition.kill)){
                                conditionResult = false;
                            }else{
                                conditionResult = true;
                            }
                        }

                        if(listenerConfig.type == TileListenerConfig.TRIGGER_TYPE_AND){
                            if(!conditionResult){
                                listenerResult = false;
                                break b;
                            }else{
                                listenerResult = true;
                            }
                        }
                        if(listenerConfig.type == TileListenerConfig.TRIGGER_TYPE_OR){
                            if(conditionResult){
                                listenerResult = true;
                                break b;
                            }else{
                                listenerResult = false;
                            }
                        }
                    }

                    //触发效果
                    if(listenerResult){
                        c:for(int effectId : listenerConfig.effects){
                            d:for(TileEffectConfig effectConfig : tileConfig.effectConfigList){
                                if(effectConfig.id == effectId){
                                    effectConfigBuffer.add(effectConfig);
                                    //System.out.println("listener tile effect config id = "+effectConfig.id);
                                    break d;
                                }
                            }
                        }

                        tileConfig.listenerConfigList.remove(index);
                        listSize --;
                    }else{
                        index ++;
                    }

                }else{ //触发器未设置触发条件的，直接删除触发器
                    tileConfig.listenerConfigList.remove(index);
                    listSize --;
                }
            }
        }

        //处理效果的释放
        int bufferSize = effectConfigBuffer.size();
        if(bufferSize > 0){
            int index = 0;
            while(index < bufferSize){
                TileEffectConfig effectConfig = effectConfigBuffer.get(index);
                if(effectConfig.delayTime > 0){
                    effectConfig.delayTime -= GameConstant.FRAME_TIME;
                    index ++;
                }else{
                    if(effectConfig.type == TileEffectConfig.EFFECT_TYPE_NPC){
                        byte state = effectConfig.state;
                        if(state == TileEffectConfig.EFFECT_TYPE_STATE_UNUSE){
                            createMonster(mapLogic,tileConfig,effectConfig);
                            if(effectConfig.destroyEffectId == 0){
                                effectConfigBuffer.remove(index);
                                bufferSize --;
                            }else{
                                effectConfig.state = TileEffectConfig.EFFECT_TYPE_STATE_USED;
                                index ++;
                            }
                        }
                        if(state == TileEffectConfig.EFFECT_TYPE_STATE_USED){
                            if(tileConfig.containsGroupConfig(effectConfig.npcGroupId)){
                                index ++;
                            }else{
                                //System.out.println("remove tile effect config id = "+effectConfig.id);
                                effectConfigBuffer.remove(index);
                                //添加上一个效果器销毁时触发的效果器
                                d:for(TileEffectConfig effectConfig2 : tileConfig.effectConfigList){
                                    if(effectConfig2.id == effectConfig.destroyEffectId){
                                        effectConfigBuffer.add(effectConfig2);
                                       // System.out.println("listener tile effect config id = "+effectConfig2.id);
                                        tileConfig.effectConfigList.remove(effectConfig2);
                                        break d;
                                    }
                                }
                                bufferSize --;
                            }
                        }
                    }

                    if(effectConfig.type == TileEffectConfig.EFFECT_TYPE_ROAD){
                        //删除目标路点
                        player.removeNextPosition(effectConfig.removeRoadPoint);
                        effectConfigBuffer.remove(index);
                        bufferSize --;
                    }

                    if(effectConfig.type == TileEffectConfig.EFFECT_TYPE_BUFF){
                        //TODO 给主角添加一个buff
                        effectConfigBuffer.remove(index);
                        bufferSize --;
                    }
                }
            }
        }

        return false;
    }

    private List<TileEffectConfig> effectConfigBuffer = new ArrayList<>();

    private void createMonster(MapLogic mapLogic, MapTileConfig tileConfig,TileEffectConfig effectConfig){
       // System.out.println("BattleGuardLogic createMonster groupId = "+effectConfig.npcGroupId);
        //怪物的出生控制
        int lastGroupSize = tileConfig.groupConfigList.size();
        for(int i=0;i<lastGroupSize;i++){
            TileGroupConfig groupConfig = tileConfig.groupConfigList.get(i);
            if(effectConfig.npcGroupId == groupConfig.groupId){
                int npcSize = groupConfig.npcDetails.size();
                if(npcSize >0){
                    for(int j=0;j<npcSize;j++){
                        TileNpcDetail npcDetail = groupConfig.npcDetails.get(j);
                        int randomTime = 0;
                        if(npcDetail.getMaxDelayTime() == npcDetail.getMinDelayTime()){
                            randomTime = npcDetail.getMaxDelayTime();
                        }
                        if(npcDetail.getMaxDelayTime() > npcDetail.getMinDelayTime()){
                            randomTime = random.nextInt(npcDetail.getMaxDelayTime()-npcDetail.getMinDelayTime())+npcDetail.getMinDelayTime();
                        }
                        npcDetail.setCurDelayTime(randomTime/10);

                        mapLogic.addWaitBornNpc(npcDetail);

                    }
                }

                //添加刷怪轮次，并将信息发送给客户端
                battleContainer.curNpcRounds ++ ;
                BattleNpcRoundWarning npcRoundWarning = new BattleNpcRoundWarning();
                npcRoundWarning.setNpcRound(battleContainer.curNpcRounds);
                battleContainer.sendBattleData(npcRoundWarning);
                //System.out.println(npcRoundWarning.toString());
                break ;
            }
        }

    }

    public float getTwoPositionDistance(float[] pos1,float[] pos2){
        return (float)Math.sqrt(Math.pow(pos1[0]-pos2[0],2)+Math.pow(pos1[2]-pos2[2],2));
    }
}
