package com.dykj.rpg.uc.nacos.service;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.dykj.rpg.common.consts.NacosEnum;
import com.dykj.rpg.common.consts.ServerKey;
import com.dykj.rpg.common.module.uc.model.ServerModel;
import com.dykj.rpg.common.redis.cache.ServerNewCacheMgr;
import com.dykj.rpg.common.redis.data.InstanceData;
import com.dykj.rpg.common.redis.data.ServerData;
import com.dykj.rpg.uc.dao.ServerNewDao;
import com.dykj.rpg.uc.nacos.core.ServiceEvenListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author jyb
 * @date 2020/12/31 16:25
 * @Description
 */
@Service
@Order(3)
public class ServerRegisterService {
    @Resource
    private ServerNewDao serverNewDao;
    @NacosInjected
    private ConfigService configService;
    @NacosInjected
    private NamingService namingService;
    @Resource
    private ServerNewCacheMgr serverNewCacheMgr;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @PostConstruct
    public void subscribeService() {
        try {
            List<ServerModel> serverModelList = serverNewDao.queryAll();
            for (ServerModel s : serverModelList) {
                Instance instance = null;
                try {
                    instance = namingService.selectOneHealthyInstance(ServerKey.SERVICE_PREFIX + s.getServerId(), ServerKey.GAME_SERVER_SERVICE);
                } catch (Exception e) {
                    logger.info("ServerRegisterService subscribeService  server {} has no service ", s.getServerId());
                }
                ServerData serverData = new ServerData();
                serverData.setServerModel(s);
                if (instance != null) {
                    serverData.setInstance(new InstanceData(instance,s.getServerId()));
                }
                serverNewCacheMgr.put(serverData);
                namingService.subscribe(ServerKey.SERVICE_PREFIX + s.getServerId(), ServerKey.GAME_SERVER_SERVICE, new ServiceEvenListener());
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}