package com.dykj.rpg.game.module.equip.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.dykj.rpg.common.attribute.AttributeUnit;
import com.dykj.rpg.common.attribute.consts.DateFixConstant;
import com.dykj.rpg.common.config.constant.ConfigEnum;
import com.dykj.rpg.common.config.dao.ConfigDao;
import com.dykj.rpg.common.config.dao.EqEntryBoxDao;
import com.dykj.rpg.common.config.dao.EqEntryEffectDao;
import com.dykj.rpg.common.config.dao.EqEntryNumDao;
import com.dykj.rpg.common.config.dao.EquipBasicDao;
import com.dykj.rpg.common.config.dao.EquipUpgradeDao;
import com.dykj.rpg.common.config.dao.ValueRangeDao;
import com.dykj.rpg.common.config.model.EqEntryEffectModel;
import com.dykj.rpg.common.config.model.EquipBasicModel;
import com.dykj.rpg.common.config.model.EquipUpgradeModel;
import com.dykj.rpg.common.consts.CalculationEnum;
import com.dykj.rpg.common.consts.EffectType;
import com.dykj.rpg.common.consts.ItemOperateEnum;
import com.dykj.rpg.common.consts.ItemTypeEnum;
import com.dykj.rpg.common.data.dao.EquipEntryDao;
import com.dykj.rpg.common.data.dao.EquipPosDao;
import com.dykj.rpg.common.data.dao.PlayerEquipDao;
import com.dykj.rpg.common.data.model.EquipEntryModel;
import com.dykj.rpg.common.data.model.EquipPosModel;
import com.dykj.rpg.common.data.model.PlayerEquipModel;
import com.dykj.rpg.common.data.model.logic.Entry;
import com.dykj.rpg.common.data.model.logic.EntryUnit;
import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.module.attribute.service.AttributeService;
import com.dykj.rpg.game.module.cache.EquipPosCache;
import com.dykj.rpg.game.module.cache.PlayerEquipCache;
import com.dykj.rpg.game.module.cache.logic.EquipCache;
import com.dykj.rpg.game.module.equip.logic.EntryResult;
import com.dykj.rpg.game.module.item.consts.ItemPromp;
import com.dykj.rpg.game.module.item.response.ItemJoinModel;
import com.dykj.rpg.game.module.item.response.ItemResponse;
import com.dykj.rpg.game.module.item.service.ItemService;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.task.logic.refresh.TaskScheduleRefreshUtil;
import com.dykj.rpg.game.module.util.CmdUtil;
import com.dykj.rpg.protocol.equip.EntryRs;
import com.dykj.rpg.protocol.equip.EntryUnitRs;
import com.dykj.rpg.protocol.equip.EquipDownRq;
import com.dykj.rpg.protocol.equip.EquipDownRs;
import com.dykj.rpg.protocol.equip.EquipPosUpRs;
import com.dykj.rpg.protocol.equip.EquipRs;
import com.dykj.rpg.protocol.equip.EquipUpRq;
import com.dykj.rpg.protocol.equip.EquipUpRs;
import com.dykj.rpg.protocol.item.ItemRs;

/**
 * @author jyb
 * @date 2020/11/23 11:38
 * @Description
 */
@Service
public class EquipService {
    @Resource
    private EquipBasicDao equipBasicDao;
    @Resource
    private EqEntryNumDao eqEntryNumDao;
    @Resource
    private EqEntryBoxDao eqEntryBoxDao;
    @Resource
    private EqEntryEffectDao eqEntryEffectDao;
    @Resource
    private ValueRangeDao valueRangeDao;
    @Resource
    private PlayerEquipDao playerEquipDao;
    @Resource
    private EquipEntryDao equipEntryDao;
    @Resource
    private AttributeService attributeService;
    @Resource
    private EquipPosDao equipPosDao;
    @Resource
    private EquipUpgradeDao equipUpgradeDao;
    @Resource
    private ConfigDao configDao;
    @Resource
    private ItemService itemService;


    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 初始化装备信息
     *
     * @param player
     */
    public void initPlayerEquipCache(Player player) {
        PlayerEquipCache playerEquipCache = player.cache().getPlayerEquipCache();
        if (playerEquipCache == null) {
            playerEquipCache = new PlayerEquipCache();
            player.cache().setPlayerEquipCache(playerEquipCache);
            List<PlayerEquipModel> playerEquipModelList = playerEquipDao.getEquipEntryModels(player.getPlayerId());
            if (playerEquipModelList != null && playerEquipModelList.size() > 0) {
                //初始化装备词条
                for (PlayerEquipModel playerEquip : playerEquipModelList) {
                    EquipCache equipCache = new EquipCache(playerEquip);
                    List<EquipEntryModel> equipEntryModels = equipEntryDao.getEquipEntryModels(playerEquip.getInstanceId());
                    equipEntryModels.forEach(equipEntryModel -> equipCache.getEquipEntryModelMap().put(equipEntryModel.getPosition(), equipEntryModel));
                    playerEquipCache.getEquipCacheMap().put(equipCache.getPlayerEquipModel().getInstanceId(), equipCache);
                }
            }
        }
        //计算装备自增id
        playerEquipCache.calculateSequence();
    }

