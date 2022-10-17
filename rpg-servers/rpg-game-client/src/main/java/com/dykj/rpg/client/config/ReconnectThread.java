package com.dykj.rpg.client.config;

import com.dykj.rpg.client.ClientSession;
import com.dykj.rpg.client.NettyClientFactory;
import com.dykj.rpg.common.consts.UcCodeEnum;
import com.dykj.rpg.common.module.uc.logic.UcMsg;
import com.dykj.rpg.util.HttpUtil;
import com.dykj.rpg.util.JsonUtil;
import com.dykj.rpg.util.spring.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: jyb
 * @Date: 2020/9/16 10:18
 * @Description:
 */
@Service
public class ReconnectThread implements Runnable {


    private Map<Integer, ClientSession> clientSessionMap = new ConcurrentHashMap<>();

    @Resource
    private LoginCmdThread loginCmdThread;

    @Override
    @Scheduled(initialDelay = 33, fixedRate = 5 * 1000)
    public void run() {
        for (ClientSession clientSession : getSessions()) {
            if (clientSession.getLoginMsgPram() == null) {
                continue;
            }
            if (clientSession.getReconnectTime() >= 1) {
                removeSession(clientSession.getId());
            } else {
                try {
                    clientSession.getLoginMsgPram().setReconnect(true);
                    loginCmdThread.execLoginMsg(clientSession.getLoginMsgPram());
                    clientSession.setReconnectTime(clientSession.getReconnectTime() + 1);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
    }


    public void putSession(ClientSession clientSession) {
        clientSessionMap.put(clientSession.getId(), clientSession);
    }

    public void removeSession(int sessionId) {
        clientSessionMap.remove(sessionId);
    }

    public List<ClientSession> getSessions() {
        return new ArrayList<>(clientSessionMap.values());
    }

}
