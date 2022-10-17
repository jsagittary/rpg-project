package com.dykj.rpg.game.module.quartz.job;

import com.alibaba.fastjson.JSONArray;
import com.dykj.rpg.common.config.dao.TaskActiveDao;
import com.dykj.rpg.common.config.model.TaskActiveModel;
import com.dykj.rpg.common.config.model.TaskTypeModel;
import com.dykj.rpg.common.data.dao.PlayerInfoAttachedDao;
import com.dykj.rpg.common.data.dao.PlayerTaskDao;
import com.dykj.rpg.common.data.model.PlayerInfoAttachedModel;
import com.dykj.rpg.common.data.model.PlayerTaskModel;
import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.player.service.PlayerCacheService;
import com.dykj.rpg.game.module.quartz.core.QuartzConsts;
import com.dykj.rpg.game.module.task.consts.TaskTypeEnum;
import com.dykj.rpg.game.module.task.logic.service.TaskService;
import com.dykj.rpg.game.module.util.CmdUtil;
import com.dykj.rpg.protocol.player.UpdPlayerInfoRs;
import com.dykj.rpg.protocol.task.TaskRefreshListRs;
import com.dykj.rpg.protocol.task.TaskRs;
import com.dykj.rpg.util.date.DateUtils;
import com.dykj.rpg.util.spring.BeanFactory;
import org.apache.commons.lang.StringUtils;
import org.quartz.CronExpression;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @Description 任务刷新Job
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/11
 */
