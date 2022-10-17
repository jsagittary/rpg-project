package com.dykj.rpg.game.module.mail.hanlder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.core.GameHandler;
import com.dykj.rpg.game.module.mail.service.MailService;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.protocol.mail.GetAllMailAwardRq;
import com.dykj.rpg.util.spring.BeanFactory;

/**
 * @author CaoBing
 * @date 2021年5月7日
 * @Description:
 */
public class GetAllMailAwardHandler extends GameHandler<GetAllMailAwardRq> {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void doHandler(GetAllMailAwardRq getAllMailAwardRq, Player player) {
		ErrorCodeEnum errorCodeEnum = BeanFactory.getBean(MailService.class).getAllMailAward(player, getAllMailAwardRq);
		if (errorCodeEnum != ErrorCodeEnum.SUCCESS) {
			sendError(player, errorCodeEnum);
		}
	}
}
