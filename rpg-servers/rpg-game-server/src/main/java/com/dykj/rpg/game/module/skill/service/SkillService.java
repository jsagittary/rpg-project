package com.dykj.rpg.game.module.skill.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import com.dykj.rpg.common.config.dao.*;
import com.dykj.rpg.common.config.model.*;
import com.dykj.rpg.common.data.model.*;
import com.dykj.rpg.game.module.cache.PlayerItemCache;
import com.dykj.rpg.game.module.cache.PlayerRuneCache;
import com.dykj.rpg.game.module.task.logic.refresh.TaskScheduleRefreshUtil;
import com.dykj.rpg.net.protocol.Protocol;
import com.dykj.rpg.protocol.skill.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.dykj.rpg.common.attribute.consts.DateFixConstant;
import com.dykj.rpg.common.attribute.consts.SkillMainConsts;
import com.dykj.rpg.common.consts.CommonConsts;
import com.dykj.rpg.common.consts.ItemOperateEnum;
import com.dykj.rpg.common.consts.ItemTypeEnum;
import com.dykj.rpg.common.data.dao.PlayerSkillDao;
import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.module.cache.PlayerSkillCache;
import com.dykj.rpg.game.module.equip.logic.EntryResult;
import com.dykj.rpg.game.module.equip.service.EquipService;
import com.dykj.rpg.game.module.item.consts.ItemPromp;
import com.dykj.rpg.game.module.item.response.ItemJoinModel;
import com.dykj.rpg.game.module.item.response.ItemResponse;
import com.dykj.rpg.game.module.item.service.ItemService;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.player.service.PlayerCacheService;
import com.dykj.rpg.game.module.util.CmdUtil;
import com.dykj.rpg.protocol.game2battle.EnterBattleSkillInfo;
import com.dykj.rpg.protocol.game2battle.SkillAttribute;
import com.dykj.rpg.util.random.RandomUtil;
import com.dykj.rpg.util.reflex.ReflexUtil;

/**
 * @author jyb
 * @date 2020/11/11 15:04
 * @Description
 */
@Service
public class SkillService {

    @Resource
    private CharacterBasicDao characterBasicDao;

    @Resource
    private SkillCharacterBasicDao skillCharacterBasicDao;// 技能基础

    @Resource
    private ItemDao itemDao;// 道具基础

    @Resource
    private EquipService equipService;

    @Resource
    private SkillAttrBasicDao skillAttrBasicDao;

    @Resource
    private ItemService itemService;// 玩家背包

    @Resource
    private PlayerSkillDao playerSkillDao;// 玩家技能

    @Resource
    private SkillUpgradeDao skillUpgradeDao;// 技能升级资源消耗

    @Resource
    private SkillUpgradeEffectDao skillUpgradeEffectDao;

    @Resource
    private SkillCharacterCarrierDao skillCharacterCarrierDao;

    @Resource
    private SkillCharacterEffectDao skillCharacterEffectDao;

    @Resource
    private SkillCharacterStateDao skillCharacterStateDao;

    @Resource
    private SkillCoreSlotDao skillCoreSlotDao;// 技能核心槽dao

    @Resource
    private SkillSoalUpgradeDao skillSoalUpgradeDao;

    @Resource
    private SkillStarUseDao skillStarUseDao;

    @Resource
    private SkillStarEffectDao skillStarEffectDao;
    @Resource
    private SoulBasicDao soulBasicDao;

    @Resource
    private SkillRunesEffectDao skillRunesEffectDao;//法术符文

    private Logger logger = LoggerFactory.getLogger(getClass());

    public PlayerSkillCache initSkills(Player player) {
        PlayerInfoModel infoModel = player.cache().getPlayerInfoModel();
        CharacterBasicModel characterBasicModel = characterBasicDao.getConfigByKey(infoModel.getProfession());
        Map<Integer, Integer> initializeManualSkills = characterBasicModel.getInitializeManualSkills();// 初始化主动技能
        PlayerSkillCache playerSkillCache = player.cache().getPlayerSkillCache();

        if (null == playerSkillCache) {
            playerSkillCache = new PlayerSkillCache();

            for (Map.Entry<Integer, Integer> entry : initializeManualSkills.entrySet()) {
                Integer position = entry.getKey();
                Integer SkillId = entry.getValue();
                PlayerSkillModel playerSkillModel = new PlayerSkillModel();
                playerSkillModel.setPlayerId(player.getPlayerId());
                playerSkillModel.setPosition(position);
                playerSkillModel.setSkillLevel(1);
                playerSkillModel.setSkillStarLevel(0);
                playerSkillModel.setSkillExp(0);
                playerSkillModel.setSkillId(SkillId);
                // TODO 注意临时添加技能类型后续要修改
                playerSkillModel.setSkillType(1);// 临时添加后续要修改
                playerSkillDao.queueInsert(playerSkillModel);

                playerSkillCache.getPlayerSkillModelMap().put(SkillId, playerSkillModel);
            }
            player.cache().setPlayerSkillCache(playerSkillCache);
        }

        return playerSkillCache;
    }

    public PlayerSkillCache initSkills1(Player player) {
        PlayerInfoModel infoModel = player.cache().getPlayerInfoModel();
        CharacterBasicModel characterBasicModel = characterBasicDao.getConfigByKey(infoModel.getProfession());
        Map<Integer, Integer> initializeManualSkills = characterBasicModel.getInitializeManualSkills();// 初始化主动技能
        PlayerSkillCache playerSkillCache = player.cache().getPlayerSkillCache();
        for (Map.Entry<Integer, Integer> entry : initializeManualSkills.entrySet()) {
            Integer position = entry.getKey();
            Integer SkillId = entry.getValue();
            PlayerSkillModel playerSkillModel = new PlayerSkillModel();
            playerSkillModel.setPlayerId(player.getPlayerId());
            playerSkillModel.setPosition(position);
            playerSkillModel.setSkillLevel(1);
            playerSkillModel.setSkillStarLevel(0);
            playerSkillModel.setSkillExp(0);
            playerSkillModel.setSkillId(SkillId);
            playerSkillModel.setSoulChangePos(0);
            // TODO 注意临时添加技能类型后续要修改
            playerSkillModel.setSkillType(1);// 临时添加后续要修改
            playerSkillDao.queueInsert(playerSkillModel);
            playerSkillCache.getPlayerSkillModelMap().put(SkillId, playerSkillModel);
        }
        player.cache().setPlayerSkillCache(playerSkillCache);

        return playerSkillCache;
    }

    public SkillRs skillRs(PlayerSkillModel playerSkillModel) {
        SkillRs skillRs = new SkillRs();
        skillRs.setSkillId(playerSkillModel.getSkillId());
        skillRs.setPosition(playerSkillModel.getPosition());
        skillRs.setSkillExp(playerSkillModel.getSkillExp());
        skillRs.setSkillLevel(playerSkillModel.getSkillLevel());
        skillRs.setSkillStarLevel(playerSkillModel.getSkillStarLevel());
        skillRs.setSkillType(playerSkillModel.getSkillType());
        skillRs.setTrainSoulPos(playerSkillModel.getSoulChangePos());
        if (playerSkillModel.getSoulChangeTime() != null) {
            skillRs.setSoulChangeTime(playerSkillModel.getSoulChangeTime().getTime());
        } else {
            skillRs.setSoulChangeTime(0L);
        }
        return skillRs;
    }

    public PlayerSkillRs playerSkillRs(PlayerSkillCache playerSkillCache) {
        PlayerSkillRs playerSkillRs = new PlayerSkillRs();
        for (PlayerSkillModel playerSkillModel : playerSkillCache.getPlayerSkillModelMap().values()) {
            playerSkillRs.getSkills().add(skillRs(playerSkillModel));
        }
        return playerSkillRs;
    }

