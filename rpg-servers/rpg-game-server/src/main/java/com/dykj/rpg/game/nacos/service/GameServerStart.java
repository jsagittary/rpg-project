package com.dykj.rpg.game.nacos.service;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.PreservedMetadataKeys;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.dykj.rpg.common.consts.NacosEnum;
import com.dykj.rpg.common.consts.ServerKey;
import com.dykj.rpg.common.module.uc.model.PhysicalMachineModel;
import com.dykj.rpg.common.redis.cache.ServerNewCacheMgr;
import com.dykj.rpg.common.redis.data.InstanceData;
import com.dykj.rpg.game.module.server.logic.GameServerConfig;
import com.dykj.rpg.net.jetty.JettyServer;
import com.dykj.rpg.net.netty.NettyServer;
import com.dykj.rpg.util.IPUtil;
import com.dykj.rpg.util.JsonUtil;
import com.dykj.rpg.util.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.util.*;

/**
 * @author jyb
 * @date 2020/12/18 13:51
 * @Description
 */
@Service
public class GameServerStart {


    @NacosInjected
    private NamingService namingService;

    @Resource
    private GameServerConfig gameServerConfig;

    private Logger logger = LoggerFactory.getLogger(getClass());
    @NacosInjected
    private ConfigService configService;

    @Resource
    private JettyServer jettyRunner;
    @Resource
    private NettyServer nettyRunner;

    @Resource
    private ServerNewCacheMgr serverNewCacheMgr;

    private static final  int TIMEOUT = 30;

    @PostConstruct
    public void registerInstance() {
        if (gameServerConfig.getNodes() == null || gameServerConfig.getNodes().length < 1) {
            System.exit(-1);
            logger.error("NacosServerService error serverNodes  is null ");
            return;
        }
        try {
            Set<String> ips = IPUtil.getIpAddress();
            String content = configService.getConfig(NacosEnum.PHYSICAL_MACHINE.getDataId(), NacosEnum.PHYSICAL_MACHINE.getGroup(), 5000);
            List<PhysicalMachineModel> physicalMachineModelList = JsonUtil.toList(content, PhysicalMachineModel.class);
            int i = 0;
            PhysicalMachineModel local = null;
            for (; i < gameServerConfig.getNodes().length; i++) {
                for (PhysicalMachineModel physicalMachineModel : physicalMachineModelList) {
                    if (physicalMachineModel.getMachineId() == gameServerConfig.getNodes()[i]) {
                        for (String ip : ips) {
                            if (ip.equals(physicalMachineModel.getInternetIp()) || ip.equals(physicalMachineModel.getIntranetIp())) {
                                local = physicalMachineModel;
                                break;
                            }
                        }
                    }
                }
            }
            if (local == null) {
                logger.error("NacosServerService start error ips {} content {} ", Arrays.toString(ips.toArray()), content);
                System.exit(-1);
                return;
            }
            Instance instance = new Instance();
            instance.setIp(local.getInternetIp());
            String serviceName = ServerKey.SERVICE_PREFIX + gameServerConfig.getServerId();
            instance.setServiceName(serviceName);
            instance.getMetadata().put(PreservedMetadataKeys.HEART_BEAT_TIMEOUT, "30");
            instance.getMetadata().put(PreservedMetadataKeys.IP_DELETE_TIMEOUT, "30");
            instance.getMetadata().put(PreservedMetadataKeys.HEART_BEAT_INTERVAL, "30");
            Map<String, String> ports = generatePort(local.getInternetIp(), serviceName);
            instance.setPort(Integer.valueOf(ports.get(ServerKey.NACOS_PORT)));
            instance.getMetadata().putAll(ports);
            instance.getMetadata().put(ServerKey.SERVICE_PREFIX, String.valueOf(gameServerConfig.getServerId()));
            namingService.registerInstance(instance.getServiceName(), ServerKey.GAME_SERVER_SERVICE, instance);
            serverNewCacheMgr.setInstance(new InstanceData(instance, gameServerConfig.getServerModel(), gameServerConfig.getServerId()));
           /* startJetty();
            startNetty();*/
            if (SystemUtil.isLinux()) {
                loadProperties(instance);
            }
        } catch (Exception e) {
            logger.error("NacosServerService register error ", e);
            System.exit(-1);
        }
    }


