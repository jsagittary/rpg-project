package com.dykj.rpg.battle.constant;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 游戏参数配置
 */
public class GameConstant {
    /**
     * 最大int
     */
    public final static int MAX_INTEGER = 0x7fffffff;
    /**
     * 服务器ID
     */
    public static int SERVER_ID = 0;
    /**
     * netty服务器ip
     */
    public static String NETTY_SERVER_IP = "127.0.0.1";
    /**
     * netty服务器端口
     */
    public static int NETTY_SERVER_PORT = 8201;
    /**
     * netty服务器类型
     */
    public static String NETTY_SERVER_TYPE = "udp";

    /**
     * 服务器每秒跑的游戏帧数，最好能整除1000
     */
    public static int GAME_FRAME = 10;
    /**
     * 服务器跑帧时每帧毫秒数
     */
    public static int FRAME_TIME = 1000/GAME_FRAME;
    /**
     * 服务器同时运行的最大战斗场次
     */
    public static int MAX_BATTLE_RUNNING = 3000;
    /**
     * 单场战斗的最大时长限定(以帧数做标准)，暂定为15分钟
     */
    public static int SINGLE_BATTLE_MAX_TIME = 15*60000;

    /**
     * 服务器单场战斗中最大战斗对象个数
     */
    public static int MAX_DETOUR_AGENT = 50;

    /**
     * 服务器跑帧的线程数
     */
    public static int RUN_FRANE_THREAD_SIZE = 6;
    /**
     * 服务器同步数据的线程数
     */
    public static int DATA_UPDATE_THREAD_SIZE = 1;

    /**
     * 对战斗逻辑进行时间轮切分管理的刻度（段数）
     */
    public static int ONE_FRAME_TICKNUM = 10;

    /**
     * 预先计算群体寻路帧数
     */
    public static int CROWN_ADVANCE_FRAME_NUM = 10;

    /**
     * 小怪对象池大小
     */
    public static int BATTLE_MONSTER_POOL_SIZE = 50;

    /**
     * kcp协议结构体大小
     */
    public static int KCP_MAX_LENGTH = 1400-24;

    /**
     * 技能资源最大类型ID
     */
    public static int SKILL_RESOURCE_MAX_ID = 10;

    /**
     * 是否需要将战斗服注册到缓存服
     */
    public static boolean REGISTER_TO_CACHE = false;

    /**
     * 机器人战斗场次上限 （用于测试）
     */
    public static int MAX_ROBOT_BATTLE_NUM = 0;
    /**
     * 是否打印伤害计算过程 （用于测试）
     */
    public static boolean LOG_HURT_CALCULATION = false;
    /**
     * 测试地图名 （用于测试）
     */
    public static String TEST_MAP_NAME = "";
    /**
     * 测试地图过关条件 （用于测试）
     */
    public static String TEST_MAP_CONDITION = "";
    /**
     * 测试的技能ID集合 （用于测试）
     */
    public static String TEST_SKILLS = "";
    /**
     * 测试的药水ID集合 （用于测试）
     */
    public static String TEST_POTIONS = "";
    /**
     * 测试的技巧ID集合 （用于测试）
     */
    public static String TEST_AIS = "";


    public static void initConfigurature() {
        try{
            Properties properties = new Properties();
            InputStream in = GameConstant.class.getClassLoader().getResourceAsStream("battle.properties");
            properties.load(in);
            in.close();

            SERVER_ID = Integer.parseInt(properties.getProperty("server_id").trim());
            NETTY_SERVER_IP = properties.getProperty("netty_server_ip").trim();
            NETTY_SERVER_PORT = Integer.parseInt(properties.getProperty("netty_server_port").trim());
            NETTY_SERVER_TYPE = properties.getProperty("netty_server_type").trim();
            GAME_FRAME = Integer.parseInt(properties.getProperty("game_frame_per_second").trim());
            FRAME_TIME = 1000/GAME_FRAME;
            MAX_BATTLE_RUNNING = Integer.parseInt(properties.getProperty("max_battle_running").trim());
            SINGLE_BATTLE_MAX_TIME = Integer.parseInt(properties.getProperty("single_battle_max_time").trim());
            MAX_DETOUR_AGENT = Integer.parseInt(properties.getProperty("max_detour_agent").trim());
            RUN_FRANE_THREAD_SIZE = Integer.parseInt(properties.getProperty("run_frame_thread_size").trim());
            DATA_UPDATE_THREAD_SIZE = Integer.parseInt(properties.getProperty("data_update_thread_size").trim());
            ONE_FRAME_TICKNUM = Integer.parseInt(properties.getProperty("one_frame_ticknum").trim());
            CROWN_ADVANCE_FRAME_NUM = Integer.parseInt(properties.getProperty("crown_advance_frame_num").trim());
            SKILL_RESOURCE_MAX_ID = Integer.parseInt(properties.getProperty("skill_resource_max_id").trim());
            MAX_ROBOT_BATTLE_NUM = Integer.parseInt(properties.getProperty("max_robot_battle_num").trim());
            REGISTER_TO_CACHE = Integer.parseInt(properties.getProperty("register_to_cache").trim()) == 0 ? false : true;
            LOG_HURT_CALCULATION = Integer.parseInt(properties.getProperty("log_hurt_calculation").trim()) == 0 ? false : true;
            TEST_MAP_NAME = properties.getProperty("test_map_name").trim();
            TEST_MAP_CONDITION = properties.getProperty("test_map_condition").trim();
            TEST_SKILLS = properties.getProperty("test_skills").trim();
            TEST_POTIONS = properties.getProperty("test_potions").trim();
            TEST_AIS = properties.getProperty("test_ais").trim();

            System.out.println("====================测试数据====================");
            System.out.println("测试地图 => "+TEST_MAP_NAME);
            System.out.println("测试条件 => "+TEST_MAP_CONDITION);
            System.out.println("测试技能 => "+TEST_SKILLS);
            System.out.println("测试药水 => "+TEST_POTIONS);
            System.out.println("测试技巧 => "+TEST_AIS);

        }catch (Exception e){e.printStackTrace();}
    }

}
