package com.dykj.rpg.game.module.battle.service;

import com.dykj.rpg.common.config.dao.CharacterBasicDao;
import com.dykj.rpg.common.config.dao.MisBasicDao;
import com.dykj.rpg.common.config.dao.SoulBasicDao;
import com.dykj.rpg.common.config.dao.TaskBasicDao;
import com.dykj.rpg.common.config.model.CharacterBasicModel;
import com.dykj.rpg.common.config.model.MisBasicModel;
import com.dykj.rpg.common.config.model.SoulBasicModel;
import com.dykj.rpg.common.consts.ItemOperateEnum;
import com.dykj.rpg.common.consts.ItemTypeEnum;
import com.dykj.rpg.common.data.cache.BattleCacheMgr;
import com.dykj.rpg.common.data.dao.PlayerMissionDao;
import com.dykj.rpg.common.data.dao.PlayerTaskDao;
import com.dykj.rpg.common.data.model.PlayerAiModel;
import com.dykj.rpg.common.data.model.PlayerMissionModel;
import com.dykj.rpg.common.data.model.PlayerSoulSkinModel;
import com.dykj.rpg.common.data.model.cache.BattleCache;
import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.ice.client.BattleClient;
import com.dykj.rpg.game.module.ai.service.AiService;
import com.dykj.rpg.game.module.attribute.service.AttributeService;
import com.dykj.rpg.game.module.event.mission.MissionEventManager;
import com.dykj.rpg.game.module.item.consts.ItemPromp;
import com.dykj.rpg.game.module.item.response.ItemJoinModel;
import com.dykj.rpg.game.module.item.response.ItemResponse;
import com.dykj.rpg.game.module.item.service.ItemService;
import com.dykj.rpg.game.module.item.service.RandomItemService;
import com.dykj.rpg.game.module.mission.service.MissionService;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.player.service.PlayerCacheService;
import com.dykj.rpg.game.module.server.logic.GameServerConfig;
import com.dykj.rpg.game.module.skill.service.SkillService;
import com.dykj.rpg.game.module.task.logic.service.TaskService;
import com.dykj.rpg.game.module.util.CmdUtil;
import com.dykj.rpg.protocol.game2battle.*;
import com.dykj.rpg.protocol.gameBattle.BattleResultRs;
import com.dykj.rpg.protocol.gameBattle.StartBattleRs;
import com.dykj.rpg.protocol.item.ItemRs;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author jyb
 * @date 2020/12/4 17:48
 * @Description
 */
@Service
public class BattleService {
    @Resource
    private AttributeService attributeService;

    @Resource
    private SkillService skillService;
    @Resource
    private GameServerConfig gameServerConfig;

    @Resource
    private BattleCacheMgr battleCacheMgr;

    @Resource
    private PlayerCacheService playerCacheService;

    @Resource
    private MisBasicDao misBasicDao;
    @Resource
    private RandomItemService randomItemService;
    @Resource
    private ItemService itemService;
    @Resource
    private PlayerMissionDao playerMissionDao;
    @Resource
    private CharacterBasicDao characterBasicDao;

    @Resource
    private TaskBasicDao taskBasicDao;

    @Resource
    private PlayerTaskDao playerTaskDao;

    @Resource
    private TaskService taskService;

    @Resource
    private MissionService missionService;

    @Resource
    private AiService aiService;

    @Resource
    private SoulBasicDao soulBasicDao;

    private Logger logger = LoggerFactory.getLogger(getClass());


    private Logger gameLogger = LoggerFactory.getLogger("game");

    /**
     * 创建EnterBattleRoleInfo
     *
     * @param player
     * @return
     */
    public EnterBattleRoleInfo createBattleRole(Player player) {
        EnterBattleRoleInfo battleRoleInfo = new EnterBattleRoleInfo();
        battleRoleInfo.setRoleId(player.cache().getPlayerInfoModel().getProfession());
        battleRoleInfo.setRoleLevel(player.cache().getPlayerInfoModel().getLv());
        attributeService.refresh(player);
        Map<String, Integer> attributeMap = player.cache().getAttributeCache().getAttributes();
        for (Map.Entry<String, Integer> entry : attributeMap.entrySet()) {
            if (entry.getValue() == null || entry.getValue() == 0) {
                continue;
            }
            String[] rs = entry.getKey().split(":");
            battleRoleInfo.getAttributeInfos().add(new AttributeInfo(Short.valueOf(rs[0]), Byte.valueOf(rs[1]), entry.getValue()));
        }
        return battleRoleInfo;

    }

