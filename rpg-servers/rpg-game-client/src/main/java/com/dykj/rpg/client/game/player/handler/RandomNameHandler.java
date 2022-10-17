package com.dykj.rpg.client.game.player.handler;

import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.net.handler.core.AbstractClientHandler;
import com.dykj.rpg.protocol.player.RandomNameRs;

/**
 * @author jyb
 * @date 2020/11/7 18:22
 * @Description
 */
public class RandomNameHandler  extends AbstractClientHandler<RandomNameRs>
{
    @Override
    protected void doHandler(RandomNameRs randomNameRs, ISession session) {
        System.out.println(randomNameRs.toString());
    }
}