package com.dykj.rpg.game.module.event.cache.event;

import com.dykj.rpg.common.data.dao.EquipPosDao;
import com.dykj.rpg.common.data.model.EquipPosModel;
import com.dykj.rpg.game.module.cache.EquipPosCache;
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
 * @date 2021/5/10 15:25
 * @Description
 */
@Component
public class EquipPosCacheEvent extends CacheEvent<EquipPosCache> {

    @Resource
    private EquipPosDao equipPosDao;


    @Override
    public void loadFromDb(Player player) {
        EquipPosCache equipPosCache = player.cache().getEquipPosCache();
        List<EquipPosModel> equipPosModels = equipPosDao.getEquipEquipPosModels(player.getPlayerId());
        if (equipPosModels != null && equipPosModels.size() > 0) {
            for (EquipPosModel equipPosModel : equipPosModels) {
                equipPosCache.getEquipPosModelMap().put(equipPosModel.getPos(), equipPosModel);
            }
        }
    }

    @Override
    public void send(Player player) {
        EquipPosInfoRs equipPosInfoRs = new EquipPosInfoRs();
        Collection<EquipPosModel> equipPosModels = player.cache().getEquipPosCache().getEquipPosModelMap().values();
        for (EquipPosModel equipPosModel : equipPosModels) {
            equipPosInfoRs.getEquipPosInfos().add(new EquipPosUpRs(equipPosModel.getPos(), equipPosModel.getPosLv()));
        }
        CmdUtil.sendMsg(player, equipPosInfoRs);
    }

    @Override
    public void refreshCache(Player player) {

    }
}