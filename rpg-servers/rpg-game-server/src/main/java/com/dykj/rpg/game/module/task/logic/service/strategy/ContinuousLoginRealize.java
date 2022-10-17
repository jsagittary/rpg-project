package com.dykj.rpg.game.module.task.logic.service.strategy;

import com.dykj.rpg.common.data.model.PlayerInfoModel;
import com.dykj.rpg.common.data.model.PlayerTaskModel;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.task.consts.TaskFinishConditionEnum;
import com.dykj.rpg.game.module.task.logic.response.TaskConditions;
import com.dykj.rpg.game.module.task.logic.service.strategy.core.TaskFinishFactory;
import com.dykj.rpg.game.module.task.logic.service.strategy.core.TaskFinishStrategy;
import com.dykj.rpg.util.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description 任务完成条件 - 7-连续登录
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/19
 */
@Component
public class ContinuousLoginRealize extends TaskFinishStrategy
{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void afterPropertiesSet() throws Exception
	{
		TaskFinishFactory.register(TaskFinishConditionEnum.CONTINUOUS_LOGIN, this);
	}

	/**
	 * 策略方法
	 * @param player 玩家信息
	 * @param playerTaskModel 玩家任务
	 * @param taskFinish 任务完成条件配置
	 * @param conditionsModel 参数集
	 * @return 玩家任务
	 */
	@Override
	public PlayerTaskModel realize(Player player, PlayerTaskModel playerTaskModel, List<Integer> taskFinish, TaskConditions conditionsModel)
	{
		PlayerInfoModel playerInfoModel = player.cache().getPlayerInfoModel();
		if (null == playerInfoModel)
		{
			logger.error("玩家id:{}, 获取玩家信息为空!", player.getPlayerId());
			return null;
		}
		//玩家第一次登录则登录次数加1
		if (null == playerInfoModel.getLogoutTime())
		{
			playerTaskModel.setTaskSchedule(1);
		}
		else
		{
			LocalDateTime loginTime = DateUtils.conversionLocalDateTime(playerInfoModel.getLoginTime()).withSecond(0);//登录时间
			LocalDateTime logoutTime = DateUtils.conversionLocalDateTime(playerInfoModel.getLogoutTime()).withSecond(0);//最后登出时间
			Duration duration = Duration.between(logoutTime, loginTime);
			//如果最后一次登出时间和最后一次登录时间相差没有1天则判断是否过了0点并且判断是否已经跨天
			if (duration.toDays() == 0 && logoutTime.getDayOfMonth() != loginTime.getDayOfMonth())
			{
				playerTaskModel.setTaskSchedule(playerTaskModel.getTaskSchedule() + 1);
			}
			//如果最后一次登出时间和最后一次登录时间相差1天则直接累计次数
			else if (duration.toDays() == 1)
			{
				playerTaskModel.setTaskSchedule(playerTaskModel.getTaskSchedule() + 1);
			}
			//如果都不满足上述情况则把进度重置
			else
			{
				playerTaskModel.setTaskSchedule(1);
			}
			if (playerTaskModel.getTaskSchedule() >= taskFinish.get(1))
			{
				playerTaskModel.setTaskSchedule(taskFinish.get(1));
				playerTaskModel.setTaskStatus(2);
			}
		}
		logger.debug("玩家id:{}, 刷新任务状态完毕, playerTaskModel:{}", player.getPlayerId(), playerTaskModel.toString());
		return playerTaskModel;
	}
}