package com.dykj.rpg.client.servlet;

import com.dykj.rpg.client.config.LoginCmdThread;
import com.dykj.rpg.client.config.SendMsgThreadManager;
import com.dykj.rpg.client.game.player.service.LoginService;
import com.dykj.rpg.common.log.LogoutLog;
import com.dykj.rpg.common.log.temporary.*;
import com.dykj.rpg.protocol.common.GmCommonRq;
import com.dykj.rpg.protocol.item.*;
import com.dykj.rpg.protocol.player.RandomNameRq;
import com.dykj.rpg.protocol.skill.SkillUpgradeListRq;
import com.dykj.rpg.protocol.skill.SkillUpgradeRq;
import com.dykj.rpg.util.JsonUtil;
import com.dykj.rpg.util.random.RandomUtil;
import com.dykj.rpg.util.spring.BeanFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scala.Int;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


/**
 * @Author: jyb
 * @Date: 2019/1/9 16:56
 * @Description:
 */
@RestController
public class PlayerController
{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    public static int num = 0;

    public static int reconnect = 0;

    /**
     * 登录
     *
     * @param
     * @param num 登录几个号
     */
    @RequestMapping("/login")
    public String login(Integer num, Integer reconnect)
    {
        if (num != null)
        {
            this.num = num;
        }
        if (reconnect != null)
        {
            this.reconnect = reconnect;
        }
        // BeanFactory.getBean(LoginCmdThread.class).setNum(loginNum);
        LoginService loginService = BeanFactory.getBean(LoginService.class);
        loginService.login(this.num);
        return "login success";
    }

    @RequestMapping("/close")
    public String closeConnect(Integer reconnect)
    {
        if (reconnect != null)
        {
            this.reconnect = reconnect;
        }
        SendMsgThreadManager sendMsgThreadManager = BeanFactory.getBean(SendMsgThreadManager.class);
        sendMsgThreadManager.closeConnect();
        num = 0;
        LoginCmdThread.i.set(0);
        LoginService.ATOMIC_INTEGER.set(1001);
        return "close success";
    }


    @RequestMapping("/closeAdmin")
    public String closeCloseAdmin()
    {
        SendMsgThreadManager sendMsgThreadManager = BeanFactory.getBean(SendMsgThreadManager.class);
        if (LoginService.session != null)
        {
            LoginService.session.sessionClosed();
            sendMsgThreadManager.removeSession(LoginService.session);
        }
        return "close admin success";
    }

    @RequestMapping("/modifyName")
    public String modifyName(String name)
    {
        return "modify name success ";
    }

    @RequestMapping("/testKafka")
    public String modifyName()
    {
        int roleId = 12345678;
        int userId = 88888;
        int serverId = 1001;
        KafkaTemplate<String, String> kafkaTemplate = BeanFactory.getBean(KafkaTemplate.class);
        for (int i = 0; i < 100; i++)
        {
            LogoutLog logoutLog = new LogoutLog();
            logoutLog.setChannel("NAN");
            logoutLog.setLogDate(new Date());
            logoutLog.setLevel(i);
            logoutLog.setRoleId(roleId);
            logoutLog.setUserId(userId);
            logoutLog.setTeamExp(RandomUtil.getRandomNumber(10000));
            logoutLog.setVipLevel(i);
            logoutLog.setOlSeconds(RandomUtil.getRandomNumber(10000));
            logoutLog.setLogonDate(new Date());
            logoutLog.setServerId(serverId);
            kafkaTemplate.send("WangYu_game_t_log_logout", JsonUtil.toJsonString(logoutLog));
        }
        return "modify name success ";
    }

    @RequestMapping("/randomName")
    public String randomName()
    {
        RandomNameRq randomNameRq = new RandomNameRq();
        LoginService.session.write(randomNameRq);
        return "update item success ";
    }

    /**
     * 测试gm指令添加物品
     * @return
     */
    @RequestMapping("/gmAddItem")
    public String gmAddItem(String command)
    {
        GmCommonRq gmCommonRq = new GmCommonRq();
        gmCommonRq.setCommand(command);
        LoginService.session.write(gmCommonRq);
        return "add item success ";
    }

