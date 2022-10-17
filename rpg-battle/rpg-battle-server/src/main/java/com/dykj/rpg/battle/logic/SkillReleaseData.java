package com.dykj.rpg.battle.logic;

import com.dykj.rpg.battle.basic.BasicRoleSkill;
import com.dykj.rpg.battle.basic.BasicSkillCarrier;
import com.dykj.rpg.battle.role.BattleRole;
import com.dykj.rpg.protocol.battle.BattleSkillCarrierInfo;

import java.util.ArrayList;
import java.util.List;

public class SkillReleaseData extends BattleObject{

    /**
     * 普通技能
     */
    public static final byte SKILL_RESOURCE_TYPE_BASIC = 1;
    /**
     * buff[包括精通] 产生的技能
     */
    public static final byte SKILL_RESOURCE_TYPE_BUFF = 2;

    public BattleContainer battleContainer;

    public MapLogic mapLogic;

    public BasicRoleSkill roleSkill;

    public int skillGuid;

    public byte skillResourceType;

    public List<Integer> hitRoleModelIds;

    public List<BasicSkillCarrier> skillCarriers;

    public BattleRole hostRole;

    public BattleRole targetRole;


    public SkillReleaseData(){
        hitRoleModelIds = new ArrayList<>();
        skillCarriers = new ArrayList<>();
    }

    public void init(BattleContainer battleContainer,MapLogic mapLogic,BasicRoleSkill roleSkill,BattleRole targetRole,byte skillResourceType,int skillGuid){
        this.battleContainer = battleContainer;
        this.mapLogic = mapLogic;
        this.roleSkill = roleSkill;
        this.hostRole = roleSkill.hostRole;
        this.targetRole = targetRole;
        this.skillResourceType = skillResourceType;
        this.skillGuid = skillGuid;
    }

    @Override
    public void release() {
        this.mapLogic = null;
        this.roleSkill = null;
        this.skillGuid = 0;

        hitRoleModelIds.clear();

        for(BasicSkillCarrier skillCarrier : skillCarriers){
            battleContainer.battlePoolManager.restoreSkillCarrier(skillCarrier);
        }
        skillCarriers.clear();

        hostRole = null;
        targetRole = null;
    }

    @Override
    public void selfCheak() {

    }

    public BasicRoleSkill getRoleSkill(){
        return roleSkill;
    }

    public void addNewCarrier(BasicSkillCarrier skillCarrier){

        //System.out.println("add carrier carrierId = "+skillCarrier.getCarrierId()+"  modelId = "+skillCarrier.getModelId());

        skillCarriers.add(skillCarrier);
    }

    public void removeDestroyCarrier(BasicSkillCarrier skillCarrier){

        //System.out.println("remove carrier modelId = "+skillCarrier.getModelId());

        battleContainer.battlePoolManager.restoreSkillCarrier(skillCarrier);

        skillCarriers.remove(skillCarrier);
    }

    public void removeCarrierBattleRole(BattleRole battleRole){
        System.out.println("removeCarrierBattleRole skillCarriers.size = "+skillCarriers.size());
        for(BasicSkillCarrier skillCarrier : skillCarriers){
            System.out.println("removeCarrierBattleRole scan skillCarrier "+skillCarrier.modelId);
            if(skillCarrier.triggerRole == battleRole){
                skillCarrier.triggerRole = null;
                System.out.println("removeCarrierBattleRole triggerRole !!!");
            }
            if(skillCarrier.hostRole == battleRole){
                skillCarrier.hostRole = null;
                System.out.println("removeCarrierBattleRole hostRole !!!");
            }
            if(skillCarrier.targetRole == battleRole){
                skillCarrier.targetRole = null;
                System.out.println("removeCarrierBattleRole targetRole !!!");
            }
        }
    }

    public boolean addHitRoleModelId(int modelId){

        if(skillResourceType == SKILL_RESOURCE_TYPE_BUFF){
            return false;
        }

        if(hitRoleModelIds.contains(modelId)){
            return false;
        }
        hitRoleModelIds.add(modelId);
        return true;
    }

    public int getCarrierSize(){
        return skillCarriers.size();
    }

    public void update(int frameNum){
        if(skillCarriers.size() > 0){
            int size = skillCarriers.size();
            BasicSkillCarrier skillCarrier;
            for(int i=0;i<size;){
                skillCarrier = skillCarriers.get(i);
                if(skillCarrier.getState() == BasicSkillCarrier.CARRIER_STATE_DESTROY){
                    removeDestroyCarrier(skillCarrier);
                    size--;
                }else{
                    skillCarrier.update(frameNum);
                    i++;
                }
            }
        }
    }

    public void getSyncCarrier(List<BattleSkillCarrierInfo> skillCarrierList){
        if(skillCarriers.size() > 0){
            for(BasicSkillCarrier carrier : skillCarriers){
                BattleSkillCarrierInfo skillCarrier = carrier.getSyncData();
                if(skillCarrier != null){
                    skillCarrierList.add(skillCarrier);
                }
            }
        }
    }

}
