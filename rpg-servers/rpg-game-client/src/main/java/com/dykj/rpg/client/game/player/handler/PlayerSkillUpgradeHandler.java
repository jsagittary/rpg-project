package com.dykj.rpg.client.game.player.handler;

import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.net.handler.core.AbstractClientHandler;
import com.dykj.rpg.protocol.skill.SkillUpgradeRs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/24
 */
public class PlayerSkillUpgradeHandler extends AbstractClientHandler<SkillUpgradeRs>
{
    private final Logger logger = LoggerFactory.getLogger(getClazz());

    @Override
    protected void doHandler(SkillUpgradeRs skillUpgradeRs, ISession session)
    {
        logger.info("技能升级后返回协议: {}", skillUpgradeRs.toString());
    }
}