    /**
     * 初始化一件装备
     *
     * @param equipId
     * @return
     */
    public EquipCache initEquip(int equipId, Player player) {
        EquipBasicModel equipBasicModel = equipBasicDao.getConfigByKey(equipId);
        if (equipBasicModel == null) {
            logger.error("equip config {}  not exist ", equipId);
            return null;
        }
        PlayerEquipCache playerEquipCache = player.cache().getPlayerEquipCache();
        if (playerEquipCache == null) {
            playerEquipCache = new PlayerEquipCache();
            playerEquipCache.calculateSequence();
            player.cache().setPlayerEquipCache(playerEquipCache);
        }
        PlayerEquipModel playerEquipModel = new PlayerEquipModel();
        playerEquipModel.setEquipId(equipId);
        playerEquipModel.setPlayerId(player.getPlayerId());
        playerEquipModel.setEquipPos(-1);
        playerEquipModel.setInstanceId(playerEquipCache.generateInstanceId(player.getPlayerId()));
        int equipLock = 0;
        if (null != equipBasicModel.getEquipLock() && equipBasicModel.getEquipLock() == 2)
        {
            equipLock = 1;
        }
        playerEquipModel.setEquipLock(equipLock);
        EquipCache equipCache = new EquipCache(playerEquipModel);
        playerEquipCache.getEquipCacheMap().put(equipCache.getInstanceId(), equipCache);


        //装备主属性
        EquipEntryModel mainEquipEntryModel = randomEquipEntry(equipBasicModel.getEquipMainattribute(), player.cache().getPlayerInfoModel().getProfession(), playerEquipModel.getInstanceId());
        logger.debug("playerEquipModel {} ", playerEquipModel.toString());
        //获得所有的词条box
        List<Integer> effectBox = getEntryEffectBox(equipBasicModel);

        List<EquipEntryModel> equipEntryModels = new ArrayList<>();
        if (effectBox != null && effectBox.size() > 0) {
            for (Integer effectBoxId : effectBox) {
                //生成装备的词条
                EquipEntryModel equipEntryModel = randomEquipEntry(effectBoxId, player.cache().getPlayerInfoModel().getProfession(), playerEquipModel.getInstanceId());
                if (equipEntryModel != null) {
                    equipEntryModels.add(equipEntryModel);
                }
            }
            //给词条排序
            if (equipEntryModels.size() > 0) {
                refreshEffectIndex(equipEntryModels);
            }
        }
        //装载主属性
        mainEquipEntryModel.setPosition(-1);//主属性位置为-1
        equipEntryDao.queueInsert(mainEquipEntryModel);
        logger.debug("EquipEntryModel  main {} ", mainEquipEntryModel.toString());
        equipEntryModels.add(mainEquipEntryModel);
        equipEntryModels.forEach(equipEntryModel -> equipCache.getEquipEntryModelMap().put(equipEntryModel.getPosition(), equipEntryModel));
        //计算基础积分
        playerEquipModel.setBaseScore(calculationScore(equipCache));
        //入库
        playerEquipDao.queueInsert(playerEquipModel);
        return equipCache;
    }



