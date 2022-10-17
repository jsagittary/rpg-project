package com.dykj.rpg.battle.role;

/**
 * 小怪行为和数据处理类
 */

import com.dykj.rpg.battle.attribute.RoleAttributes;
import com.dykj.rpg.battle.config.TileNpcDetail;
import com.dykj.rpg.battle.constant.*;
import com.dykj.rpg.battle.logic.BattleContainer;
import com.dykj.rpg.common.attribute.consts.AttributeBasicConstant;
import com.dykj.rpg.common.config.model.NpcAiModel;
import com.dykj.rpg.common.config.model.NpcBasicModel;

import java.util.List;

public class BattleMonster extends BattleRole{

    public NpcBasicModel npcBasicModel;

    public NpcAiModel npcAiModel;

    public int bornTime = 2000;

    public BattleMonster(){
    }

    public void init(BattleContainer battleContainer, TileNpcDetail tileNpcDetail, int skillSourceType,int roleLevel,int modelId, NpcBasicModel basicModel, NpcAiModel aiModel, RoleAttributes roleAttributes){
        super.init(battleContainer ,tileNpcDetail,0,skillSourceType,RoleTypeConstant.BATTLE_ROLE_MONSTER,BattleCampConstant.BATTLE_CAMP_ENEMY,tileNpcDetail.npcId,roleLevel,modelId,roleAttributes);

        this.npcBasicModel = basicModel;
        this.npcAiModel = aiModel;

        maxHitTimesStun = npcBasicModel.getHitTimesStun();
        curHitTimes = 0;
        maxStunTime = npcBasicModel.getStunTime()/10;
        curStunTime = 0;

        //怪物出生时间
        bornTime = npcBasicModel.getBornDuration()/10;
        //视野范围
        buffManager.addRoleAttribute(AttributeBasicConstant.SHI_YE_FAN_WEI_ZHI,0,(int)(npcAiModel.getAiViewRange()*100));
        //跑动速度
        buffManager.addRoleAttribute(AttributeBasicConstant.YI_DONG_SU_DU_PAO_ZHI,0,(int) (npcAiModel.getRunSpeed()*10000));

        attributeManager.radius = 0.35f;

        List<Integer> skillIds = npcBasicModel.getNpcSkill();

        if(skillIds != null && skillIds.size() > 0){
            for(int skillId : skillIds){
                skillManager.installRoleSkill(skillId,0);
            }
        }
        skillManager.printRoleSkills();

        setBornState(bornTime);
    }


    @Override
    public void release() {
        super.release();
    }

    private int testNum = 0;

    public boolean update(int frameNum){

        this.frameNum = frameNum;

        //1.刷新技能cd,技能资源和角色资源等信息
        if(!super.update(frameNum)){
            return true;
        }

        return battleContainer.battleLogic.monsterRunLogic(frameNum,this);

    }

    public boolean isArriveTarget(){
        byte purpos = stateManager.getPurpos();
        if(purpos == RolePurposConstant.ROLE_PURPOS_FIND_OBJECT){
            float distance = (float)Math.sqrt(Math.pow(currentPosition[0]-targetRole.getCurrentPosition()[0],2)+Math.pow(currentPosition[2]-targetRole.getCurrentPosition()[2],2));
            //float attackDistance = 2.5f;
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