@Component
public class TaskRefreshJob implements Job
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException
    {
        TaskTypeModel taskTypeModel = (TaskTypeModel) context.getJobDetail().getJobDataMap().get(QuartzConsts.JOB_TASK_TYPE_BASIS);
        //查询所有在线玩家
        Map<Integer, Player> onlinePlayerMap = BeanFactory.getBean(PlayerCacheService.class).getOnlinePlayers();
        if (null == onlinePlayerMap || onlinePlayerMap.isEmpty())
        {
            return;
        }
        for (Map.Entry<Integer, Player> entry : onlinePlayerMap.entrySet())
        {
            this.logic(entry.getValue(), taskTypeModel, false);
        }
        logger.info("任务类型:{}, 定时重置在线玩家任务状态操作完毕!", taskTypeModel.getTaskTypeId());
    }

    /**
     * 任务重置逻辑
     * @param player 玩家信息
     * @param taskTypeModel 任务类型基础
     * @param flag true-针对登录时长时间未登录的玩家做任务重置    false-定时任务重置
     */
    public void logic(Player player, TaskTypeModel taskTypeModel, boolean flag)
    {
        TaskService taskService = BeanFactory.getBean(TaskService.class);
        //根据任务类型查询触发状态为1的任务列表
        List<PlayerTaskModel> taskModelList = taskService.getPlayerTaskList(player, taskTypeModel.getTaskTypeId());
        if (null == taskModelList || taskModelList.isEmpty())
        {
            return;
        }
        PlayerInfoAttachedModel playerInfoAttachedModel = BeanFactory.getBean(TaskService.class).getPlayerInfoAttached(player);
        if (null == playerInfoAttachedModel)
        {
            logger.error("玩家id:{}, 使用GM指令增加守护者奖励时长时查询玩家信息为空!", player.getPlayerId());
            return;
        }
        List<PlayerTaskModel> geneResetList = new ArrayList<>();//待更新任务列表
        //当前时间
        Date currentDate = DateUtils.conversionDate(LocalDateTime.now().withSecond(0));
        if (flag)
        {
            //解析Cron表达式获得执行时间
            CronExpression expression = null;
            try
            {
                expression = new CronExpression(taskTypeModel.getRefreshTime());
            }
            catch (ParseException e)
            {
                logger.error("cron:{}, 转换cron表达式异常!", taskTypeModel.getRefreshTime(), e);
                return;
            }
            LocalDateTime refreshLocalDate = DateUtils.conversionLocalDateTime(expression.getNextValidTimeAfter(currentDate));
            for (PlayerTaskModel playerTaskModel : taskModelList)
            {
                boolean is = false;
                if (null != playerTaskModel.getLastRefreshTime())
                {
                    //拿到最后一次刷新时间
                    LocalDateTime lastRefreshLocal = DateUtils.conversionLocalDateTime(playerTaskModel.getLastRefreshTime());
                    Duration duration = Duration.between(lastRefreshLocal, refreshLocalDate);//计算时间差
                    //如果当前任务类型等于日常则判断时间差是否相差1天   相差1天表示昨天已经刷新过如不是则表示有N天未登录则刷新任务状态
                    if (playerTaskModel.getTaskType() == TaskTypeEnum.DAILY.getType() && duration.toDays() >= 1)
                    {
                        is = true;
                    }
                    //如果当前任务类型等于周常则判断时间差是否相差7天   相差7天表示昨天已经刷新过如不是则表示有N天未登录则刷新任务状态
                    else if (playerTaskModel.getTaskType() == TaskTypeEnum.WEEK.getType() && duration.toDays() >= 7)
                    {
                        is = true;
                    }
                }
                if (is)
                {
                    //把日常和周常的任务状态及进度重置
                    playerTaskModel.setTaskStatus(1);
                    playerTaskModel.setTaskSchedule(0);
                    playerTaskModel.setLastRefreshTime(currentDate);
                    geneResetList.add(playerTaskModel);
                }
            }
        }
        else
        {
            for (PlayerTaskModel playerTaskModel : taskModelList)
            {
                //把日常和周常的任务状态及进度重置
                playerTaskModel.setTaskStatus(1);
                playerTaskModel.setTaskSchedule(0);
                playerTaskModel.setLastRefreshTime(currentDate);
                geneResetList.add(playerTaskModel);
            }
        }
        if (null != geneResetList && !geneResetList.isEmpty())
        {
            List<TaskRs> list = new ArrayList<>();//存储协议
            PlayerTaskDao playerTaskDao = BeanFactory.getBean(PlayerTaskDao.class);
            geneResetList.forEach(e -> {
                player.cache().getPlayerTaskCache().updateCache(e);
                playerTaskDao.queueUpdate(e);
                list.add(taskService.taskRs(e));
            });
            //推送刷新后的任务列表协议
            TaskRefreshListRs taskRefreshListRs = new TaskRefreshListRs();
            taskRefreshListRs.setTaskType(taskTypeModel.getTaskTypeId());
            taskRefreshListRs.setTaskList(list);
            CmdUtil.sendMsg(player, taskRefreshListRs);
            logger.info("玩家id:{}, 刷新任务状态操作完成后返回协议:{}", player.getPlayerId(), taskRefreshListRs.toString());
            //重置活跃度并推送协议
            UpdPlayerInfoRs updPlayerInfoRs = new UpdPlayerInfoRs();
            if (taskTypeModel.getTaskTypeId() == TaskTypeEnum.DAILY.getType())
            {
                playerInfoAttachedModel.setDailyActivity(0);
                updPlayerInfoRs.setDailyActivity(playerInfoAttachedModel.getDailyActivity());
            }
            if (taskTypeModel.getTaskTypeId() == TaskTypeEnum.WEEK.getType())
            {
                playerInfoAttachedModel.setWeekActivity(0);
                updPlayerInfoRs.setWeekActivity(playerInfoAttachedModel.getWeekActivity());
            }
            //清空活跃度奖励列表id
            if (StringUtils.isNotBlank(playerInfoAttachedModel.getActivityRewardList()))
            {
                TaskActiveDao taskActiveDao = BeanFactory.getBean(TaskActiveDao.class);
                List<Integer> activityRewardList = JSONArray.parseArray(playerInfoAttachedModel.getActivityRewardList(), Integer.class);
                Iterator<Integer> iterator = activityRewardList.iterator();
                while (iterator.hasNext())
                {
                    Integer activityTypeId = iterator.next();
                    TaskActiveModel taskActiveModel = taskActiveDao.getConfigByKey(activityTypeId);
                    if (null == taskActiveModel)
                    {
                        logger.error("玩家id:{}, 活跃宝箱id:{}, 更新活跃度奖励列表(activity_reward_list)时查询活跃宝箱表基础配置信息为空!", player.getPlayerId(), activityTypeId);
                        continue;
                    }
                    if (taskActiveModel.getTaskType().equals(taskTypeModel.getTaskTypeId()))
                    {
                        iterator.remove();
                    }
                }
                playerInfoAttachedModel.setActivityRewardList(JSONArray.toJSONString(activityRewardList));
            }
            player.cache().setPlayerInfoAttachedModel(playerInfoAttachedModel);
            BeanFactory.getBean(PlayerInfoAttachedDao.class).queueUpdate(playerInfoAttachedModel);
            //更新活跃度后推送协议
            CmdUtil.sendMsg(player, updPlayerInfoRs);
            logger.info("玩家id:{}, 刷新活跃度后返回协议:{}", player.getPlayerId(), updPlayerInfoRs.toString());
        }
    }
}