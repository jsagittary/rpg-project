package com.dykj.rpg.game.module.event.login.event;

import com.dykj.rpg.common.attribute.AttributeArray;
import com.dykj.rpg.game.module.attribute.service.AttributeService;
import com.dykj.rpg.game.module.event.core.AbstractEvent;
import com.dykj.rpg.game.module.player.logic.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author jyb
 * @date 2020/11/10 14:41
 * @Description
 */
@Component
public class AttrRefreshEvent extends AbstractEvent {
    @Resource
    private AttributeService attributeService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void doEvent(Object... prams) throws Exception {
        Player player = (Player) prams[0];
        attributeService.refresh(player);
        logger.info("player {}  AttributeArray {} ", player.getPlayerId(), attributeService.attributeString(player.cache().getAttributeCache().getAttributes()));
    }
}