    /**
     * 创建进入战斗服对象
     *
     * @param player
     * @param missionId
     * @param battleType
     * @return
     */
    public PlayerEnterBattleRequest enterBattleRequest(Player player, int missionId, byte battleType) {
        PlayerEnterBattleRequest playerEnterBattleRequest = new PlayerEnterBattleRequest();
        playerEnterBattleRequest.setMissionId(missionId);
        playerEnterBattleRequest.setBattleType(battleType);
        playerEnterBattleRequest.setSessionId(player.getSession().getId());
        playerEnterBattleRequest.setPlayerId(player.getPlayerId());
        playerEnterBattleRequest.setSkillInfos(skillService.createEnterBattleSkills(player));
        playerEnterBattleRequest.setRoleInfo(createBattleRole(player));
        List<ItemRs> itemRs = itemService.getChildItems(player, ItemTypeEnum.CONSUMABLES, ItemTypeEnum.ConsumablesTypeEnum.BATTLE_POTION);
        if (itemRs != null && itemRs.size() > 0) {
            playerEnterBattleRequest.setItems(itemRs);
        }

        PlayerSoulSkinModel useSoulSkin = player.cache().getPlayerSoulSkinCache().getUseSoulSkin();
        if (null != useSoulSkin) {
            playerEnterBattleRequest.setSoulId(useSoulSkin.getSoulSkinId());
        }else {
            CharacterBasicModel characterBasicModel = characterBasicDao.getConfigByKey(player.getInfo().getProfession());
            SoulBasicModel soulSkinModel = soulBasicDao.getConfigByKey(characterBasicModel.getSoulSkin());
            playerEnterBattleRequest.setSoulId(soulSkinModel.getSoulSkinId());
        }

        List<AiBattleRs> Ais = new ArrayList<>();
        Collection<PlayerAiModel> playerAiModels = player.cache().getPlayerAiCache().getPlayerAiModelMap().values();
        for (PlayerAiModel playerAiModel : playerAiModels) {
            if(playerAiModel.getPosition() == -1){
                continue;
            }
            Ais.add(aiService.aiBattleRs(playerAiModel));
        }
        playerEnterBattleRequest.setAiBattles(Ais);
        return playerEnterBattleRequest;
    }

    /**
     * 进入战斗服
     *
     * @param player
     * @param missionId
     * @param battleType
     */
    public boolean enterBattleServer(Player player, int missionId, byte battleType) {
        try {
            PlayerEnterBattleRequest playerEnterBattleRequest = enterBattleRequest(player, missionId, battleType);
            gameLogger.info("enterBattleServer success  player {} name {}  result {}  ", player.cache().getPlayerInfoModel().getPlayerId(), player.cache().getPlayerInfoModel().getName(), playerEnterBattleRequest.toString());
            return BattleClient.getInstance().enterToBattleCache(gameServerConfig.getServerId(), player.getPlayerId(), playerEnterBattleRequest);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("BattleService enterBattleServer error  player {}   missionId {}  battleType {} ", player.getPlayerId(), missionId, battleType);
            return false;
        }
    }


    /**
     * 进入战斗服之后的返回
     *
     * @param enterBattleSuccessResponse
     */
    public void enterToBattleServerSuccess(EnterBattleSuccessResponse enterBattleSuccessResponse) {
//        BattleCache battleCache = battleCacheMgr.getCache(enterBattleSuccessResponse.getBattleId().longValue());
//        if (battleCache != null) {
//            logger.error("enterToBattleServerSuccess error :  battleCache is exist {} ", battleCache.toString());
//            return;
//        }
        BattleCache battleCache = new BattleCache();
        battleCache.setBattleType(enterBattleSuccessResponse.getBattleType());
        battleCache.setCreateTime(System.currentTimeMillis());
        battleCache.setConfigId(enterBattleSuccessResponse.getMissionId());
        battleCache.setGameBattleId(enterBattleSuccessResponse.getBattleId());

        battleCache.setIp(enterBattleSuccessResponse.getAddr());
        battleCache.setPort(enterBattleSuccessResponse.getPort());
        battleCache.setPlayerIds(enterBattleSuccessResponse.getPlayerIds());
        battleCacheMgr.addCache(battleCache);
        StartBattleRs startBattleRs = new StartBattleRs();
        startBattleRs.setBattleId(enterBattleSuccessResponse.getBattleId());
        startBattleRs.setBattleType(enterBattleSuccessResponse.getBattleType());
        startBattleRs.setAddr(enterBattleSuccessResponse.getAddr());
        startBattleRs.setPort(enterBattleSuccessResponse.getPort());
        //推送前端开始战斗消息
        for (Integer playerId : enterBattleSuccessResponse.getPlayerIds()) {
            Player player = playerCacheService.getCache(playerId);
            if (player == null) {
                continue;
            }
            logger.info("enterToBattleServerSuccess  player {}  battleId  {} ", player.getPlayerId(), enterBattleSuccessResponse.getBattleId());
            CmdUtil.sendMsg(player, startBattleRs);
        }
    }


