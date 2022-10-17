package com.dykj.rpg.game.module.event.login.event;

import com.dykj.rpg.common.data.dao.EquipPosDao;
import com.dykj.rpg.common.data.model.EquipPosModel;
import com.dykj.rpg.game.module.cache.EquipPosCache;
import com.dykj.rpg.game.module.equip.service.EquipService;
import com.dykj.rpg.game.module.event.core.AbstractEvent;
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
 * @date 2020/11/26 14:06
 * @Description
 */
@Component
public class EquipEvent extends AbstractEvent {
	@Resource
	private EquipPosDao equipPosDao;
	@Resource
	private EquipService equipService;

	@Override
	public void doEvent(Object... prams) throws Exception {
		Player player = (Player) prams[0];
		equipService.initPlayerEquipCache(player);
		initEquipPosCache(player);
	}

	/**
	 * 初始化装备位置信息
	 *
	 * @param player
	 */
	private void initEquipPosCache(Player player) {
		EquipPosInfoRs equipPosInfoRs = new EquipPosInfoRs();
		EquipPosCache equipPosCache = player.cache().getEquipPosCache();
		if (equipPosCache == null) {
			equipPosCache = new EquipPosCache();
			List<EquipPosModel> equipPosModels = equipPosDao.getEquipEquipPosModels(player.getPlayerId());
			if (equipPosModels != null && equipPosModels.size() > 0) {
				for (EquipPosModel equipPosModel : equipPosModels) {
					equipPosCache.getEquipPosModelMap().put(equipPosModel.getPos(), equipPosModel);
				}
			}
		}

		Collection<EquipPosModel> equipPosModels = equipPosCache.getEquipPosModelMap().values();
		for (EquipPosModel equipPosModel : equipPosModels) {
			equipPosInfoRs.getEquipPosInfos().add(new EquipPosUpRs(equipPosModel.getPos(), equipPosModel.getPosLv()));
		}
		player.cache().setEquipPosCache(equipPosCache);
		CmdUtil.sendMsg(player, equipPosInfoRs);
	}
}