package com.dykj.rpg.client.game.player.handler;

import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.net.handler.core.AbstractClientHandler;
import com.dykj.rpg.protocol.skill.PlayerSkillListGmRs;
import com.dykj.rpg.protocol.skill.SkillLevelListGmRs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/24
 */
public class PlayerSkillListGmHandler extends AbstractClientHandler<PlayerSkillListGmRs>
{
    private final Logger logger = LoggerFactory.getLogger(getClazz());

    @Override
    protected void doHandler(PlayerSkillListGmRs playerSkillListGmRs, ISession session)
    {
        logger.info("GM指令获取玩家所有技能列表后返回协议: {}", playerSkillListGmRs.toString());
    }
}