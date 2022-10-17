package com.dykj.rpg.client.game.player.service;

import com.dykj.rpg.client.config.LoginCmdThread;
import com.dykj.rpg.client.config.LoginMsgPram;
import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.protocol.login.GetPlayerInfoRq;
import com.dykj.rpg.protocol.login.ReconnectRq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: jyb
 * @Date: 2019/1/8 18:54
 * @Description:
 */
@Service
public class LoginService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${test.account}")
    private String account;
    @Value("${client.name}")
    private String clientName;
    @Value("${server.identity}")
    private int serverId;

    public final static AtomicInteger ATOMIC_INTEGER = new AtomicInteger(1001);

    public static ISession session;
    @Resource
    private LoginCmdThread loginCmdThread;

    public String getName() {
        String account = clientName + "_" + ATOMIC_INTEGER.getAndIncrement();
        return account;
    }

    /**
     * 拿角色选择界面信息
     * @param loginMsgPram
     * @param session
     */
    public void getPlayersRq(LoginMsgPram loginMsgPram,ISession session) {
        GetPlayerInfoRq getPlayerInfoRq = new GetPlayerInfoRq();
        getPlayerInfoRq.setAccountKey(loginMsgPram.getAccountKey());
        session.write(getPlayerInfoRq);
        if(loginMsgPram.getAccount().equals(account)){
            this.session =session;
        }
    }

    /**
     * 重连
     * @param loginMsgPram
     * @param session
     */
    public void reconnect(LoginMsgPram loginMsgPram,ISession session){
        ReconnectRq reconnectRq = new ReconnectRq();
        reconnectRq.setPlayerId(loginMsgPram.getPlayerRs().getPlayerId());
        session.write(reconnectRq);
        if(loginMsgPram.getAccount().equals(account)){
            this.session =session;
        }
    }

    public void login(int num) {
        if (num == 0) {
            LoginMsgPram loginMsgPram = new LoginMsgPram(account, "NAN",serverId);
            loginMsgPram.setName(account);
            loginCmdThread.execLoginMsg(loginMsgPram);
        } else {
            for (int i = 0; i < num; i++) {
                String account = clientName + "_" + ATOMIC_INTEGER.getAndIncrement();
                LoginMsgPram loginMsgPram = new LoginMsgPram(account, "NAN",serverId);
                loginMsgPram.setName(account);
                loginCmdThread.execLoginMsg(loginMsgPram);
            }
        }
    }
}
