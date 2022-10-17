package com.dykj.rpg.uc.service;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.dykj.rpg.common.consts.ServerKey;
import com.dykj.rpg.common.module.uc.model.AccountInfoModel;
import com.dykj.rpg.common.module.uc.model.ServerModel;
import com.dykj.rpg.common.redis.cache.ServerNewCacheMgr;
import com.dykj.rpg.common.redis.data.ServerData;
import com.dykj.rpg.common.module.uc.logic.ServerList;
import com.dykj.rpg.common.module.uc.logic.UcServer;
import com.dykj.rpg.uc.dao.ServerNewDao;
import com.dykj.rpg.uc.nacos.service.ServerRegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ServerService {
    @Resource
    private AccountService accountService;

    @Resource
    private ServerNewCacheMgr serverNewCacheMgr;


    @NacosInjected
    private NamingService namingService;

    @Resource
    private ServerNewDao serverNewDao;

    @Resource
    private ServerRegisterService serverRegisterService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 拿到服务器列表（后续逐步完善这个方法）
     *
     * @param channel
     * @param version
     * @return
     */
    public ServerList getServerDataList(String channel, String account, String version, String ipAddress) {
        AccountInfoModel accountInfoModel = accountService.getAccount(channel, account);
        List<ServerData> serverDatas = serverNewCacheMgr.values();
        ServerList serverList = new ServerList();
        boolean flag = false;
        for (ServerData serverData : serverDatas) {
            ServerModel serverMode = serverData.getServerModel();

            //判断是否有白名单配置
            boolean noHaveWhite = serverMode.getAccountWhiteLists() == null && serverMode.getIpWhiteLists() == null;
            if (noHaveWhite) {
                serverList.getUcServers().add(new UcServer(serverMode));
            } else {
                //判断满足白名单条件
                boolean isWhite = serverMode.getAccountWhiteLists() != null && serverMode.getAccountWhiteLists().contains(account) || serverMode.getIpWhiteLists() != null && serverMode.getIpWhiteLists().contains(ipAddress);
                if (!isWhite) {
                    continue;
                }
                serverList.getUcServers().add(new UcServer(serverMode));
            }

            if (serverData.getServerModel().getServerId() == accountInfoModel.getLastLoginServer()) {
                flag = true;
            }
        }
        if (flag) {
            serverList.setLastServerId(accountInfoModel.getLastLoginServer());
        }
        return serverList;
    }

    /**
     * 查找一个可用的服务
     *
     * @param serverId
     * @return
     */
    public Instance selectOneHealthyInstance(int serverId) {
        Instance instance = null;
        try {
            instance = namingService.selectOneHealthyInstance(ServerKey.SERVICE_PREFIX + serverId, ServerKey.GAME_SERVER_SERVICE);
        } catch (Exception e) {
            logger.info("ServerRegisterService subscribeService  server {} has no service ", serverId);
        }
        return instance;
    }

    /**
     * 添加区服
     *
     * @param serverModel
     * @return
     */
    public int addServer(ServerModel serverModel) {
        int total = 0;

        try {
            total = serverNewDao.insert(serverModel);
            serverRegisterService.subscribeService();
        } catch (Exception e) {
            logger.info("ServerService addServer is error serverModel {} ", serverModel);
        }
        return total;
    }

    /**
     * 修改区服
     *
     * @param serverModel
     * @return
     */
    public int updateServer(ServerModel serverModel) {
        int total = 0;

        try {
            ServerModel server = serverNewDao.queryByPrimaykey(serverModel.getServerId());
            if (server == null) {
                return 0;
            }

            Field[] declaredFields = serverModel.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                Object getMethod = getGetMethod(serverModel, declaredField.getName());
                if (null != getMethod) {
                    if ((getMethod == int.class || getMethod instanceof Integer) && (int) getMethod == 0) {
                        continue;
                    }
                    setValue(server, server.getClass(), declaredField.getName(), ServerModel.class.getDeclaredField(declaredField.getName()).getType(), getMethod);
                }
            }

            total = serverNewDao.updateByPrimaykey(server);
            serverRegisterService.subscribeService();
        } catch (Exception e) {
            logger.info("ServerService updateServer is error serverModel {} ", serverModel);
        }
        return total;
    }


    /**
     * 根据属性，获取get方法
     *
     * @param ob   对象
     * @param name 属性名
     * @return
     * @throws Exception
     */
    public static Object getGetMethod(Object ob, String name) throws Exception {
        Method[] m = ob.getClass().getMethods();
        for (int i = 0; i < m.length; i++) {
            if (("get" + name).toLowerCase().equals(m[i].getName().toLowerCase())) {
                return m[i].invoke(ob);
            }
        }
        return null;
    }

    /**
     * 根据属性，拿到set方法，并把值set到对象中
     *
     * @param obj       对象
     * @param clazz     对象的class
     * @param filedName 需要设置值得属性
     * @param typeClass
     * @param value
     */
    public static void setValue(Object obj, Class<?> clazz, String filedName, Class<?> typeClass, Object value) {
        filedName = removeLine(filedName);
        String methodName = "set" + filedName.substring(0, 1).toUpperCase() + filedName.substring(1);
        try {
            Method method = clazz.getDeclaredMethod(methodName, new Class[]{typeClass});
            method.invoke(obj, new Object[]{getClassTypeValue(typeClass, value)});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 处理字符串  如：  abc_dex ---> abcDex
     *
     * @param str
     * @return
     */
    public static String removeLine(String str) {
        if (null != str && str.contains("_")) {
            int i = str.indexOf("_");
            char ch = str.charAt(i + 1);
            char newCh = (ch + "").substring(0, 1).toUpperCase().toCharArray()[0];
            String newStr = str.replace(str.charAt(i + 1), newCh);
            String newStr2 = newStr.replace("_", "");
            return newStr2;
        }
        return str;
    }

    /**
     * 通过class类型获取获取对应类型的值
     *
     * @param typeClass class类型
     * @param value     值
     * @return Object
     */
    private static Object getClassTypeValue(Class<?> typeClass, Object value) {
        if (typeClass == int.class || value instanceof Integer) {
            if (null == value) {
                return 0;
            }
            return value;
        } else if (typeClass == short.class) {
            if (null == value) {
                return 0;
            }
            return value;
        } else if (typeClass == byte.class) {
            if (null == value) {
                return 0;
            }
            return value;
        } else if (typeClass == double.class) {
            if (null == value) {
                return 0;
            }
            return value;
        } else if (typeClass == long.class) {
            if (null == value) {
                return 0;
            }
            return value;
        } else if (typeClass == String.class) {
            if (null == value) {
                return "";
            }
            return value;
        } else if (typeClass == boolean.class) {
            if (null == value) {
                return true;
            }
            return value;
        } else if (typeClass == BigDecimal.class) {
            if (null == value) {
                return new BigDecimal(0);
            }
            return new BigDecimal(value + "");
        } else {
            return typeClass.cast(value);
        }
    }


}