    private void loadProperties(Instance instance) {
        try {
            Properties prop = new Properties();
            String path = gameServerConfig.getPath() + ServerKey.SERVICE_PREFIX + gameServerConfig.getServerId() + "/global.properties";
            logger.info("loadProperties path {} ",path);
            FileOutputStream oFile = new FileOutputStream(gameServerConfig.getPath() + ServerKey.SERVICE_PREFIX + gameServerConfig.getServerId() + "/global.properties", false);//true表示追加打开
            prop.setProperty(ServerKey.NET_SERVER_HOST, instance.getIp());
            prop.setProperty(ServerKey.NACOS_PORT, instance.getMetadata().get(ServerKey.NACOS_PORT));
            prop.setProperty(ServerKey.JETTY_PORT, instance.getMetadata().get(ServerKey.JETTY_PORT));
            prop.setProperty(ServerKey.NETTY_PORT, instance.getMetadata().get(ServerKey.NETTY_PORT));
            prop.setProperty(ServerKey.NACOS_PORT, instance.getMetadata().get(ServerKey.NACOS_PORT));
            prop.store(oFile, "The New properties file");
            oFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> generatePort(String ip, String serviceName) throws Exception {
        Map<String, String> map = new HashMap<>();
        char[] str = String.valueOf(gameServerConfig.getServerId()).toCharArray();

        StringBuffer value = new StringBuffer();
        value.append(gameServerConfig.getServerId());
        if (str.length < 4) {
            for (int index = 0; index < 4; index++) {
                if (index > str.length - 1) {
                    value.append(0);
                }
            }
        }
        String nettyPort = String.valueOf(Integer.valueOf(value.toString()) + 100);
        String jettyPort = String.valueOf(Integer.valueOf(value.toString()) + 200);
        String nacosPort = String.valueOf(Integer.valueOf(value.toString()) + 300);
        map.put(ServerKey.NETTY_PORT, nettyPort);
        map.put(ServerKey.JETTY_PORT, jettyPort);
        map.put(ServerKey.NACOS_PORT, nacosPort);
        List<Instance> instances = namingService.getAllInstances(serviceName, ServerKey.GAME_SERVER_SERVICE);
        List<Integer> ports = new ArrayList<>();
        if (instances != null && instances.size() > 0) {
            for (Instance instance : instances) {
                if (instance.getIp().equals(ip)) {
                    String existNettyPort = instance.getMetadata().get(ServerKey.NETTY_PORT);
                    String existJettyPort = instance.getMetadata().get(ServerKey.JETTY_PORT);
                    String existNacosPort = instance.getMetadata().get(ServerKey.NACOS_PORT);
                    if (nettyPort.equals(existNettyPort) || jettyPort.equals(existJettyPort) || nacosPort.equals(existNacosPort)) {
                        ports.add(Integer.valueOf(existNettyPort));
                        ports.add(Integer.valueOf(existJettyPort));
                        ports.add(Integer.valueOf(existNacosPort));
                    }

                }
            }
            if (ports.size() > 0) {
                Collections.sort(ports);
                int max = ports.get(0);
                map.put(ServerKey.NETTY_PORT, String.valueOf(max + 400));
                map.put(ServerKey.JETTY_PORT, String.valueOf(max + 500));
                map.put(ServerKey.NACOS_PORT, String.valueOf(max + 600));
            }

        }
        return map;
    }


    public void startJetty() throws Exception {
        jettyRunner.setPort(Integer.valueOf(serverNewCacheMgr.getInstance().getMetadata().get(ServerKey.JETTY_PORT)));
        jettyRunner.start();
    }


    public void startNetty() throws Exception {
        nettyRunner.setPort(Integer.valueOf(serverNewCacheMgr.getInstance().getMetadata().get(ServerKey.NETTY_PORT)));
        nettyRunner.start();
    }
}