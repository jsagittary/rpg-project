package com.dykj.rpg.client.config;

import com.dykj.rpg.client.NettyClientFactory;
import com.dykj.rpg.common.consts.UcCodeEnum;
import com.dykj.rpg.common.module.uc.logic.LoginMsg;
import com.dykj.rpg.common.module.uc.logic.ServerList;
import com.dykj.rpg.common.module.uc.logic.UcMsg;
import com.dykj.rpg.common.module.uc.logic.UcServer;
import com.dykj.rpg.util.HttpUtil;
import com.dykj.rpg.util.JsonUtil;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class LoginCmdThread implements Runnable {

    @Value("${uc.login.url}")
    private String url;

    @Resource
    private NettyClientFactory nettyClientFactory;

    @Resource
    private SendMsgThreadManager sendMsgThreadManager;

    private Logger logger = LoggerFactory.getLogger(getClass());


    private final BlockingQueue<LoginMsgPram> queue = new LinkedBlockingQueue<LoginMsgPram>(4000);

    private static Map<Integer, LoginServerInfo> loginInfos = new ConcurrentHashMap<>();

    public static AtomicInteger i = new AtomicInteger(0);

    @Scheduled(initialDelay = 33, fixedRate = 100)
    @Override
    public void run() {
        while (true) {
            try {
                LoginMsgPram loginMsgPram = queue.take();
                login(loginMsgPram);
            } catch (Exception e) {

            }

        }
    }

    public void execLoginMsg(LoginMsgPram loginMsgPram) {
        queue.add(loginMsgPram);
    }

    public void login(LoginMsgPram loginMsgPram) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("account", loginMsgPram.getAccount());
            map.put("channel", loginMsgPram.getChannel());
            UcMsg ucMsg;
            UcServer server;
            if (!loginMsgPram.isReconnect()) {
                String url = this.url + "/getServerList";
                String text = HttpUtil.sendHttpPost(url, map);
                logger.info("LoginCmdThread login getServerList {} ", text);
                ucMsg = JsonUtil.toInstance(text, UcMsg.class);
                ServerList serverList = JsonUtil.toInstance(ucMsg.getData(), ServerList.class);
                server = getServer(serverList, loginMsgPram.getServerId());
                map.put("serverId", String.valueOf(server.getServerId()));
            }else {
                map.put("serverId", String.valueOf(loginMsgPram.getServerId()));
            }

            String url = this.url + "/getLoginMsg";
            String msg = HttpUtil.sendHttpPost(url, map);
            ucMsg = JsonUtil.toInstance(msg, UcMsg.class);
            logger.info("LoginCmdThread login getLoginMsg {} ", msg);
            if (ucMsg.getCode() == UcCodeEnum.OK.getCode()) {
                LoginMsg loginMsg = JsonUtil.toInstance(ucMsg.getData(), LoginMsg.class);
                String address = loginMsg.getAddress();
                String[] host = address.split(":");
                loginMsgPram.setAccountKey(loginMsg.getAccountKey());
                loginMsgPram.setServerId(loginMsgPram.getServerId());
                Channel channel = nettyClientFactory.createNetty(host[0], Integer.valueOf(host[1]));
                sendMsgThreadManager.getLoginMsgPramMap().put(channel, loginMsgPram);
                logger.info("LoginCmdThread login {} ", loginMsgPram.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public synchronized UcServer getServer(ServerList serverList, int serverId) {
        if (serverId != 0) {
            for (UcServer server : serverList.getUcServers()) {
                if (server.getServerId() == serverId) {
                    return server;
                }
            }
        }
        for (UcServer server : serverList.getUcServers()) {
            if (loginInfos.get(server.getServerId()) == null) {
                LoginServerInfo loginServerInfo = new LoginServerInfo(server, server.getServerId());
                loginInfos.put(server.getServerId(), loginServerInfo);
                return server;
            }
        }
        List<LoginServerInfo> loginServerInfos = new ArrayList<>(loginInfos.values());
        Collections.sort(loginServerInfos);
        LoginServerInfo l = loginServerInfos.get(0);
        l.setTime(l.getTime() + 1);
        return l.getServer();
    }

}
