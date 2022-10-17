package com.dykj.rpg.game.module.soul.servcie;

import com.dykj.rpg.common.config.dao.SoulBasicDao;
import com.dykj.rpg.common.config.model.SoulBasicModel;
import com.dykj.rpg.common.consts.CalculationEnum;
import com.dykj.rpg.common.consts.ItemOperateEnum;
import com.dykj.rpg.common.data.dao.PlayerSoulSkinDao;
import com.dykj.rpg.common.data.model.PlayerSoulSkinModel;
import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.module.cache.PlayerSoulSkinCache;
import com.dykj.rpg.game.module.item.consts.ItemPromp;
import com.dykj.rpg.game.module.item.response.ItemJoinModel;
import com.dykj.rpg.game.module.item.response.ItemResponse;
import com.dykj.rpg.game.module.item.service.ItemService;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.soul.consts.SoulConsts;
import com.dykj.rpg.game.module.util.CmdUtil;
import com.dykj.rpg.protocol.soul.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author jyb
 * @date 2021/4/26 14:08
 * @Description
 */
@Service
public class SoulService {


    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private SoulBasicDao soulBasicDao;
    @Resource
    private PlayerSoulSkinDao playerSoulSkinDao;
    @Resource
    private ItemService itemService;

    /**
     * 是否解锁
     *
     * @param player
     * @param soulId
     * @return
     */
    public ErrorCodeEnum isUnlock(Player player, int soulId) {
        SoulBasicModel soulBasicModel = soulBasicDao.getConfigByKey(soulId);
        if (soulBasicModel.getOpen().size() == 1) {
            //[0] 表示没有条件
            if (soulBasicModel.getOpen().get(0).get(0) == 0) {
                if (soulBasicModel.getOpen().size() > 1) {
                    return ErrorCodeEnum.CLIENT_PRAMS_ERROR;
                }
            }
        }
        ErrorCodeEnum errorCodeEnum = ErrorCodeEnum.SUCCESS;
        for (List<Integer> conditions : soulBasicModel.getOpen()) {
            if (conditions.get(0) == SoulConsts.PLAYER_LEVEL) {
                if (player.getInfo().getLv() < conditions.get(1)) {
                    errorCodeEnum = ErrorCodeEnum.PLAYER_LEVEL_ERROR;
                    break;
                }
            }
        }
        return errorCodeEnum;
    }


    public ErrorCodeEnum unlockSoulSkin(int soulSkinId, Player player) {
        PlayerSoulSkinCache playerSoulSkinCache = player.cache().getPlayerSoulSkinCache();
        if (playerSoulSkinCache.get(soulSkinId) != null) {
            logger.error("SoulService unlockSoulSkin error  player {} soulSkinId {} is unlock", player.getPlayerId(), soulSkinId);
            return ErrorCodeEnum.CLIENT_PRAMS_ERROR;
        }
        ErrorCodeEnum errorCodeEnum = isUnlock(player, soulSkinId);
        if (errorCodeEnum != ErrorCodeEnum.SUCCESS) {
            return errorCodeEnum;
        }
        PlayerSoulSkinModel playerSoulSkinModel = new PlayerSoulSkinModel();
        playerSoulSkinModel.setPlayerId(player.getPlayerId());
        playerSoulSkinModel.setSoulSkinId(soulSkinId);
        playerSoulSkinModel.setUseSoulSkin(0);
        playerSoulSkinDao.queueInsert(playerSoulSkinModel);
        playerSoulSkinCache.put(playerSoulSkinModel);
        CmdUtil.sendMsg(player, new UnlockSoulSkinRs());
        return ErrorCodeEnum.SUCCESS;
    }


    /**
     * 购买
     *
     * @param soulSkinId
     * @param player
     * @return
     */
    public ErrorCodeEnum buySoulSkin(int soulSkinId, Player player) {
        PlayerSoulSkinCache playerSoulSkinCache = player.cache().getPlayerSoulSkinCache();
        if (playerSoulSkinCache.get(soulSkinId) != null) {
            logger.error("SoulService buySoulSkin error  player {} soulSkinId {} is unlock", player.getPlayerId(), soulSkinId);
            return ErrorCodeEnum.CLIENT_PRAMS_ERROR;
        }
        SoulBasicModel soulBasicModel = soulBasicDao.getConfigByKey(soulSkinId);
        List<ItemJoinModel> itemJoinModels = ItemJoinModel.items(soulBasicModel.getOpenUse(), CalculationEnum.CALCULATION);
        if (itemJoinModels != null && itemJoinModels.size() > 0) {
            ItemResponse itemResponse = itemService.batchUpdateItemPush(player, itemJoinModels, ItemOperateEnum.BUY_SOUL_SKIN, ItemPromp.GENERIC);
            if (itemResponse.getCodeEnum() == ErrorCodeEnum.SUCCESS) {
                PlayerSoulSkinModel playerSoulSkinModel = new PlayerSoulSkinModel();
                playerSoulSkinModel.setPlayerId(player.getPlayerId());
                playerSoulSkinModel.setSoulSkinId(soulSkinId);
                playerSoulSkinModel.setUseSoulSkin(0);
                playerSoulSkinCache.put(playerSoulSkinModel);
                playerSoulSkinDao.queueInsert(playerSoulSkinModel);
                CmdUtil.sendMsg(player, new BuySoulSkinRs());
            } else {
                return itemResponse.getCodeEnum();
            }
        }
        return ErrorCodeEnum.SUCCESS;
    }

    /**
     * 购买
     *
     * @param soulSkinId
     * @param player
     * @return
     */
    public ErrorCodeEnum useSoulSkin(int soulSkinId, Player player) {
        PlayerSoulSkinCache playerSoulSkinCache = player.cache().getPlayerSoulSkinCache();
        PlayerSoulSkinModel playerSoulSkinModel = playerSoulSkinCache.get(soulSkinId);
        if (playerSoulSkinModel == null) {
            logger.error("SoulService useSoulSkin error  player {} soulSkinId {} is not unlock", player.getPlayerId(), soulSkinId);
            return ErrorCodeEnum.CLIENT_PRAMS_ERROR;
        }
        if(playerSoulSkinModel.getUseSoulSkin()==1){
            logger.error("SoulService useSoulSkin error  player {} soulSkinId {} is use", player.getPlayerId(), soulSkinId);
            return ErrorCodeEnum.CLIENT_PRAMS_ERROR;
        }
        PlayerSoulSkinModel lastOnSkin = playerSoulSkinCache.getUseSoulSkin();
        if (lastOnSkin != null) {
            lastOnSkin.setUseSoulSkin(0);
            playerSoulSkinDao.queueUpdate(lastOnSkin);
        }
        playerSoulSkinModel.setUseSoulSkin(1);
        playerSoulSkinDao.queueUpdate(playerSoulSkinModel);
        CmdUtil.sendMsg(player,new UseSoulSkinRs());
        return ErrorCodeEnum.SUCCESS;
    }


    public SoulSkinLoginRs soulSkinLoginRs(Player player){
        PlayerSoulSkinCache playerSoulSkinCache = player.cache().getPlayerSoulSkinCache();
        SoulSkinLoginRs soulSkinLoginRs = new SoulSkinLoginRs();
        playerSoulSkinCache.getPlayerSoulSkinCacheMap().values().forEach(playerSoulSkinModel -> {
            SoulSkinRs  soulSkinRs= new SoulSkinRs(playerSoulSkinModel.getSoulSkinId(),playerSoulSkinModel.getUseSoulSkin());
            soulSkinLoginRs.getSoulSkins().add(soulSkinRs);
        });
        return  soulSkinLoginRs;
    }

}