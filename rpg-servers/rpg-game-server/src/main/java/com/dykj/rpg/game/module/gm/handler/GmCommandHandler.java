package com.dykj.rpg.game.module.gm.handler;

import com.dykj.rpg.common.consts.CommonConsts;
import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.core.GameHandler;
import com.dykj.rpg.game.module.gm.core.GmStrategyManage;
import com.dykj.rpg.game.module.gm.response.GmResponse;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.util.CmdUtil;
import com.dykj.rpg.protocol.common.GmCommonRq;
import com.dykj.rpg.util.spring.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * @Description 处理gm指令handler
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/23
 */
public class GmCommandHandler extends GameHandler<GmCommonRq>
{
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void doHandler(GmCommonRq gmCommonRq, Player player)
    {
        String command = gmCommonRq.getCommand().trim();
        //校验键值对格式是否符合规范
        Pattern patternMap = Pattern.compile(CommonConsts.GM_COMMAND_MAP);
        //校验数组格式是否符合规范
        Pattern patternArr = Pattern.compile(CommonConsts.GM_COMMAND_ARR);
        if (patternMap.matcher(command).matches() || patternArr.matcher(command).matches())
        {
            try
            {
                GmStrategyManage gmStrategyManage = BeanFactory.getBean(GmStrategyManage.class);
                GmResponse gmResponse = gmStrategyManage.execute(command, player);
                if (null != gmResponse)
                {
                    if (gmResponse.getCodeEnum().equals(ErrorCodeEnum.SUCCESS))
                    {
                        if (null != gmResponse.getProtocol())
                            CmdUtil.sendMsg(player, gmResponse.getProtocol());
                    }
                    else
                    {
                        CmdUtil.sendErrorMsg(player.getSession(), gmCommonRq.getCode(), gmResponse.getCodeEnum());
                    }
                }
            }
            catch (Exception e)
            {
                logger.error("执行{}指令异常, 请检查后重试!", gmCommonRq.toString(), e);
                CmdUtil.sendErrorMsg(player.getSession(), gmCommonRq.getCode(), ErrorCodeEnum.GM_COMMAND_ERROR);
            }
        }
        else
        {
            logger.info("GM指令: {}格式错误!", gmCommonRq.getCommand());
            CmdUtil.sendErrorMsg(player.getSession(), gmCommonRq.getCode(), ErrorCodeEnum.GM_COMMAND_FORMAT_ERROR);
        }
        logger.info("根据指令: {} 执行指定GM服务完毕!", gmCommonRq.getCommand());
    }
}