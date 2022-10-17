package com.dykj.rpg.game.module.event.login.event;

import com.dykj.rpg.common.config.dao.CharacterBasicDao;
import com.dykj.rpg.common.config.dao.SoulBasicDao;
import com.dykj.rpg.common.config.model.CharacterBasicModel;
import com.dykj.rpg.common.config.model.SoulBasicModel;
import com.dykj.rpg.common.config.model.SoulSkinModel;
import com.dykj.rpg.common.data.dao.PlayerSoulSkinDao;
import com.dykj.rpg.common.data.model.PlayerSoulSkinModel;
import com.dykj.rpg.game.module.cache.PlayerSoulSkinCache;
import com.dykj.rpg.game.module.event.core.AbstractEvent;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.soul.servcie.SoulService;
import com.dykj.rpg.game.module.util.CmdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jyb
 * @date 2021/4/26 17:32
 * @Description
 */
@Component
public class LoginSoulSkinEvent extends AbstractEvent {
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
    public void doEvent(Object... prams) throws Exception {
        Player player = (Player) prams[0];
        PlayerSoulSkinCache playerSoulSkinCache = player.cache().getPlayerSoulSkinCache();
        if (playerSoulSkinCache == null) {
            playerSoulSkinCache = new PlayerSoulSkinCache();
            player.cache().setPlayerSoulSkinCache(playerSoulSkinCache);
            List<PlayerSoulSkinModel> playerSoulSkinModels = playerSoulSkinDao.getPlayerSoulSkins(player.getPlayerId());
            if (playerSoulSkinModels == null || playerSoulSkinModels.size() < 1) {
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
            } else {
                playerSoulSkinModels.forEach(playerSoulSkinModel -> player.cache().getPlayerSoulSkinCache().put(playerSoulSkinModel));
                if (player.cache().getPlayerSoulSkinCache().getUseSoulSkin() == null) {
                    if(playerSoulSkinModels.size()>0){
                        playerSoulSkinModels.get(0).setUseSoulSkin(1);
                        playerSoulSkinDao.queueUpdate(playerSoulSkinModels.get(0));
                    }
                }
            }
        }
        CmdUtil.sendMsg(player, soulService.soulSkinLoginRs(player));
    }
}