    /**
     * 出售物品
     * @return
     */
    @RequestMapping("/sellItems")
    public String sellItems(Integer itemId, Integer itemNum, Integer itemOperateType)
    {
        ItemUniversalListRq itemUniversalListRq = new ItemUniversalListRq();
        itemUniversalListRq.setOperation(itemOperateType);
        List<ItemUniversalRq> itemUniversalArr = new ArrayList<ItemUniversalRq>();
        ItemUniversalRq itemUniversalRq = new ItemUniversalRq();
        itemUniversalRq.setInstId(0L);
        itemUniversalRq.setItemId(itemId);
        itemUniversalRq.setItemNum(itemNum);
        itemUniversalArr.add(itemUniversalRq);
        itemUniversalListRq.setItemUniversalArr(itemUniversalArr);
        LoginService.session.write(itemUniversalListRq);
        return String.format("根据道具id: %s, 道具数量:%s, 操作类型:%s 出售成功!", itemId, itemNum, itemOperateType);
    }

    /**
     * 背包扩展
     * @return
     */
    @RequestMapping("/itemExpand")
    public String itemExpand()
    {
        ItemExpandRq itemExpandRq = new ItemExpandRq();
        LoginService.session.write(itemExpandRq);
        return "发送扩展背包协议....";
    }

    /**
     * 过期物品
     * @return
     */
    @RequestMapping("/itemExpired")
    public String itemExpired()
    {
        ItemExpiredRq itemExpandRq = new ItemExpiredRq();
        LoginService.session.write(itemExpandRq);
        return "发送过期物品协议....";
    }

    /**
     * 物品上锁
     * @return
     */
    @RequestMapping("/itemLock")
    public String itemLock(Long instId, Integer itemId, Integer isLock)
    {
        ItemLockRq itemExpandRq = new ItemLockRq(instId, itemId, isLock);
        LoginService.session.write(itemExpandRq);
        return "发送物品上锁协议....";
    }

    /**
     * 技能升级
     * @return
     */
    @RequestMapping("/skillUpgrade")
    public String skillUpgrade(Integer skillId, String items)
    {
        List<SkillUpgradeRq> skillUpgrades = new ArrayList<>();
        Map<Integer, Integer> collect = Arrays.stream(items.split(",")).map(e -> e.split(":")).collect(Collectors.toMap(e -> Integer.parseInt(e[0]), e -> Integer.parseInt(e[1])));
        for (Integer key : collect.keySet())
        {
            SkillUpgradeRq skillUpgradeRq = new SkillUpgradeRq(key, collect.get(key));
            skillUpgrades.add(skillUpgradeRq);
        }

        LoginService.session.write(new SkillUpgradeListRq(skillId, skillUpgrades));
        return "发送技能升级协议....";
    }


