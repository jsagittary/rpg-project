package com.dykj.rpg.battle.manager;

import com.dykj.rpg.battle.ai.AiTrigger;
import com.dykj.rpg.battle.attribute.SkillAttributes;
import com.dykj.rpg.battle.basic.BasicRoleSkill;
import com.dykj.rpg.battle.constant.GameConstant;
import com.dykj.rpg.battle.constant.RoleTypeConstant;
import com.dykj.rpg.battle.data.WaitReleaseSkillData;
import com.dykj.rpg.battle.dictionary.StaticDictionary;
import com.dykj.rpg.battle.logic.BattleContainer;
import com.dykj.rpg.battle.logic.MapLogic;
import com.dykj.rpg.battle.role.BattleRole;
import com.dykj.rpg.battle.util.CalculationUtil;
import com.dykj.rpg.common.config.model.ItemModel;
import com.dykj.rpg.common.config.model.SkillCharacterBasicModel;
import com.dykj.rpg.common.consts.ItemTypeEnum;
import com.dykj.rpg.net.protocol.ProtocolPool;
import com.dykj.rpg.protocol.battle.*;
import com.dykj.rpg.protocol.game2battle.EnterBattleSkillInfo;
import com.dykj.rpg.protocol.item.ItemRs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SkillManager {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private BattleContainer battleContainer;

    private BattleRole battleRole;

    /**
     * 自动技能 包括核心槽技能
     */
    private List<BasicRoleSkill> autoSkills = new ArrayList<>();

    /**
     * 主动技能
     */
    private List<BasicRoleSkill> manualSkills = new ArrayList<>();

    /**
     * 药水技能
     */
    private List<BasicRoleSkill> potionSkills = new ArrayList<>();

    /**
     * 元素精通技能
     */
    private List<BasicRoleSkill> elementSkills = new ArrayList<>();

    /**
     * 灵魂之影技能
     */
    private List<BasicRoleSkill> soulSkills = new ArrayList<>();

    public BasicRoleSkill soulBornSkill;

    private BattleRole soulTargetRole;

    public int soulSkillId = 0;

    private int soulBornTime = 0;

    private int soulCdTime = 0;

    public SkillManager(BattleRole battleRole) {
        this.battleRole = battleRole;
    }

    public void init(BattleContainer battleContainer) {
        this.battleContainer = battleContainer;
        //installElementSkills();
    }

    public void release() {
        int size = autoSkills.size();
        while (size > 0) {
            battleContainer.battlePoolManager.restoreRoleSkill(autoSkills.get(0));
            size--;
        }
        autoSkills.clear();

        size = manualSkills.size();
        while (size > 0) {
            battleContainer.battlePoolManager.restoreRoleSkill(manualSkills.get(0));
            size--;
        }
        manualSkills.clear();

        size = potionSkills.size();
        while (size > 0) {
            battleContainer.battlePoolManager.restoreRoleSkill(potionSkills.get(0));
            size--;
        }
        potionSkills.clear();

        size = elementSkills.size();
        while (size > 0) {
            battleContainer.battlePoolManager.restoreRoleSkill(elementSkills.get(0));
            size--;
        }
        elementSkills.clear();

        size = soulSkills.size();
        while (size > 0) {
            battleContainer.battlePoolManager.restoreRoleSkill(soulSkills.get(0));
            size--;
        }
        soulSkills.clear();

        this.battleContainer = null;

        this.soulBornSkill = null;
        this.soulBornTime = 0;
        this.soulCdTime = 0;
    }

    /**
     * 自动装配元素精通技能
     */
    public void installElementSkills() {

        List<SkillCharacterBasicModel> list = StaticDictionary.getInstance().getSkillCharacterBasicModelsByReleaseType(BasicRoleSkill.SKILL_RELEASE_TYPE_ELEMENT);
        if (list != null && list.size() > 0) {
            for (SkillCharacterBasicModel model : list) {

                SkillAttributes skillAttributes = new SkillAttributes(model.getSkillId(), null);

                BasicRoleSkill skill = battleContainer.battlePoolManager.borrowRoleSkill();
                skill.init(battleContainer, battleRole, skillAttributes, 0);
                skill.setMaxElementReleaseNum(3);

//                ElementRoleSkill skill = new ElementRoleSkill(battleRole,skillAttributes,0);
//                skill.init(3);
                elementSkills.add(skill);
            }
        }
    }

    /**
     * 装配养成技能 （主要用于主角装配技能）
     *
     * @param skillInfo
     */
    public BasicRoleSkill installRoleSkill(EnterBattleSkillInfo skillInfo) {

        SkillAttributes skillAttributes = new SkillAttributes(skillInfo.getSkillId(), skillInfo.getAttributeInfos());

        SkillCharacterBasicModel skillBasicModel = skillAttributes.getDevelopSkillBasic();

        //灵魂之影技能
        if (skillInfo.getSkillPosition() >= 5) {

            BasicRoleSkill skill = battleContainer.battlePoolManager.borrowRoleSkill();
            skill.init(battleContainer, battleRole, skillAttributes, skillInfo.getSkillPosition());

            soulSkills.add(skill);
            //给灵魂之影按照位置顺序排序
            if (soulSkills.size() > 1) {
                Collections.sort(soulSkills, Comparator.comparingInt(BasicRoleSkill::getSkillSeat));
            }
            return skill;

        }

        //自动释放的技能
        if (skillInfo.getSkillPosition() == 1 || skillBasicModel.getSkillType() == BasicRoleSkill.SKILL_RELEASE_TYPE_AUTO) {

            BasicRoleSkill skill = battleContainer.battlePoolManager.borrowRoleSkill();
            skill.init(battleContainer, battleRole, skillAttributes, skillInfo.getSkillPosition());

            //AutoRoleSkill skill = new AutoRoleSkill(battleRole,skillAttributes,skillInfo.getSkillPosition());
            if (battleRole.attributeManager.maxAttackDistance == 0 || battleRole.attributeManager.maxAttackDistance < skill.maxDistance) {
                battleRole.attributeManager.maxAttackDistance = skill.maxDistance;
            }
            if (battleRole.attributeManager.minAttackDistance == 0 || battleRole.attributeManager.minAttackDistance > skill.maxDistance) {
                battleRole.attributeManager.minAttackDistance = skill.maxDistance;
            }
            autoSkills.add(skill);
            return skill;
        }

        if (skillInfo.getSkillPosition() != 1 && skillBasicModel.getSkillType() == BasicRoleSkill.SKILL_RELEASE_TYPE_MANUAL) {
            BasicRoleSkill skill = battleContainer.battlePoolManager.borrowRoleSkill();
            skill.init(battleContainer, battleRole, skillAttributes, skillInfo.getSkillPosition());
            //ManualRoleSkill skill = new ManualRoleSkill(battleRole,skillAttributes,skillInfo.getSkillPosition());
            manualSkills.add(skill);
            return skill;
        }

//        if(skill.getSkillType() == BasicRoleSkill.SKILL_RELEASE_TYPE_POTION){
//            potionSkills.add(skill);
//        }
        return null;

    }

    /**
     * 装配基础技能 (主要用于npc和主角测试)
     *
     * @param skillId
     */
    public BasicRoleSkill installRoleSkill(int skillId, int skillSeat) {

        SkillAttributes skillAttributes = new SkillAttributes(skillId, null);

        SkillCharacterBasicModel skillBasicModel = skillAttributes.getDevelopSkillBasic();

        //灵魂之影技能
        if (skillSeat >= 5) {

            BasicRoleSkill skill = battleContainer.battlePoolManager.borrowRoleSkill();
            skill.init(battleContainer, battleRole, skillAttributes, skillSeat);

            soulSkills.add(skill);

            //skill.maxCdTime = 3000;

            return skill;

        }

        //自动释放的技能
        if (skillSeat == 1 || skillBasicModel.getSkillType() == BasicRoleSkill.SKILL_RELEASE_TYPE_AUTO) {

            BasicRoleSkill skill = battleContainer.battlePoolManager.borrowRoleSkill();
            skill.init(battleContainer, battleRole, skillAttributes, skillSeat);

            if (battleRole.attributeManager.maxAttackDistance == 0 || battleRole.attributeManager.maxAttackDistance < skill.maxDistance) {
                battleRole.attributeManager.maxAttackDistance = skill.maxDistance;
            }
            if (battleRole.attributeManager.minAttackDistance == 0 || battleRole.attributeManager.minAttackDistance > skill.maxDistance) {
                battleRole.attributeManager.minAttackDistance = skill.maxDistance;
            }

            if(battleRole.getRoleType() == RoleTypeConstant.BATTLE_ROLE_PLAYER){
                skill.maxCdTime = 1000;
            }

            autoSkills.add(skill);
            return skill;
        }

        if (skillBasicModel.getSkillType() == BasicRoleSkill.SKILL_RELEASE_TYPE_MANUAL) {

            BasicRoleSkill skill = battleContainer.battlePoolManager.borrowRoleSkill();
            skill.init(battleContainer, battleRole, skillAttributes, skillSeat);

            if (battleRole.attributeManager.maxAttackDistance == 0 || battleRole.attributeManager.maxAttackDistance < skill.maxDistance) {
                battleRole.attributeManager.maxAttackDistance = skill.maxDistance;
            }
            if (battleRole.attributeManager.minAttackDistance == 0 || battleRole.attributeManager.minAttackDistance > skill.maxDistance) {
                battleRole.attributeManager.minAttackDistance = skill.maxDistance;
            }

            skill.maxCdTime = 1000;
            //ManualRoleSkill skill = new ManualRoleSkill(battleRole,skillAttributes,skillSeat);
            manualSkills.add(skill);
            return skill;
        }

        return null;

    }

    /**
     * 装配药水技能 （如 生命药水）
     *
     * @param itemRs
     */
    public void installPotionSkill(ItemRs itemRs) {
        ItemModel itemModel = StaticDictionary.getInstance().getItemModelById(itemRs.getItemId());
        if (itemModel != null && itemModel.getItemType() == ItemTypeEnum.CONSUMABLES.getItemType()) {//战斗药水
            try {
                int skillId = Integer.parseInt(itemModel.getItemUseResult());
                SkillAttributes skillAttributes = new SkillAttributes(skillId, null);

                BasicRoleSkill skill = battleContainer.battlePoolManager.borrowRoleSkill();
                skill.init(battleContainer, battleRole, skillAttributes, 0);
                skill.setPotionInfo(itemRs);

                potionSkills.add(skill);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 装配药水技能 （主要用于npc和主角测试）
     * @param itemId
     * @param itemNum
     */
    public void installPotionSkill(int itemId,int itemNum) {
        ItemModel itemModel = StaticDictionary.getInstance().getItemModelById(itemId);
        if (itemModel != null && itemModel.getItemType() == ItemTypeEnum.CONSUMABLES.getItemType()) {//战斗药水
            try {
                int skillId = Integer.parseInt(itemModel.getItemUseResult());
                SkillAttributes skillAttributes = new SkillAttributes(skillId, null);

                BasicRoleSkill skill = battleContainer.battlePoolManager.borrowRoleSkill();
                skill.init(battleContainer, battleRole, skillAttributes, 0);
                skill.setPotionInfo(itemId,itemNum);

                potionSkills.add(skill);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void update(int frameNum) {
        /**
         * 技能cd时间刷新
         */
        for (BasicRoleSkill roleSkill : autoSkills) {
            roleSkill.update(frameNum);
        }

        for (BasicRoleSkill roleSkill : manualSkills) {
            roleSkill.update(frameNum);
        }

        for (BasicRoleSkill roleSkill : soulSkills) {
            roleSkill.update(frameNum);
        }

        for (BasicRoleSkill roleSkill : potionSkills) {
            roleSkill.update(frameNum);
        }


    }

    public BasicRoleSkill getRoleSkillBySkillId(int skillId) {
        for (BasicRoleSkill roleSkill : autoSkills) {
            if (roleSkill.getSkillId() == skillId) {
                return roleSkill;
            }
        }
        for (BasicRoleSkill roleSkill : manualSkills) {
            if (roleSkill.getSkillId() == skillId) {
                return roleSkill;
            }
        }
        for (BasicRoleSkill roleSkill : elementSkills) {
            if (roleSkill.getSkillId() == skillId) {
                return roleSkill;
            }
        }
        for (BasicRoleSkill roleSkill : soulSkills) {
            if (roleSkill.getSkillId() == skillId) {
                return roleSkill;
            }
        }

        return null;
    }

    /**
     * 获取客户端需要显示的基础技能
     *
     * @return
     */
    public void sendBasicSkillForClient(List<PlayerSkillInfo> skillInfos) {
        for (BasicRoleSkill roleSkill : autoSkills) {
            if (roleSkill.getSkillSeat() > 0) {
                addSkillInfoToList(skillInfos,roleSkill);
            }
        }
        for (BasicRoleSkill roleSkill : manualSkills) {
            if (roleSkill.getSkillSeat() > 0) {
                addSkillInfoToList(skillInfos,roleSkill);
            }
        }
        for (BasicRoleSkill roleSkill : soulSkills) {
            if (roleSkill.getSkillSeat() > 0) {
                addSkillInfoToList(skillInfos,roleSkill);
            }
        }
    }

    private void addSkillInfoToList(List<PlayerSkillInfo> skillInfos,BasicRoleSkill roleSkill){
        PlayerSkillInfo skillInfo = (PlayerSkillInfo)ProtocolPool.getInstance().borrowProtocol(PlayerSkillInfo.code);
        skillInfo.setId(roleSkill.getSkillId());
        skillInfo.setSeat((byte)roleSkill.getSkillSeat());
        skillInfo.setCdTime(roleSkill.getMaxCdTime());

        skillInfos.add(skillInfo);
    }

    /**
     * 获取客户端需要显示的药水
     *
     */
    public void sendPotionInfoForClient(List<BattlePotionInfo> potionInfos) {
        for (BasicRoleSkill roleSkill : potionSkills) {
            BattlePotionInfo potionInfo = (BattlePotionInfo)ProtocolPool.getInstance().borrowProtocol(BattlePotionInfo.code);
            potionInfo.setPotionId(roleSkill.potionId);
            potionInfo.setNum(roleSkill.potionNum);
            potionInfo.setCdTime(roleSkill.maxCdTime);
            potionInfos.add(potionInfo);
        }
    }

    /**
     * 获取游戏服需要的药水
     *
     */
    public void sendPotionInfoForGameServer(List<ItemRs> itemRsList) {
        for (BasicRoleSkill roleSkill : potionSkills) {
            ItemRs itemRs = (ItemRs)ProtocolPool.getInstance().borrowProtocol(ItemRs.code);
            itemRs.setItemType(ItemTypeEnum.CONSUMABLES.getItemType());
            itemRs.setItemId(roleSkill.potionId);
            itemRs.setItemNum(roleSkill.potionNum-roleSkill.initPotionNum);

            itemRsList.add(itemRs);
        }
    }
    //为技能更新mapLogic，由于载体交给了mapLogic管理，载体的实时更新在mapLogic中进行
    public void setMapLogic(MapLogic mapLogic) {
        for (BasicRoleSkill roleSkill : autoSkills) {
            roleSkill.setMapLogic(mapLogic);
        }
        for (BasicRoleSkill roleSkill : manualSkills) {
            roleSkill.setMapLogic(mapLogic);
        }
        for (BasicRoleSkill roleSkill : elementSkills) {
            roleSkill.setMapLogic(mapLogic);
        }
        for (BasicRoleSkill roleSkill : soulSkills) {
            roleSkill.setMapLogic(mapLogic);
        }
        for (BasicRoleSkill roleSkill : potionSkills) {
            roleSkill.setMapLogic(mapLogic);
        }
    }

    /**
     * 释放自动技能
     */
    public void autoReleaseSkill(boolean ignoreCdTime) {
        for (BasicRoleSkill roleSkill : autoSkills) {
            //返回true为自动释放技能成功
            if (roleSkill.releaseSkill(roleSkill.hostRole.targetRole,null,ignoreCdTime)) {
                battleRole.setStillState(Math.max(roleSkill.getMaxSingTime(), roleSkill.getAnimatorTime()));

                if (battleRole.getRoleType() == RoleTypeConstant.BATTLE_ROLE_PLAYER) {
                    PlayerSkillInfo skillInfo = new PlayerSkillInfo();
                    skillInfo.setId(roleSkill.getSkillId());
                    skillInfo.setCdTime(roleSkill.getCdTime());
                    battleRole.sendBattleData(skillInfo);
                }

                //增加魂能量值
                battleRole.attributeManager.addSoulEnergy(roleSkill.createSoulEnergy);

                break;
            }

        }
    }

    public void breakAutoReleaseSkill() {
        for (BasicRoleSkill roleSkill : autoSkills) {
            roleSkill.breakSkill();
        }
    }

    /**
     * 释放灵魂之影技能
     */
    public void soulReleaseSkill(int frameNum,boolean ignoreCdTime) {
        if (soulBornTime > 0) {
            soulBornTime -= GameConstant.FRAME_TIME;
        }

        if (soulBornTime > 0) {
            return;
        }

        if (soulBornTime <= 0) {
            if (soulBornSkill != null && soulBornSkill.releaseSkill(soulTargetRole,null,ignoreCdTime)) {
                //动作时间延长一秒 客户端做收尾
                soulCdTime =soulBornSkill.getAnimatorTime()+1000;

                PlayerSkillInfo skillInfo = new PlayerSkillInfo();
                skillInfo.setId(soulBornSkill.getSkillId());
                skillInfo.setCdTime(soulBornSkill.maxCdTime);
                battleRole.sendBattleData(skillInfo);

                BattlePosition battlePosition = new BattlePosition();
                battlePosition.setPosX((int) Math.floor(soulBornSkill.soulRolePos[0] * 100));
                battlePosition.setPosZ((int) Math.floor(soulBornSkill.soulRolePos[2] * 100));
                battlePosition.setMapIndex(soulBornSkill.mapLogic.getMapIndex());
                battlePosition.setDirection((short) soulBornSkill.soulRolePos[3]);

                SoulRoleInfo soulRoleInfo = new SoulRoleInfo();
                soulRoleInfo.setFrameNum(frameNum);
                soulRoleInfo.setModelId(battleRole.getModelId());
                soulRoleInfo.setSkillId(soulBornSkill.getSkillId());
                soulRoleInfo.setSkillTime(soulBornSkill.getAnimatorTime());
                soulRoleInfo.setSoulPos(battlePosition);
                battleRole.sendBattleData(soulRoleInfo);
                //System.out.println(skillInfo);
                logger.info("灵魂之影技能释放 {} ",soulBornSkill.getSkillId());
                soulBornSkill.cdTime = soulBornSkill.maxCdTime;
                soulBornSkill = null;
                soulTargetRole = null;
                soulSkillId = 0;

                return;
            }
        }

        if (soulCdTime > 0) {
            soulCdTime -= GameConstant.FRAME_TIME;
        }

        if (soulCdTime > 0) {
            return;
        }

        if (soulCdTime <= 0) {
            int soulEnergy = battleRole.attributeManager.curSoulEnergy;
            for (BasicRoleSkill skill : soulSkills) {

                if (skill.cdTime <= 0 && skill.useSoulEnergy <= soulEnergy) {

                    soulSkillId = skill.skillId;
                    //先使用AI获取一次目标对象
                    battleRole.aiManager.matchAi(AiTrigger.AI_TRIGGER_SOUL_RELEASE_SKILL);

                    if(soulTargetRole == null){
                        soulTargetRole = getSoulTargetRole(skill);
                        if (soulTargetRole == null) {
                            soulSkillId = 0;
                            return;
                        }
                    }

                    //消耗魂能量值
                    battleRole.attributeManager.addSoulEnergy(-skill.useSoulEnergy);

                    float[] hostPos = skill.hostRole.currentPosition;
                    float[] targetPos = soulTargetRole.currentPosition;
                    float distance = (float) Math.sqrt(Math.pow(targetPos[0] - hostPos[0], 2) + Math.pow(targetPos[2] - hostPos[2], 2));
                    float[] soulPos = skill.hostRole.mapLogic.findSoulPosition(hostPos, targetPos, Math.min(skill.maxDistance, distance));
                    if (soulPos == null) {
                        break;
                    }
                    SkillCharacterBasicModel skillCharacterBasicModel =StaticDictionary.getInstance().getSkillCharacterBasicModelById(skill.skillId);
                    soulPosResult(soulPos, hostPos, skillCharacterBasicModel.getSoulDistance().get(1)/100f, skillCharacterBasicModel.getSoulDistance().get(0)/100f);
                    short direction = (short) CalculationUtil.vectorToAngle(targetPos[0] - hostPos[0], targetPos[2] - hostPos[2]);
                    skill.soulRolePos[0] = soulPos[0];
                    skill.soulRolePos[1] = soulPos[1];
                    skill.soulRolePos[2] = soulPos[2];
                    skill.soulRolePos[3] = direction;

                    BattlePosition battlePosition = new BattlePosition();
                    battlePosition.setPosX((int) Math.floor(soulPos[0] * 100));
                    battlePosition.setPosZ((int) Math.floor(soulPos[2] * 100));
                    battlePosition.setMapIndex(skill.mapLogic.getMapIndex());
                    battlePosition.setDirection(direction);

                    SoulRoleBorn soulRoleBron = new SoulRoleBorn();
                    soulRoleBron.setFrameNum(frameNum);
                    soulRoleBron.setModelId(battleRole.getModelId());
                    soulRoleBron.setSoulPos(battlePosition);
                    battleRole.sendBattleData(soulRoleBron);
                    logger.info("灵魂之影技能出生 {} ",skill.getSkillId());
                    //System.out.println(soulRoleBron);

                    soulBornTime = 200;
                    soulBornSkill = skill;

                    break;
                }
            }
        }
    }

    /**
     * 获取灵魂之影的目标对象
     */
    private BattleRole getSoulTargetRole(BasicRoleSkill skill){
        BattleRole targetRole;
        if(skill.hostRole.targetRole != null){
            return skill.hostRole.targetRole;
        }
        return skill.hostRole.mapLogic.findNearestEnemy();
    }

    /**
     * 外部设置灵魂之影的目标对象
     */
    public void setSoulTargetRole(BattleRole soulTargetRole) {
        this.soulTargetRole = soulTargetRole;
    }

    /**
     * 灵魂之影的位置 不能出现在主角位置之外
     *
     * @param soulPos
     * @param hostPos
     * @param
     */
    private void soulPosResult(float[] soulPos, float[] hostPos, float maxR, float minR) {
        float changeX = soulPos[0] - hostPos[0];
        float changeZ = soulPos[2] - hostPos[2];
        float angle = CalculationUtil.vectorToAngle(changeX, changeZ);
        float distance = (float) Math.sqrt(Math.pow(soulPos[0] - hostPos[0], 2) + Math.pow(soulPos[2] - hostPos[2], 2));
        if (distance > maxR) {
            soulPos[0] = (float) Math.cos(angle * Math.PI / 180) * maxR + hostPos[0];
            soulPos[2] = (float) Math.sin(angle * Math.PI / 180) * maxR + hostPos[2];
        }
        if (distance < minR) {
            soulPos[0] = (float) Math.cos(angle * Math.PI / 180) * minR + hostPos[0];
            soulPos[2] = (float) Math.sin(angle * Math.PI / 180) * minR + hostPos[2];
        }
    }

    public void manualReleaseSkill(WaitReleaseSkillData waitReleaseSkillData, boolean ignoreCdTime) {
        for (BasicRoleSkill roleSkill : manualSkills) {
            if (roleSkill.getSkillId() == waitReleaseSkillData.skillId) {
                if (roleSkill.releaseSkill(roleSkill.hostRole.targetRole,waitReleaseSkillData.pos,ignoreCdTime)) {
                    //等待最少一个技能动作时间
                    battleRole.setStillState(Math.max(roleSkill.getMaxSingTime(), roleSkill.getAnimatorTime()));

                    if (battleRole.getRoleType() == RoleTypeConstant.BATTLE_ROLE_PLAYER) {
                        PlayerSkillInfo skillInfo = (PlayerSkillInfo) ProtocolPool.getInstance().borrowProtocol(PlayerSkillInfo.code);
                        skillInfo.setId(roleSkill.getSkillId());
                        skillInfo.setCdTime(roleSkill.getCdTime());
                        battleRole.sendBattleData(skillInfo);

                    }

                    //增加魂能量值
                    battleRole.attributeManager.addSoulEnergy(roleSkill.createSoulEnergy);
                }
            }
        }
    }

    public void potionReleaseSkill(int potionId,boolean ignoreCdTime){
        for (BasicRoleSkill roleSkill : potionSkills) {
            if (roleSkill.potionId == potionId) {
                if (roleSkill.releaseSkill(roleSkill.hostRole.targetRole,null,ignoreCdTime)) {
                    //等待最少一个技能动作时间
                    battleRole.setStillState(Math.max(roleSkill.getMaxSingTime(), roleSkill.getAnimatorTime()));

                    BattlePotionInfo potionInfo = new BattlePotionInfo();
                    potionInfo.setPotionId(roleSkill.potionId);
                    potionInfo.setNum(roleSkill.potionNum);
                    potionInfo.setCdTime(roleSkill.getCdTime());
                    System.out.println(potionInfo.toString());
                    battleRole.sendBattleData(potionInfo);
                }
            }
        }
    }

    public BasicRoleSkill getPotionSkillByPotionId(int potionId){
        for(BasicRoleSkill roleSkill : potionSkills){
            if (roleSkill.potionId == potionId) {
                return roleSkill;
            }
        }
        return null;
    }

    public void elementReleaseSkill(int elementType,boolean ignoreCdTime) {
        for (BasicRoleSkill elementSkill : elementSkills) {
            if (elementSkill.elementType == elementType) {
                elementSkill.releaseSkill(elementSkill.hostRole.targetRole,null,ignoreCdTime);
            }
        }
    }

    public boolean hasCanReleaseAutoSkill(float distance) {
        for (BasicRoleSkill autoSkill : autoSkills) {
            if (autoSkill.cdTime <= 0 && autoSkill.minDistance <= distance && autoSkill.maxDistance >= distance) {
                return true;
            }
        }
        return false;
    }

    public void printRoleSkills(){
        StringBuffer sb = new StringBuffer();
        sb.append("BattleRole [modelId="+battleRole.getModelId()+",skills=[");
        for(BasicRoleSkill skill : autoSkills){
            sb.append("[skillId="+skill.skillId+"],");
        }
        for(BasicRoleSkill skill : manualSkills){
            sb.append("[skillId="+skill.skillId+"],");
        }
        sb.append("]]");
        System.out.println(sb.toString());
    }
}
