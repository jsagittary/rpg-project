package com.dykj.rpg.uc.servlet;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.dykj.rpg.common.consts.ServerKey;
import com.dykj.rpg.common.consts.UcCodeEnum;
import com.dykj.rpg.common.module.uc.logic.LoginMsg;
import com.dykj.rpg.common.module.uc.logic.ServerList;
import com.dykj.rpg.common.module.uc.logic.UcMsg;
import com.dykj.rpg.common.module.uc.model.AccountInfoModel;
import com.dykj.rpg.common.module.uc.model.ServerModel;
import com.dykj.rpg.common.redis.cache.AccountCacheMgr;
import com.dykj.rpg.common.redis.cache.LoginDataCacheMgr;
import com.dykj.rpg.common.redis.cache.ServerNewCacheMgr;
import com.dykj.rpg.common.redis.data.InstanceData;
import com.dykj.rpg.common.redis.data.LoginData;
import com.dykj.rpg.common.redis.data.ServerData;
import com.dykj.rpg.uc.remote.consumer.GameConsumer;
import com.dykj.rpg.uc.service.AccountService;
import com.dykj.rpg.uc.service.ServerService;
import com.dykj.rpg.util.IPUtil;
import com.dykj.rpg.util.JsonUtil;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@RestController
public class ServerHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @ResponseBody
    @RequestMapping(value = "getServerList", method = RequestMethod.POST)
    public UcMsg getServerList(HttpServletRequest request, HttpServletResponse response) {
        String channel = request.getParameter("channel");
        if (channel != null && channel.equals("win2015")) {
            channel = "NAN";
        }
        String system = request.getParameter("system");
        String account = request.getParameter("account");
        String version = request.getParameter("version");
        System.out.println("channel=" + channel + "; account=" + account + ";version =" + version);
        if (version == null) {
            version = "1.0.0";
        }
        if (null == account || account.equals("")) {
            return new UcMsg(UcCodeEnum.ACCOUNT_NOT_EXIST);
        }
        if (null == channel || channel.equals("")) {
            return new UcMsg(UcCodeEnum.CHANNEL_NOT_EXIST);
        }

        //TODO 这里可能要验证sdk
        String ipAddress = IPUtil.getIpAddress(request);
        ServerList serverList = BeanFactory.getBean(ServerService.class).getServerDataList(channel, account, version, ipAddress);
        return new UcMsg(JsonUtil.toJsonString(serverList));
    }

    @ResponseBody
    @RequestMapping(value = "loginUc", method = RequestMethod.POST)
    public UcMsg loginUc(String channel, String account) {
        AccountInfoModel accountInfoModel = BeanFactory.getBean(AccountService.class).getAccount(channel, account);
        return new UcMsg(JsonUtil.toJsonString(accountInfoModel));
    }


    @ResponseBody
    @RequestMapping(value = "testDubbo", method = RequestMethod.GET)
    public String testDubbo() {
        GameConsumer gameConsumer = BeanFactory.getBean(GameConsumer.class);
        return gameConsumer.getHost(1);
    }

    @ResponseBody
    @RequestMapping(value = "getServerHost", method = RequestMethod.POST)
    public UcMsg getServerAddress(int serverId) {
        GameConsumer gameConsumer = BeanFactory.getBean(GameConsumer.class);
        String address = gameConsumer.getHost(serverId);
        if (null == address || address.equals("")) {
            return new UcMsg(UcCodeEnum.ADDRESS_NOT_EXIST);
        } else {
            return new UcMsg(address);
        }
    }


