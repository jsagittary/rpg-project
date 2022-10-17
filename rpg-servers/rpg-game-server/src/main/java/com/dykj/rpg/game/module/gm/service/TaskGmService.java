package com.dykj.rpg.game.module.gm.service;

import com.dykj.rpg.common.data.dao.PlayerInfoAttachedDao;
import com.dykj.rpg.common.data.dao.PlayerTaskDao;
import com.dykj.rpg.common.data.model.PlayerInfoAttachedModel;
import com.dykj.rpg.common.data.model.PlayerTaskModel;
import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.module.gm.core.GmStrategy;
import com.dykj.rpg.game.module.task.consts.TaskOpenConditionEnum;
import com.dykj.rpg.game.module.task.consts.TaskTypeEnum;
import com.dykj.rpg.game.module.task.logic.service.TaskService;
import com.dykj.rpg.game.module.util.CmdUtil;
import com.dykj.rpg.protocol.player.UpdPlayerInfoRs;
import com.dykj.rpg.protocol.task.*;
import com.dykj.rpg.util.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @Description gm指令道具实现
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/11/24
 */
@Service
public class TaskGmService extends GmStrategy
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private PlayerTaskDao playerTaskDao;

    @Resource
    private PlayerInfoAttachedDao playerInfoAttachedDao;

    @Resource
    private TaskService taskService;

    @Override
    public String serviceName()
    {
        return "task";
    }

    /**
     * 重置当前角色所有日常、周常任务
     */
    public void resetTask()
    {
        //查询当前玩家下的所有日常和周常任务
        List<PlayerTaskModel> taskModels = taskService.getPlayerTaskList(player, TaskTypeEnum.DAILY.getType(), TaskTypeEnum.WEEK.getType());
        if (null == taskModels || taskModels.isEmpty())
        {
            logger.error("玩家id:{}, 使用GM指令重置任务时未查询到当前玩家下的日周常任务!", player.getPlayerId());
            CmdUtil.sendErrorMsg(player.getSession(), new UpdateTaskListRs().getCode(), ErrorCodeEnum.NOT_MATCH_CORRESPONDING_TYPE_TASK);
            return;
        }
        //重置日周常任务
        for (PlayerTaskModel playerTaskModel : taskModels)
        {
            if (playerTaskModel.getTriggerStatus() == 0)
            {
                continue;
            }
            player.cache().getPlayerTaskCache().queueDelete(playerTaskModel);
            playerTaskDao.queueDelete(playerTaskModel);
        }
        //任务触发
        taskService.triggerTaskPush(player, TaskOpenConditionEnum.DEFAULT);
        logger.debug("玩家id:{}, 使用GM指令重置日常、周常任务操作完毕！", player.getPlayerId());
    }

    /**
     * 重置当前角色所有主线任务状态
     */
    public void resetMainStatus()
    {
        List<PlayerTaskModel> taskModels = taskService.getPlayerTaskList(player, TaskTypeEnum.MAIN_LINE.getType());
        if (null == taskModels || taskModels.isEmpty())
        {
            logger.error("玩家id:{}, 使用GM指令重置任务状态时未查询到当前玩家下的主线任务!", player.getPlayerId());
            CmdUtil.sendErrorMsg(player.getSession(), new UpdateTaskListRs().getCode(), ErrorCodeEnum.NOT_MATCH_CORRESPONDING_TYPE_TASK);
            return;
        }
        //重置主线任务状态
        List<TaskRs> taskList = new ArrayList<>();
        for (PlayerTaskModel playerTaskModel : taskModels)
        {
            if (playerTaskModel.getTriggerStatus() == 0)
            {
                continue;
            }
            playerTaskModel.setTaskStatus(1);
            playerTaskModel.setTaskSchedule(0);
            player.cache().getPlayerTaskCache().updateCache(playerTaskModel);
            playerTaskDao.queueUpdate(playerTaskModel);
            taskList.add(taskService.taskRs(playerTaskModel));
        }
        logger.debug("玩家id:{}, 使用GM指令重置主线任务状态操作完毕!", player.getPlayerId());
    }

    /**
     * 刷新活跃度状态
     */
    public void refreshActivity()
    {
        UpdPlayerInfoRs updPlayerInfoRs = new UpdPlayerInfoRs();
        PlayerInfoAttachedModel playerInfoAttachedModel = taskService.getPlayerInfoAttached(player);
        if (null == playerInfoAttachedModel)
        {
            logger.error("玩家id:{}, 使用GM指令刷新活跃度状态时查询玩家信息为空!", player.getPlayerId());
            CmdUtil.sendErrorMsg(player.getSession(), updPlayerInfoRs.getCode(), ErrorCodeEnum.NOT_MATCH_CORRESPONDING_TYPE_TASK);
            return;
        }
        playerInfoAttachedModel.setDailyActivity(0);
        playerInfoAttachedModel.setWeekActivity(0);
        playerInfoAttachedModel.setActivityRewardList(null);
        player.cache().setPlayerInfoAttachedModel(playerInfoAttachedModel);
        playerInfoAttachedDao.queueUpdate(playerInfoAttachedModel);
        updPlayerInfoRs.setDailyActivity(playerInfoAttachedModel.getDailyActivity());
        updPlayerInfoRs.setWeekActivity(playerInfoAttachedModel.getWeekActivity());
        CmdUtil.sendMsg(player, updPlayerInfoRs);
        logger.debug("玩家id:{}, 使用GM指令刷新活跃度状态操作完毕！ 返回协议:{}", player.getPlayerId(), updPlayerInfoRs.toString());
    }

    /**
     * 增加守护者奖励时长
     * @param day 增加天数
     */
    public void addDuration(String day)
    {
        UpdPlayerInfoRs updPlayerInfoRs = new UpdPlayerInfoRs();
        PlayerInfoAttachedModel playerInfoAttachedModel = taskService.getPlayerInfoAttached(player);
        if (null == playerInfoAttachedModel)
        {
            logger.error("玩家id:{}, 使用GM指令增加守护者奖励时长时查询玩家信息为空!", player.getPlayerId());
            CmdUtil.sendErrorMsg(player.getSession(), updPlayerInfoRs.getCode(), ErrorCodeEnum.NOT_MATCH_CORRESPONDING_TYPE_TASK);
            return;
        }
        if (playerInfoAttachedModel.getIsProtector() == 0)
        {
            logger.error("玩家id:{}, 使用GM指令增加守护者奖励时长时发现没有开通守护者奖励因此无法增加有效时长!", player.getPlayerId());
            CmdUtil.sendErrorMsg(player.getSession(), updPlayerInfoRs.getCode(), ErrorCodeEnum.NOT_OPENED_GUARDIAN);
            return;
        }
        //激活时间
        LocalDateTime activeDateTime = DateUtils.conversionLocalDateTime(playerInfoAttachedModel.getProtectorActiveTime());
        //增加截止时间天数
        LocalDateTime lastLocalDateTime =  DateUtils.conversionLocalDateTime(playerInfoAttachedModel.getProtectorLastTime())
                .plusDays(Long.parseLong(day));
        playerInfoAttachedModel.setProtectorLastTime(DateUtils.conversionDate(lastLocalDateTime));
        player.cache().setPlayerInfoAttachedModel(playerInfoAttachedModel);
        playerInfoAttachedDao.queueUpdate(playerInfoAttachedModel);
        //计算剩余时间
        Duration duration = Duration.between(activeDateTime, lastLocalDateTime);
        updPlayerInfoRs.setProtectorRemainingTime(duration.toMinutes());//设置守护者奖励剩余时间, 单位:分
        updPlayerInfoRs.setProtectorLastTime(playerInfoAttachedModel.getProtectorLastTime().getTime());
        CmdUtil.sendMsg(player, updPlayerInfoRs);
        logger.debug("玩家id:{}, 使用GM指令增加守护者奖励时长操作完毕！ 返回协议:{}", player.getPlayerId(), updPlayerInfoRs.toString());
    }
}