package com.dykj.rpg.battle.manager;

import com.dykj.rpg.battle.ai.AiTrigger;
import com.dykj.rpg.battle.ai.BasicAiLogic;
import com.dykj.rpg.battle.basic.BasicSkillBuff;
import com.dykj.rpg.battle.constant.RoleTypeConstant;
import com.dykj.rpg.battle.dictionary.StaticDictionary;
import com.dykj.rpg.battle.logic.BattleContainer;
import com.dykj.rpg.battle.role.BattleRole;
import com.dykj.rpg.common.config.model.AiCharacterBasicModel;
import com.dykj.rpg.net.protocol.ProtocolPool;
import com.dykj.rpg.protocol.battle.BattleAiInfo;
import com.dykj.rpg.protocol.battle.BattleSkillBuffInfo;

import java.util.*;

public class AiManager {

    /**
     * -----------------------------ai状态-------------------------------
     */
    /**
     * ai普通状态
     */
    public static final byte AI_STATE_NORMAL = 0;
    /**
     * ai同步状态
     */
    public static final byte AI_STATE_SYNC = 1;

    private BattleRole battleRole;

    private BattleContainer battleContainer;

    /**
     * 技巧集合
     */
    List<BasicAiLogic> allTimeAiLogics = new ArrayList<>();

    /**
     * 技巧集合
     */
    List<BasicAiLogic> soulReleaseSkillAiLogics = new ArrayList<>();

    public AiManager(BattleRole battleRole){
        this.battleRole = battleRole;

    }

    public void init(BattleContainer battleContainer){
        this.battleContainer = battleContainer;
    }

    public void release(){
        int size = allTimeAiLogics.size();
        while (size > 0) {
            //battleContainer.battlePoolManager.restoreRoleSkill(aiLogics.get(0));
            size--;
        }
        allTimeAiLogics.clear();

        size = soulReleaseSkillAiLogics.size();
        while(size > 0){
            size--;
        }
        soulReleaseSkillAiLogics.clear();
    }

    /**
     * 更新技巧CD
     * @param frameNum
     */
    public void update(int frameNum){
        int size = allTimeAiLogics.size();
        for(int i=0;i<size;i++){
            allTimeAiLogics.get(i).update();
        }

        size = soulReleaseSkillAiLogics.size();
        for(int i=0;i<size;i++){
            soulReleaseSkillAiLogics.get(i).update();
        }
    }


    /**
     * 安装技巧
     */
    public void installAi(int aiId,List<Integer> conditionParams,List<Integer> actionParams){

        AiCharacterBasicModel model = StaticDictionary.getInstance().getAiCharacterBasicModel(aiId);
        if(model != null){
            if(conditionParams.size() == 0){
                conditionParams = model.getAiConditionParams();
            }
            if(actionParams.size() == 0){
                actionParams = model.getAiActionParams();
            }
            int triggerType = model.getAiTrigger();
            int conditionId = model.getAiCondition();
            int actionId = model.getAiAction();
            int cdTime = model.getAiCd();

            BasicAiLogic aiLogic = new BasicAiLogic();
            aiLogic.init(battleContainer,battleRole,aiId,triggerType,conditionId,conditionParams,actionId,actionParams,cdTime);

            if(triggerType == AiTrigger.AI_TRIGGER_ALL_TIME){
                allTimeAiLogics.add(aiLogic);
            }

            if(triggerType == AiTrigger.AI_TRIGGER_SOUL_RELEASE_SKILL){
                soulReleaseSkillAiLogics.add(aiLogic);
            }
        }
    }

    /**
     * 安装技巧(怪物和本地测试使用)
     */
    public void installAi(int aiId){

        AiCharacterBasicModel model = StaticDictionary.getInstance().getAiCharacterBasicModel(aiId);
        if(model != null){
            int triggerType = model.getAiTrigger();
            int conditionId = model.getAiCondition();
            List<Integer> conditionParams = model.getAiConditionParams();
            int actionId = model.getAiAction();
            List<Integer> actionParams = model.getAiActionParams();
            int cdTime = model.getAiCd();

            BasicAiLogic aiLogic = new BasicAiLogic();
            aiLogic.init(battleContainer,battleRole,aiId,triggerType,conditionId,conditionParams,actionId,actionParams,cdTime);

            if(triggerType == AiTrigger.AI_TRIGGER_ALL_TIME){
                allTimeAiLogics.add(aiLogic);
            }

            if(triggerType == AiTrigger.AI_TRIGGER_SOUL_RELEASE_SKILL){
                soulReleaseSkillAiLogics.add(aiLogic);
            }
        }
    }

    /**
     * 技巧释放条件判断
     * @param triggerType
     */
    public void matchAi(int triggerType){

        if(triggerType == AiTrigger.AI_TRIGGER_ALL_TIME){
            int size = allTimeAiLogics.size();

            for(int i=0;i<size;i++){
                useAiLogic(allTimeAiLogics.get(i));
            }
        }

        if(triggerType == AiTrigger.AI_TRIGGER_SOUL_RELEASE_SKILL){
            int size = soulReleaseSkillAiLogics.size();

            for(int i=0;i<size;i++){
                useAiLogic(soulReleaseSkillAiLogics.get(i));
            }
        }

    }

    private void useAiLogic(BasicAiLogic aiLogic){
        if(aiLogic.trigger()){
            if(aiLogic.matchCondition()){
                if(aiLogic.action()){
                    aiLogic.curCdTime = aiLogic.maxCdTime;
                    aiLogic.state = AI_STATE_SYNC;
                }
            }
        }
    }

    public void sendBattleAiInfoForClient(List<BattleAiInfo> aiInfos){
        for (BasicAiLogic aiLogic : allTimeAiLogics) {
            addAiInfoToList(aiInfos,aiLogic);
        }

        for (BasicAiLogic aiLogic : soulReleaseSkillAiLogics) {
            addAiInfoToList(aiInfos,aiLogic);
        }
    }

    private void addAiInfoToList(List<BattleAiInfo> aiInfos,BasicAiLogic aiLogic){
        BattleAiInfo aiInfo = (BattleAiInfo) ProtocolPool.getInstance().borrowProtocol(BattleAiInfo.code);
        aiInfo.setAiId(aiLogic.aiId);
        aiInfo.setCdTime(aiLogic.curCdTime);

        aiInfos.add(aiInfo);
    }

    /**
     * 客户端更改的AI信息
     * @return
     */
    public void getSyncAiInfo(List<BattleAiInfo> aiInfoList){
        for (BasicAiLogic aiLogic : allTimeAiLogics) {
            if(aiLogic.state == AI_STATE_SYNC){
                aiLogic.state = AI_STATE_NORMAL;
                addAiInfoToList(aiInfoList,aiLogic);
            }
        }

        for (BasicAiLogic aiLogic : soulReleaseSkillAiLogics) {
            if(aiLogic.state == AI_STATE_SYNC){
                aiLogic.state = AI_STATE_NORMAL;
                addAiInfoToList(aiInfoList,aiLogic);
            }
        }
    }

}