    /**
     * 拿出一件装备，effect_box的集合
     * <p>
     * 1、先随机出该装备的最大词条数量
     * <p>
     * 2、取出必出的词条
     * <p>
     * 3、剩余的数量取equip_entry_effect_box_id_group 去随机 补充数量
     *
     * @param equipBasicModel
     * @return
     */
    public List<Integer> getEntryEffectBox(EquipBasicModel equipBasicModel) {
        List<Integer> result = new ArrayList<>();
        //生成词条的数量
        int equipEntryNum = eqEntryNumDao.getEqEntryNum(equipBasicModel.getEquipEntryId());
        logger.info("getEntryEffectBox equipEntryNum {} ", equipEntryNum);
        //先添加必出的词条box
        if (equipBasicModel.getEquipNecessaryEntry() != null && equipBasicModel.getEquipNecessaryEntry().size() > 0) {
            result.addAll(equipBasicModel.getEquipNecessaryEntry());
            equipEntryNum = equipEntryNum - equipBasicModel.getEquipNecessaryEntry().size();
        }

        //添加随机的词条box
        if (equipEntryNum > 0) {
            List<Integer> randomResult = equipBasicDao.getEntryEffectBox(equipBasicModel, equipEntryNum);
            result.addAll(randomResult);
        }
        return result;
    }

    /**
     * 随机词条属性
     *
     * @param equipEntryEffectBoxId
     * @param characterId
     * @return
     */
    public EquipEntryModel randomEquipEntry(int equipEntryEffectBoxId, int characterId, long instanceId) {
        //随机词条的id
        int equip_entry_effect_id = eqEntryBoxDao.randomEntryEffectId(equipEntryEffectBoxId, characterId);
        EqEntryEffectModel eqEntryEffectModel = eqEntryEffectDao.getConfigByKey(equip_entry_effect_id);
        if (eqEntryEffectModel == null) {
            logger.error("EquipService randomEquipEntry  error :equip_entry_effect_id {} ", equip_entry_effect_id);
        }
        //随机值域
        int[] values = valueRangeDao.getRandomValue(eqEntryEffectModel.getEntryValueRange());
        if (eqEntryEffectModel.getEntryBigType().size() != values.length) {
            logger.error("randomEquipEntry error  equip_entry_effect_id {} ", equip_entry_effect_id);
            return null;
        }
        //拼接每条词条单元结果
        List<String> effectResult = getEntryEffectResult(eqEntryEffectModel.getEntryBigType(), values);
        if (effectResult != null && effectResult.size() > 0) {
            EntryUnit[] entryUnits = new EntryUnit[effectResult.size()];
            for (int i = 0; i < effectResult.size(); i++) {
                entryUnits[i] = new EntryUnit(effectResult.get(i));
            }
            Entry entry = new Entry(entryUnits);
            EquipEntryModel equipEntryModel = new EquipEntryModel();
            equipEntryModel.setInstanceId(instanceId);
            equipEntryModel.updateEntry(entry);
            equipEntryModel.setEntryEffectId(equip_entry_effect_id);
            return equipEntryModel;
        }
        return null;
    }

