//package com.dykj.rpg.client.core;
//
//import com.dykj.rpg.client.ClientSession;
//import com.dykj.rpg.client.NettyClientFactory;
//import com.dykj.rpg.util.HttpUtil;
//import com.dykj.rpg.util.spring.BeanFactory;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @Author: jyb
// * @Date: 2020/9/15 18:07
// * @Description:
// */
//public class ReconnectTask implements Runnable {
//
//    private ClientSession clientSession;
//
//    private String url;
//
//    public ReconnectTask(ClientSession clientSession, String url) {
//        this.clientSession = clientSession;
//        this.url = url;
//    }
//
//    @Override
//    public void run() {
//        Map<String, String> map = new HashMap<>();
//        map.put("serverId", String.valueOf(clientSession.getServerId()));
//        String address = null;
//        try {
//            address = HttpUtil.httpGet(url, map);
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//        String[] host = address.split(":");
//        BeanFactory.getBean(NettyClientFactory.class).createNetty(host[0], Integer.valueOf(host[1]));
//    }
//}
