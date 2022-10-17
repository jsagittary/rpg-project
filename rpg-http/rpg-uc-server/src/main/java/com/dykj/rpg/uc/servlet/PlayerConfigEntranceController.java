package com.dykj.rpg.uc.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dykj.rpg.common.consts.BaseStatusCodeConstants;
import com.dykj.rpg.common.consts.CommonConsts;
import com.dykj.rpg.common.consts.ServerKey;
import com.dykj.rpg.common.module.uc.logic.UcMsg;
import com.dykj.rpg.common.redis.cache.ServerNewCacheMgr;
import com.dykj.rpg.common.redis.data.InstanceData;
import com.dykj.rpg.common.redis.data.ServerData;
import com.dykj.rpg.util.HttpUtil;
import com.dykj.rpg.util.spring.BeanFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description uc游戏配置Controller
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/9/29
 */
@RequestMapping("playerConfigEntrance")
@Controller
public class PlayerConfigEntranceController
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 刷新游戏服配置数据
     * @param servers 服务器ip:serverid(格式为:游戏服ip:serverId(多个以逗号隔开))
     * @param tableNames 需要刷新的表名(多个以逗号隔开)
     * @return UcMsg
     */
    @PostMapping("refreshTableData")
    @ResponseBody
    public UcMsg refreshTableData(@RequestParam String servers,
                                  @RequestParam String tableNames)
    {
        logger.info("uc端开始处理刷新内存 {} 表数据请求......", tableNames);
        UcMsg ucMsg = new UcMsg();
        ucMsg.setCode(BaseStatusCodeConstants.UNKNOW_EXCEPTION);
        //校验表名格式
        if (StringUtils.isNotBlank(tableNames) && !tableNames.matches("^\\w+(,+\\w+)*$"))
        {
            String err = String.format("表名: %s 格式不正确!", tableNames);
            ucMsg.setDesc(err);
            logger.error(err);
            return ucMsg;
        }

        //存储解析后的服务器地址及serverId
        List<Map<String, Integer>> serversList = null;
        //校验服务器ip:serverid
        if (StringUtils.isNotBlank(servers))
        {
            if (servers.matches("^\\d+.+\\d+.+\\d+.+\\d+:+\\d+(,+\\d+.+\\d+.+\\d+.+\\d+:+\\d+)*$"))
            {
                try
                {
                    serversList = Stream.of(servers.split(CommonConsts.STR_COMMA)).map(e ->
                    {
                        String[] arr = e.split(CommonConsts.STR_COLON);
                        Map<String, Integer> map = new HashMap<>();
                        map.put(arr[0], Integer.valueOf(arr[1]));
                        return map;
                    }).collect(Collectors.toList());
                }
                catch (Exception e)
                {
                    String err = String.format("解析服务器ip及serverId: %s 错误, 请检查后再试!", servers);
                    ucMsg.setDesc(err);
                    logger.error(err);
                    return ucMsg;
                }
            }
            else
            {
                String err = String.format("服务器ip及serverId: %s 格式不正确!", servers);
                ucMsg.setDesc(err);
                logger.error(err);
                return ucMsg;
            }
        }

        StringBuilder stringBuilder = new StringBuilder();//存储响应信息;
        ServerNewCacheMgr serverNewCacheMgr = BeanFactory.getBean(ServerNewCacheMgr.class);
        for (ServerData serverData : serverNewCacheMgr.values())
        {
            InstanceData instance = serverData.getInstance();
            if(null == serversList || serversList.isEmpty())
            {
                if (null != instance)
                {
                    this.sendRequest(serverData, tableNames, stringBuilder);
                }
            }
            else
            {
                for (Map<String, Integer> serverMap : serversList)
                {
                    String refreshHost = serverMap.keySet().stream().findFirst().get();//需要刷新的服务器地址
                    Integer refreshServerId = serverMap.values().stream().findFirst().get();//需要刷新的服务器的serverid
                    if (null != instance && (instance.getIp().equals(refreshHost) && instance.getServerId() == refreshServerId))
                    {
                        this.sendRequest(serverData, tableNames, stringBuilder);
                    }
                }
            }
        }
        ucMsg.setCode(BaseStatusCodeConstants.OK);
        ucMsg.setDesc(stringBuilder.toString());
        logger.info("所有游戏服务器处理刷新 {} 内存表数据响应结果: {}", tableNames, ucMsg.toString());
        return ucMsg;
    }

    /**
     * 发送请求
     * @param serverData 服务器信息
     * @param tableNames 表名集
     * @param stringBuilder 响应信息
     */
    private void sendRequest(ServerData serverData, String tableNames, StringBuilder stringBuilder)
    {
        int jettyPort = Integer.parseInt(serverData. getInstance().getMetadata().get(ServerKey.JETTY_PORT));
        String ip = serverData.getInstance().getIp();
        String httpUrl = "http://" + ip + ":" + jettyPort + "/playerConfig/refreshTableData";
        logger.info("根据拉取的游戏服务器url: {}, 开始刷新{} 表数据......", httpUrl, tableNames);
        Map<String, String> params = new HashMap<String, String>();
        params.put("tableNames", tableNames);
        String response = HttpUtil.sendHttpPost(httpUrl, params);
        if (StringUtils.isBlank(response))
        {
            stringBuilder.append(String.format("游戏服地址: %s 刷新配置表失败!", ip)).append(System.lineSeparator());
        }
        else
        {
            JSONObject responseJson = JSON.parseObject(response);
            stringBuilder.append(String.format("游戏服地址: %s %s", ip, responseJson.getString("desc"))).append(System.lineSeparator());
        }
    }
}