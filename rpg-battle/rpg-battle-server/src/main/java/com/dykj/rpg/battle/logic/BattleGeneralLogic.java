package com.dykj.rpg.battle.logic;

import com.dykj.rpg.battle.basic.BasicBattleLogic;
import com.dykj.rpg.battle.config.MapTileConfig;
import com.dykj.rpg.battle.config.TileGroupConfig;
import com.dykj.rpg.battle.config.TileNpcDetail;
import com.dykj.rpg.battle.constant.*;
import com.dykj.rpg.battle.role.BattleBoss;
import com.dykj.rpg.battle.role.BattleMonster;
import com.dykj.rpg.battle.role.BattlePlayer;
import com.dykj.rpg.battle.role.BattleRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;


/**
 * 普通关卡（boss通关）
 * 玩法说明：普通的先杀怪，到最后打BOSS的玩法类型。
 * 玩法体验：在打小怪的过程中积累，最后在BOSS战中爆发的整体体验。
 */

public class BattleGeneralLogic extends BasicBattleLogic {

    private Logger logger = LoggerFactory.getLogger("BattleGeneralLogic");

    public BattleGeneralLogic(BattleContainer battleContainer){
        super(battleContainer);
    }

    @Override
    public boolean playerRunLogic(int frameNum,BattlePlayer player) {

        if(!player.stateManager.canActionFree()){
            return true;
        }

        boolean roleMove = false;

        //2 角色寻找目标+移动
        //判断玩家是否可以正常移动
        if(player.stateManager.canActionFree()){
            if(player.nextRoadPoint[3] >= 0 && Math.abs(player.currentPosition[0]-player.nextRoadPoint[0]) <= 0.5f && Math.abs(player.currentPosition[2]-player.nextRoadPoint[2]) <= 0.5f){
                player.nextRoadPoint[3] = -1;
            }
            // 2.1 判断角色是否需要寻找目标，如果没有确定的移动目标，进行目标锁定
            if(player.stateManager.needFindNewTarget()){
                playerFindNewTarget(player);
            }
            // 2.2 判断角色是否在向目标移动
            if(player.stateManager.isMoveToTarget()){
                //到达目的地
                if(player.isArriveTarget()){
                    byte purpos = player.stateManager.getPurpos();
                    //寻敌到达
                    if(purpos == RolePurposConstant.ROLE_PURPOS_FIND_OBJECT){
                        //TODO 目标改为进入战斗
                        player.stateManager.addPurpos(RolePurposConstant.ROLE_PURPOS_FIGHT);
                    }
                }else{
                    //角色移动，移动成功直接结束运算帧
                    if(player.run()){
                        roleMove = true;
                        player.animManager.addAnim(frameNum,RoleAnimConstant.ROLE_ANIM_MOVE,0);
                    }
                }
            }
        }

        //4. 角色其它行为，主要是战斗
        if(!roleMove){
            //修改玩家移动方式
            if(player.moveType != RoleMoveTypeConstant.ROLE_MOVE_TYPE_WAIT){
                player.moveType = RoleMoveTypeConstant.ROLE_MOVE_TYPE_WAIT;
            }
            byte purpos = player.stateManager.getPurpos();
            //玩家在打斗中
            if(purpos == RolePurposConstant.ROLE_PURPOS_FIGHT){
                //自动技能没有被buff禁用
                if(player.buffManager.canReleaseRoleSkill()){
                    //当攻击目标存在，并且没有跑出了自己的攻击范围，进行攻击计算，否则结束当前打斗，重新进行寻敌
                    float targetDistance = player.mapLogic.getTwoRoleDistance(player,player.targetRole);
                    if(player.targetRole != null && player.targetRole.getState() != RoleStateConstant.ROLE_STATE_DEATH &&  targetDistance <= player.attributeManager.maxAttackDistance){
                        //修改角色朝向，确保正面对准目标角色
                        player.setCurrentDir(player.targetRole.currentPosition[0]-player.currentPosition[0],player.targetRole.currentPosition[2]-player.currentPosition[2]);

                        if(player.needArriveTarget()){
                            if(targetDistance >= player.attributeManager.minAttackDistance){
                                player.setFreeState();
                            }
                        }else{
                            //释放技能
                            player.skillManager.autoReleaseSkill(false);
                        }

                    }else{
                        player.setStillState(200);
                    }
                }
            }
        }


        return true;
    }

