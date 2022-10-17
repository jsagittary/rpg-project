package com.dykj.rpg.game.module.task.logic.service.strategy;

import com.dykj.rpg.common.data.dao.PlayerMissionDao;
import com.dykj.rpg.common.data.model.PlayerMissionModel;
import com.dykj.rpg.common.data.model.PlayerTaskModel;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.task.consts.TaskFinishConditionEnum;
import com.dykj.rpg.game.module.task.logic.response.TaskConditions;
import com.dykj.rpg.game.module.task.logic.service.strategy.core.TaskFinishFactory;
import com.dykj.rpg.game.module.task.logic.service.strategy.core.TaskFinishStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 任务完成条件 - 5=挑战通过关卡XXX
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/19
 */
@Component
public class ChallengeMissionRealize extends TaskFinishStrategy
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private PlayerMissionDao playerMissionDao;

    @Override
    public void afterPropertiesSet() throws Exception
    {
        TaskFinishFactory.register(TaskFinishConditionEnum.CHALLENGE_MISSION, this);
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
        PlayerMissionModel playerMissionModel = player.cache().getPlayerMissionModel();
        if (null == playerMissionModel)
        {
            logger.error("玩家id:{}, 获取玩家关卡为空!", player.getPlayerId());
            return null;
        }
        if (playerMissionModel.getMissionId() == taskFinish.get(1) && null != playerMissionModel.getLastBattleTime())
        {
            playerTaskModel.setTaskStatus(2);
            playerTaskModel.setTaskSchedule(1);
        }
        logger.debug("玩家id:{}, 刷新任务状态完毕, playerTaskModel:{}", player.getPlayerId(), playerTaskModel.toString());
        return playerTaskModel;
    }
}