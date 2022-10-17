package com.dykj.rpg.game.module.event.cache.event;

import com.dykj.rpg.common.data.dao.EquipEntryDao;
import com.dykj.rpg.common.data.dao.EquipPosDao;
import com.dykj.rpg.common.data.dao.PlayerEquipDao;
import com.dykj.rpg.common.data.model.EquipEntryModel;
import com.dykj.rpg.common.data.model.EquipPosModel;
import com.dykj.rpg.common.data.model.PlayerEquipModel;
import com.dykj.rpg.game.module.cache.PlayerEquipCache;
import com.dykj.rpg.game.module.cache.logic.EquipCache;
import com.dykj.rpg.game.module.event.cache.CacheEvent;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.util.CmdUtil;
import com.dykj.rpg.protocol.equip.EquipPosInfoRs;
import com.dykj.rpg.protocol.equip.EquipPosUpRs;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * @author jyb
 * @date 2021/5/8 16:57
 * @Description
 */
@Component
public class EquipCacheEvent extends CacheEvent<PlayerEquipCache> {
    @Resource
    private PlayerEquipDao playerEquipDao;
    @Resource
    private EquipEntryDao equipEntryDao;

    @Override
    public void loadFromDb(Player player) {
        PlayerEquipCache playerEquipCache = player.cache().getPlayerEquipCache();
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

    @Override
    public void refreshCache(Player player) {
        //计算装备自增id
        player.cache().getPlayerEquipCache().calculateSequence();
    }
}