    /**
     * 战斗结算接口，后续可能会优化，怎么兼容多种后的结算
     *
     * @param battleFinishResultResponse
     */
    public void battleResult(BattleFinishResultResponse battleFinishResultResponse) {
        logger.info("battleResult  player {}  battleId  {} ", battleFinishResultResponse.getResults().get(0).getPlayerId(), battleFinishResultResponse.getBattleId());
        BattleCache battleCache = battleCacheMgr.getCache(battleFinishResultResponse.getBattleId());
        if (battleCache == null) {
            logger.error("BattleCache is not exist  battleFinishResultResponse {} ", battleFinishResultResponse.getBattleId());
            return;
        }
        MisBasicModel misBasicModel = misBasicDao.getConfigByKey(battleCache.getConfigId());

        MisBasicModel nextMissModel = misBasicDao.getNextOpenMission(battleCache.getConfigId());
        for (BattleFinishPersonalResultResponse battleFinishPersonalResultResponse : battleFinishResultResponse.getResults()) {
            if (!battleCache.getPlayerIds().contains(battleFinishPersonalResultResponse.getPlayerId())) {
                logger.error(" player {}  is not in battleCache {} ", battleFinishPersonalResultResponse.toString(), battleCache.toString());
                continue;
            }

            Player player = playerCacheService.getCache(battleFinishPersonalResultResponse.getPlayerId());
            if (player == null) {
                logger.error(" player {}  is not in cache  {}", battleFinishPersonalResultResponse.toString());
                continue;
            }
            PlayerMissionModel playerMissionModel = player.cache().getPlayerMissionModel();
            if (playerMissionModel == null) {
                logger.error(" playerMission is not  exist", battleFinishPersonalResultResponse.toString());
                continue;
            }
            BattleResultRs battleResultRs = new BattleResultRs();
            //关卡通关奖励
            battleResultRs.setResult(battleFinishPersonalResultResponse.getResult());
            //处理消耗品

            if (battleFinishPersonalResultResponse.getItems() != null && battleFinishPersonalResultResponse.getItems().size() > 0) {
                List<ItemJoinModel> itemJoinModels = new ArrayList<>();
                for (ItemRs item : battleFinishPersonalResultResponse.getItems()) {
                    itemJoinModels.add(new ItemJoinModel(item));
                }
                ItemResponse itemResponse = itemService.batchUpdateItemPush(player, itemJoinModels, ItemOperateEnum.BATTLE_USE_ITEM, ItemPromp.GENERIC);
                if (itemResponse.getCodeEnum() == ErrorCodeEnum.SUCCESS) {
                    gameLogger.info("battleResult   物品消耗: {} ", Arrays.toString(battleFinishPersonalResultResponse.getItems().toArray()));
                }
            }


            if (battleFinishPersonalResultResponse.getResult()) {
                List<ItemJoinModel> itemJoinModels = randomItemService.randomItem(misBasicModel.getReward(), player.cache().getPlayerInfoModel().getProfession());
                if (itemJoinModels != null && itemJoinModels.size() > 0) {
                    ItemResponse itemResponse = itemService.batchUpdateItemPush(player, itemJoinModels, ItemOperateEnum.MISSION_AWARD, ItemPromp.GENERIC);
                    if (itemResponse.getCodeEnum() == ErrorCodeEnum.SUCCESS) {
                        battleResultRs.setAward(itemResponse.getItemRsList());
                        gameLogger.info("battleResult 关卡奖励 : {} ", Arrays.toString(battleResultRs.getAward().toArray()));
                    }
                }
                //关卡战斗服随机奖励
                List<ItemRs> itemRs = battleFinishPersonalResultResponse.getAwards();
                List<ItemJoinModel> randomItems = new ArrayList<>();
                if (itemRs != null && itemRs.size() > 0) {
                    randomItemService.addItemModel(randomItems, itemRs);
                    ItemResponse itemResponse = itemService.batchUpdateItemPush(player, randomItems, ItemOperateEnum.MISSION_AWARD, ItemPromp.GENERIC);
                    if (itemResponse.getCodeEnum() == ErrorCodeEnum.SUCCESS) {
                        battleResultRs.setRandomAward(itemResponse.getItemRsList());
                        gameLogger.info("battleResult 随机奖励 : {} ", Arrays.toString(battleResultRs.getRandomAward().toArray()));
                    }
                }
                //新关卡开启要把老关卡的挂机奖励结算一次
                playerMissionModel.setLastBattleTime(new Date());
                playerMissionDao.queueUpdate(playerMissionModel);
                if (nextMissModel != null) {
                    playerMissionModel.setMissionId(nextMissModel.getMisId());
                    battleResultRs.setOpenMissionId(nextMissModel.getMisId());
                    battleResultRs.setBattleCdEndTime(playerMissionModel.getLastBattleTime().getTime() + nextMissModel.getWaitTime() * 1000);
                }
            } else {
                if (battleFinishPersonalResultResponse.getConditions() != null && battleFinishPersonalResultResponse.getConditions().size() > 0) {
                    battleResultRs.setConditions(battleFinishPersonalResultResponse.getConditions());
                }
            }
            battleCacheMgr.removeCache(battleFinishResultResponse.getBattleId());
            CmdUtil.sendMsg(player, battleResultRs);
            gameLogger.info("BattleService battleResult  player {} name {}  result {}  ", player.cache().getPlayerInfoModel().getPlayerId(), player.cache().getPlayerInfoModel().getName(), battleResultRs.toString());
            //如果战斗胜利
            if (battleFinishPersonalResultResponse.getResult()) {
                //战斗胜利触发关卡任务事件监听
                BeanFactory.getBean(MissionEventManager.class).doEvents(player);
            }
        }
    }
}