    /**
     * 穿戴装备
     *
     * @param player
     * @param skillUpRq
     * @return
     */
    public ErrorCodeEnum skillUp(Player player, SkillUpRq skillUpRq) {
        PlayerSkillCache playerSkillCache = player.cache().getPlayerSkillCache();
        if (playerSkillCache == null) {
            logger.error("skillUp error ： playerSkillCache not exist {} ", player.getPlayerId());
            return ErrorCodeEnum.DATA_ERROR;
        }

        PlayerSkillModel playerSkillModel = playerSkillCache.getPlayerSkillModelMap().get(skillUpRq.getSkillId());
        if (playerSkillModel == null) {
            return ErrorCodeEnum.PLAYER_HAS_NO_SKILL;
        }

        SkillCharacterBasicModel skillBasicModel = skillCharacterBasicDao.getConfigByKey(skillUpRq.getSkillId());
        if (skillBasicModel == null) {
            logger.error("skillUp  pram error ： {} ", skillUpRq.toString());
            return ErrorCodeEnum.CLIENT_PRAMS_ERROR;
        }
        // 核心槽技能不能装纯治疗技能
        if (skillUpRq.getPosition() == 1 && skillBasicModel.getLabelType().size() == 1
                && skillBasicModel.getLabelType().get(0) == 5) {
            logger.error("skillUp  type error ： {} ", skillUpRq.toString());
            return ErrorCodeEnum.SKILL_UP_TYPE_ERROR;
        }

        SkillUpRs skillUpRs = new SkillUpRs();
        PlayerSkillModel upSkill = playerSkillCache.getUpSkillByPosition(skillUpRq.getPosition());
        if (upSkill != null) {
            upSkill.setPosition(-1);
            skillUpRs.setDownSkill(skillRs(upSkill));
            playerSkillDao.queueUpdate(upSkill);
        }
        playerSkillModel.setPosition(skillUpRq.getPosition());
        skillUpRs.setUpSkill(skillRs(playerSkillModel));
        playerSkillDao.queueUpdate(playerSkillModel);
        CmdUtil.sendMsg(player, skillUpRs);

        // TODO 此处要重新计算玩家的属性

        return ErrorCodeEnum.SUCCESS;
    }

    /**
     * 穿戴装备
     *
     * @param player
     * @param skillDownRq
     * @return
     */
    public ErrorCodeEnum skillDown(Player player, SkillDownRq skillDownRq) {
        PlayerSkillCache playerSkillCache = player.cache().getPlayerSkillCache();

        if (playerSkillCache == null) {
            logger.error("skillDown error ： playerSkillCache not exist {} ", player.getPlayerId());
            return ErrorCodeEnum.DATA_ERROR;
        }
        PlayerSkillModel upSkill = playerSkillCache.getUpSkillByPosition(skillDownRq.getPosition());
        if (upSkill == null) {
            logger.error("skillDown  pram error ： {} ", skillDownRq.toString());
            return ErrorCodeEnum.CLIENT_PRAMS_ERROR;
        }
        upSkill.setPosition(-1);
        playerSkillDao.queueUpdate(upSkill);
        SkillDownRs skillDownRs = new SkillDownRs();
        skillDownRs.setDownSkill(skillRs(upSkill));
        CmdUtil.sendMsg(player, skillDownRs);

        // TODO 此处要重新计算玩家的属性
        return ErrorCodeEnum.SUCCESS;
    }

    /**
     * 创建技能集合
     *
     * @param player
     * @return
     */
    public List<EnterBattleSkillInfo> createEnterBattleSkills(Player player) {
        Map<Integer, List<EntryResult>> entryResults = equipService.skillAttribute(player);
        PlayerSkillCache playerSkillCache = player.cache().getPlayerSkillCache();
        List<PlayerSkillModel> playerSkillModels = playerSkillCache.getUpSkill();
        PlayerRuneCache playerRuneCache = player.cache().getPlayerRuneCache();
        List<EnterBattleSkillInfo> enterBattleSkillInfos = new ArrayList<>();
        for (PlayerSkillModel skill : playerSkillModels) {
            EnterBattleSkillInfo enterBattleSkillInfo = new EnterBattleSkillInfo();
            enterBattleSkillInfo.setSkillId(skill.getSkillId());
            enterBattleSkillInfo.setLevel(skill.getSkillLevel());
            enterBattleSkillInfo.setSkillPosition(skill.getPosition());
            // 技能等级词条值修正
            zhiXiuZheng(enterBattleSkillInfo, skill, skillEntryResults(skill));
            // 装备词条值修正
            zhiXiuZheng(enterBattleSkillInfo, skill, entryResults);
            //技能星级词条值修正
            zhiXiuZheng(enterBattleSkillInfo, skill, skillStarLevelEntryResults(skill));
            // 核心技能槽值修正
            skillCoreGrooveEntryResults(enterBattleSkillInfo, skill);
            // 符文值修正
            skillRunesEntryResults(enterBattleSkillInfo, skill, playerRuneCache);
            //灵魂之影修正
            soulEntryResults(skill, player);
            // 覆盖修正
            fuGaiXiuZheng(enterBattleSkillInfo, skill, entryResults);
            // 初次激活
            chuCiJiHuo(enterBattleSkillInfo, skill, entryResults);

            enterBattleSkillInfos.add(enterBattleSkillInfo);
        }
        // 新技能
        if (createNewSkill(entryResults) != null) {
            enterBattleSkillInfos.addAll(createNewSkill(entryResults));
        }
        // 值修正传给战斗服为覆盖修正，告知战斗服直接覆盖
        reloadAttributeType(enterBattleSkillInfos);
        return enterBattleSkillInfos;
    }


    //灵魂之影值修正
    private Map<Integer, List<EntryResult>> soulEntryResults(PlayerSkillModel skill, Player player) {
        Map<Integer, List<EntryResult>> entryResults = new HashMap<>();
        PlayerSoulSkinModel playerSoulSkinModel = player.cache().getPlayerSoulSkinCache().getUseSoulSkin();
        if (playerSoulSkinModel == null) {
            return entryResults;
        }
        if (skill.getPosition() < 5) {
            return entryResults;
        }
        SoulBasicModel soulBasicModel = soulBasicDao.getConfigByKey(playerSoulSkinModel.getSoulSkinId());
        if (soulBasicModel == null) {
            logger.error("soulBasicModel is not exist {} ", playerSoulSkinModel.getSoulSkinId());
            return entryResults;
        }
        List<EntryResult> entryResultList = new ArrayList<>();
        List<List<Integer>> corrects = soulBasicModel.getCorrect();
        // 0修正属性： 1修正方式： 2修正值
        for (List<Integer> correct : corrects) {
            entryResultList.add(soulEntryResult(skill, correct));
        }
        return entryResults;
    }

    /**
     * @param skill
     * @param bigTypes // 0修正属性： 1修正方式： 2修正值
     * @return
     */
    private EntryResult soulEntryResult(PlayerSkillModel skill, List<Integer> bigTypes) {
        EntryResult entryResult = new EntryResult();
        entryResult.setSkillId(skill.getSkillId());
        entryResult.setId(bigTypes.get(0));
        entryResult.setType(DateFixConstant.ZHI_XIU_ZHENG);
        entryResult.setEffectId(skill.getSkillId());
        if (bigTypes.get(1) == DateFixConstant.ZHI_XIU_ZHENG) {
            entryResult.setValue(bigTypes.get(2));
        } else if (bigTypes.get(1) == DateFixConstant.ZENG_BI_XIU_ZHENG) {
            entryResult.setIncrease(bigTypes.get(2));
        } else if (bigTypes.get(1) == DateFixConstant.JIAN_BI_XIU_ZHENG) {
            entryResult.setReduction(bigTypes.get(2));
        }
        return entryResult;
    }