    @Override
    public boolean monsterRunLogic(int frameNum,BattleMonster monster) {

        if(!monster.stateManager.canActionFree()){
            return true;
        }

        boolean needContinue = true;

        //2. 角色寻找目标+移动
        if(monster.stateManager.canActionFree()){
            // 2.1 判断角色是否需要寻找目标，如果没有确定的移动目标，进行目标锁定
            if(monster.stateManager.needFindNewTarget()){
                //地图正常运行，查看是否进入视野（警戒位置）
                byte mapState = monster.getMapLogic().getState();
                if(mapState == MapLogicStateConstant.MAPLOGIC_STATE_RUN){
                    float distance = monster.getMapLogic().getRoleToPlayerDistance(monster);
                    if(distance <= monster.attributeManager.vision){ //进入怪物视野
                        monster.setCrowdTargetRole(monster.getMapLogic().getPlayer());
                    }
                }
                //地块任务完成，此时怪物需要停止所有行动
                if(mapState == MapLogicStateConstant.MAPLOGIC_STATE_FINISH){
                    monster.stateManager.addPurpos(RolePurposConstant.ROLE_PURPOS_NULL);
                }
            }

            // 2.2 判断角色是否在向目标移动
            if(monster.stateManager.isMoveToTarget()){
                //到达目的地
                if(monster.isArriveTarget()){
                    byte purpos = monster.stateManager.getPurpos();
                    //寻敌到达
                    if(purpos == RolePurposConstant.ROLE_PURPOS_FIND_OBJECT){
                        //TODO 目标改为进入战斗
                        monster.stateManager.addPurpos(RolePurposConstant.ROLE_PURPOS_FIGHT);
                    }
                }else{
                    //角色移动，移动成功直接结束运算帧
                    if(monster.run()){
                        needContinue = false;
                        monster.animManager.addAnim(frameNum,RoleAnimConstant.ROLE_ANIM_MOVE,0);
                    }
                }
            }

        }else{
            if(monster.stateManager.getState() == RoleStateConstant.ROLE_STATE_BORN){
                monster.animManager.addAnim(frameNum,RoleAnimConstant.ROLE_ANIM_BORN,0);
            }
            return false;
        }


        if(needContinue){
            if(monster.moveType != RoleMoveTypeConstant.ROLE_MOVE_TYPE_WAIT){
                monster.moveType = RoleMoveTypeConstant.ROLE_MOVE_TYPE_WAIT;
            }
            byte purpos = monster.stateManager.getPurpos();
            //怪物在打斗中
            if(purpos == RolePurposConstant.ROLE_PURPOS_FIGHT){
                //自动技能没有被buff禁用
                if(monster.buffManager.canReleaseRoleSkill()){
                    //当攻击目标存在，并且没有跑出了自己的攻击范围，进行攻击计算，否则结束当前打斗，重新进行寻敌
                    float targetDistance = monster.getMapLogic().getTwoRoleDistance(monster,monster.targetRole);
                    if(monster.targetRole != null && monster.targetRole.getState() != RoleStateConstant.ROLE_STATE_DEATH && targetDistance <= monster.attributeManager.maxAttackDistance){
                        //修改角色朝向，确保正面对准目标角色
                        monster.setCurrentDir(monster.targetRole.currentPosition[0]-monster.currentPosition[0],monster.targetRole.currentPosition[2]-monster.currentPosition[2]);

                        if(monster.needArriveTarget()){
                            if(targetDistance > monster.attributeManager.minAttackDistance){
                                monster.setFreeState();
                            }
                        }else{
                            monster.skillManager.autoReleaseSkill(false);
                        }
                    }else{
                        monster.setStillState(200);
                    }
                }
            }
        }

        return true;
    }

    @Override
    public boolean bossRunLogic(int frameNum,BattleBoss boss) {
        return false;
    }

    private void playerFindNewTarget(BattlePlayer player){
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
}