    /**
     * 新增测试数据往数据中心kafka推送
     *
     * @param modelName
     * @param count
     * @return
     */
    @RequestMapping("/testLog")
    public String testLog(String modelName, Integer count)
    {
        if (StringUtils.isBlank(modelName)) return "model名称不能为空!";
        Integer finalCount = (null == count || count == 0) ? 5000 : count;//数据条数
        KafkaTemplate<String, String> kafkaTemplate = BeanFactory.getBean(KafkaTemplate.class);
        String[] arr = modelName.split(",");
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(arr.length);
        Map<String, List> resultMap = this.assemblyTestData();//拿到生成元数据
        try
        {
            for (String name : arr)
            {
                List sourceDataList = resultMap.get(name);
                if (null != sourceDataList && !sourceDataList.isEmpty())
                {
                    fixedThreadPool.execute(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            for (int i = 0; i < finalCount; i++)
                            {
                                int index = RandomUtil.randomIndexNotDuplicate(sourceDataList.size(), 1).stream().findFirst().get();
                                String json = JsonUtil.toJsonString(sourceDataList.get(index));
                                logger.info("往数据中心推送测试数据: {}", json);
                                kafkaTemplate.send(String.format("WangYu_game_%s", name), json);
                            }
                        }
                    });
                }
            }
        }
        catch (Exception e)
        {
            logger.error("往数据中心推送日志测试数据失败!", e);
        }
        finally
        {
            fixedThreadPool.shutdown();
        }
        return "testLog success";
    }

    /**
     * 组装测试数据
     *
     * @return
     */
    private Map<String, List> assemblyTestData()
    {
        Map<String, List> resultMap = new HashMap<>();//存储所有手动日志临时数据map
        int serverId = 1004;//服务器id
        int roleId1 = 1;//玩家1
        int roleId2 = 2;//玩家2
        int roleId3 = 3;//玩家3
        int roleId4 = 4;//玩家4
        int userId1 = 1;//用户id1
        int userId2 = 2;//用户id2
        int userId3 = 3;//用户id3
        int userId4 = 4;//用户id4
        String channel = "NAN";//渠道
        int playLevel1 = 35;//玩家1等级
        int playLevel2 = 46;//玩家2等级
        int playLevel3 = 58;//玩家3等级
        int playLevel4 = 26;//玩家4等级
        int playVipLevel1 = 35;//玩家1 vip等级
        int playVipLevel2 = 46;//玩家2 vip等级
        int playVipLevel3 = 58;//玩家3 vip等级
        int playVipLevel4 = 26;//玩家4 vip等级
        int type = 1;//类型
        String playReward1 = "获得宝石128, 金币28523";//玩家1 奖励
        String playReward2 = "获得SSS武器一件, 金币2000000";//玩家2 奖励
        String playReward3 = "获得宝石2888";//玩家3 奖励
        String playReward4 = "获得金币1000000";//玩家4 奖励

        List<LogActivity> logActivityList = new ArrayList<>();//活动日志表
        logActivityList.add(new LogActivity(new Date(), serverId, roleId1, userId1, channel, playLevel1, playVipLevel1, type, playReward1));
        logActivityList.add(new LogActivity(new Date(), serverId, roleId2, userId2, channel, playLevel2, playVipLevel2, type, playReward2));
        logActivityList.add(new LogActivity(new Date(), serverId, roleId3, userId3, channel, playLevel3, playVipLevel3, type, playReward3));
        logActivityList.add(new LogActivity(new Date(), serverId, roleId4, userId4, channel, playLevel4, playVipLevel4, type, playReward4));
        resultMap.put(LogActivity.class.getSimpleName(), logActivityList);
        logger.info("初始化{}(活动日志表)数据完毕......", LogActivity.class.getSimpleName());

        List<LogEquip> logEquipList = new ArrayList<>();//装备日志表
        logEquipList.add(new LogEquip(new Date(), serverId, roleId1, userId1, channel, playLevel1, playVipLevel1, 0l, 0, 0, type, 0, 0));
        logEquipList.add(new LogEquip(new Date(), serverId, roleId2, userId2, channel, playLevel2, playVipLevel2, 0l, 0, 0, type, 0, 0));
        logEquipList.add(new LogEquip(new Date(), serverId, roleId3, userId3, channel, playLevel3, playVipLevel3, 0l, 0, 0, type, 0, 0));
        logEquipList.add(new LogEquip(new Date(), serverId, roleId4, userId4, channel, playLevel4, playVipLevel4, 0l, 0, 0, type, 0, 0));
        resultMap.put(LogEquip.class.getSimpleName(), logEquipList);
        logger.info("初始化{}(装备日志表)数据完毕......", LogEquip.class.getSimpleName());

        List<LogGoods> logGoodsList = new ArrayList<>();//物品消耗日志
        logGoodsList.add(new LogGoods(new Date(), serverId, roleId1, String.valueOf(userId1), channel, playLevel1, playVipLevel1, 0, 0, 0, 0, type, 0));
        logGoodsList.add(new LogGoods(new Date(), serverId, roleId2, String.valueOf(userId2), channel, playLevel2, playVipLevel2, 0, 0, 0, 0, type, 0));
        logGoodsList.add(new LogGoods(new Date(), serverId, roleId3, String.valueOf(userId3), channel, playLevel3, playVipLevel3, 0, 0, 0, 0, type, 0));
        logGoodsList.add(new LogGoods(new Date(), serverId, roleId4, String.valueOf(userId4), channel, playLevel4, playVipLevel4, 0, 0, 0, 0, type, 0));
        resultMap.put(LogGoods.class.getSimpleName(), logGoodsList);
        logger.info("初始化{}(物品消耗日志表)数据完毕......", LogGoods.class.getSimpleName());

        List<Loglogout> loglogoutList = new ArrayList<>();//登出日志
        ZoneId zoneId = ZoneId.systemDefault();
        loglogoutList.add(new Loglogout(new Date(), serverId, roleId1, String.valueOf(userId1), channel, playLevel1, playVipLevel1, Date.from(LocalDateTime.now().plusDays(1).atZone(zoneId).toInstant()), 0, 0));
        loglogoutList.add(new Loglogout(new Date(), serverId, roleId2, String.valueOf(userId2), channel, playLevel2, playVipLevel2, Date.from(LocalDateTime.now().plusDays(2).atZone(zoneId).toInstant()), 0, 0));
        loglogoutList.add(new Loglogout(new Date(), serverId, roleId3, String.valueOf(userId3), channel, playLevel3, playVipLevel3, Date.from(LocalDateTime.now().plusDays(3).atZone(zoneId).toInstant()), 0, 0));
        loglogoutList.add(new Loglogout(new Date(), serverId, roleId4, String.valueOf(userId4), channel, playLevel4, playVipLevel4, Date.from(LocalDateTime.now().plusDays(4).atZone(zoneId).toInstant()), 0, 0));
        resultMap.put(Loglogout.class.getSimpleName(), loglogoutList);
        logger.info("初始化{}(登出日志表)数据完毕......", Loglogout.class.getSimpleName());

        List<LogOnlineNum> logOnlineNumList = new ArrayList<>();//在线人数日志
        logOnlineNumList.add(new LogOnlineNum(new Date(), serverId, 4));
        resultMap.put(LogOnlineNum.class.getSimpleName(), logOnlineNumList);
        logger.info("初始化{}(在线人数日志表)数据完毕......", LogOnlineNum.class.getSimpleName());

        List<LogPlayerRes> logPlayerResList = new ArrayList<>();//玩家可恢复资源变更日志表
        logPlayerResList.add(new LogPlayerRes(new Date(), serverId, roleId1, String.valueOf(userId1), channel, playLevel1, playVipLevel1, type, 120, 200, 80, 1, 80));
        logPlayerResList.add(new LogPlayerRes(new Date(), serverId, roleId2, String.valueOf(userId2), channel, playLevel2, playVipLevel2, type, 100, 200, 100, 1, 100));
        logPlayerResList.add(new LogPlayerRes(new Date(), serverId, roleId3, String.valueOf(userId3), channel, playLevel3, playVipLevel3, type, 20, 200, 180, 1, 180));
        logPlayerResList.add(new LogPlayerRes(new Date(), serverId, roleId4, String.valueOf(userId4), channel, playLevel4, playVipLevel4, type, 100, 200, 100, 1, 100));
        resultMap.put(LogPlayerRes.class.getSimpleName(), logPlayerResList);
        logger.info("初始化{}(玩家可恢复资源变更日志表)数据完毕......", LogPlayerRes.class.getSimpleName());

        List<LogRecharge> logRechargeList = new ArrayList<>();//支付表
        logRechargeList.add(new LogRecharge(new Date(), serverId, roleId1, String.valueOf(userId1), channel, playLevel1, playVipLevel1, 112, 56, 20000, 1, 50, "XSY123145646", "PLXSY123145646"));
        logRechargeList.add(new LogRecharge(new Date(), serverId, roleId2, String.valueOf(userId2), channel, playLevel2, playVipLevel2, 113, 57, 20000, 1, 50, "XSY12314324", "PLXSY12314324"));
        logRechargeList.add(new LogRecharge(new Date(), serverId, roleId3, String.valueOf(userId3), channel, playLevel3, playVipLevel3, 114, 58, 20000, 1, 50, "XSY1231432445", "PLXSY1231432445"));
        logRechargeList.add(new LogRecharge(new Date(), serverId, roleId4, String.valueOf(userId4), channel, playLevel4, playVipLevel4, 115, 59, 20000, 1, 50, "XSY1231478566", "PLXSY1231478566"));
        resultMap.put(LogRecharge.class.getSimpleName(), logRechargeList);
        logger.info("初始化{}(支付表)数据完毕......", LogRecharge.class.getSimpleName());

        List<LogRechargeOrder> logRechargeOrderList = new ArrayList<>();//订单日志表
        logRechargeOrderList.add(new LogRechargeOrder(new Date(), serverId, roleId1, String.valueOf(userId1), channel, playLevel1, playVipLevel1, 112, 56, 20000, 1, 123, 1, "XSY123145646"));
        logRechargeOrderList.add(new LogRechargeOrder(new Date(), serverId, roleId2, String.valueOf(userId2), channel, playLevel2, playVipLevel2, 113, 57, 20000, 1, 124, 1, "XSY12314324"));
        logRechargeOrderList.add(new LogRechargeOrder(new Date(), serverId, roleId3, String.valueOf(userId3), channel, playLevel3, playVipLevel3, 114, 58, 20000, 1, 125, 1, "XSY1231432445"));
        logRechargeOrderList.add(new LogRechargeOrder(new Date(), serverId, roleId4, String.valueOf(userId4), channel, playLevel4, playVipLevel4, 115, 59, 20000, 1, 126, 1, "XSY1231478566"));
        resultMap.put(LogRechargeOrder.class.getSimpleName(), logRechargeOrderList);
        logger.info("初始化{}(订单日志表)数据完毕......", LogRechargeOrder.class.getSimpleName());

        List<LogRegist> logRegistList = new ArrayList<>();//注册
        logRegistList.add(new LogRegist(new Date(), serverId, roleId1, String.valueOf(userId1), channel, playLevel1, playVipLevel1, "5049", "192.168.0.47"));
        logRegistList.add(new LogRegist(new Date(), serverId, roleId2, String.valueOf(userId2), channel, playLevel2, playVipLevel2, "5088", "192.168.0.47"));
        logRegistList.add(new LogRegist(new Date(), serverId, roleId3, String.valueOf(userId3), channel, playLevel3, playVipLevel3, "4532", "192.168.0.47"));
        logRegistList.add(new LogRegist(new Date(), serverId, roleId4, String.valueOf(userId4), channel, playLevel4, playVipLevel4, "1257", "192.168.0.47"));
        resultMap.put(LogRegist.class.getSimpleName(), logRegistList);
        logger.info("初始化{}(注册表)数据完毕......", LogRegist.class.getSimpleName());

        List<LogRresource> logRresourceList = new ArrayList<>();//资源
        logRresourceList.add(new LogRresource(new Date(), serverId, roleId1, String.valueOf(userId1), channel, playLevel1, playVipLevel1, type, 123, 23, 1));
        logRresourceList.add(new LogRresource(new Date(), serverId, roleId2, String.valueOf(userId2), channel, playLevel2, playVipLevel2, type, 343, 234, 1));
        logRresourceList.add(new LogRresource(new Date(), serverId, roleId3, String.valueOf(userId3), channel, playLevel3, playVipLevel3, type, 45, 3, 1));
        logRresourceList.add(new LogRresource(new Date(), serverId, roleId4, String.valueOf(userId4), channel, playLevel4, playVipLevel4, type, 233, 67, 1));
        resultMap.put(LogRresource.class.getSimpleName(), logRresourceList);
        logger.info("初始化{}(资源表)数据完毕......", LogRresource.class.getSimpleName());

        List<LogSection> logSectionList = new ArrayList<>();//玩家副本记录
        logSectionList.add(new LogSection(new Date(), serverId, roleId1, String.valueOf(userId1), channel, playLevel1, playVipLevel1, 100, 1, 7, "战士|法师|吸血鬼", "12345|564|78989", 16354165, 20000));
        logSectionList.add(new LogSection(new Date(), serverId, roleId2, String.valueOf(userId2), channel, playLevel2, playVipLevel2, 102, 1, 8, "战士|法师|吸血鬼", "12745|564|7989", 54354043, 20000));
        logSectionList.add(new LogSection(new Date(), serverId, roleId3, String.valueOf(userId3), channel, playLevel3, playVipLevel3, 103, 1, 3, "战士|法师|吸血鬼", "768|564|7899", 456123, 20000));
        logSectionList.add(new LogSection(new Date(), serverId, roleId4, String.valueOf(userId4), channel, playLevel4, playVipLevel4, 104, 1, 2, "战士|法师|吸血鬼", "98090|564|8989", 1235454, 20000));
        resultMap.put(LogSection.class.getSimpleName(), logSectionList);
        logger.info("初始化{}(玩家副本记录表)数据完毕......", LogSection.class.getSimpleName());

        List<StageData> stageDataList = new ArrayList<>();//关卡信息表
        stageDataList.add(new StageData(1, 111, "com/dykj/rpg/common/log/temporary", "1541:1651,11156:4141,151:441", 2, 6, 1, "1", "深渊", 12000, "剧情副本", 12, "100|1055|11151|15152|5131230|1651", 1231313, 1200000, 123, 212, 1, 3, "", 12213131, 123, 12313131, "", "", 12, 22, "", "zhanshi", "", "", ""));
        resultMap.put(StageData.class.getSimpleName(), stageDataList);
        logger.info("初始化{}(关卡信息表)数据完毕......", StageData.class.getSimpleName());
        return resultMap;
    }

}
