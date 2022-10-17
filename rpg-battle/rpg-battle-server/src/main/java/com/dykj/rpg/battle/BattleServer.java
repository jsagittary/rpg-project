package com.dykj.rpg.battle;

import com.dykj.rpg.battle.constant.GameConstant;
import com.dykj.rpg.battle.detour.MapLoader;
import com.dykj.rpg.battle.dictionary.StaticDictionary;
import com.dykj.rpg.battle.ice.client.BattleCacheClient;
import com.dykj.rpg.battle.manager.BattleManager;
import com.dykj.rpg.mapping.ProtocolMapping;
import com.dykj.rpg.net.netty.NettyServer;
import com.dykj.rpg.net.protocol.ProtocolPool;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BattleServer {
    private static Logger logger = LoggerFactory.getLogger(BattleServer.class);
    public static void main(String[] args) {
        //IceBox.Server icebox = new IceBox.Server();
        //icebox.main(new String[] {"--Ice.Config=icebox.properties"});
        long now = System.currentTimeMillis();
        startServer();
        logger.info("BattleServer START SUCCESS %% STARTTIME=" + (System.currentTimeMillis() - now));
        logger.info("BattleServer START COMPLETE");
        logger.info("LINUX LOG SUCCESS");
    }

    private static void startServer(){

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");

        BeanFactory.setApplicationContext(applicationContext);

        //加载配置参数 (来源：resouces/battle.properties)
        GameConstant.initConfigurature();

        NettyServer server = new NettyServer();
        try{
            //server.setAddress(nettyServerIp);
            server.setPort(GameConstant.NETTY_SERVER_PORT);
            server.setType(GameConstant.NETTY_SERVER_TYPE);
            server.start();



        }catch (Exception e){
            e.printStackTrace();
        }

        if(GameConstant.REGISTER_TO_CACHE){
            //启动缓存服务
            //CacheManager.getInstance().startMainServer();
            BattleCacheClient.getInstance().start(GameConstant.SERVER_ID);
        }

        //加载游戏静态数据 (来源：数据库)
        StaticDictionary.getInstance();

        //开启协议对象池
        ProtocolPool.getInstance().init(ProtocolMapping.getClassMap());

        //预加载地图网格信息 (来源：resouces/obj)
        MapLoader.getInstance().loadMap();

        //启动战斗服务,添加机器人战斗
        BattleManager.getInstance().addRobotBattle();

        //testOne();
    }
}