    // 值修正传给战斗服为覆盖修正，告知战斗服直接覆盖
    public void reloadAttributeType(List<EnterBattleSkillInfo> battleSkillInfos) {
        for (EnterBattleSkillInfo enterBattleSkillInfo : battleSkillInfos) {
            for (SkillAttribute skillAttribute : enterBattleSkillInfo.getAttributeInfos()) {
                if (skillAttribute.getPrams() == DateFixConstant.ZHI_XIU_ZHENG) {
                    skillAttribute.setPrams(DateFixConstant.FU_GAI_XIU_ZHENG);
                }
            }
        }
    }

    /**
     * 技能等级词条值修正
     */
    public Map<Integer, List<EntryResult>> skillEntryResults(PlayerSkillModel skill) {
        Map<Integer, List<EntryResult>> entryResults = new HashMap<>();
        SkillUpgradeEffectModel skillUpgradeEffectModel = skillUpgradeEffectDao.getConfigByKey(skill.getSkillId());
        if (skillUpgradeEffectModel == null) {
            logger.error("SkillService levelUPZhiXiuZheng error  {} :", skill.getSkillId());
            return entryResults;
        }
        List<EntryResult> entryResultList = new ArrayList<>();
        for (int i = 0; i < skillUpgradeEffectModel.getEntryBigType().size(); i++) {
            List<Integer> bigTypes = skillUpgradeEffectModel.getEntryBigType().get(i);
            int value;
            int effectId;
            try {
                value = skillUpgradeEffectModel.getGrowValue().get(i).get(skill.getSkillLevel() - 1);
                if (value == 0) {
                    continue;
                }
                effectId = skillUpgradeEffectModel.getType().get(i);
            } catch (Exception e) {
                logger.error("levelUPZhiXiuZheng error skill {} index {} level {}  e {} ", skill.getSkillId(), i, skill.getSkillLevel(), e);
                return entryResults;
            }
            entryResultList.add(this.entryResult(skill, bigTypes, effectId, value));
        }
        entryResults.put(DateFixConstant.ZHI_XIU_ZHENG, entryResultList);
        return entryResults;
    }

    /**
     * 技能升星成长
     */
    public Map<Integer, List<EntryResult>> skillStarLevelEntryResults(PlayerSkillModel skill) {
        Map<Integer, List<EntryResult>> entryResults = new HashMap<>();
        SkillStarEffectModel skillStarEffectModel = skillStarEffectDao.getConfigByKey(skill.getSkillId());
        if (skillStarEffectModel == null) {
            logger.error("SkillService starLevelUPZhiXiuZheng error  {} :", skill.getSkillId());
            return entryResults;
        }
        List<EntryResult> entryResultList = new ArrayList<>();
        for (int i = 0; i < skillStarEffectModel.getEntryBigType().size(); i++) {
            List<Integer> bigTypes = skillStarEffectModel.getEntryBigType().get(i);
            int value;
            int effectId;
            try {
                value = skillStarEffectModel.getGrowValue().get(i).get(skill.getSkillLevel() - 1);
                if (value == 0) {
                    continue;
                }
                effectId = skillStarEffectModel.getType().get(i);
            } catch (Exception e) {
                logger.error("starLevelUPZhiXiuZheng error skill {} index {} starLevel {}  e {} ", skill.getSkillId(), i, skill.getSkillLevel(), e);
                return entryResults;
            }
            entryResultList.add(this.entryResult(skill, bigTypes, effectId, value));
        }
        entryResults.put(DateFixConstant.ZHI_XIU_ZHENG, entryResultList);
        return entryResults;
    }

    private EntryResult entryResult(PlayerSkillModel skill, List<Integer> bigTypes, int effectId, int value) {
        EntryResult entryResult = new EntryResult();
        entryResult.setSkillId(skill.getSkillId());
        entryResult.setId(bigTypes.get(2));
        entryResult.setType(DateFixConstant.ZHI_XIU_ZHENG);
        entryResult.setEffectId(effectId);
        if (bigTypes.get(3) == DateFixConstant.ZHI_XIU_ZHENG) {
            entryResult.setValue(value);
        } else if (bigTypes.get(3) == DateFixConstant.ZENG_BI_XIU_ZHENG) {
            entryResult.setIncrease(value);
        } else if (bigTypes.get(3) == DateFixConstant.JIAN_BI_XIU_ZHENG) {
            entryResult.setReduction(value);
        }
        return entryResult;
    }


    /**
     * 技能核心槽
     *
     * @param skill
     * @return
     */
    public void skillCoreGrooveEntryResults(EnterBattleSkillInfo enterBattleSkillInfo, PlayerSkillModel skill) {
        Optional<SkillCoreSlotModel> first = skillCoreSlotDao.getConfigs().stream()
                .filter(e -> e.getSkillId() == skill.getSkillId() && e.getSkillPosition() == skill.getPosition())
                .findFirst();
        if (first.isPresent()) {
            Map<Integer, List<EntryResult>> entryResults = new HashMap<>();
            SkillCoreSlotModel skillCoreSlotModel = first.get();
            List<EntryResult> entryResultList = new ArrayList<>();
            for (int i = 0; i < skillCoreSlotModel.getEntryBigType().size(); i++) {
                List<Integer> bigTypes = skillCoreSlotModel.getEntryBigType().get(i);
                int value;
                int effectId;
                try {
                    value = skillCoreSlotModel.getGrowValue().get(i);
                    effectId = skillCoreSlotModel.getType().get(i);
                } catch (Exception e) {
                    logger.error("levelUPZhiXiuZheng error index {} level {}  e {} ", i, skill.getSkillLevel(), e);
                    return;
                }
                entryResultList.add(this.entryCoverResult(skill, bigTypes, value, effectId, enterBattleSkillInfo));
            }
            entryResults.put(DateFixConstant.ZHI_XIU_ZHENG, entryResultList);
            // 技能槽值修正
            zhiXiuZheng(enterBattleSkillInfo, skill, entryResults);
        }
    }

    /**
     * 符文
     */
    private void skillRunesEntryResults(EnterBattleSkillInfo enterBattleSkillInfo, PlayerSkillModel skill, PlayerRuneCache playerRuneCache)
    {
        List<PlayerRuneModel> playerRuneList = playerRuneCache.getRuneList(skill.getSkillId());
        if (null != playerRuneList && !playerRuneList.isEmpty())
        {
            for (PlayerRuneModel playerRuneModel : playerRuneList)
            {
                SkillRunesEffectModel skillRunesEffectModel = skillRunesEffectDao.getConfigByKey(playerRuneModel.getRuneId());
                if (null != skillRunesEffectModel)
                {
                    Map<Integer, List<EntryResult>> entryResults = new HashMap<>();
                    List<EntryResult> entryResultList = new ArrayList<>();
                    for (int i = 0; i < skillRunesEffectModel.getEntryBigType().size(); i++) {
                        List<Integer> bigTypes = skillRunesEffectModel.getEntryBigType().get(i);
                        int value;
                        int effectId;
                        try {
                            value = skillRunesEffectModel.getGrowValue().get(i).get(i);
                            effectId = skillRunesEffectModel.getType().get(i);
                        } catch (Exception e) {
                            logger.error("levelUPZhiXiuZheng error index {} level {}  e {} ", i, skill.getSkillLevel(), e);
                            return;
                        }
                        entryResultList.add(this.entryCoverResult(skill, bigTypes, value, effectId, enterBattleSkillInfo));
                    }
                    entryResults.put(DateFixConstant.ZHI_XIU_ZHENG, entryResultList);
                    // 符文值修正
                    zhiXiuZheng(enterBattleSkillInfo, skill, entryResults);
                }
            }
        }
    }