//    @ResponseBody
//    @RequestMapping(value = "getLoginMsg", method = RequestMethod.POST)
//    public UcMsg getServerAddress(int serverId, String channel, String account) {
//        GameConsumer gameConsumer = BeanFactory.getBean(GameConsumer.class);
//        AccountService accountService = BeanFactory.getBean(AccountService.class);
//        AccountInfoModel accountInfoModel = accountService.getAccount(channel, account);
//        LoginDataCacheMgr loginDataCacheMgr = BeanFactory.getBean(LoginDataCacheMgr.class);
//        AccountCacheMgr accountCacheMgr = BeanFactory.getBean(AccountCacheMgr.class);
//        if (accountInfoModel == null) {
//            return new UcMsg(UcCodeEnum.ACCOUNT_NOT_EXIST);
//        }
//        String address =null;
//        try {
//          gameConsumer.getHost(serverId);
//        }catch (Exception e){
//            e.printStackTrace();
//            logger.error("ServerHandler getServerAddress error : server {}  is not open ",serverId);
//            return new UcMsg(UcCodeEnum.SERVER_NOT_OPEN);
//        }
//        if (null == address || address.equals("")) {
//            return new UcMsg(UcCodeEnum.ADDRESS_NOT_EXIST);
//        } else {
//            LoginMsg loginMsg = new LoginMsg(accountInfoModel.getAccountKey(), address);
//            LoginData loginData = new LoginData(accountInfoModel.getAccountKey(), account, channel, serverId);
//            loginDataCacheMgr.set(accountInfoModel.getAccountKey(), loginData, 10 * 60 * 1000, TimeUnit.MILLISECONDS);
//            if (accountInfoModel.getLastLoginServer() != serverId) {
//                accountInfoModel.setLastLoginServer(serverId);
//                accountCacheMgr.setTimeOut(accountInfoModel, AccountService.KEEP_TIME);
//                accountService.updateAccount(accountInfoModel);
//            }
//            return new UcMsg(JsonUtil.toJsonString(loginMsg));
//        }
//    }


    @ResponseBody
    @RequestMapping(value = "getLoginMsg", method = RequestMethod.POST)
    public UcMsg getServerAddress(int serverId, String channel, String account) {
        AccountService accountService = BeanFactory.getBean(AccountService.class);
        AccountInfoModel accountInfoModel = accountService.getAccount(channel, account);
        LoginDataCacheMgr loginDataCacheMgr = BeanFactory.getBean(LoginDataCacheMgr.class);
        AccountCacheMgr accountCacheMgr = BeanFactory.getBean(AccountCacheMgr.class);
        if (accountInfoModel == null) {
            return new UcMsg(UcCodeEnum.ACCOUNT_NOT_EXIST);
        }
        ServerData serverData = BeanFactory.getBean(ServerNewCacheMgr.class).get(serverId);
        if (serverData == null) {
            return new UcMsg(UcCodeEnum.SERVER_NOT_REGISTER);
        }
        if (serverData.getInstance() == null) {
            try {
                BeanFactory.getBean(ServerNewCacheMgr.class).lock(serverData.getServerModel().getServerId());
                Instance instance = BeanFactory.getBean(ServerService.class).selectOneHealthyInstance(serverId);
                if (instance == null) {
                    return new UcMsg(UcCodeEnum.SERVER_NOT_REGISTER);
                } else {
                    serverData.setInstance(new InstanceData(instance, serverId));
                    BeanFactory.getBean(ServerNewCacheMgr.class).put(serverData);
                }
            } finally {
                BeanFactory.getBean(ServerNewCacheMgr.class).unlock(serverData.getServerModel().getServerId());
            }
        }
        String address = serverData.getInstance().getIp() + ":" + serverData.getInstance().getMetadata().get(ServerKey.NETTY_PORT);
        LoginMsg loginMsg = new LoginMsg(accountInfoModel.getAccountKey(), address);
        LoginData loginData = new LoginData(accountInfoModel.getAccountKey(), account, channel, serverId);
        loginDataCacheMgr.set(accountInfoModel.getAccountKey(), loginData, 10 * 60 * 1000, TimeUnit.MILLISECONDS);
        if (accountInfoModel.getLastLoginServer() != serverId) {
            accountInfoModel.setLastLoginServer(serverId);
            accountCacheMgr.setTimeOut(accountInfoModel, AccountService.KEEP_TIME);
            accountService.updateAccount(accountInfoModel);
        }
        return new UcMsg(JsonUtil.toJsonString(loginMsg));
    }


    /**
     * 添加区服
     *
     * @param server
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addServer", method = RequestMethod.POST)
    public UcMsg addServer(ServerModel server) {
        try {
            /**
             * 判断参数
             */
            if (null == server) {
                logger.error("ServerHandler addServer : server {},desc{}}", server, UcCodeEnum.PARAM_ERROR.getDesc());
                return new UcMsg(UcCodeEnum.PARAM_ERROR);
            }

            /**
             * json转成对象
             */
            ServerService serverService = BeanFactory.getBean(ServerService.class);
            int total = serverService.addServer(server);
            /**
             * 插入失败
             */
            if (total == 0) {
                logger.error("ServerHandler addServer : server {},desc{}}", server, UcCodeEnum.SYS_ERROR.getDesc());
                return new UcMsg(UcCodeEnum.SYS_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("ServerHandler addServer : server {},desc{}}", server, UcCodeEnum.SYS_ERROR.getDesc());
            return new UcMsg(UcCodeEnum.SYS_ERROR);
        }
        return new UcMsg(UcCodeEnum.OK);
    }

    /**
     * 修改区服信息
     *
     * @param server
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateServer", method = RequestMethod.POST)
    public UcMsg updateServer(ServerModel server) {
        //判断参数
        if (null == server || server.getServerId() == 0) {
            logger.error("ServerHandler updateServer : server {},desc{}}", server, UcCodeEnum.PARAM_ERROR.getDesc());
            return new UcMsg(UcCodeEnum.PARAM_ERROR);
        }

        try {
            ServerService serverService = BeanFactory.getBean(ServerService.class);
            int total = serverService.updateServer(server);
            //修改失败
            if (total == 0) {
                logger.error("ServerHandler updateServer : server {},desc{}}", server, UcCodeEnum.SERVER_NOT_EXIST.getDesc());
                return new UcMsg(UcCodeEnum.SERVER_NOT_EXIST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("ServerHandler updateServer : server {},desc{}", server, UcCodeEnum.SYS_ERROR.getDesc());
            return new UcMsg(UcCodeEnum.SYS_ERROR);
        }
        return new UcMsg(UcCodeEnum.OK);
    }

    /* *//**
     * 查询区服相关信息
     *
     * @param serverId
     * @return
     *//*
    @ResponseBody
    @RequestMapping(value = "/server/findServer.do", method = RequestMethod.POST)
    public UcMsg findServer(String serverId) {
        Server serveInfo = null;
        try {
            *//**
     * 判断参数
     *//*
            if (null == serverId || serverId.equals("")) {
                logger.error("ServerHandler findServer : server {},desc{}}", serverId, UcCodeEnum.PARAM_ERROR.getDesc());
                return new Message(UcCodeEnum.PARAM_ERROR);
            }
            ServerService serverService = BeanFactory.getBean(ServerService.class);
            serveInfo = serverService.getServer(Integer.parseInt(serverId));
            if (serveInfo == null) {
                logger.error("ServerHandler updateServer : server {},desc{}}", serverId, UcCodeEnum.SERVER_NOT_EXIST.getDesc());
                return new Message(UcCodeEnum.SERVER_NOT_EXIST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("ServerHandler updateServer : server {},desc{}", serverId, UcCodeEnum.SYS_ERROR.getDesc());
            return new Message(UcCodeEnum.SYS_ERROR);
        }
        return new Message(UcCodeEnum.SUCCESS, JSON.toJSONString(serveInfo));
    }*/
}
