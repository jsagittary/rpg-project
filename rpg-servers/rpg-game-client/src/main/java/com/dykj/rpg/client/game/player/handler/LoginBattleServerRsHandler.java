package com.dykj.rpg.client.game.player.handler;

import com.dykj.rpg.client.UdpClient;
import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.net.handler.core.AbstractClientHandler;
import com.dykj.rpg.protocol.battle.LoginBattleServerRq;
import com.dykj.rpg.protocol.battle.LoginBattleServerRs;
import io.netty.buffer.Unpooled;

public class LoginBattleServerRsHandler extends AbstractClientHandler<LoginBattleServerRs> {

    private static int requestNum = 0;
    @Override
    protected void doHandler(LoginBattleServerRs loginBattleServerRs, ISession session) {
        System.out.println(loginBattleServerRs.toString());

        if(requestNum < 20){
            UdpClient udpClient = UdpClient.getInstance();

            LoginBattleServerRq request = new LoginBattleServerRq();
            request.setUserId(12345678);
            udpClient.send(Unpooled.wrappedBuffer(request.encode()));

            requestNum ++ ;
        }

    }
}
