package com.dykj.rpg.uc.nacos.core;

import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.dykj.rpg.common.consts.ServerKey;
import com.dykj.rpg.common.redis.cache.ServerNewCacheMgr;
import com.dykj.rpg.common.redis.data.InstanceData;
import com.dykj.rpg.common.redis.data.ServerData;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jyb
 * @date 2020/12/31 16:52
 * @Description
 */
public class ServiceEvenListener implements EventListener {


    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onEvent(Event event) {
        if (event == null) {
            return;
        }
        NamingEvent namingEvent = (NamingEvent) event;
        int serverId = getServerId(namingEvent.getServiceName());
        if (serverId == 0) {
            return;
        }
        ServerData serverData = BeanFactory.getBean(ServerNewCacheMgr.class).get(serverId);
        if (serverData == null) {
            logger.error("ServiceEvenListener serverData is null serverId {} ", serverId);
            return;
        }

        try {
            BeanFactory.getBean(ServerNewCacheMgr.class).lock(serverData.getServerModel().getServerId());
            if (serverData.getInstance() == null) {
                if (namingEvent.getInstances() != null && namingEvent.getInstances().size() > 0) {
                    serverData.setInstance(new InstanceData(namingEvent.getInstances().get(0), serverId));
                    BeanFactory.getBean(ServerNewCacheMgr.class).put(serverData);
                    logger.info("server {} chose node {} ", serverData.getServerModel().getServerId(), serverData.getInstance().toString());
                } else {
                    serverData.setInstance(null);
                    BeanFactory.getBean(ServerNewCacheMgr.class).put(serverData);
                    logger.info("=============server {} has no node  chose================= ", serverData.getServerModel().getServerId());
                    return;
                }
            }
            boolean flag = false;
            for (Instance instance : namingEvent.getInstances()) {
                if (instance.getIp().equals(serverData.getInstance().getIp()) && instance.getPort() == serverData.getInstance().getPort()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                if (namingEvent.getInstances() != null && namingEvent.getInstances().size() > 0) {
                    InstanceData instanceData = new InstanceData(namingEvent.getInstances().get(0), serverId);
                    logger.info("server {} node {} is offline , chose new node {} ", serverData.getInstance().toString(), instanceData.toString());
                    serverData.setInstance(instanceData);
                    BeanFactory.getBean(ServerNewCacheMgr.class).put(serverData);
                } else {
                    serverData.setInstance(null);
                    BeanFactory.getBean(ServerNewCacheMgr.class).put(serverData);
                    logger.info("server {} node {} is offline , no node  chose ", serverData.getServerModel().getServerId());
                }
            } else {
                logger.info("server {} node {} is online ", serverData.getServerModel().getServerId(), serverData.getInstance().toString());

            }

        } finally {
            BeanFactory.getBean(ServerNewCacheMgr.class).unlock(serverData.getServerModel().getServerId());
        }

        //logger.info("+++++++++++++++++++++++++++++" + namingEvent.toString());
    }

    /**
     * game_server_service@@server_1001
     *
     * @param serviceName
     * @return
     */
    private int getServerId(String serviceName) {

        try {
            String[] names = serviceName.split("@@");
            if (!names[0].equals(ServerKey.GAME_SERVER_SERVICE)) {
                return 0;
            }
            String[] serverStr = names[1].split("\\_");
            if (!serverStr[0].equals("server")) {
                return 0;
            }
            return Integer.valueOf(serverStr[1]);
        } catch (Exception e) {
            logger.error("ServiceEvenListener error  serviceName {} ", serviceName, e);
        }
        return 0;
    }
}