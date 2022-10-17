package com.dykj.rpg.game.module.event.login.event;

import com.dykj.rpg.common.data.model.PlayerInfoAttachedModel;
import com.dykj.rpg.game.module.event.core.AbstractEvent;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.task.logic.service.TaskService;
import com.dykj.rpg.game.module.util.CmdUtil;
import com.dykj.rpg.protocol.player.PlayerAttachedRs;
import com.dykj.rpg.util.date.DateUtils;
import com.dykj.rpg.util.spring.BeanFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @Description 玩家登陆前初始化附属信息
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/23
 */
@Component
public class LoginAttacheEvent extends AbstractEvent
{

    @Override
    public void doEvent(Object... prams) throws Exception
    {
        Player player = (Player) prams[0];
        PlayerAttachedRs playerAttachedRs = new PlayerAttachedRs();
        PlayerInfoAttachedModel playerInfoAttachedModel = player.cache().getPlayerInfoAttachedModel();
        if (null != playerInfoAttachedModel)
        {
            playerAttachedRs.setDailyActivity(playerInfoAttachedModel.getDailyActivity());
            playerAttachedRs.setWeekActivity(playerInfoAttachedModel.getWeekActivity());
            playerAttachedRs.setIsProtector(playerInfoAttachedModel.getIsProtector());
            // 计算守护者奖励剩余时间
            if (playerInfoAttachedModel.getIsProtector() == 1)
            {
                LocalDateTime currentDateTime = LocalDateTime.now();
                LocalDateTime endLocalDateTime = DateUtils.conversionLocalDateTime(playerInfoAttachedModel.getProtectorLastTime());
                // 计算时间差
                Duration duration = Duration.between(currentDateTime, endLocalDateTime);
                playerAttachedRs.setProtectorRemainingTime(duration.toMinutes());// 设置守护者奖励剩余时间, 单位:分
                playerAttachedRs.setProtectorLastTime(playerInfoAttachedModel.getProtectorLastTime().getTime());// 守护者奖励截止时间
            }
            playerAttachedRs.setActivityRewardList(playerInfoAttachedModel.getActivityRewardList());
            CmdUtil.sendMsg(player, playerAttachedRs);
        }
    }
}
