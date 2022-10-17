package com.dykj.rpg.game.module.event.login.event;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.dykj.rpg.common.data.dao.PlayerMailDao;
import com.dykj.rpg.common.data.model.PlayerMailModel;
import com.dykj.rpg.game.module.cache.PlayerMailCache;
import com.dykj.rpg.game.module.event.core.AbstractEvent;
import com.dykj.rpg.game.module.mail.service.MailService;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.util.CmdUtil;

/**
 * @author CaoBing
 * @date 2021年4月20日
 * @Description:
 */
@Component
public class LoginMailEvent extends AbstractEvent {

	@Resource
	private PlayerMailDao playerMailDao;

	@Resource
	private MailService mailService;

	@Override
	public void doEvent(Object... prams) throws Exception {
		Player player = (Player) prams[0];
		PlayerMailCache playerMailCache = player.cache().getPlayerMailCache();
		if (playerMailCache == null) {
			playerMailCache = new PlayerMailCache();
			List<PlayerMailModel> playerMailModels = playerMailDao.getPlayerMails(player.getPlayerId());
			if (playerMailModels != null && playerMailModels.size() > 0) {
				for (PlayerMailModel playerMailModel : playerMailModels) {
					playerMailCache.getPlayerMailModelMap().put(playerMailModel.getInstanceId(), playerMailModel);
				}
			}
		}
		player.cache().setPlayerMailCache(playerMailCache);
		playerMailCache.calculateSequence();
		CmdUtil.sendMsg(player, mailService.playerMailRs(playerMailCache));
	}
}
