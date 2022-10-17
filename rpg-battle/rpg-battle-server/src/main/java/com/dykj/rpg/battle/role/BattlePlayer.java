package com.dykj.rpg.battle.role;

import com.dykj.rpg.battle.attribute.RoleAttributes;
import com.dykj.rpg.battle.constant.*;
import com.dykj.rpg.battle.data.WaitReleaseSkillData;
import com.dykj.rpg.battle.data.WaitUsePotionData;
import com.dykj.rpg.battle.logic.BattleContainer;
//import com.dykj.rpg.cache.RoleBattleModel;
//import com.dykj.rpg.cache.RolePositionModel;
import com.dykj.rpg.common.config.model.CharacterBasicModel;
import com.dykj.rpg.protocol.battle.*;
import com.dykj.rpg.protocol.item.ItemRs;

import java.util.*;

/**
 * 玩家角色行为和数据处理类
 */

public class BattlePlayer extends BattleRole {

    /**
     * 当前角色的下一个目标路点[x,y,z,id]
     */
    public float[] nextRoadPoint = new float[]{0,0,0,-1};
    /**
     * 拾取的奖励列表
     */
    public Map<Long,ItemRs> totalLootInfoMap = new HashMap<>();

    public int soulId;

    /**
     * 初始化玩家角色
     * @param battleContainer
     * @param basicModel
     * @param roleAttributes
     */
    public void init(BattleContainer battleContainer,int playerId,int skillSourceType,int roleId, int roleLevel,int modelId,CharacterBasicModel basicModel, RoleAttributes roleAttributes) {
        super.init(battleContainer,null,playerId,skillSourceType,RoleTypeConstant.BATTLE_ROLE_PLAYER,BattleCampConstant.BATTLE_CAMP_OWN,roleId,roleLevel,modelId,roleAttributes);

        maxHitTimesStun = basicModel.getHitTimesStun();
        curHitTimes = 0;
        maxStunTime = basicModel.getStunTime()/10;
        curStunTime = 0;

        attributeManager.radius = 0.5f;
    }

    @Override
    public void release() {
        super.release();

        nextRoadPoint[0] = 0;
        nextRoadPoint[1] = 0;
        nextRoadPoint[2] = 0;
        nextRoadPoint[3] = -1;

        totalLootInfoMap.clear();

        soulId = 0;
    }

    public void addLootInfo(BattleRoleLootInfo lootInfo){
        for(LootDetailInfo detailInfo : lootInfo.getLootDetails()){
            long key = (((long)detailInfo.getDetailType()) << 32 & 0xffffffff00000000L | detailInfo.getDetailId());
            ItemRs itemRs = totalLootInfoMap.get(key);
            if(itemRs == null){
                itemRs = new ItemRs();
                itemRs.setItemType(detailInfo.getDetailType());
                itemRs.setItemId(detailInfo.getDetailId());
                itemRs.setItemNum(0);
                totalLootInfoMap.put(key,itemRs);
            }
            itemRs.setItemNum(itemRs.getItemNum()+detailInfo.getNum());
        }
    }

    public List<ItemRs> getTotalLootInfoList(){
        return new ArrayList<ItemRs>(totalLootInfoMap.values());

    }

    public boolean update(int frameNum){
        this.frameNum = frameNum;

        //0.(1)查看自己血量，如果为0判断死亡
        if(attributeManager.curBlood <= 0){
            logger.info("player dead !!!");
            battleContainer.finishBattle(BattleContainer.BATTLE_FINISH_TYPE_NORMAL,false,10);
            runFinish = true;
            return true;
        }

        //1.刷新技能cd,技能资源和角色资源等信息
        if(!super.update(frameNum)){
           return true;
        }

        //处理药水的使用，与角色状态无关
        WaitUsePotionData waitUsePotionData = battleContainer.waitHandlerDataManager.getFirstUsePotion(playerId);
        if(waitUsePotionData != null){
            skillManager.potionReleaseSkill(waitUsePotionData.potionId,waitUsePotionData.ignoreCdTime);
        }

        //3.优先处理玩家的手动操作
        WaitReleaseSkillData waitReleaseSkillData = battleContainer.waitHandlerDataManager.getFirstReleaseSkill(playerId);
        //自动技能没有被buff禁用
        if(buffManager.canReleaseRoleSkill()){
            if(waitReleaseSkillData != null){
                skillManager.manualReleaseSkill(waitReleaseSkillData,false);
            }
        }

        //释放灵魂之影技能
        if(stateManager.getPurpos()!=RolePurposConstant.ROLE_PURPOS_FIND_OBJECT&&stateManager.getPurpos()!=RolePurposConstant.ROLE_PURPOS_FIND_POSITION&&stateManager.getPurpos()!=RolePurposConstant.ROLE_PURPOS_FIND_EXIT){
            skillManager.soulReleaseSkill(frameNum,false);
        }else if(skillManager.soulBornSkill!=null){
            skillManager.soulReleaseSkill(frameNum,false);
        }

        return battleContainer.battleLogic.playerRunLogic(frameNum,this);

    }

    /**
     * 删除路点
     * @param index
     */
    public void removeNextPosition(int index){
        if((int)(nextRoadPoint[3]) == index){
            nextRoadPoint[3] = -1;
        }
        mapLogic.removePlayerNextRoadPointByIndex(index);
    }

    /**
     * 用于技巧ai
     * @param x
     * @param z
     */
    public void insertNextPosition(float x,float z){

        nextRoadPoint[0] = x;
        nextRoadPoint[1] = 0;
        nextRoadPoint[2] = z;
        nextRoadPoint[3] = 0;

        setPathTargetPosition(nextRoadPoint);

    }

    /**
     * 判断是否到达目标
     * @return
     */
    public boolean isArriveTarget(){
        byte purpos = stateManager.getPurpos();
        if(purpos == RolePurposConstant.ROLE_PURPOS_FIND_OBJECT){
            float distance = (float)Math.sqrt(Math.pow(currentPosition[0]-targetRole.getCurrentPosition()[0],2)+Math.pow(currentPosition[2]-targetRole.getCurrentPosition()[2],2));

            System.out.println("attributeManager.maxAttackDistance = "+attributeManager.maxAttackDistance+" distance = "+distance);

            if(distance <= attributeManager.maxAttackDistance*0.9f){
                if(distance <= attributeManager.minAttackDistance*0.9f || skillManager.hasCanReleaseAutoSkill(distance)){
                    return true;
                }
                return false;
            }else{
                return false;
            }
        }

        return false;
    }

    /**
     * 判断战斗时是否需要再次靠近目标
     * @return
     */
    public boolean needArriveTarget(){
        byte purpos = stateManager.getPurpos();
        if(purpos == RolePurposConstant.ROLE_PURPOS_FIGHT){
            float distance = (float)Math.sqrt(Math.pow(currentPosition[0]-targetRole.getCurrentPosition()[0],2)+Math.pow(currentPosition[2]-targetRole.getCurrentPosition()[2],2));
            if(distance >= attributeManager.minAttackDistance*0.9f){
                if(skillManager.hasCanReleaseAutoSkill(distance)){
                    return false;
                }
                return true;
            }else {
                return false;
            }
        }
        return false;
    }

}
