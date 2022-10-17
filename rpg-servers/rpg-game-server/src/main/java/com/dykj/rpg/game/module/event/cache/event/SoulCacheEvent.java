package com.dykj.rpg.game.module.event.cache.event;

import com.dykj.rpg.common.config.dao.CharacterBasicDao;
import com.dykj.rpg.common.config.dao.SoulBasicDao;
import com.dykj.rpg.common.config.model.CharacterBasicModel;
import com.dykj.rpg.common.config.model.SoulBasicModel;
import com.dykj.rpg.common.data.dao.PlayerSoulSkinDao;
import com.dykj.rpg.common.data.model.PlayerSoulSkinModel;
import com.dykj.rpg.game.module.cache.PlayerSkillCache;
import com.dykj.rpg.game.module.cache.PlayerSoulSkinCache;
import com.dykj.rpg.game.module.event.cache.CacheEvent;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.soul.servcie.SoulService;
import com.dykj.rpg.game.module.util.CmdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author jyb
 * @date 2021/5/10 20:23
 * @Description
 */
@Component
public class SoulCacheEvent extends CacheEvent<PlayerSoulSkinCache> {
    @Resource
    private PlayerSoulSkinDao playerSoulSkinDao;
    @Resource
    private SoulBasicDao soulBasicDao;
    @Resource
    private CharacterBasicDao characterBasicDao;
    @Resource
    private SoulService soulService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void loadFromDb(Player player) {
        PlayerSoulSkinCache playerSoulSkinCache = player.cache().getPlayerSoulSkinCache();
        List<PlayerSoulSkinModel> playerSoulSkinModels = playerSoulSkinDao.getPlayerSoulSkins(player.getPlayerId());
        playerSoulSkinModels.forEach(playerSoulSkinModel -> playerSoulSkinCache.put(playerSoulSkinModel));
    }

    @Override
    public void refreshCache(Player player) {
        if (player.cache().getPlayerSoulSkinCache().getPlayerSoulSkinCacheMap().size() < 1) {
            CharacterBasicModel characterBasicModel = characterBasicDao.getConfigByKey(player.getInfo().getProfession());
            SoulBasicModel soulSkinModel = soulBasicDao.getConfigByKey(characterBasicModel.getSoulSkin());
            if (soulSkinModel != null) {
                PlayerSoulSkinModel playerSoulSkinModel = new PlayerSoulSkinModel();
                playerSoulSkinModel.setSoulSkinId(soulSkinModel.getSoulSkinId());
                playerSoulSkinModel.setUseSoulSkin(1);
                playerSoulSkinModel.setPlayerId(player.getPlayerId());
                player.cache().getPlayerSoulSkinCache().put(playerSoulSkinModel);
                playerSoulSkinDao.queueInsert(playerSoulSkinModel);
            } else {
                logger.error("LoginSoulSkinEvent error soulSkinModel {} is not exist ", characterBasicModel.getSoulSkin());
            }
        }
    }
    @Override
    public void send(Player player) {
        CmdUtil.sendMsg(player, soulService.soulSkinLoginRs(player));
    }
}