    private EntryResult entryCoverResult(PlayerSkillModel skill, List<Integer> bigTypes, int value, int effectId, EnterBattleSkillInfo enterBattleSkillInfo)
    {
        EntryResult entryResult = new EntryResult();
        entryResult.setSkillId(skill.getSkillId());
        entryResult.setId(bigTypes.get(2));
        entryResult.setType(DateFixConstant.ZHI_XIU_ZHENG);
        entryResult.setEffectId(effectId);
        if (bigTypes.get(3) == DateFixConstant.ZHI_XIU_ZHENG) {
            entryResult.setValue(value);
        } else if (bigTypes.get(3) == DateFixConstant.ZENG_BI_XIU_ZHENG) {
            entryResult.setIncrease(value);
        } else if (bigTypes.get(3) == DateFixConstant.JIAN_BI_XIU_ZHENG) {
            entryResult.setReduction(value);
        } else if (bigTypes.get(3) == DateFixConstant.LIN_SHI_FU_GAI_XIU_ZHENG) {
            SkillAttribute skillAttribute = new SkillAttribute();
            skillAttribute.setTableKey(effectId);
            skillAttribute.setSkillAttrId(bigTypes.get(2));
            skillAttribute.setPrams(bigTypes.get(3));
            skillAttribute.setValue(value);
            enterBattleSkillInfo.getAttributeInfos().add(skillAttribute);
        }
        return entryResult;
    }

    public void zhiXiuZheng(EnterBattleSkillInfo enterBattleSkillInfo, PlayerSkillModel skill, Map<Integer, List<EntryResult>> entryResults) {
        // 查询是否有词条进技能替换技能
        List<EntryResult> entryResultList = entryResults.get(DateFixConstant.ZHI_XIU_ZHENG);
        if (entryResultList != null && entryResultList.size() > 0) {
            for (EntryResult _result : entryResultList) {
                if (_result.getSkillId() == skill.getSkillId()) {
                    SkillAttribute skillAttribute = createSkillAttribute(enterBattleSkillInfo, _result.getId(),
                            _result.getEffectId(), _result.getType());
                    int originalValue = (Integer) getValue(_result.getId(), _result.getEffectId());
                    int value = _result.getValue() + originalValue;
                    value = (int) Math.floor(
                            value * ((1 + _result.getIncrease() / 10000f) / (1 + _result.getReduction() / 10000f)));
                    // skillAttribute.setSkillAttrId(_result.getId());
                    // skillAttribute.setEffectId(_result.getEffectId());
                    // skillAttribute.setPrams(_result.getType());
                    skillAttribute.setValue(value + skillAttribute.getValue());
                    enterBattleSkillInfo.getAttributeInfos().add(skillAttribute);
                }
            }
        }
    }

    /**
     * 从某个enterBattleSkillInfo 找出 SkillAttribute
     *
     * @param enterBattleSkillInfo
     * @param SkillAttrId
     * @param effectId
     * @param type
     * @return
     */
    public SkillAttribute createSkillAttribute(EnterBattleSkillInfo enterBattleSkillInfo, int SkillAttrId, int effectId,
                                               int type) {
        for (SkillAttribute skillAttribute : enterBattleSkillInfo.getAttributeInfos()) {
            if (skillAttribute.getTableKey() == effectId && skillAttribute.getSkillAttrId() == SkillAttrId
                    && skillAttribute.getPrams() == type) {
                return skillAttribute;
            }
        }
        return new SkillAttribute(effectId, SkillAttrId, type, 0);
    }

