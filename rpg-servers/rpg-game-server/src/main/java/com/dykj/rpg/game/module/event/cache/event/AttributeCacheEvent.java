package com.dykj.rpg.game.module.event.cache.event;

import com.dykj.rpg.game.module.attribute.service.AttributeService;
import com.dykj.rpg.game.module.event.core.AbstractEvent;
import com.dykj.rpg.game.module.player.logic.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author jyb
 * @date 2021/5/10 17:32
 * @Description
 */
@Component
public class AttributeCacheEvent extends AbstractEvent {
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