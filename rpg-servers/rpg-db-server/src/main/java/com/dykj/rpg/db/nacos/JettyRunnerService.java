package com.dykj.rpg.db.nacos;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.dykj.rpg.common.consts.NacosEnum;
import com.dykj.rpg.common.consts.ServerKey;
import com.dykj.rpg.common.module.uc.model.PhysicalMachineModel;
import com.dykj.rpg.db.nacos.config.DbConfig;
import com.dykj.rpg.net.jetty.JettyServer;
import com.dykj.rpg.util.JsonUtil;
import com.dykj.rpg.util.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.util.*;

/**
 * @author jyb
 * @date 2020/12/29 20:36
 * @Description
 */
@Service
@Order(2)
public class JettyRunnerService {

    @Resource
    private JettyServer jettyRunner;
    @NacosInjected
    private ConfigService configService;
    @NacosInjected
    private NamingService namingService;
    @Resource
    private DbConfig dbConfig;

    private Logger logger = LoggerFactory.getLogger(getClass());


    @PostConstruct
    public void start() {
        try {

            String content = configService.getConfig(NacosEnum.PHYSICAL_MACHINE.getDataId(), NacosEnum.PHYSICAL_MACHINE.getGroup(), 5000);
            List<PhysicalMachineModel> physicalMachineModelList = JsonUtil.toList(content, PhysicalMachineModel.class);
            Iterator<PhysicalMachineModel> it = physicalMachineModelList.iterator();
            PhysicalMachineModel physicalMachineModel = null;
            while (it.hasNext()) {
                PhysicalMachineModel p = it.next();
                if (p.getMachineId() == dbConfig.getDbNode()) {
                    physicalMachineModel = p;
                    break;
                }
            }
            jettyRunner.setPort(dbConfig.getJettyPort());
            jettyRunner.start();
            if (SystemUtil.isLinux()) {
                loadProperties();
            }
        } catch (Exception e) {
            logger.error("NacosClientConfigService publishPhysicalMachineConfig error", e);
            System.exit(-1);
        }
    }

    private void loadProperties() {
        FileOutputStream oFile = null;
        try {
            Properties prop = new Properties();
            String path = dbConfig.getPath() + ServerKey.DB_SERVICE_PREFIX + dbConfig.getServerId() + "/global.properties";
            logger.info("loadProperties path {} ", path);
            oFile = new FileOutputStream(path, false);//true表示追加打开
            prop.setProperty(ServerKey.NET_SERVER_HOST, jettyRunner.getHost());
            prop.setProperty(ServerKey.JETTY_PORT, String.valueOf(jettyRunner.getPort()));
            prop.store(oFile, "The New properties file");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (oFile != null) {
                try {
                    oFile.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 测试方法 保留
     *
     * @param ip
     * @param serviceName
     * @return
     * @throws Exception
     */
    @Deprecated
    private Map<String, String> generatePort(String ip, String serviceName) throws Exception {
        Map<String, String> map = new HashMap<>();
        char[] str = String.valueOf(1001).toCharArray();
        StringBuffer value = new StringBuffer();
        value.append(1001);
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

}