    /**
     * 覆盖修正
     *
     * @param enterBattleSkillInfo
     * @param skill
     * @param entryResults
     */
    public void fuGaiXiuZheng(EnterBattleSkillInfo enterBattleSkillInfo, PlayerSkillModel skill,
                              Map<Integer, List<EntryResult>> entryResults) {
        // 查询是否有词条进技能替换技能
        List<EntryResult> entryResultList = entryResults.get(DateFixConstant.FU_GAI_XIU_ZHENG);
        if (entryResultList != null && entryResultList.size() > 0) {
            for (EntryResult _result : entryResultList) {
                if (_result.getSkillId() == skill.getSkillId()) {
                    int originalValue = _result.getValue();
                    boolean flag = false;
                    for (SkillAttribute s : enterBattleSkillInfo.getAttributeInfos()) {
                        if (_result.getId() == s.getSkillAttrId() && _result.getEffectId() == s.getTableKey()) {
                            s.setValue(originalValue);
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        SkillAttribute skillAttribute = new SkillAttribute();
                        skillAttribute.setSkillAttrId(_result.getId());
                        skillAttribute.setValue(_result.getValue());
                        skillAttribute.setTableKey(_result.getEffectId());
                        skillAttribute.setPrams(_result.getType());
                        enterBattleSkillInfo.getAttributeInfos().add(skillAttribute);
                    }
                }
            }
        }
    }

    /**
     * 初次激活
     *
     * @param enterBattleSkillInfo
     * @param skill
     * @param entryResults
     */
    public void chuCiJiHuo(EnterBattleSkillInfo enterBattleSkillInfo, PlayerSkillModel skill,
                           Map<Integer, List<EntryResult>> entryResults) {
        // 查询是否有词条进技能替换技能
        List<EntryResult> entryResultList = entryResults.get(DateFixConstant.DIAN_LIANG_XIU_ZHENG);
        if (entryResultList != null && entryResultList.size() > 0) {
            // 对通一个类型的EntryResult合并，以便去计算点亮的优先级，每一组点亮取前面的点亮
            List<EntryResult> resultList = subChuCiJiHuoEntryResult(entryResultList);
            for (EntryResult _result : resultList) {
                if (_result.getSkillId() == skill.getSkillId()) {
                    List<List<Integer>> originalValue = null;
                    Object o = getValue(_result.getId(), _result.getEffectId());
                    if (o != null) {
                        originalValue = (List<List<Integer>>) o;
                    }
                    // 在点亮的时候，如果有多个点亮取第一个
                    if (originalValue != null) {
                        for (List<Integer> ss : originalValue) {
                            for (Integer result : ss) {
                                if (_result.getChuCiJiHuoList().contains(result)) {
                                    SkillAttribute skillAttribute = new SkillAttribute();
                                    skillAttribute.setSkillAttrId(_result.getId());
                                    skillAttribute.setTableKey(_result.getEffectId());
                                    skillAttribute.setPrams(_result.getType());
                                    skillAttribute.setValue(result);
                                    enterBattleSkillInfo.getAttributeInfos().add(skillAttribute);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 合并同一个类型初次点亮
     *
     * @param entryResultList
     * @return
     */
    private List<EntryResult> subChuCiJiHuoEntryResult(List<EntryResult> entryResultList) {
        List<EntryResult> results = new ArrayList<>();
        for (EntryResult e : entryResultList) {
            if (results.contains(e)) {
                for (EntryResult _r : results) {
                    if (_r.equals(e)) {
                        _r.getChuCiJiHuoList().add(e.getValue());
                        break;
                    }
                }
            } else {
                e.getChuCiJiHuoList().add(e.getValue());
                results.add(e);
            }
        }
        return results;
    }

    /**
     * 类型为0 且没有被用到的 属于新进能
     *
     * @param entryResults
     * @return
     */
    public List<EnterBattleSkillInfo> createNewSkill(Map<Integer, List<EntryResult>> entryResults) {
        List<EnterBattleSkillInfo> battleSkillInfos = new ArrayList<>();
        List<EntryResult> entryResultList = entryResults.get(DateFixConstant.JI_NENG_TI_HUAN);
        if (entryResultList == null || entryResultList.size() < 1) {
            return null;
        }
        for (EntryResult entryResult : entryResultList) {
            EnterBattleSkillInfo enterBattleSkillInfo = new EnterBattleSkillInfo();
            enterBattleSkillInfo.setSkillId(entryResult.getSkillId());
            enterBattleSkillInfo.setSkillPosition(-1);
            enterBattleSkillInfo.setLevel(1);
            battleSkillInfos.add(enterBattleSkillInfo);

        }
        return battleSkillInfos;
    }

    /**
     * 技能升级实现
     *
     * @param player             玩家信息
     * @param skillUpgradeListRq 技能升级协议
     */
    public void skillUpgrade(Player player, SkillUpgradeListRq skillUpgradeListRq) {
        if (0 == skillUpgradeListRq.getSkillId() || null == skillUpgradeListRq.getSkillUpgrades() || skillUpgradeListRq.getSkillUpgrades().isEmpty()) {
            logger.error("玩家id:{}, 协议号:{}, 技能升级时技能id为空或资源消耗列表为空!", player.getPlayerId(), skillUpgradeListRq.getCode());
            CmdUtil.sendErrorMsg(player.getSession(), skillUpgradeListRq.getCode(), ErrorCodeEnum.CLIENT_PRAMS_ERROR);
            return;
        }
        PlayerSkillModel playerSkillModel = player.cache().getPlayerSkillCache().getPlayerSkillModelMap().get(skillUpgradeListRq.getSkillId());//玩家技能
        if (null == playerSkillModel) {
            logger.error("玩家id:{}, 技能id:{}, 当前技能不存在!", player.getPlayerId(), skillUpgradeListRq.getSkillId());
            CmdUtil.sendErrorMsg(player.getSession(), skillUpgradeListRq.getCode(), ErrorCodeEnum.CLIENT_PRAMS_ERROR);
            return;
        }
        SkillCharacterBasicModel skillCharacterBasicModel = skillCharacterBasicDao.getConfigByKey(playerSkillModel.getSkillId());//技能基础
        SkillStarEffectModel skillStarEffectModel = skillStarEffectDao.getConfigByKey(playerSkillModel.getSkillId());//技能升星
        if (null == skillCharacterBasicModel || null == skillStarEffectModel) {
            logger.error("玩家id:{}, 技能id:{}, 查询技能基础或技能升星配置表数据为空!", player.getPlayerId(), skillUpgradeListRq.getSkillId());
            CmdUtil.sendErrorMsg(player.getSession(), skillUpgradeListRq.getCode(), ErrorCodeEnum.CONFIG_ERROR);
            return;
        }

        ErrorCodeEnum errorCodeEnum;//错误码
        //1. 根据星级判断是否达到等级上限
        errorCodeEnum = this.conditionJudgment(player, skillUpgradeListRq.getSkillId(), skillStarEffectModel, playerSkillModel, true);
        if (!errorCodeEnum.equals(ErrorCodeEnum.SUCCESS)) {
            CmdUtil.sendErrorMsg(player.getSession(), skillUpgradeListRq.getCode(), errorCodeEnum);
            return;
        }
        //根据技能id去技能升级消耗资源部匹配对应技能品质的数据
        Optional<SkillUpgradeModel> skillUpgOpt = skillUpgradeDao.getConfigs().stream().filter(e -> e.getItemQualityType().equals(skillCharacterBasicModel.getItemQualityType())).filter(k -> k.getSkillLevel() == playerSkillModel.getSkillLevel()).findFirst();
        if (!skillUpgOpt.isPresent()) {
            logger.error("玩家id:{}, 协议号:{}, 技能id:{}, 未匹配到技能升级所需的资源数据!", player.getPlayerId(), skillUpgradeListRq.getCode(), playerSkillModel.getSkillId());
            CmdUtil.sendErrorMsg(player.getSession(), skillUpgradeListRq.getCode(), ErrorCodeEnum.CONFIG_ERROR);
            return;
        }
        //技能升级所需的资源消耗配置
        SkillUpgradeModel skillUpgradeModel = skillUpgOpt.get();
        //最终更新道具列表
        List<ItemJoinModel> finalModelList = new ArrayList<>();

        //2. 根据品质进行分类
        Map<String, List<ItemJoinModel>> qualityMap = new HashMap<>();//存储对应品质的道具  key-道具类型:道具品质:数量 value-item
        for (SkillUpgradeRq upg : skillUpgradeListRq.getSkillUpgrades()) {
            errorCodeEnum = this.qualitySort(player, upg.getItemId(), upg.getItemNum(), skillUpgradeModel.getNeedQualityItem(), qualityMap);
            if (!errorCodeEnum.equals(ErrorCodeEnum.SUCCESS)) {
                CmdUtil.sendErrorMsg(player.getSession(), skillUpgradeListRq.getCode(), errorCodeEnum);
                return;
            }
        }

        //3. 判断同类型技能书是否足够
        errorCodeEnum = this.skillBookEnough(player, qualityMap, finalModelList);
        if (!errorCodeEnum.equals(ErrorCodeEnum.SUCCESS)) {
            CmdUtil.sendErrorMsg(player.getSession(), skillUpgradeListRq.getCode(), errorCodeEnum);
            return;
        }

        //4. 判断是否满足养成道具组(消耗指定道具组配置格式: 道具类型:道具ID:数量)
        List<ItemJoinModel> spendGoldList = new ArrayList<>();//存储任务花费多少金币list
        List<ItemJoinModel> diamondList = new ArrayList<>();//存储任务花费多少钻石list
        errorCodeEnum = this.itemGroupEnough(player, skillUpgradeListRq.getSkillId(),
                skillUpgradeModel.getNeedItem(), finalModelList, spendGoldList, diamondList, 0);
        if (!errorCodeEnum.equals(ErrorCodeEnum.SUCCESS)) {
            CmdUtil.sendErrorMsg(player.getSession(), skillUpgradeListRq.getCode(), errorCodeEnum);
            return;
        }

        //5. 调用背包更新方法
        ItemResponse itemResponse = itemService.batchUpdateItemPush(player, finalModelList, ItemOperateEnum.USE, ItemPromp.GENERIC);
        if (itemResponse.getCodeEnum().equals(ErrorCodeEnum.SUCCESS)) {
            SkillUpgradeRs skillUpgradeRs = new SkillUpgradeRs();
            skillUpgradeRs.setSkillId(playerSkillModel.getSkillId());
            //技能升级概率
            boolean successRate = RandomUtil.isInTheLimits(skillUpgradeModel.getSuccessRate(), CommonConsts.THOUSAND_VALUE);
            if (successRate) {
                //技能升级成功
                playerSkillModel.setSkillLevel(playerSkillModel.getSkillLevel() + 1);
                playerSkillDao.queueUpdate(playerSkillModel);
                //组装协议
                skillUpgradeRs.setSkillLevel(playerSkillModel.getSkillLevel());
                skillUpgradeRs.setStatus(true);

                /*
                 * 触发任务进度刷新
                 * 任务完成条件类型:
                 *      【激活期间】提升法术等级X次任务
                 *      【累计期间】成功提升任意法术等级X次
                 */
                TaskScheduleRefreshUtil.spellsLevelSchedule(player);
            } else {
                //技能升级失败
                skillUpgradeRs.setSkillLevel(playerSkillModel.getSkillLevel());
                skillUpgradeRs.setStatus(false);
            }
            //推送技能升级协议
            CmdUtil.sendMsg(player, skillUpgradeRs);
            logger.debug("玩家id:{}, 协议号:{}, 请求协议:{}, 技能升级处理完毕, 返回协议:{}", player.getPlayerId(), skillUpgradeListRq.getCode(), skillUpgradeListRq.toString(), skillUpgradeRs.toString());
            //判断当前如果有消耗货币的行为则触发
            this.triggerTaskRefresh(player, spendGoldList, diamondList);
        }
    }

    /**
     * 1、根据技能等级和技能星级做条件判断
     *
     * @param type true-技能升级判定  false-技能升星判定
     */
    private ErrorCodeEnum conditionJudgment(Player player, int skillId, SkillStarEffectModel skillStarEffectModel, PlayerSkillModel playerSkillModel, boolean type) {
        List<Integer> starLevelList = skillStarEffectModel.getStarLevel();//星级
        List<Integer> skillLimitnum = skillStarEffectModel.getSkillLimitnum();//技能等级上限
        if (starLevelList.size() != skillLimitnum.size()) {
            logger.error("玩家id:{}, 技能id:{}, 技能升星配置表星级上限与技能等级上限不匹配!", player.getPlayerId(), skillId);
            return ErrorCodeEnum.CONFIG_ERROR;
        }
        for (int i = 0; i < starLevelList.size(); i++) {
            if (playerSkillModel.getSkillStarLevel() == starLevelList.get(i)) {
                int skillLevel = skillLimitnum.get(i);
                if (type) {
                    if (playerSkillModel.getSkillLevel() >= skillLevel) {
                        logger.error("玩家id:{}, 技能id:{}, 当前技能等级已达到上限!", player.getPlayerId(), skillId);
                        return ErrorCodeEnum.SKILL_LEVEL_UPPER_LIMIT;
                    }
                } else {
                    //如果是最后一个元素则达到配置上限后弹出错误提示
                    if (i == (starLevelList.size() - 1) && playerSkillModel.getSkillLevel() >= skillLevel) {
                        logger.error("玩家id:{}, 技能id:{}, 当前技能星级已达到配置上限!", player.getPlayerId(), skillId);
                        return ErrorCodeEnum.SKILL_NOT_RISING_STAR_CONDITION;
                    } else {
                        if (playerSkillModel.getSkillLevel() > skillLevel) {
                            logger.error("玩家id:{}, 技能id:{}, 当前技能等级已超出对应升星范围!", player.getPlayerId(), skillId);
                            return ErrorCodeEnum.SKILL_GRADE_BEYOND_RANGE;
                        }
                    }
                }
            }
        }
        return ErrorCodeEnum.SUCCESS;
    }

    /**
     * 2、 根据品质进行分类
     */
    private ErrorCodeEnum qualitySort(Player player, int itemId, int itemNum, List<List<Integer>> needQualityItem, Map<String, List<ItemJoinModel>> qualityMap) {
        if (0 == itemId || 0 == itemNum) {
            logger.error("玩家id:{}, 道具id或道具数量为空!", player.getPlayerId());
            return ErrorCodeEnum.CLIENT_PRAMS_ERROR;
        }
        PlayerItemModel playerItemModel = itemService.getItemInfo(player, itemId);//根据道具id拿到指定玩家背包里道具
        if (null == playerItemModel) {
            logger.error("玩家id:{}, 道具id:{}, 当前道具不存在!", player.getPlayerId(), itemId);
            return ErrorCodeEnum.ITEM_NOT_EXIST;
        }
        boolean needQuality = false;
        int minusAmount = 0;//need_quality_item字段对应必要消耗数量
        ItemModel itemModel = itemDao.getConfigByKey(playerItemModel.getItemId());//拿到道具基础信息
        for (List<Integer> needQualitys : needQualityItem) {
            if (playerItemModel.getItemType() == needQualitys.get(0) && itemModel.getItemQualityType().equals(needQualitys.get(1))) {
                needQuality = true;
                minusAmount = needQualitys.get(2);
                break;
            }
        }
        if (!needQuality) {
            logger.error("玩家id:{}, 道具id:{}, 道具类型:{}, 道具品质:{},与\"养成需求道具组\"不匹配", player.getPlayerId(), playerItemModel.getItemId(), playerItemModel.getItemType(), itemModel.getItemQualityType());
            return ErrorCodeEnum.CONFIG_ERROR;
        }

        ItemJoinModel itemJoinModel = new ItemJoinModel(playerItemModel.getItemId(), itemNum, playerItemModel.getItemType());
        String key = playerItemModel.getItemType() + CommonConsts.STR_COLON + itemModel.getItemQualityType() + CommonConsts.STR_COLON + minusAmount;
        if (qualityMap.containsKey(key)) {
            List<ItemJoinModel> itemJoinModels = qualityMap.get(key);
            itemJoinModels.add(itemJoinModel);
        } else {
            List<ItemJoinModel> itemJoinModels = new ArrayList<>();
            itemJoinModels.add(itemJoinModel);
            qualityMap.put(key, itemJoinModels);
        }
        return ErrorCodeEnum.SUCCESS;
    }

    /**
     * 3、 判断同类型技能书是否足够
     */
    private ErrorCodeEnum skillBookEnough(Player player, Map<String, List<ItemJoinModel>> qualityMap, List<ItemJoinModel> finalModelList) {
        for (Map.Entry<String, List<ItemJoinModel>> entry : qualityMap.entrySet()) {
            //判断发送过来的同品质总数是否足够扣减
            String[] keys = entry.getKey().split(CommonConsts.STR_COLON);
            int minusAmount = Integer.parseInt(keys[2]);//资源消耗表里need_quality_item字段对应必要消耗数量
            int sum = entry.getValue().stream().mapToInt(ItemJoinModel::getItemNum).sum();//计算同类型和品质的技能书数量是否足以被扣减
            if (sum < minusAmount) {
                logger.error("玩家id:{}, 技能书品质:{}, 该品质的技能书数量不够!", player.getPlayerId(), keys[1]);
                return ErrorCodeEnum.SKILL_BOOK_QUANTITY_NOT_ENOUGH;
            } else {
                List<ItemJoinModel> corresSkillList = new ArrayList<>();//存储该品质的满足minusAmount扣减的集合
                //依次遍历把满足扣减数量的同品质的ItemJoinModel进行存储, 满足upgSum数量即可
                for (ItemJoinModel model : entry.getValue()) {
                    if (!corresSkillList.isEmpty()) {
                        int sumNum = corresSkillList.stream().mapToInt(ItemJoinModel::getItemNum).sum();
                        if (sumNum >= minusAmount) {
                            break;
                        }
                    }
                    corresSkillList.add(model);
                }

                //判断背包持有数量是否满足扣减数量(corresSkillList中存储同品质的扣减道具均满足配置表里配置的消耗总数)
                int tempNum = 0;//临时存储有多个同品质的道具一次没扣完则遍历去扣
                for (ItemJoinModel model : corresSkillList) {
                    PlayerItemModel playerItemModel = itemService.getItemInfo(player, model.getItemId());
                    if (model.getItemNum() > playerItemModel.getItemNum()) {
                        logger.error("玩家id:{}, 道具id:{}, 客户端传入数量{}大于背包所持有数量{}且不足以被消耗!", player.getPlayerId(), model.getItemId(), model.getItemNum(), playerItemModel.getItemNum());
                        return ErrorCodeEnum.SKILL_QUANTITY_NOT_ENOUGH;
                    } else {
                        //tempNum不为0则表示还需扣减
                        if (tempNum > 0) {
                            //判断tempNum是否大于当前背包该道具的持有数量则消耗当前道具继续遍历下一个同品质道具
                            if (tempNum > playerItemModel.getItemNum()) {
                                finalModelList.add(new ItemJoinModel(model.getItemId(), -playerItemModel.getItemNum(), model.getItemType()));
                                tempNum -= playerItemModel.getItemNum();
                            } else {
                                //如果tempNum小于或等于背包持有的当前道具数量则直接扣减
                                finalModelList.add(new ItemJoinModel(model.getItemId(), -tempNum, model.getItemType()));
                                break;
                            }
                        }
                        //如果传入的数量大于等于必须消耗的数量则把当前minusAmount作为数量减去
                        else if (model.getItemNum() >= minusAmount) {
                            finalModelList.add(new ItemJoinModel(model.getItemId(), -minusAmount, model.getItemType()));
                            break;
                        }
                        //如果传入的数量小于必须消耗的数量则tempNum用于存储后续需要减去的数量
                        else {
                            tempNum = minusAmount - model.getItemNum();
                            finalModelList.add(new ItemJoinModel(model.getItemId(), -model.getItemNum(), model.getItemType()));
                        }
                    }
                }
            }
        }
        return ErrorCodeEnum.SUCCESS;
    }

    /**
     * /4、 判断是否满足养成道具组(消耗指定道具组配置格式: 道具类型:道具ID:数量) (如果是法术升星则执行第4步判断是否满足法术本体消耗)
     */
    private ErrorCodeEnum itemGroupEnough(Player player, int skillId, List<List<Integer>> needItem,
                                          List<ItemJoinModel> finalModelList, List<ItemJoinModel> spendGoldList, List<ItemJoinModel> diamondList,
                                          Integer oneselfNumber) {
        PlayerItemCache playerItemCache = player.cache().getPlayerItemCache();//拿到玩家背包数据
        if (null == playerItemCache || null == playerItemCache.values() || playerItemCache.values().isEmpty()) {
            logger.error("玩家id:{}, 获取背包数据为空!", player.getPlayerId());
            return ErrorCodeEnum.CLIENT_PRAMS_ERROR;
        }
        for (List<Integer> needs : needItem) {
            Optional<PlayerItemModel> first = playerItemCache.values().stream().filter(playerItemModel -> playerItemModel.getItemType() == needs.get(0) && playerItemModel.getItemId() == needs.get(1) && playerItemModel.getItemNum() >= needs.get(2)).findFirst();
            if (!first.isPresent()) {
                logger.error("玩家id:{}, 技能id:{}, 根据技能升星资源消耗配置表need_item字段校验需要消耗的道具数量不足!", player.getPlayerId(), skillId);
                return ErrorCodeEnum.SKILL_QUANTITY_NOT_ENOUGH;
            }
            ItemJoinModel itemJoinModel = new ItemJoinModel(first.get().getItemId(), -needs.get(2), first.get().getItemType());
            finalModelList.add(itemJoinModel);

            //如果当前道具类型为货币类型
            ItemModel itemModel = itemDao.getConfigByKey(itemJoinModel.getItemId());
            if (null == itemModel) {
                logger.error("玩家id:{}, 道具id:{}, 对应道具基础配置表数据为空!", player.getPlayerId(), itemModel.getItemId());
                return ErrorCodeEnum.CONFIG_ERROR;
            }
            if (itemModel.getItemType() == ItemTypeEnum.CURRENCY.getItemType()) {
                //如果道具子类型为金币并且数量小于0则触发【激活期间】花费X金币和【累计期间】花费X金币
                if (itemModel.getItemTypeDetail() == ItemTypeEnum.CurrencyTypeEnum.GOLD.getSubclassType() && itemJoinModel.getItemNum() < 0) {
                    spendGoldList.add(itemJoinModel);
                }
                //如果道具子类型为钻石并且数量小于0则触发【激活期间】花费X钻石和【累计期间】花费X钻石
                else if (itemModel.getItemTypeDetail() == ItemTypeEnum.CurrencyTypeEnum.RECHARGE_DIAMOND.getSubclassType() && itemJoinModel.getItemNum() < 0) {
                    diamondList.add(itemJoinModel);
                }
            }
        }

        //5、 判断是否满足消耗本体数量
        if (null != oneselfNumber && oneselfNumber != 0) {
            //1. 根据条件匹配对应道具
            List<PlayerItemModel> playerItemModels = new ArrayList<>();
            for (PlayerItemModel item : playerItemCache.values()) {
                ItemModel itemModel = itemDao.getConfigByKey(item.getItemId());
                if (null == itemModel) {
                    logger.error("玩家id:{}, 道具id:{}, 对应道具基础配置表数据为空!", player.getPlayerId(), itemModel.getItemId());
                    return ErrorCodeEnum.CONFIG_ERROR;
                }
                if (StringUtils.isNotBlank(itemModel.getItemUseResult()) && StringUtils.isNumeric(itemModel.getItemUseResult())
                        && Integer.valueOf(itemModel.getItemUseResult()).equals(skillId)) {
                    playerItemModels.add(item);
                }
            }
            if (playerItemModels.isEmpty()) {
                logger.error("玩家id:{}, 技能id:{}, 当前背包中没有该匹配的技能书道具!", player.getPlayerId(), skillId);
                return ErrorCodeEnum.CONFIG_ERROR;
            }
            //2. 判断是否满足本地法术消耗
            List<ItemJoinModel> list = new ArrayList<>();
            for (PlayerItemModel itemModel : playerItemModels) {
                int num;
                if (itemModel.getItemNum() >= oneselfNumber) {
                    num = -oneselfNumber;
                } else {
                    num = -itemModel.getItemNum();
                }
                ItemJoinModel itemJoinModel = new ItemJoinModel(itemModel.getItemId(), num, itemModel.getItemType());
                list.add(itemJoinModel);
                //3. 如果满足消耗本体则退出
                if (list.stream().mapToInt(e -> Math.abs(e.getItemNum())).sum() >= oneselfNumber) {
                    break;
                }
            }
            finalModelList.addAll(list);
        }
        return ErrorCodeEnum.SUCCESS;
    }

    /**
     * 触发任务刷新
     */
    private void triggerTaskRefresh(Player player, List<ItemJoinModel> spendGoldList, List<ItemJoinModel> diamondList) {
        //判断当前如果有消耗金币的行为则触发
        if (!spendGoldList.isEmpty()) {
            /*
             * 触发任务进度刷新
             * 任务完成条件类型:
             *      【激活期间】花费X金币
             *      【累计期间】花费X金币
             */
            TaskScheduleRefreshUtil.goldSchedule(player, spendGoldList.stream().mapToInt(e -> Math.abs(e.getItemNum())).sum());
        }
        //判断当前如果有消耗钻石的行为则触发
        if (!diamondList.isEmpty()) {
            /*
             * 触发任务进度刷新
             * 任务完成条件类型:
             *      【激活期间】花费X钻石
             *      【累计期间】花费X钻石
             */
            TaskScheduleRefreshUtil.diamondSchedule(player, diamondList.stream().mapToInt(e -> Math.abs(e.getItemNum())).sum());
        }
    }

    /**
     * 法术升星
     *
     * @param player                玩家信息
     * @param skillRisingStarListRq 协议
     */
    public void skillRisingStar(Player player, SkillRisingStarListRq skillRisingStarListRq) {
        if (0 == skillRisingStarListRq.getSkillId() || null == skillRisingStarListRq.getSkillRisingStar() || skillRisingStarListRq.getSkillRisingStar().isEmpty()) {
            logger.error("玩家id:{}, 协议号:{}, 技能升级时技能id为空或资源消耗列表为空!", player.getPlayerId(), skillRisingStarListRq.getCode());
            CmdUtil.sendErrorMsg(player.getSession(), skillRisingStarListRq.getCode(), ErrorCodeEnum.CLIENT_PRAMS_ERROR);
            return;
        }
        PlayerSkillModel playerSkillModel = player.cache().getPlayerSkillCache().getPlayerSkillModelMap().get(skillRisingStarListRq.getSkillId());//玩家技能
        if (null == playerSkillModel) {
            logger.error("玩家id:{}, 技能id:{}, 当前技能不存在!", player.getPlayerId(), skillRisingStarListRq.getSkillId());
            CmdUtil.sendErrorMsg(player.getSession(), skillRisingStarListRq.getCode(), ErrorCodeEnum.SKILL_NOT_EXISTED);
            return;
        }
        SkillCharacterBasicModel skillCharacterBasicModel = skillCharacterBasicDao.getConfigByKey(playerSkillModel.getSkillId());//技能基础
        SkillStarEffectModel skillStarEffectModel = skillStarEffectDao.getConfigByKey(playerSkillModel.getSkillId());//技能升星
        if (null == skillCharacterBasicModel || null == skillStarEffectModel) {
            logger.error("玩家id:{}, 技能id:{}, 查询技能基础或技能升星配置表数据为空!", player.getPlayerId(), skillRisingStarListRq.getSkillId());
            CmdUtil.sendErrorMsg(player.getSession(), skillRisingStarListRq.getCode(), ErrorCodeEnum.CONFIG_ERROR);
            return;
        }

        ErrorCodeEnum errorCodeEnum;//错误码
        //最终更新道具列表
        List<ItemJoinModel> finalModelList = new ArrayList<>();

        //1、 判断当前技能等级是否达到升星条件
        errorCodeEnum = this.conditionJudgment(player, skillRisingStarListRq.getSkillId(), skillStarEffectModel, playerSkillModel, false);
        if (!errorCodeEnum.equals(ErrorCodeEnum.SUCCESS)) {
            CmdUtil.sendErrorMsg(player.getSession(), skillRisingStarListRq.getCode(), errorCodeEnum);
            return;
        }

        //根据技能品质和技能星级匹配技能升星消耗资源表数据
        Optional<SkillStarUseModel> skillUpgOpt = skillStarUseDao.getConfigs().stream()
                .filter(e -> e.getItemQualityType().equals(skillCharacterBasicModel.getItemQualityType()))
                .filter(k -> k.getSkillStarLevel() == playerSkillModel.getSkillStarLevel()).findFirst();
        if (!skillUpgOpt.isPresent()) {
            logger.error("玩家id:{}, 技能id:{}, 未匹配到技能升级所需的资源数据!", player.getPlayerId(), playerSkillModel.getSkillId());
            CmdUtil.sendErrorMsg(player.getSession(), skillRisingStarListRq.getCode(), ErrorCodeEnum.CONFIG_ERROR);
            return;
        }
        SkillStarUseModel skillStarUseModel = skillUpgOpt.get();

        //2、根据品质进行分类
        Map<String, List<ItemJoinModel>> qualityMap = new HashMap<>();//存储对应品质的道具  key-道具类型:道具品质:数量 value-item
        for (SkillRisingStar upg : skillRisingStarListRq.getSkillRisingStar()) {
            errorCodeEnum = this.qualitySort(player, upg.getItemId(), upg.getItemNum(), skillStarUseModel.getNeedQualityItem(), qualityMap);
            if (!errorCodeEnum.equals(ErrorCodeEnum.SUCCESS)) {
                CmdUtil.sendErrorMsg(player.getSession(), skillRisingStarListRq.getCode(), errorCodeEnum);
                return;
            }
        }

        //3、判断同类型技能书是否足够
        errorCodeEnum = this.skillBookEnough(player, qualityMap, finalModelList);
        if (!errorCodeEnum.equals(ErrorCodeEnum.SUCCESS)) {
            CmdUtil.sendErrorMsg(player.getSession(), skillRisingStarListRq.getCode(), errorCodeEnum);
            return;
        }

        //4、判断是否满足养成道具组(消耗指定道具组配置格式: 道具类型:道具ID:数量)
        //5、 判断是否满足消耗本体数量
        List<ItemJoinModel> spendGoldList = new ArrayList<>();//存储任务花费多少金币list
        List<ItemJoinModel> diamondList = new ArrayList<>();//存储任务花费多少钻石list
        errorCodeEnum = this.itemGroupEnough(player, skillRisingStarListRq.getSkillId(), skillStarUseModel.getNeedItem(),
                finalModelList, spendGoldList, diamondList, skillStarUseModel.getOneselfNumber());
        if (!errorCodeEnum.equals(ErrorCodeEnum.SUCCESS)) {
            CmdUtil.sendErrorMsg(player.getSession(), skillRisingStarListRq.getCode(), errorCodeEnum);
            return;
        }

        //6、调用背包更新方法
        ItemResponse itemResponse = itemService.batchUpdateItemPush(player, finalModelList, ItemOperateEnum.USE, ItemPromp.GENERIC);
        if (itemResponse.getCodeEnum().equals(ErrorCodeEnum.SUCCESS)) {
            //技能升星成功
            int starLevel = 0;
            for (int i = 0; i < skillStarEffectModel.getStarLevel().size(); i++) {
                if (playerSkillModel.getSkillStarLevel() == skillStarEffectModel.getStarLevel().get(i)) {
                    //如果是最后的下标
                    if (i != 0 && (i + 1) == skillStarEffectModel.getStarLevel().get(i - 1)) {
                        starLevel = skillStarEffectModel.getStarLevel().get(i);
                    } else {
                        starLevel = skillStarEffectModel.getStarLevel().get(i + 1);
                    }
                    break;
                }
            }
            playerSkillModel.setSkillStarLevel(starLevel);
            player.cache().getPlayerSkillCache().updateCache(playerSkillModel);
            playerSkillDao.queueUpdate(playerSkillModel);
            //组装协议
            SkillRisingStarRs skillRisingStarRs = new SkillRisingStarRs();
            skillRisingStarRs.setSkillId(skillRisingStarListRq.getSkillId());
            skillRisingStarRs.setStatus(true);
            skillRisingStarRs.setSkillStarLevel(playerSkillModel.getSkillStarLevel());
            //推送技能升级协议
            CmdUtil.sendMsg(player, skillRisingStarRs);
            logger.debug("玩家id:{}, 协议号:{}, 请求协议:{}, 技能升星处理完毕, 返回协议:{}", player.getPlayerId(), skillRisingStarListRq.getCode(), skillRisingStarListRq.toString(), skillRisingStarRs.toString());
            //判断当前如果有消耗货币的行为则触发
            this.triggerTaskRefresh(player, spendGoldList, diamondList);
        }
    }

    /**
     * 属性计算拿到表里原始数据
     *
     * @param skillAttrId
     * @return
     */
    public Object getValue(int skillAttrId, int effectId) {
        SkillAttrBasicModel skillAttrBasicModel = skillAttrBasicDao.getConfigByKey(skillAttrId);
        if (skillAttrBasicModel == null) {
            logger.error("EquipService flush  :skillAttrBasicModel is not exist {} ", skillAttrId);
            return 0;
        }
        Object object = null;

        if (skillAttrBasicModel.getSkillMain() == SkillMainConsts.skill_character_basic) {
            object = skillCharacterBasicDao.getConfigByKey(effectId);
            if (object == null) {
                logger.error("skillCharacterBasicModel is not exist {} ", effectId);
                return 0;
            }
        } else if (skillAttrBasicModel.getSkillMain() == SkillMainConsts.skill_character_carrier) {
            object = skillCharacterCarrierDao.getConfigByKey(effectId);
            if (object == null) {
                logger.error("skillCharacterCarrierModel is not exist {} ", effectId);
                return 0;
            }

        } else if (skillAttrBasicModel.getSkillMain() == SkillMainConsts.skill_character_effec) {
            object = skillCharacterEffectDao.getConfigByKey(effectId);
            if (object == null) {
                logger.error("skillCharacterEffectModel is not exist {} ", effectId);
                return 0;
            }

        } else if (skillAttrBasicModel.getSkillMain() == SkillMainConsts.skill_character_state) {
            object = skillCharacterStateDao.getConfigByKey(effectId);
            if (object == null) {
                logger.error("skillCharacterStateModel is not exist {} ", effectId);
                return 0;
            }
        }
        String skill_parameter = skillAttrBasicModel.getSkillParameter().replace("_", "");
        Object value = ReflexUtil.getValueByName(object, skill_parameter);
        return value;
    }

    public int getVipSoulNum(int vip) {
        int num = 0;
        for (SkillSoalUpgradeModel skillSoalUpgradeModel : skillSoalUpgradeDao.getConfigs()) {
            if (skillSoalUpgradeModel.getVip() == vip) {
                num = num > skillSoalUpgradeModel.getPosIdx() ? num : skillSoalUpgradeModel.getPosIdx();
            }
        }
        return num;
    }
}