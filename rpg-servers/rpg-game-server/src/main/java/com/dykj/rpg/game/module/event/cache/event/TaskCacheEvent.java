package com.dykj.rpg.game.module.event.cache.event;

import com.dykj.rpg.common.config.dao.TaskBasicDao;
import com.dykj.rpg.common.config.dao.TaskTypeDao;
import com.dykj.rpg.common.config.model.TaskBasicModel;
import com.dykj.rpg.common.config.model.TaskTypeModel;
import com.dykj.rpg.common.data.dao.PlayerTaskDao;
import com.dykj.rpg.common.data.model.PlayerTaskModel;
import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.module.cache.PlayerTaskCache;
import com.dykj.rpg.game.module.event.cache.CacheEvent;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.quartz.job.TaskRefreshJob;
import com.dykj.rpg.game.module.task.consts.TaskFinishConditionEnum;
import com.dykj.rpg.game.module.task.consts.TaskOpenConditionEnum;
import com.dykj.rpg.game.module.task.consts.TaskTypeEnum;
import com.dykj.rpg.game.module.task.logic.refresh.TaskScheduleRefreshUtil;
import com.dykj.rpg.game.module.task.logic.response.TaskConditions;
import com.dykj.rpg.game.module.task.logic.response.TaskResponse;
import com.dykj.rpg.game.module.task.logic.service.TaskService;
import com.dykj.rpg.util.spring.BeanFactory;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Description 玩家登陆前初始化任务相关数据
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/5/10
 */
@Component
public class TaskCacheEvent extends CacheEvent<PlayerTaskCache>
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private TaskBasicDao taskBasicDao;

    @Resource
    private TaskTypeDao taskTypeDao;

    @Resource
    private PlayerTaskDao playerTaskDao;

    @Resource
    private TaskService taskService;

    @Override
    public void loadFromDb(Player player)
    {
        PlayerTaskCache playerTaskCache = player.cache().getPlayerTaskCache();
        if (null == playerTaskCache || playerTaskCache.values().isEmpty())
        {
            List<PlayerTaskModel> playerTaskModels = playerTaskDao.queryForList(player.getPlayerId());
            if (null != playerTaskModels && !playerTaskModels.isEmpty())
            {
                for (PlayerTaskModel playerTaskModel : playerTaskModels)
                {
                    playerTaskCache.updateCache(playerTaskModel);
                }
            }
        }
        player.cache().setPlayerTaskCache(playerTaskCache);
        //登录时判断当前玩家有没有累计期间的任务没有则初始化
        this.initGrandTotal(player);
    }

    @Override
    public void refreshCache(Player player)
    {
        //登录加载任务列表时进行任务刷新重置(因定时任务刷新重置时主要针对在线玩家, 这里登录时对过了定时任务执行时间的玩家进行任务重置)
        Collection<TaskTypeModel> collection = taskTypeDao.getConfigs();
        if (null != collection && !collection.isEmpty())
        {
            for (TaskTypeModel taskTypeModel : collection)
            {
                if (taskTypeModel.getKey() == TaskTypeEnum.MAIN_LINE.getType())
                {
                    continue;
                }
                if (!CronExpression.isValidExpression(taskTypeModel.getRefreshTime()))
                {
                    logger.error("玩家id:{}, refreshTime:{}, 配置无效!", player.getPlayerId(), taskTypeModel.getRefreshTime());
                    continue;
                }
                BeanFactory.getBean(TaskRefreshJob.class).logic(player, taskTypeModel, true);
            }
        }
        //登录时触发默认类型日周常任务   任务前置类型: 默认开放
        TaskResponse taskResponse = taskService.triggerTaskPush(player, TaskOpenConditionEnum.DEFAULT, TaskOpenConditionEnum.GRADE);
        if (taskResponse.getCodeEnum().equals(ErrorCodeEnum.SUCCESS))
        {
            /*
             * 触发任务进度刷新
             * 任务完成条件类型:
             *      6-登录游戏
             *      7-连续登录
             */
            TaskScheduleRefreshUtil.commonSchedule(player,
                    new TaskConditions(TaskFinishConditionEnum.LOGIN_GAME),
                    new TaskConditions(TaskFinishConditionEnum.CONTINUOUS_LOGIN));
        }
    }

    /**
     * 判断有没有累计期间任务没有则新增
     * @param player 玩家信息
     */
    private void initGrandTotal(Player player)
    {
        Collection<TaskBasicModel> taskBasicModels = taskBasicDao.getConfigs();
        if (null == taskBasicModels || taskBasicModels.isEmpty())
        {
            return;
        }
        PlayerTaskCache playerTaskCache = player.cache().getPlayerTaskCache();
        List<PlayerTaskModel> inslist = new ArrayList<>();//存储待添加的累计任务
        if (null == playerTaskCache.values() || playerTaskCache.values().isEmpty())
        {
            for (TaskBasicModel taskBasicModel : taskBasicModels)
            {
                for (List<Integer> list : taskBasicModel.getTaskFinish())
                {
                    //如果是累计期间类型
                    if (list.get(0) == TaskFinishConditionEnum.ACCUMULATION_PERIOD.getType())
                    {
                        inslist.add(this.assemblyModel(player, taskBasicModel));
                        break;
                    }
                }
            }
        }
        else
        {
            for (TaskBasicModel taskBasicModel : taskBasicModels)
            {
                for (List<Integer> list : taskBasicModel.getTaskFinish())
                {
                    //如果是累计期间类型
                    if (list.get(0) == TaskFinishConditionEnum.ACCUMULATION_PERIOD.getType())
                    {
                        if (playerTaskCache.values().stream().noneMatch(e -> e.getTaskId() == taskBasicModel.getTaskId()))
                        {
                            inslist.add(this.assemblyModel(player, taskBasicModel));
                            break;
                        }
                    }
                }
            }
        }
        if (!inslist.isEmpty())
        {
            inslist.forEach(e ->
            {
                playerTaskCache.updateCache(e);
                playerTaskDao.queueInsert(e);
            });
            logger.error("玩家id:{}, 登录时初始化累计期间任务数量: {}", player.getPlayerId(), inslist.size());
        }
    }

    /**
     * 组装model
     * @param player 玩家信息
     * @param taskBasicModel  任务基础
     * @return 玩家任务
     */
    private PlayerTaskModel assemblyModel(Player player, TaskBasicModel taskBasicModel)
    {
        //初始化累计任务   任务激活时间为空且触发状态为未触发
        PlayerTaskModel playerTaskModel = new PlayerTaskModel();
        playerTaskModel.setPlayerId(player.getPlayerId());
        playerTaskModel.setTaskId(taskBasicModel.getTaskId());
        playerTaskModel.setTaskType(taskBasicModel.getTaskType());
        playerTaskModel.setTaskStatus(1);
        playerTaskModel.setTaskSchedule(0);
        playerTaskModel.setTaskRewardStatus(0);
        playerTaskModel.setProtectorRewardStatus(0);
        playerTaskModel.setTriggerStatus(0);
        return playerTaskModel;
    }
}