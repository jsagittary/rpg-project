package com.dykj.rpg.battle.ice.server;

import Ice.*;
import IceBox.Service;
import com.dykj.rpg.battle.constant.GameConstant;
import com.dykj.rpg.battle.dictionary.StaticDictionary;
import com.dykj.rpg.battle.ice.client.BattleCacheClient;
import com.dykj.rpg.battle.logic.BattleContainer;
import com.dykj.rpg.battle.manager.BattleManager;
import com.dykj.rpg.battle.constant.BattleTypeConstant;
import com.dykj.rpg.battle.detour.MapLoader;
import com.dykj.rpg.battle.ice.service._BattleServiceDisp;
import com.dykj.rpg.battle.role.BattleRole;
import com.dykj.rpg.net.core.UdpSessionManager;
import com.dykj.rpg.net.netty.NettyServer;
import com.dykj.rpg.protocol.battle.BattleRoleBasic;
import com.dykj.rpg.protocol.battle.BattleRoleBasicInfoRs;
import com.dykj.rpg.protocol.battle.ReleaseSkillRq;
import com.dykj.rpg.util.spring.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.Exception;
import java.util.List;

public class BattleServiceImpl extends _BattleServiceDisp implements Service {

    private static final long serialVersionUID = 3308104399466330919L;

    private ObjectAdapter _adapter;

    private NettyServer server;

    private int battleServerId;

    private int registerToCache;

    @Override
    public void start(String name, Communicator communicator, String[] strings) {
        System.out.println("defaultLocator = "+communicator.getDefaultLocator());
        battleServerId = Integer.parseInt(communicator.getProperties().getProperty("BattleServer.id"));
        registerToCache = Integer.parseInt(communicator.getProperties().getProperty("BattleServer.registerToCache"));

        //GameConstant.LOG_HURT_CALCULATION = Integer.parseInt(communicator.getProperties().getProperty("Log.hurtCalculation"))==1;
        //GameConstant.TEST_LEVEL_MAP =  communicator.getProperties().getProperty("Test.levelMapName");
        //GameConstant.TEST_SKILLS = communicator.getProperties().getProperty("Test.skills");

        String adapterName = communicator.getProperties().getProperty("Adapter.Name");
        System.out.println("ServiceName = "+name);
        System.out.println("AdapterName = "+adapterName);

        String nettyServerIp = communicator.getProperties().getProperty("NettyServer.ip");
        String nettyServerPort = communicator.getProperties().getProperty("NettyServer.port");
        String nettyServerType = communicator.getProperties().getProperty("NettyServer.type");
        System.out.println("nettyServerIp = "+nettyServerIp);
        System.out.println("nettyServerPort = "+nettyServerPort);
        System.out.println("nettyServerType = "+nettyServerType);

        // 通讯器创建适配器
        _adapter = communicator.createObjectAdapter(adapterName);
        // ice对象
        Ice.Object object = this;
        // 适配器添加ice对象
        _adapter.add(object, communicator.stringToIdentity(name));

        // 激活适配器
        _adapter.activate();
        System.out.println(name + "-- adapter 激活成功");

        //Admin admin = (Admin)communicator.getAdmin();
        ObjectPrx admin = communicator.getAdmin();
        //String[] nodeNames = admin.getAllNodeNames();

        try {
            startServer(nettyServerIp,nettyServerPort,nettyServerType);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void startServer(String nettyServerIp,String nettyServerPort,String nettyServerType){

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

        //预加载地图网格信息 (来源：resouces/obj)
        MapLoader.getInstance().loadMap();

        //启动战斗服务,添加机器人战斗
        BattleManager.getInstance().addRobotBattle();

        //testOne();
   }

    private void testOne(){

        UdpSessionManager.getInstance().register(1,1,null);
        BattleContainer battleContainer = BattleManager.getInstance().createOneBattle(1);
        battleContainer.startBattle();

        //testResponse(battleLogic);

    }

    private static void testResponse(BattleContainer battleContainer){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);

                    System.out.println("--------------testResponse-------------");
                    battleContainer.waitHandlerDataManager.addReleaseSkillToWaitMap(1,1,23004,null);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    private void testRequest(){
        BattleRoleBasicInfoRs response = new BattleRoleBasicInfoRs();
        List<BattleRoleBasic> roleBasicList = response.getRoleBasics();
        BattleContainer battleContainer = BattleManager.getInstance().getBattleContainerByPlayerId(1);
        if(battleContainer != null){
            List<BattleRole> roles = battleContainer.getValidBattleRoles();
            if(roles != null && roles.size() > 0){
                for(BattleRole battleRole : roles){
                    if(battleRole != null){

                        BattleRoleBasic roleBasic = new BattleRoleBasic();
                        roleBasic.setId(battleRole.getModelId());
                        roleBasic.setType(battleRole.getRoleType());
                        roleBasic.setRoleId(battleRole.getRoleId());
                        roleBasic.setLevel(battleRole.getRoleLevel());
                        roleBasic.setMaxBlood(battleRole.attributeManager.maxBlood);

                        roleBasicList.add(roleBasic);
                    }
                }
            }
        }

        System.out.println(response.toString());
    }

    @Override
    public void stop() {

    }

    @Override
    public String[] registerToBattleServer(int sessionId, int userId, Current __current) {
        if(sessionId != 0 && userId != 0){

            //CallbackGameServerPrx gameServerPrx = CallbackGameServerPrxHelper.uncheckedCast(__current.con.createProxy(Util.stringToIdentity("CallbackGameServer")));

            //UdpSessionManager.getInstance().register(sessionId,userId,gameServerPrx);

            return new String[]{"true",server.getAddress(),server.getPort()+""};
        }else{
            return new String[]{"false"};
        }
    }

    @Override
    public void kickOut(int sessionId, Current __current) {
        UdpSessionManager.getInstance().fireSession(sessionId);
    }
}
