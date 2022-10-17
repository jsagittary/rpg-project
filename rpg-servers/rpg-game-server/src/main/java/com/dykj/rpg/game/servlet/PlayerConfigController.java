package com.dykj.rpg.game.servlet;

import com.dykj.rpg.common.module.uc.logic.UcMsg;
import com.dykj.rpg.game.module.player.service.PlayerConfigService;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Description 游戏配置Controller
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/9/29
 */
@RequestMapping("playerConfig")
@Controller
public class PlayerConfigController
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("refreshTableData")
    @ResponseBody
    public UcMsg refreshTableData(@RequestParam(required = false) String tableNames)
    {
        logger.info("开始刷新内存 {} 表数据......", tableNames);
        PlayerConfigService playerConfigService = BeanFactory.getBean(PlayerConfigService.class);
        UcMsg ucMsg = playerConfigService.refreshTableData(tableNames);
        logger.info("刷新内存 {} 表数据结束, 返回结果: [{}]", tableNames, ucMsg.getDesc());
        return ucMsg;
    }
}