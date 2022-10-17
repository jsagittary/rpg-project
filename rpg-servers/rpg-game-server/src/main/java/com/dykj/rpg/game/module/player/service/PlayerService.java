package com.dykj.rpg.game.module.player.service;

import com.dykj.rpg.common.config.constant.ConfigEnum;
import com.dykj.rpg.common.config.dao.*;
import com.dykj.rpg.common.config.model.ConfigModel;
import com.dykj.rpg.common.config.model.ItemModel;
import com.dykj.rpg.common.config.model.RandomNameTextModel;
import com.dykj.rpg.common.config.model.TaskBasicModel;
import com.dykj.rpg.common.consts.ItemTypeEnum;
import com.dykj.rpg.common.data.cache.PlayerNameCacheMgr;
import com.dykj.rpg.common.data.dao.*;
import com.dykj.rpg.common.data.model.PlayerInfoModel;
import com.dykj.rpg.common.data.model.PlayerItemModel;
import com.dykj.rpg.common.data.model.PlayerTaskModel;
import com.dykj.rpg.common.module.uc.model.AccountInfoModel;
import com.dykj.rpg.common.redis.data.GlobalPlayerData;
import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.consts.LoginTypeEnum;
import com.dykj.rpg.game.module.cache.PlayerCache;
import com.dykj.rpg.game.module.cache.logic.EquipCache;
import com.dykj.rpg.game.module.data.cache.PIdIncreaseCacheMgr;
import com.dykj.rpg.game.module.equip.service.EquipService;
import com.dykj.rpg.game.module.event.data.LoadFormDbManager;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.server.logic.GameServerConfig;
import com.dykj.rpg.game.module.skill.service.SkillService;
import com.dykj.rpg.game.module.task.consts.TaskFinishConditionEnum;
import com.dykj.rpg.game.module.util.CmdUtil;
import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.protocol.item.ItemRs;
import com.dykj.rpg.protocol.login.*;
import com.dykj.rpg.protocol.player.PlayerRs;
import com.dykj.rpg.protocol.player.RandomNameRq;
import com.dykj.rpg.protocol.player.RandomNameRs;
import com.dykj.rpg.util.random.RandomUtil;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PlayerService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private PlayerInfoDao playerInfoDao;
    @Resource
    private GameServerConfig gameServerConfig;
    @Resource
    private GlobalPlayerDao globalPlayerDao;
    @Resource
    private PlayerCacheService playerCacheService;
    @Resource
    private LoadFormDbManager loadFormDbManager;
    @Resource
    private PIdIncreaseCacheMgr pIdIncreaseCacheMgr;
    @Resource
    private RandomNameTextDao randomNameTextDao;
    @Resource
    private ConfigDao configDao;
    @Resource
    private PlayerNameCacheMgr playerNameCacheMgr;

    @Resource
    private CharacterBasicDao characterBasicDao;

    @Resource
    private SkillService skillService;

    @Resource
    private ItemDao itemDao;

    @Resource
    private PlayerItemDao playerItemDao;
    @Resource
    private EquipService equipService;

    /**
     * 玩家信息
     *
     * @param
     * @return
     */
    public PlayerRs playerRs(PlayerInfoModel playerInfoModel) {
        PlayerRs rs = new PlayerRs();
        rs.setPlayerId(playerInfoModel.getPlayerId());
        rs.setName(playerInfoModel.getName());
        rs.setProfession(playerInfoModel.getProfession());
        rs.setChoose(playerInfoModel.getChoose());
        rs.setLevel(playerInfoModel.getLv());
        rs.setExp(playerInfoModel.getExp());
        rs.setBackCapacity(playerInfoModel.getBackCapacity());
        rs.setVip(playerInfoModel.getVipLv());
        return rs;
    }

    public GetPlayerInfoRs getPlayerInfoRs(List<PlayerInfoModel> playerInfoModelList) {
        GetPlayerInfoRs playerInfoRs = new GetPlayerInfoRs();
        List<PlayerRs> playerRsList = new ArrayList<>();
        for (PlayerInfoModel playerInfoModel : playerInfoModelList) {
            // 被删除的号不发送
            if (playerInfoModel.getStatus() == 1) {
                continue;
            }
            PlayerRs playerRs = playerRs(playerInfoModel);
            playerRsList.add(playerRs);
        }
        playerInfoRs.setPlayers(playerRsList);
        return playerInfoRs;
    }

    public RegisterMsgRs registerMsg(Player player) {
        RegisterMsgRs registerMsgRs = new RegisterMsgRs(playerRs(player.cache().getPlayerInfoModel()));
        return registerMsgRs;
    }

    /**
     * 查找玩家 先从缓存拿 拿不到从数据库拿
     *
     * @param
     * @param
     * @return
     */
    public Player getPlayerCache(AccountInfoModel accountInfoModel) {
        Player player = playerCacheService.getPlayerByAccount(accountInfoModel.getAccountKey(),
                gameServerConfig.getServerId());
        if (player != null) {
            player.setAccountInfoModel(accountInfoModel);
            loadFormDbManager.doEvents(player.cache().getPlayerInfoModel());
        }
        return player;
    }

    /**
     * 从数据库查找玩家
     *
     * @param playerId
     * @return
     */
    public Player createPlayer(int playerId) {
        PlayerInfoModel playerInfoModel = playerInfoDao.queryByPrimaykey(playerId);
        if (playerInfoModel != null) {
            Player player = new Player(playerId);
            PlayerCache playerCache = new PlayerCache(playerInfoModel);
            player.getCaches().put(playerId, playerCache);
            return player;
        }
        return null;
    }

    @Transactional
    public Player register(RegisterMsgRq msg, AccountInfoModel accountInfoModel) {
        PlayerInfoModel playerInfoModel = createPlayer(msg, accountInfoModel);
        Player player = playerCacheService.getPlayerByAccount(accountInfoModel.getAccountKey(),
                gameServerConfig.getServerId());
        if (player == null) {
            player = new Player(playerInfoModel.getPlayerId());
            player.setAccountInfoModel(accountInfoModel);
        }
        PlayerCache playerCache = new PlayerCache(playerInfoModel);
        player.getCaches().put(playerInfoModel.getPlayerId(), playerCache);
        chosePlayer(player, playerInfoModel.getPlayerId());

        // 关卡

        // 创建技能
        skillService.initSkills(player);

        return player;
    }

    /**
     * 临时方法(创建玩家)
     *
     * @param msg
     * @param
     * @return
     */

    public PlayerInfoModel createPlayer(RegisterMsgRq msg, AccountInfoModel accountInfoModel) {
        PlayerInfoModel playerInfoModel = new PlayerInfoModel();
        playerInfoModel.setServerId(gameServerConfig.getServerId());
        playerInfoModel.setCreatedTime(new Date());
        playerInfoModel.setLoginTime(new Date());
        playerInfoModel.setName(msg.getName());
        playerInfoModel.setChoose(1);
        playerInfoModel.setLv(1);
        playerInfoModel.setProfession(msg.getProfession());
        playerInfoModel.setAccountKey(accountInfoModel.getAccountKey());
        playerInfoModel.setPlayerId(pIdIncreaseCacheMgr.incrementAndGet());
        ConfigModel configModel = configDao.getConfigByKey(ConfigEnum.BAGSIZE.getConfigType());
        playerInfoModel.setBackCapacity(Integer.parseInt(configModel.getValue()));

        playerInfoDao.insert(playerInfoModel);

        // 像公共数据源插入数据
        GlobalPlayerData globalPlayerData = new GlobalPlayerData(playerInfoModel);
        globalPlayerDao.insert(globalPlayerData);
        return playerInfoModel;
    }

    /**
     * 获得玩家登录界面的展示信息
     *
     * @param player
     * @return
     */
    public GetPlayerInfoRs getPlayerInfoRs(Player player) {
        GetPlayerInfoRs playerInfoRs = new GetPlayerInfoRs();
        if (player == null) {
            return playerInfoRs;
        }
        List<PlayerRs> playerRsList = new ArrayList<>();
        for (PlayerInfoModel playerInfoModel : player.getAllInfos()) {
            // 被删除的号不发送
            if (playerInfoModel.getStatus() == 1) {
                continue;
            }
            PlayerRs playerRs = playerRs(playerInfoModel);
            playerRsList.add(playerRs);
        }
        playerInfoRs.setPlayers(playerRsList);
        if (player.cache() != null) {
            for (EquipCache equipCache : player.cache().getPlayerEquipCache().getDressEquips()) {
                ItemRs itemRs = equipService.itemRs(equipCache);
                playerInfoRs.getEquips().add(itemRs);

            }
        }
        return playerInfoRs;
    }

    /**
     * 初始化玩家缓存playerInfo
     *
     * @param accountKey
     * @param serverId
     */
    public Player initPlayerInfo(int accountKey, int serverId) {
        Player player = playerCacheService.getPlayerByAccount(accountKey, serverId);
        if (player == null) {
            List<PlayerInfoModel> list = playerInfoDao.queryByAccountKey(accountKey, serverId);
            if (list.size() > 0) {
                player = new Player();
                boolean flag = false;
                for (PlayerInfoModel playerInfoModel : list) {
                    PlayerCache playerCache = new PlayerCache(playerInfoModel);
                    player.getCaches().put(playerInfoModel.getPlayerId(), playerCache);
                    if (playerInfoModel.getChoose() == 1) {
                        player.setPlayerId(playerInfoModel.getPlayerId());
                        //被选中的角色需要同步装备 所以这里需要初始化
                        equipService.initPlayerEquipCache(player);
                        flag = true;
                    }
                }
                if(!flag){
                    PlayerInfoModel playerInfoModel = list.get(0);
                    playerInfoModel.setChoose(1);
                    playerInfoDao.queueUpdate(playerInfoModel);
                    player.setPlayerId(playerInfoModel.getPlayerId());
                    //被选中的角色需要同步装备 所以这里需要初始化
                    equipService.initPlayerEquipCache(player);
                }
                playerCacheService.getPlayers().put(player.getPlayerId(), player);
            }
        }
        return player;
    }


    /**
     * 选着一个角色 就需要把另外几个角色设置为未选择状态
     *
     * @param player
     */
    public void chosePlayer(Player player, int playerId) {
        if (player.getPlayerId() != playerId) {
            PlayerCache lastPlayerCache = player.cache();
            PlayerCache playerCache = player.getPlayerCache(playerId);
            lastPlayerCache.getPlayerInfoModel().setChoose(0);
            playerCache.getPlayerInfoModel().setChoose(1);
            player.setPlayerId(playerId);
            playerInfoDao.queueUpdate(playerCache.getPlayerInfoModel());
            playerInfoDao.queueUpdate(lastPlayerCache.getPlayerInfoModel());
            BeanFactory.getBean(PlayerCacheService.class).remove(lastPlayerCache.getPlayerId());
            BeanFactory.getBean(PlayerCacheService.class).addPlayer(player);
        }
        // equipService.initEquip(111014001,player);
    }

    /**
     * 玩家名称随机生成
     *
     * @return
     */
    public void generateRolesName(RandomNameRq randomNameRq, ISession session) {
        Collection<RandomNameTextModel> collections = randomNameTextDao.getConfigs();
        if (null == collections || collections.isEmpty()) {
            logger.error("协议号:{}, 查询随机名字静态配置数据为空!", randomNameRq.getCode());
            CmdUtil.sendErrorMsg(session, randomNameRq.getCode(), ErrorCodeEnum.CONFIG_ERROR);
            return;
        }
        // 拿到名字随机次数限制
        ConfigModel configModel = configDao.getConfigByKey(ConfigEnum.RANDNAMELIMIT.getConfigType());
        if (null == configModel) {
            logger.error("协议号:{}, 查询config常量配置表\"随机取名次数\"阈值为空!", randomNameRq.getCode());
            CmdUtil.sendErrorMsg(session, randomNameRq.getCode(), ErrorCodeEnum.CONFIG_ERROR);
            return;
        }

        StringBuilder randomName = new StringBuilder();
        int randomTextSize = collections.size();// RandnameTextModel配置表数据总数
        String firstStr = null;// 前缀
        String secondStr = null;// 中间位
        String thirdStr = null;// 后缀

        int threshold = 0;
        try {
            threshold = Integer.parseInt(configModel.getValue());
        } catch (NumberFormatException e) {
            logger.error("{} 配置表对应随机名称次数阈值转换为数值异常!", ConfigModel.class.getSimpleName(), e);
            CmdUtil.sendErrorMsg(session, randomNameRq.getCode(), ErrorCodeEnum.DATA_ERROR);
            return;
        }

        for (int i = 0; i < threshold; i++) {
            int randomFirstIndex = RandomUtil.randomIndexNotDuplicate(randomTextSize, 1).stream().findFirst().get();
            int randomSecondIndex = RandomUtil.randomIndexNotDuplicate(randomTextSize, 1).stream().findFirst().get();
            int randomThirdIndex = RandomUtil.randomIndexNotDuplicate(randomTextSize, 1).stream().findFirst().get();
            int count = 0;
            for (RandomNameTextModel randnameTextModel : collections) {
                if (count == randomFirstIndex) {
                    firstStr = randnameTextModel.getNameListFirst();
                }
                if (count == randomSecondIndex) {
                    secondStr = randnameTextModel.getNameListSecond();
                }
                if (count == randomThirdIndex) {
                    thirdStr = randnameTextModel.getNameListThird();
                }
                count++;
            }

            randomName.append(firstStr).append(secondStr).append(thirdStr);
            boolean flag = playerNameCacheMgr.addName(randomName.toString(), false);
            if (flag) {
                break;
            } else {
                if (i != (threshold - 1)) {
                    randomName.setLength(0);
                }
            }
        }
        RandomNameRs randomNameRs = new RandomNameRs();
        randomNameRs.setName(randomName.toString());
        CmdUtil.sendMsg(session, randomNameRs);
        logger.info("RandomNameHandler name {}", randomNameRs.toString());
        logger.info("generateRolesName session {}  name {}  byte[]{}:",session.getId(),randomNameRs.getName(), Arrays.toString(randomNameRs.getName().getBytes()));
    }

    public boolean isExist(int characterId) {
        return characterBasicDao.isExist(characterId);
    }

    /**
     * 进入游戏成功
     *
     * @param player
     */
    public void enterGameSuccess(Player player, LoginTypeEnum loginTypeEnum) {
        EnterGameSuccess enterGameSuccess = new EnterGameSuccess(loginTypeEnum.getType());
        CmdUtil.sendMsg(player, enterGameSuccess);
    }

    public ChangePlayerRs ChangePlayer(Player player, PlayerCache playerCache) {
        chosePlayer(player, playerCache.getPlayerId());
        if (playerCache.getPlayerEquipCache() == null) {
            BeanFactory.getBean(EquipService.class).initPlayerEquipCache(player);
        }

        ChangePlayerRs changePlayerRs = new ChangePlayerRs();
        for (EquipCache equipCache : player.cache().getPlayerEquipCache().getDressEquips()) {
            ItemRs itemRs = BeanFactory.getBean(EquipService.class).itemRs(equipCache);
            changePlayerRs.getEquips().add(itemRs);
        }
        return changePlayerRs;
    }
}