    /**
     * @param entryBigType [1:1:0,2:1001:101]
     * @param values       把对于的值拼接到每一条的后面 存入数据库 1:1:0:value,2:1001:101:value
     * @return
     */
    private List<String> getEntryEffectResult(List<String> entryBigType, int[] values) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < entryBigType.size(); i++) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(entryBigType.get(i)).append(":").append(values[i]);
            result.add(stringBuffer.toString());
        }
        return result;
    }

    /**
     * 给词条排序
     */
    private void refreshEffectIndex(List<EquipEntryModel> equipEntryModels) {
        Collections.sort(equipEntryModels, (o1, o2) -> {
            int rank1 = eqEntryEffectDao.getConfigByKey(o1.getEntryEffectId()).getEntryRank();
            int rank2 = eqEntryEffectDao.getConfigByKey(o2.getEntryEffectId()).getEntryRank();
            return rank1 - rank2;
        });
        int i = 0;
        for (EquipEntryModel e : equipEntryModels) {
            e.setPosition(i);
            //入库
            equipEntryDao.queueInsert(e);
            logger.debug("EquipEntryModel  random {} ", e.toString());
            i++;
        }
    }

    /**
     * @param player
     */
    @Deprecated
    public void destroyEquipCache(Player player) {
//        PlayerEquipCache playerEquipCache = playerEquipDao.getCache(Long.valueOf(player.getPlayerId()));
//        if (playerEquipCache != null) {
//            List<PlayerEquipModel> playerEquipModels = playerEquipCache.all();
//            for (PlayerEquipModel p : playerEquipModels) {
//                equipEntryDao.removeCache(p.getInstanceId());
//            }
//            playerEquipDao.removeCache(playerEquipCache.cachePrimaryKey());
//        }
//        equipPosDao.removeCache(Long.valueOf(player.getPlayerId()));
    }

    /**
     * 添加一个装备
     *
     * @param equipId
     * @param player
     * @param itemOperateEnum
     * @return
     */
    public ItemRs addEquip(int equipId, Player player, ItemOperateEnum itemOperateEnum) {
        EquipCache equipCache = initEquip(equipId, player);
        ItemRs itemRs = itemRs(equipCache);

        /*
         * 触发任务进度刷新
         * 任务完成条件类型:
         *      【激活期间】获得X件Y品质（见下文，品质相关）或以上装备
         *      【累计期间】获得X件Y品质（见下文，品质相关）或以上装备
         */
        TaskScheduleRefreshUtil.equipSchedule(player, equipCache.getPlayerEquipModel());
        return itemRs;
    }


    public ItemRs itemRs(EquipCache equipCache) {
        ItemRs itemRs = new ItemRs();
        itemRs.setInstId(equipCache.getPlayerEquipModel().getInstanceId());
        itemRs.setItemId(equipCache.getPlayerEquipModel().getEquipId());
        itemRs.setItemType(ItemTypeEnum.EQUIPMENT.getItemType());
        itemRs.setEquip(equipRs(equipCache));
        itemRs.setItemNum(1);
        return itemRs;
    }


    public EquipRs equipRs(EquipCache equipCache) {
        EquipRs equipRs = new EquipRs();
        equipRs.setEquipPos(equipCache.getPlayerEquipModel().getEquipPos());
        equipRs.setEquipScore(equipCache.getPlayerEquipModel().getBaseScore());
        equipRs.setEquipTotalScore(calculationTotalScore(equipCache));
        equipRs.setEquipLock(equipCache.getPlayerEquipModel().getEquipLock());
        for (EquipEntryModel e : equipCache.getEquipEntryModelMap().values()) {
            EntryRs entryRs = new EntryRs();
            entryRs.setEntryEffectId(e.getEntryEffectId());
            entryRs.setPosition(e.getPosition());
            EqEntryEffectModel eqEntryEffectModel = eqEntryEffectDao.getConfigByKey(e.getEntryEffectId());
            int index = 0;
            for (EntryUnit entryUnit : e.getEntry().getEntryUnits()) {
                EntryUnitRs entryUnitRs = new EntryUnitRs();
                entryUnitRs.setType(entryUnit.getType());
                entryUnitRs.setId(entryUnit.getId());
                entryUnitRs.setTypeId(entryUnit.getTypeId());
                entryUnitRs.setPram(entryUnit.getPram());
                entryUnitRs.setEntryValueRangeId(eqEntryEffectModel.getEntryValueRange().get(index));
                entryUnitRs.setValue(entryUnit.getValue());
                entryRs.getEntryUnitRs().add(entryUnitRs);
                index++;
            }
            equipRs.getEntries().add(entryRs);
        }
        return equipRs;
    }


    /**
     * 获得一个玩家的所有装备信息
     *
     * @param player
     * @return
     */
    public List<ItemRs> getEquips(Player player) {
        List<ItemRs> itemRs = new ArrayList<>();
        PlayerEquipCache playerEquipCache = player.cache().getPlayerEquipCache();
        if (playerEquipCache == null) {
            return itemRs;
        }
        for (EquipCache equipCache : playerEquipCache.getEquipCacheMap().values()) {
            itemRs.add(itemRs(equipCache));
        }
        return itemRs;
    }

    /**
     * 穿戴装备
     */
    public ErrorCodeEnum equipUP(Player player, EquipUpRq equipUpRq) {
        PlayerEquipCache playerEquipCache = player.cache().getPlayerEquipCache();
        if (playerEquipCache == null) {
            logger.error("EquipUpHandler doHandler error : playerEquipCache not exist {}", player.getPlayerId());
            return ErrorCodeEnum.CLIENT_PRAMS_ERROR;
        }
        EquipCache equipCache = playerEquipCache.getEquipCacheMap().get(equipUpRq.getInstId());
        if (equipCache == null) {
            logger.error("EquipUpHandler doHandler error : playerEquipModel not exist {}", equipUpRq.getInstId());
            return ErrorCodeEnum.CLIENT_PRAMS_ERROR;
        }
        EquipBasicModel equipBasicModel = equipBasicDao.getConfigByKey(equipCache.getPlayerEquipModel().getEquipId());
        if (equipBasicModel == null) {
            logger.error("EquipUpHandler doHandler error : equipBasicModel not exist {}", equipCache.getPlayerEquipModel().getEquipId());
            return ErrorCodeEnum.CLIENT_PRAMS_ERROR;
        }
        if (!equipBasicModel.getEquipPartType().equals(equipUpRq.getEquipPos())) {
            logger.error("EquipUpHandler doHandler error : equipBasicModel position error {}", equipUpRq.toString());
            return ErrorCodeEnum.EQUIP_PART_ERROR;
        }
        if (!equipBasicModel.getEquipCharacterId().contains(player.cache().getPlayerInfoModel().getProfession())) {
            return ErrorCodeEnum.EQUIP_CHARACTER_ERROR;

        }
        if (player.cache().getPlayerInfoModel().getLv() < equipBasicModel.getEquipLevel()) {
            return ErrorCodeEnum.PLAYER_LEVEL_ERROR;
        }

        for (List<Integer> attributes : equipBasicModel.getEquipWearNeedAttribute()) {
            int value = player.cache().getAttributeCache().getAttributeValue(attributes.get(0), attributes.get(1));
            if (value < attributes.get(3)) {
                return ErrorCodeEnum.PLAYER_LEVEL_ERROR;
            }

        }

        EquipUpRs equipUpRs = new EquipUpRs();
        EquipCache downEquip = playerEquipCache.getByEquipPos(equipUpRq.getEquipPos());
        if (downEquip != null) {
            downEquip.getPlayerEquipModel().setEquipPos(-1);
            equipUpRs.setDownItem(itemRs(downEquip));
            playerEquipDao.queueUpdate(downEquip.getPlayerEquipModel());
        }
        equipCache.getPlayerEquipModel().setEquipPos(equipUpRq.getEquipPos());
        equipUpRs.setUpItem(itemRs(equipCache));
        playerEquipDao.queueUpdate(equipCache.getPlayerEquipModel());
        //TODO 这里要更新玩家属性相关
        CmdUtil.sendMsg(player, equipUpRs);
        return ErrorCodeEnum.SUCCESS;
    }


    /**
     * 卸下装备
     *
     * @param player
     * @param
     * @param equipUpRq
     * @return
     */
    public ErrorCodeEnum equipDown(Player player, EquipDownRq equipUpRq) {
        PlayerEquipCache playerEquipCache = player.cache().getPlayerEquipCache();
        if (playerEquipCache == null) {
            logger.error("EquipDownHandler doHandler error : playerEquipCache not exist {}", player.getPlayerId());
            return ErrorCodeEnum.CLIENT_PRAMS_ERROR;
        }

        EquipCache equipCache = playerEquipCache.getByEquipPos(equipUpRq.getEquipPos());
        if (equipCache == null) {
            logger.error("EquipDown doHandler error : playerEquipModel is null {}", equipUpRq.getEquipPos());
            return ErrorCodeEnum.CLIENT_PRAMS_ERROR;
        }
        EquipBasicModel equipBasicModel = equipBasicDao.getConfigByKey(equipCache.getPlayerEquipModel().getEquipId());
        if (equipBasicModel == null) {
            logger.error("EquipDown doHandler error : equipBasicModel not exist {}", equipCache.getPlayerEquipModel().getEquipId());
            return ErrorCodeEnum.CLIENT_PRAMS_ERROR;
        }

        EquipDownRs equipUpRs = new EquipDownRs();
        equipCache.getPlayerEquipModel().setEquipPos(-1);
        equipUpRs.setItem(itemRs(equipCache));
        playerEquipDao.queueUpdate(equipCache.getPlayerEquipModel());
        //TODO 这里要更新玩家属性相关
        CmdUtil.sendMsg(player, equipUpRs);
        return ErrorCodeEnum.SUCCESS;
    }

    /**
     * 玩家的装备属性词条
     *
     * @param player
     * @return
     */
    public Map<String, Integer> equipAttribute(Player player) {
        return attributeService.flush(attributes(player, EffectType.ATTRIBUTE));
    }

    public List<AttributeUnit> attributes(Player player, int type) {
        List<AttributeUnit> attributeUnits = new ArrayList<>();
        PlayerEquipCache playerEquipCache = player.cache().getPlayerEquipCache();
        if (playerEquipCache != null) {
            for (EquipCache equip : playerEquipCache.getDressEquips()) {

                for (EquipEntryModel equipEntry : equip.getEquipEntryModelMap().values()) {
                    for (EntryUnit e : equipEntry.getEntry().getEntryUnits()) {
                        if (e.getType() != type) {
                            continue;
                        }
                        attributeUnits.add(new AttributeUnit(e.getId(), e.getTypeId(), e.getPram(), e.getValue()));
                    }
                }
            }
        }
        return attributeUnits;
    }

    /**
     * 玩家的装备技能词条
     *
     * @param player
     * @return
     */
    public Map<Integer, List<EntryResult>> skillAttribute(Player player) {
        Map<Integer, List<EntryResult>> entryResults = new HashMap<>();
        PlayerEquipCache playerEquipCache = player.cache().getPlayerEquipCache();
        if (playerEquipCache != null) {
            for (EquipCache equipCache : playerEquipCache.getEquipCacheMap().values()) {

                for (EquipEntryModel equipEntry : equipCache.getEquipEntryModelMap().values()) {
                    EqEntryEffectModel eqEntryEffectModel = eqEntryEffectDao.getConfigByKey(equipEntry.getEntryEffectId());
                    int i = 0;
                    for (EntryUnit e : equipEntry.getEntry().getEntryUnits()) {
                        if (e.getType() != EffectType.SKILL) {
                            continue;
                        }
                        EntryResult entryResult = new EntryResult();
                        entryResult.setSkillId(e.getId());
                        entryResult.setId(e.getTypeId());
                        entryResult.setEffectId(eqEntryEffectModel.getType().get(i));
                        entryResult.setValue(e.getValue());
                        if (e.getPram() == DateFixConstant.ZHI_XIU_ZHENG || e.getPram() == DateFixConstant.ZENG_BI_XIU_ZHENG || e.getPram() == DateFixConstant.JIAN_BI_XIU_ZHENG) {
                            //这三周修正都在为DateFixConstant.ZHI_XIU_ZHENG 做服务 直接合并在一起
                            entryResult.setType(DateFixConstant.ZHI_XIU_ZHENG);
                            List<EntryResult> results = entryResults.get(DateFixConstant.ZHI_XIU_ZHENG);
                            if (results == null) {
                                results = new ArrayList<>();
                                entryResults.put(DateFixConstant.ZHI_XIU_ZHENG, results);
                            }
                            //合并属性
                            subAttributeEntryResult(results, entryResult, e.getPram());
//                            //计算属性
//                            flush(results);
                        } else {
                            entryResult.setType(e.getPram());
                            List<EntryResult> results = entryResults.get(e.getPram());
                            if (results == null) {
                                results = new ArrayList<>();
                                entryResults.put(e.getPram(), results);
                            }
                            results.add(entryResult);
                        }
                        i++;
                    }
                }
            }
        }

        return entryResults;
    }

    /**
     * 将装备的属性计算 值修正 增比修正 减比修正 合并
     *
     * @param entryResults
     * @param entryResult
     */
    private void subAttributeEntryResult(List<EntryResult> entryResults, EntryResult entryResult, int realType) {
        if (entryResults.contains(entryResult)) {
            for (EntryResult e : entryResults) {
                if (e.equals(entryResult)) {
                    if (realType == DateFixConstant.ZHI_XIU_ZHENG) {
                        e.setValue(e.getValue() + entryResult.getValue());
                    } else if (realType == DateFixConstant.ZENG_BI_XIU_ZHENG) {
                        e.setIncrease(e.getIncrease() + entryResult.getValue());
                    } else {
                        e.setReduction(e.getReduction() + entryResult.getValue());
                    }
                    break;
                }
            }
        } else {
            entryResults.add(entryResult);
        }
    }

    /**
     * 计算装备词条评分
     *
     * @param equipCache
     * @return
     */
    public int calculationScore(EquipCache equipCache) {
        int score = 0;
        for (EquipEntryModel e : equipCache.getEquipEntryModelMap().values()) {
            EqEntryEffectModel eqEntryEffectModel = eqEntryEffectDao.getConfigByKey(e.getEntryEffectId());
            if (e.getEntry() != null && e.getEntry().getEntryUnits() != null && e.getEntry().getEntryUnits().length > 0) {
                for (int i = 0; i < e.getEntry().getEntryUnits().length; i++) {
                    int scorePram = eqEntryEffectModel.getEntryScore().get(i);
                    score = score + scorePram * e.getEntry().getEntryUnits()[i].getValue();
                }
            }
        }
        return score;
    }

    /**
     * 计算一件装备总评分
     *
     * @param equipCache
     * @return
     */
    public int calculationTotalScore(EquipCache equipCache) {
        int base = calculationScore(equipCache);
        //TODO 后续还有很多养成
        return base;
    }


    /**
     * 获得装备栏养成
     *
     * @param player
     * @return
     */
    public List<AttributeUnit> getEquipPosAttribute(Player player) {
        List<AttributeUnit> attributeUnitList = new ArrayList<>();
        EquipPosCache equipPosCache = player.cache().getEquipPosCache();
        if (equipPosCache != null) {
            for (EquipPosModel equipPosModel : equipPosCache.getEquipPosModelMap().values()) {
                List<AttributeUnit> attributeUnits = new ArrayList<>();
                if (equipPosModel == null) {
                	continue;
                }
                EquipUpgradeModel equipUpgradeModel = equipUpgradeDao.getEquipUpgrade(equipPosModel.getPos(), equipPosModel.getPosLv(), player.cache().getPlayerInfoModel().getProfession());
                equipUpgradeModel.getUpgradeAttribute().forEach((list) -> attributeUnits.add(new AttributeUnit(list)));
                attributeUnitList.addAll(attributeUnits);
            }
        }
        return attributeUnitList;
    }


	public ErrorCodeEnum equipPosUp(Player player, int pos) {
    	EquipPosCache equipPosCache = player.cache().getEquipPosCache();
        boolean cacheFlag = false;
        if (equipPosCache == null) {
            cacheFlag = true;
            equipPosCache = new EquipPosCache();
        }
        EquipPosModel equipPosModel = equipPosCache.getEquipPosModelMap().get(pos);
        boolean posFlag = false;
        if (equipPosModel == null) {
            posFlag = true;
            equipPosModel = new EquipPosModel();
            equipPosModel.setPlayerId(player.getPlayerId());
            equipPosModel.setPos(pos);
            equipPosModel.setPosLv(1);
            equipPosCache.getEquipPosModelMap().put(equipPosModel.getPos(),equipPosModel);
        }
        if (equipPosModel.getPosLv() >= Integer.valueOf(configDao.getConfigByKey(ConfigEnum.EQMAXLV.getConfigType()).getValue())) {
            return ErrorCodeEnum.EQUIP_UP_MAX_LV;
        }
        if (equipPosModel.getPosLv() >= player.cache().getPlayerInfoModel().getLv()) {
            return ErrorCodeEnum.PLAYER_LEVEL_ERROR;
        }

        EquipUpgradeModel equipUpgradeModel = equipUpgradeDao.getEquipUpgrade(equipPosModel.getPos(), equipPosModel.getPosLv() + 1, player.cache().getPlayerInfoModel().getProfession());
        if (equipUpgradeModel == null) {
            logger.error("EquipPosUpHandler error equipUpgradeModel is  null pos {}  lv {} profession {}", equipPosModel.getPos(), equipPosModel.getPosLv() + 1, player.cache().getPlayerInfoModel().getProfession());
            return ErrorCodeEnum.DATA_ERROR;
        }
        ItemResponse itemResponse = itemService.batchUpdateItemPush(player, ItemJoinModel.items(equipUpgradeModel.getNeedItem(), CalculationEnum.CALCULATION), ItemOperateEnum.DECOMPOSITION, ItemPromp.GENERIC);
        if (itemResponse.getCodeEnum().equals(ErrorCodeEnum.SUCCESS)) {
            equipPosModel.setPosLv(equipPosModel.getPosLv() + 1);
            if (cacheFlag) {
            	equipPosCache.getEquipPosModelMap().put(equipPosModel.getPos(),equipPosModel);
            }
            if (posFlag) {
                equipPosDao.queueInsert(equipPosModel);
            } else {
                equipPosDao.queueUpdate(equipPosModel);
            }
            CmdUtil.sendMsg(player, new EquipPosUpRs(equipPosModel.getPos(), equipPosModel.getPosLv()));
            attributeService.refresh(player);
        }
        return ErrorCodeEnum.SUCCESS;
    }
}