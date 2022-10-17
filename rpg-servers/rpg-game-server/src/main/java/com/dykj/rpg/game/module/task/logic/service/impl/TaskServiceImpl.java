package com.dykj.rpg.game.module.task.logic.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.dykj.rpg.common.config.constant.ConfigEnum;
import com.dykj.rpg.common.config.dao.*;
import com.dykj.rpg.common.config.model.*;
import com.dykj.rpg.common.consts.CommonConsts;
import com.dykj.rpg.common.consts.ItemOperateEnum;
import com.dykj.rpg.common.consts.ItemTypeEnum;
import com.dykj.rpg.common.data.dao.PlayerInfoAttachedDao;
import com.dykj.rpg.common.data.dao.PlayerTaskDao;
import com.dykj.rpg.common.data.model.PlayerInfoAttachedModel;
import com.dykj.rpg.common.data.model.PlayerInfoModel;
import com.dykj.rpg.common.data.model.PlayerMissionModel;
import com.dykj.rpg.common.data.model.PlayerTaskModel;
import com.dykj.rpg.game.consts.ErrorCodeEnum;
import com.dykj.rpg.game.module.cache.PlayerTaskCache;
import com.dykj.rpg.game.module.item.consts.ItemPromp;
import com.dykj.rpg.game.module.item.response.ItemJoinModel;
import com.dykj.rpg.game.module.item.response.ItemResponse;
import com.dykj.rpg.game.module.item.service.ItemService;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.task.consts.TaskFinishConditionEnum;
import com.dykj.rpg.game.module.task.consts.TaskOpenConditionEnum;
import com.dykj.rpg.game.module.task.consts.TaskTypeEnum;
import com.dykj.rpg.game.module.task.logic.response.TaskConditions;
import com.dykj.rpg.game.module.task.logic.response.TaskResponse;
import com.dykj.rpg.game.module.task.logic.service.TaskService;
import com.dykj.rpg.game.module.task.logic.service.strategy.core.TaskFinishFactory;
import com.dykj.rpg.game.module.task.logic.service.strategy.core.TaskFinishStrategy;
import com.dykj.rpg.game.module.util.CmdUtil;
import com.dykj.rpg.protocol.player.PlayerAttachedRs;
import com.dykj.rpg.protocol.task.*;
import com.dykj.rpg.util.date.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description 任务系统实现
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/4
 */
@Service
public class TaskServiceImpl implements TaskService
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private TaskBasicDao taskBasicDao;

    @Resource
    private PlayerTaskDao playerTaskDao;

    @Resource
    private PlayerInfoAttachedDao playerInfoAttachedDao;

    @Resource
    private ItemDao itemDao;

    @Resource
    private TaskActiveDao taskActiveDao;

    @Resource
    private ConfigDao configDao;

    @Resource
    private ItemService itemService;

    /**
     * 获得指定的任务信息
     *
     * @param player 玩家信息
     * @param taskId 任务id
     * @return 任务信息
     */
    @Override
    public PlayerTaskModel getTaskfo(Player player, Integer taskId)
    {
        return player.cache().getPlayerTaskCache().getCache(taskId.longValue());
    }

    /**
     * 根据任务类型、触发类型为1(已触发状态)的任务列表
     *
     * @param taskType 任务类型数组
     * @return 任务列表
     */
    @Override
    public List<PlayerTaskModel> getPlayerTaskList(Player player, Integer... taskType)
    {
        PlayerTaskCache playerTaskCache = player.cache().getPlayerTaskCache();
        if (null != playerTaskCache)
        {
            return playerTaskCache.getTaskList(taskType);
        }
        return null;
    }

    /**
     * 打开界面加载任务数据
     *
     * @param player 玩家信息
     */
    @Override
    public void loadTaskData(Player player, TaskListRq taskListRq)
    {
        //加载任务列表信息
        PlayerTaskCache playerTaskCache = player.cache().getPlayerTaskCache();
        if (null != playerTaskCache)
        {
            List<TaskRs> list = new ArrayList<>();
            for (PlayerTaskModel playerTaskModel : playerTaskCache.values())
            {
                if (playerTaskModel.getTriggerStatus() == 0)
                {
                    continue;
                }
                TaskRs taskRs = new TaskRs();
                taskRs.setTaskId(playerTaskModel.getTaskId());
                taskRs.setTaskType(playerTaskModel.getTaskType());
                taskRs.setTaskStatus(playerTaskModel.getTaskStatus());
                taskRs.setTaskSchedule(playerTaskModel.getTaskSchedule());
                taskRs.setTaskRewardStatus(playerTaskModel.getTaskRewardStatus());
                taskRs.setProtectorRewardStatus(playerTaskModel.getProtectorRewardStatus());
                list.add(taskRs);
            }
            logger.debug("玩家id:{}, 任务列表数量:{}", player.getPlayerId(), list.size());
            CmdUtil.sendMsg(player, new TaskListRs(list));
        }
        //加载玩家附属信息
        PlayerInfoAttachedModel playerInfoAttachedModel = player.cache().getPlayerInfoAttachedModel();
        if (null != playerInfoAttachedModel)
        {
            PlayerAttachedRs playerAttachedRs = new PlayerAttachedRs();
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

    /**
     * 任务触发(0-无条件开放  1-等级 2-关卡进度 3-前置任务ID)入库并生成TaskTriggerListRs协议并推送
     *
     * @param player                玩家信息
     * @param taskOpenConditionEnum 任务触发类型
     * @return 响应
     */
    @Override
    public TaskResponse triggerTaskPush(Player player, TaskOpenConditionEnum... taskOpenConditionEnum)
    {
        TaskResponse response = new TaskResponse(ErrorCodeEnum.SUCCESS, null);
        Collection<TaskBasicModel> collection = taskBasicDao.getConfigs();
        if (null == collection || collection.isEmpty())
        {
            logger.error("玩家id:{}, 获取任务基础信息配置表数据为空!", player.getPlayerId());
            response.setCodeEnum(ErrorCodeEnum.CONFIG_ERROR);
            return response;
        }
        PlayerInfoModel playerInfoModel = player.cache().getPlayerInfoModel();//玩家信息
        if (null == playerInfoModel)
        {
            logger.error("玩家id:{}, 获取玩家信息为空!", player.getPlayerId());
            response.setCodeEnum(ErrorCodeEnum.DATA_ERROR);
            return response;
        }
        PlayerMissionModel playerMissionModel = player.cache().getPlayerMissionModel();//玩家通过的关卡信息
        List<PlayerTaskModel> warePlayerTaskList = new ArrayList<>();
        PlayerTaskCache playerTaskCache = player.cache().getPlayerTaskCache();//获取玩家任务缓存列表
        if (null != playerTaskCache)
        {
            for (TaskBasicModel taskBasicModel : collection)
            {
                try
                {
                    //如果该任务已存在且触发状态为1则跳过
                    if (playerTaskCache.values().stream().anyMatch(e -> e.getTaskId() == taskBasicModel.getTaskId() && e.getTriggerStatus() == 1))
                    {
                        continue;
                    }
                    List<List<Integer>> taskOpenRequireList = taskBasicModel.getTaskOpen();//获取任务开启条件
                    for (TaskOpenConditionEnum conditionEnum : taskOpenConditionEnum)
                    {
                        //如果是默认开放则在玩家登陆或gm指令重置触发
                        if (conditionEnum.equals(TaskOpenConditionEnum.DEFAULT) && (null == taskOpenRequireList || taskOpenRequireList.isEmpty()))
                        {
                            warePlayerTaskList.add(this.assemblyModel(player, taskBasicModel));
                        }
                        else
                        {
                            //过滤掉默认开放类型的任务
                            if (null == taskOpenRequireList || taskOpenRequireList.isEmpty())
                            {
                                continue;
                            }
                            //如果是等级则在角色升级触发或者是关卡进度则在战斗结算触发
                            if (conditionEnum.equals(TaskOpenConditionEnum.GRADE) || conditionEnum.equals(TaskOpenConditionEnum.CHECKPOINT_PROGRESS))
                            {
                                this.gradeCheckpointTrigger(player, taskBasicModel, taskOpenRequireList, playerInfoModel, playerMissionModel, warePlayerTaskList);
                            }
                            //如果前置任务id则在领取任务奖励时触发
                            if (conditionEnum.equals(TaskOpenConditionEnum.PRE_TASK))
                            {
                                this.preTaskTrigger(player, taskBasicModel, taskOpenRequireList, playerInfoModel, playerMissionModel, warePlayerTaskList);
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    logger.error("玩家id:{}, 任务id:{}, 触发任务生成时异常!", player.getPlayerId(), taskBasicModel.getTaskId(), e);
                }
            }
        }
        if (!warePlayerTaskList.isEmpty())
        {
            List<TaskRs> taskList = new ArrayList<>();
            for (PlayerTaskModel playerTaskModel : warePlayerTaskList)
            {
                PlayerTaskModel model = playerTaskCache.getCache((long) playerTaskModel.getTaskId());
                if (null == model)
                {
                    playerTaskDao.queueInsert(playerTaskModel);
                }
                else
                {
                    //如果当前任务存在且触发状态等于0则表示是累计任务需要把任务进度、状态都进行替换
                    if (model.getTriggerStatus() == 0)
                    {
                        playerTaskModel.setTaskSchedule(model.getTaskSchedule());
                        playerTaskModel.setTaskStatus(model.getTaskStatus());
                        playerTaskModel.setTriggerStatus(1);
                    }
                    playerTaskDao.queueUpdate(playerTaskModel);
                }
                playerTaskCache.updateCache(playerTaskModel);
                //组装协议
                taskList.add(this.taskRs(playerTaskModel));
            }
            //推送触发任务协议
            response.setProtocol(new TaskTriggerListRs(taskList));
            CmdUtil.sendMsg(player, response.getProtocol());

            //刷新任务进度
            List<TaskConditions> conditionsModelList = new ArrayList<>();
            for (TaskFinishConditionEnum conditionEnum : TaskFinishConditionEnum.values())
            {
                /* 过滤掉任务完成条件类型:
                 * 激活期间
                 * 登录游戏
                 * 连续登录
                 */
                if (conditionEnum.getType() == TaskFinishConditionEnum.DURING_ACTIVATION.getType()
                        || conditionEnum.getType() == TaskFinishConditionEnum.LOGIN_GAME.getType()
                        || conditionEnum.getType() == TaskFinishConditionEnum.CONTINUOUS_LOGIN.getType())
                {
                    continue;
                }
                else if (conditionEnum.getType() == TaskFinishConditionEnum.ACCUMULATION_PERIOD.getType())
                {
                    for (TaskFinishConditionEnum.GrandTotalEnum grandTotalEnum : TaskFinishConditionEnum.GrandTotalEnum.values())
                    {
                        /* 过滤掉任务完成条件类型:
                         * 【累计期间】花费X金币
                         * 【累计期间】花费X钻石
                         */
                        if (grandTotalEnum.getSubclassType() == TaskFinishConditionEnum.GrandTotalEnum.SPEND_GOLD.getSubclassType()
                                || grandTotalEnum.getSubclassType() == TaskFinishConditionEnum.GrandTotalEnum.SPEND_DIAMOND.getSubclassType())
                        {
                            continue;
                        }
                        conditionsModelList.add(new TaskConditions(TaskFinishConditionEnum.ACCUMULATION_PERIOD, grandTotalEnum));
                    }
                }
                else
                {
                    conditionsModelList.add(new TaskConditions(conditionEnum));
                }
            }
            TaskResponse taskResponse = this.taskFinishRealize(player, conditionsModelList);
            if (taskResponse.getCodeEnum().equals(ErrorCodeEnum.SUCCESS) && null != taskResponse.getProtocol())
            {
                response.setTaskRefresh(taskResponse.getTaskRefresh());
                CmdUtil.sendMsg(player, taskResponse.getProtocol());
            }
        }
        logger.debug("玩家id:{}, 执行任务触发结束返回协议:{}", player.getPlayerId(), response.toString());
        return response;
    }

    /**
     * 等级和关卡进度触发
     */
    private void gradeCheckpointTrigger(Player player, TaskBasicModel taskBasicModel, List<List<Integer>> taskOpenRequireList, PlayerInfoModel playerInfoModel, PlayerMissionModel playerMissionModel, List<PlayerTaskModel> warePlayerTaskList)
    {
        List<List<Integer>> tempList = new ArrayList<>();//用于存储对应类型是否都满足配置触发条件
        for (List<Integer> ele : taskOpenRequireList)
        {
            int condition = ele.get(0);//任务条件类型
            int configValue = ele.get(1);//任务条件类型对应的配置项值
            //等级
            if (condition == TaskOpenConditionEnum.GRADE.getType())
            {
                //如果当前等级满足配置项则开启任务
                if (playerInfoModel.getLv() >= configValue)
                {
                    tempList.add(ele);
                }
            }
            //关卡进度
            else if (condition == TaskOpenConditionEnum.CHECKPOINT_PROGRESS.getType())
            {
                //如果当前通过关卡id大于或等于配置的关卡id则满足触发任务条件
                if (null != playerMissionModel && playerMissionModel.getMissionId() >= configValue)
                {
                    tempList.add(ele);
                }
            }
        }
        //如果存储对应触发条件的集合数量等于任务基础配置表里的任务开启条件配置则表示该任务满足当前触发条件
        if (tempList.size() == taskOpenRequireList.size())
        {
            warePlayerTaskList.add(this.assemblyModel(player, taskBasicModel));
        }
    }

    /**
     * 前置任务触发
     */
    private void preTaskTrigger(Player player, TaskBasicModel taskBasicModel, List<List<Integer>> taskOpenRequireList, PlayerInfoModel playerInfoModel, PlayerMissionModel playerMissionModel, List<PlayerTaskModel> warePlayerTaskList)
    {
        //如果任务开启条件配置包含前置任务id类型
        if (taskOpenRequireList.stream().anyMatch(e -> e.get(0) == TaskOpenConditionEnum.PRE_TASK.getType()))
        {
            boolean flag = true;
            for (List<Integer> termList : taskOpenRequireList)
            {
                int condition = termList.get(0);//任务条件类型
                int configValue = termList.get(1);//任务条件类型对应的配置项值
                //如果是等级
                if (condition == TaskOpenConditionEnum.GRADE.getType())
                {
                    //如果等级没有达到配置项则不触发任务条件
                    if (playerInfoModel.getLv() < configValue)
                    {
                        flag = false;
                        break;
                    }
                }
                //如果是关卡进度
                else if (condition == TaskOpenConditionEnum.CHECKPOINT_PROGRESS.getType())
                {
                    //如果当前通过关卡id小于配置项关卡id则不触发任务条件
                    if (null != playerMissionModel && playerMissionModel.getMissionId() < configValue)
                    {
                        flag = false;
                        break;
                    }
                }
                //如果是前置任务id
                else
                {
                    PlayerTaskModel configPlayerTask = this.getTaskfo(player, configValue);
                    if (null == configPlayerTask || configPlayerTask.getTaskStatus() != 2)
                    {
                        flag = false;
                        break;
                    }
                }
            }
            if (flag)
            {
                warePlayerTaskList.add(this.assemblyModel(player, taskBasicModel));
            }
        }
    }

    /**
     * 组装model
     *
     * @param player         玩家信息
     * @param taskBasicModel 任务基础配置
     * @return 玩家任务model
     */
    private PlayerTaskModel assemblyModel(Player player, TaskBasicModel taskBasicModel)
    {
        PlayerTaskModel playerTaskModel = new PlayerTaskModel();
        playerTaskModel.setPlayerId(player.getPlayerId());
        playerTaskModel.setTaskId(taskBasicModel.getTaskId());
        playerTaskModel.setTaskType(taskBasicModel.getTaskType());
        playerTaskModel.setTaskStatus(1);
        playerTaskModel.setTaskSchedule(0);
        playerTaskModel.setTaskActivationTime(new Date());
        playerTaskModel.setTaskRewardStatus(0);
        playerTaskModel.setProtectorRewardStatus(0);
        playerTaskModel.setTriggerStatus(1);
        return playerTaskModel;
    }

    /**
     * 任务进度刷新(入库及生成UpdateTaskListRs协议)
     *
     * @param player           玩家信息
     * @param conditionsModels 任务完成条件参数集
     */
    @Override
    public void taskFinishRealize(Player player, TaskConditions... conditionsModels)
    {
        List<TaskConditions> collects = Arrays.stream(conditionsModels).collect(Collectors.toList());
        this.taskFinishRealize(player, collects);
    }

    /**
     * 任务进度刷新(入库及生成UpdateTaskListRs协议)
     *
     * @param player              玩家信息
     * @param conditionsModelList 任务完成条件参数集
     */
    @Override
    public TaskResponse taskFinishRealize(Player player, List<TaskConditions> conditionsModelList)
    {
        TaskResponse taskResponse = new TaskResponse(ErrorCodeEnum.SUCCESS, null);
        PlayerInfoModel playerInfoModel = player.cache().getPlayerInfoModel();//玩家基础信息
        if (null == playerInfoModel)
        {
            logger.error("玩家id:{}, 获取玩家基本信息为空!", player.getPlayerId());
            taskResponse.setCodeEnum(ErrorCodeEnum.DATA_ERROR);
            return taskResponse;
        }
        PlayerTaskCache playerTaskCache = player.cache().getPlayerTaskCache();//获取玩家任务缓存列表
        if (null != playerTaskCache)
        {
            List<PlayerTaskModel> updPlayerTaskList = new ArrayList<>();//存储待更新的任务列表
            for (PlayerTaskModel playerTaskModel : playerTaskCache.values())
            {
                try
                {
                    if (playerTaskModel.getTaskStatus() == 2)
                    {
                        continue;
                    }
                    TaskBasicModel taskBasicModel = taskBasicDao.getConfigByKey(playerTaskModel.getTaskId());//获取任务基础配置信息
                    if (null == taskBasicModel.getTaskFinish())
                    {
                        logger.error("玩家id:{}, 任务id:{}, 任务完成条件配置项时为空!", player.getPlayerId(), playerTaskModel.getTaskId());
                        continue;
                    }
                    //遍历完成条件类型
                    for (TaskConditions conditionsModel : conditionsModelList)
                    {
                        //遍历任务完成条件配置
                        for (List<Integer> taskFinish : taskBasicModel.getTaskFinish())
                        {
                            if (taskFinish.get(0) == conditionsModel.getBigCategory().getType())
                            {
                                //任务完成条件逻辑
                                TaskFinishStrategy taskFinishStrategy = TaskFinishFactory.getInvokeStrategy(conditionsModel.getBigCategory());
                                PlayerTaskModel updPlayerTaskModel = taskFinishStrategy.realize(player, playerTaskModel, taskFinish, conditionsModel);
                                if (null != updPlayerTaskModel)
                                {
                                    updPlayerTaskList.add(updPlayerTaskModel);
                                }
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    logger.error("玩家id:{}, 任务id:{}, 刷新任务进度时异常!", player.getPlayerId(), playerTaskModel.getTaskId(), e);
                }
            }
            if (!updPlayerTaskList.isEmpty())
            {
                List<TaskRs> taskList = new ArrayList<>();
                for (PlayerTaskModel playerTaskModel : updPlayerTaskList)
                {
                    playerTaskCache.updateCache(playerTaskModel);
                    playerTaskDao.queueUpdate(playerTaskModel);//异步更新
                    //已触发状态才组装协议推送给客户端
                    if (playerTaskModel.getTriggerStatus() == 1)
                    {
                        taskList.add(this.taskRs(playerTaskModel));//组装协议
                    }
                }
                taskResponse.setTaskRefresh(true);
                taskResponse.setProtocol(new UpdateTaskListRs(taskList));
                logger.debug("玩家id:{}, 刷新任务进度执行完毕, 返回协议:{}", player.getPlayerId(), taskResponse.getProtocol().toString());
            }
        }
        return taskResponse;
    }

    /**
     * 领取任务奖励(推送ReceiveTaskRewardsRs协议、更新背包列表UpdateItemListRs协议, 如果奖励包含活跃度则更新活跃度推送UpdateActivityRs协议)
     *
     * @param player               玩家信息
     * @param receiveTaskRewardsRq 请求协议
     */
    @Override
    public void receiveTaskRewards(Player player, ReceiveTaskRewardsRq receiveTaskRewardsRq)
    {
        if (null == receiveTaskRewardsRq || null == receiveTaskRewardsRq.getTaskIdList() || receiveTaskRewardsRq.getTaskIdList().isEmpty())
        {
            logger.error("玩家id:{}, 领取任务奖励时任务id列表为空!", player.getPlayerId());
            CmdUtil.sendErrorMsg(player.getSession(), receiveTaskRewardsRq.getCode(), ErrorCodeEnum.CLIENT_PRAMS_ERROR);
            return;
        }
        PlayerInfoAttachedModel playerInfoAttachedModel = this.getPlayerInfoAttached(player);//玩家附属信息表
        if (null == playerInfoAttachedModel)
        {
            logger.error("玩家id:{}, 查询玩家附属信息表数据为空!", player.getPlayerId());
            CmdUtil.sendErrorMsg(player.getSession(), receiveTaskRewardsRq.getCode(), ErrorCodeEnum.DATA_ERROR);
            return;
        }
        List<ItemJoinModel> typesList = new ArrayList<>();//存储活跃度类型集合
        List<ItemJoinModel> itemJoinModels = new ArrayList<>();//存储更新道具
        List<PlayerTaskModel> updTaskList = new ArrayList<>();//存储待更新状态任务列表
        for (Integer taskId : receiveTaskRewardsRq.getTaskIdList())
        {
            PlayerTaskModel playerTaskModel = this.getTaskfo(player, taskId);
            if (null == playerTaskModel)
            {
                logger.error("玩家id:{}, 任务id:{}, 获取玩家任务信息存为空!", player.getPlayerId(), taskId);
                CmdUtil.sendErrorMsg(player.getSession(), receiveTaskRewardsRq.getCode(), ErrorCodeEnum.DATA_ERROR);
                return;
            }
            if (playerTaskModel.getTriggerStatus() == 0)
            {
                logger.error("玩家id:{}, 任务id:{}, 当前任务处于未触发状态!", player.getPlayerId(), taskId);
                CmdUtil.sendErrorMsg(player.getSession(), receiveTaskRewardsRq.getCode(), ErrorCodeEnum.TASK_NOT_TRIGGERED);
                return;
            }
            TaskBasicModel taskBasicModel = taskBasicDao.getConfigByKey(taskId);
            if (null == taskBasicModel)
            {
                logger.error("玩家id:{}, 任务id:{}, 获取任务基础配置表信息为空!", player.getPlayerId(), taskId);
                CmdUtil.sendErrorMsg(player.getSession(), receiveTaskRewardsRq.getCode(), ErrorCodeEnum.CONFIG_ERROR);
                return;
            }
            //如果任务状态等于已完成
            if (playerTaskModel.getTaskStatus() == 2)
            {
                ErrorCodeEnum codeEnum = this.receiveAwardOperating(player, receiveTaskRewardsRq, playerTaskModel, playerInfoAttachedModel, taskBasicModel, typesList, itemJoinModels);
                if (!codeEnum.equals(ErrorCodeEnum.SUCCESS))
                {
                    CmdUtil.sendErrorMsg(player.getSession(), receiveTaskRewardsRq.getCode(), codeEnum);
                    return;
                }
                updTaskList.add(playerTaskModel);
            }
            else
            {
                logger.error("玩家id:{}, 任务id:{}, 当前任务未完成无法领取奖励!", player.getPlayerId(), taskId);
                CmdUtil.sendErrorMsg(player.getSession(), receiveTaskRewardsRq.getCode(), ErrorCodeEnum.TASK_UNDONE);
                return;
            }
        }

        int activity = 0;//活跃度
        int activityType = 0;//活跃度类型
        if (!typesList.isEmpty())
        {
            //判断活跃度类型是否一致
            long dailyActivityCount = typesList.stream().filter(e -> e.getItemType() == ItemTypeEnum.ExperienceTypeEnum.DAILY_ACTIVITY.getSubclassType()).count();
            long monthlyActivityCount = typesList.stream().filter(e -> e.getItemType() == ItemTypeEnum.ExperienceTypeEnum.MONTHLY_ACTIVITY.getSubclassType()).count();
            if (dailyActivityCount != 0 && monthlyActivityCount != 0)
            {
                logger.error("玩家id:{}, 任务基础表task_reward字段中配置的活跃度类型不一致!", player.getPlayerId());
                CmdUtil.sendErrorMsg(player.getSession(), receiveTaskRewardsRq.getCode(), ErrorCodeEnum.CONFIG_ERROR);
                return;
            }
            for (ItemJoinModel itemJoinModel : typesList)
            {
                activity += itemJoinModel.getItemNum();
            }
            //如果为周活跃值则填充周常类型
            if (typesList.stream().findFirst().get().getItemType() == ItemTypeEnum.ExperienceTypeEnum.DAILY_ACTIVITY.getSubclassType())
            {
                activityType = 1;
            }
            else
            {
                activityType = 2;
            }
        }
        boolean flag = true;
        //更新任务完成奖励获取的道具列表
        if (!itemJoinModels.isEmpty())
        {
            //更新道具奖励列表并推送道具变更协议
            ItemResponse itemResponse = itemService.batchUpdateItemPush(player, itemJoinModels, ItemOperateEnum.TASK_REWARDS, ItemPromp.BULLET_FRAME);
            if (!itemResponse.getCodeEnum().equals(ErrorCodeEnum.SUCCESS))
            {
                flag = false;
            }
        }
        if (flag)
        {
            List<TaskRs> list = new ArrayList<>();
            if (!updTaskList.isEmpty())
            {
                updTaskList.forEach(e ->
                {
                    player.cache().getPlayerTaskCache().updateCache(e);
                    playerTaskDao.queueUpdate(e);
                    list.add(this.taskRs(e));
                });
            }
            //因协议是成对出现则道具奖励更新成功后推送ReceiveTaskRewardsRs空协议响应给客户端
            CmdUtil.sendMsg(player, new ReceiveTaskRewardsRs(list));
            //更新活跃度
            this.updActivityOperating(player, playerInfoAttachedModel, activity, activityType);
            //领取任务奖励时触发任务生成(类型: 前置任务id)
            this.triggerTaskPush(player, TaskOpenConditionEnum.PRE_TASK);
        }
        logger.debug("玩家id:{}, 请求协议:{}, 执行任务奖励领取操作完毕!", player.getPlayerId(), receiveTaskRewardsRq.getTaskIdList().toString());
    }

    /**
     * 领取奖励操作
     */
    private ErrorCodeEnum receiveAwardOperating(Player player, ReceiveTaskRewardsRq receiveTaskRewardsRq, PlayerTaskModel playerTaskModel, PlayerInfoAttachedModel playerInfoAttachedModel, TaskBasicModel taskBasicModel, List<ItemJoinModel> typesList, List<ItemJoinModel> itemJoinModels)
    {
        ErrorCodeEnum codeEnum = ErrorCodeEnum.SUCCESS;
        //如果是领取普通奖励
        if (receiveTaskRewardsRq.getType() == 1)
        {
            codeEnum = this.ordinaryReward(player, receiveTaskRewardsRq, playerTaskModel, taskBasicModel, typesList, itemJoinModels);
        }
        //如果是领取守护者奖励
        else if (receiveTaskRewardsRq.getType() == 2)
        {
            codeEnum = this.protectorReward(player, receiveTaskRewardsRq, playerInfoAttachedModel, playerTaskModel, taskBasicModel, itemJoinModels);
        }
        //如果是一键领取所有奖励
        else
        {
            //如果已领取守护者奖励则领取普通奖励
            if (playerTaskModel.getProtectorRewardStatus() == 1)
            {
                codeEnum = this.ordinaryReward(player, receiveTaskRewardsRq, playerTaskModel, taskBasicModel, typesList, itemJoinModels);
            }
            //如果已领取普通奖励则领取守护者奖励
            if (playerTaskModel.getTaskRewardStatus() == 1)
            {
                //当前任务类型如果不为主线时过滤掉守护者奖励
                if (playerTaskModel.getTaskType() != TaskTypeEnum.MAIN_LINE.getType())
                {
                    codeEnum = this.protectorReward(player, receiveTaskRewardsRq, playerInfoAttachedModel, playerTaskModel, taskBasicModel, itemJoinModels);
                }
            }
            //如果普通奖励和守护者奖励都没有领取
            if (playerTaskModel.getTaskRewardStatus() == 0 && playerTaskModel.getProtectorRewardStatus() == 0)
            {
                codeEnum = this.ordinaryReward(player, receiveTaskRewardsRq, playerTaskModel, taskBasicModel, typesList, itemJoinModels);
                //如果领取普通奖励成功或者普通奖励已是领取状态
                if (codeEnum.equals(ErrorCodeEnum.SUCCESS))
                {
                    //当前任务类型如果不为主线时过滤掉守护者奖励
                    if (playerTaskModel.getTaskType() != TaskTypeEnum.MAIN_LINE.getType())
                    {
                        codeEnum = this.protectorReward(player, receiveTaskRewardsRq, playerInfoAttachedModel, playerTaskModel, taskBasicModel, itemJoinModels);
                    }
                }
            }
        }
        return codeEnum;
    }

    /**
     * 更新活跃度操作
     */
    private void updActivityOperating(Player player, PlayerInfoAttachedModel playerInfoAttachedModel, int activity, int activityType)
    {
        //如果奖励中包含活跃度
        if (activity != 0)
        {
            //根据类型更新活跃度
            if (activityType == 1)
            {
                playerInfoAttachedModel.setDailyActivity(playerInfoAttachedModel.getDailyActivity() + activity);
            }
            else
            {
                playerInfoAttachedModel.setWeekActivity(playerInfoAttachedModel.getWeekActivity() + activity);
            }
            player.cache().setPlayerInfoAttachedModel(playerInfoAttachedModel);
            playerInfoAttachedDao.queueUpdate(playerInfoAttachedModel);
            //组装协议并发送
            UpdateActivityRs updateActivityRs = new UpdateActivityRs();
            updateActivityRs.setActivity(activity);
            updateActivityRs.setActivityType(activityType);
            CmdUtil.sendMsg(player, updateActivityRs);
        }
    }

    /**
     * 领取普通奖励
     *
     * @param player               玩家信息
     * @param receiveTaskRewardsRq 协议
     * @param playerTaskModel      玩家任务
     * @param taskBasicModel       任务基础
     * @param typesList            存储活跃度类型集合
     * @param itemJoinModels       存储更新道具集合
     * @return 错误码
     */
    private ErrorCodeEnum ordinaryReward(Player player, ReceiveTaskRewardsRq receiveTaskRewardsRq, PlayerTaskModel playerTaskModel, TaskBasicModel taskBasicModel, List<ItemJoinModel> typesList, List<ItemJoinModel> itemJoinModels)
    {
        if (playerTaskModel.getTaskRewardStatus() == 1)
        {
            logger.error("玩家id:{}, 协议号:{}, 任务id:{}, 已领取过任务奖励!", player.getPlayerId(), receiveTaskRewardsRq.getCode(), taskBasicModel.getTaskId());
            return ErrorCodeEnum.RECEIVED_TASK_REWARD;
        }
        List<List<Integer>> rewardList = taskBasicModel.getTaskReward();
        if (null == rewardList || rewardList.isEmpty())
        {
            logger.error("玩家id:{}, 协议号:{}, 任务id:{}, 获取任务基础配置表\"奖励\"信息为空!", player.getPlayerId(), receiveTaskRewardsRq.getCode(), taskBasicModel.getTaskId());
            return ErrorCodeEnum.CONFIG_ERROR;
        }
        // 领取普通奖励逻辑
        for (List<Integer> list : rewardList)
        {
            int type = list.get(0);// 道具类型
            int itemId = list.get(1);// 道具id
            ItemModel itemModel = itemDao.getConfigByKey(itemId);// 道具基础信息
            if (null == itemModel || type != itemModel.getItemType())
            {
                logger.error("玩家id:{}, 协议号:{}, 道具id:{}, 获取道具基础配置表信息为空或者道具类型配置不正确!", player.getPlayerId(), receiveTaskRewardsRq.getCode(), itemModel.getItemId());
                return ErrorCodeEnum.CONFIG_ERROR;
            }
            ItemJoinModel itemJoinModel = new ItemJoinModel();
            itemJoinModel.setItemId(itemId);
            itemJoinModel.setItemNum(list.get(2));
            // 如果是活跃度类型
            if (itemModel.getItemTypeDetail() == ItemTypeEnum.ExperienceTypeEnum.DAILY_ACTIVITY.getSubclassType() || itemModel.getItemTypeDetail() == ItemTypeEnum.ExperienceTypeEnum.MONTHLY_ACTIVITY.getSubclassType())
            {
                // 如果是活跃度类型则更改type为具体活跃度
                itemJoinModel.setItemType(itemModel.getItemTypeDetail());
                typesList.add(itemJoinModel);
            }
            else
            {
                itemJoinModel.setItemType(type);
                itemJoinModels.add(itemJoinModel);
            }
        }
        // 设置领取奖励状态
        playerTaskModel.setTaskRewardStatus(1);
        return ErrorCodeEnum.SUCCESS;
    }

    /**
     * 领取守护者奖励
     *
     * @param player                  玩家信息
     * @param receiveTaskRewardsRq    协议
     * @param playerInfoAttachedModel 玩家附属信息
     * @param playerTaskModel         任务信息
     * @param taskBasicModel          任务基础配置
     * @param itemJoinModels          更新道具集合
     * @return 错误码
     */
    private ErrorCodeEnum protectorReward(Player player, ReceiveTaskRewardsRq receiveTaskRewardsRq, PlayerInfoAttachedModel playerInfoAttachedModel, PlayerTaskModel playerTaskModel, TaskBasicModel taskBasicModel, List<ItemJoinModel> itemJoinModels)
    {
        if (playerTaskModel.getProtectorRewardStatus() == 1)
        {
            logger.error("玩家id:{}, 协议号:{}, 任务id:{}, 已领取过守护者奖励!", player.getPlayerId(), receiveTaskRewardsRq.getCode(), taskBasicModel.getTaskId());
            return ErrorCodeEnum.RECEIVED_PROTECTOR_REWARD;
        }
        // 判断是否已经开通守护者
        if (playerInfoAttachedModel.getIsProtector() == 1)
        {
            // 判断任务类型不为主线
            if (playerTaskModel.getTaskType() == TaskTypeEnum.MAIN_LINE.getType())
            {
                logger.error("玩家id:{}, 协议号:{}, 任务id:{}, 当前任务类型为主线时无法领取守护者奖励!", player.getPlayerId(), receiveTaskRewardsRq.getCode(), taskBasicModel.getTaskId());
                return ErrorCodeEnum.CLIENT_PRAMS_ERROR;
            }
            List<List<Integer>> rewardList = taskBasicModel.getTaskRewardExtra();
            if (null == rewardList || rewardList.isEmpty())
            {
                logger.error("玩家id:{}, 协议号:{}, 任务id:{}, 获取任务基础配置表\"守护者奖励\"信息为空!", player.getPlayerId(), receiveTaskRewardsRq.getCode(), taskBasicModel.getTaskId());
                return ErrorCodeEnum.CONFIG_ERROR;
            }
            // 领取守护者奖励逻辑
            for (List<Integer> list : rewardList)
            {
                int type = list.get(0);// 道具类型
                int itemId = list.get(1);// 道具id
                ItemModel itemModel = itemDao.getConfigByKey(itemId);// 道具基础信息
                if (null == itemModel || type != itemModel.getItemType())
                {
                    logger.error("玩家id:{}, 协议号:{}, 道具id:{}, 获取道具基础配置表信息为空或者道具类型配置不正确!", player.getPlayerId(), receiveTaskRewardsRq.getCode(), itemModel.getItemId());
                    return ErrorCodeEnum.CONFIG_ERROR;
                }
                ItemJoinModel itemJoinModel = new ItemJoinModel();
                itemJoinModel.setItemType(type);
                itemJoinModel.setItemId(itemId);
                itemJoinModel.setItemNum(list.get(2));
                itemJoinModels.add(itemJoinModel);
            }
            // 设置领取守护者奖励状态
            playerTaskModel.setProtectorRewardStatus(1);
        }
        else
        {
            // 如果当前没有开通守护者且type=3一键领取的话则跳过
            if (receiveTaskRewardsRq.getType() != 3)
            {
                logger.error("玩家id:{}, 协议号:{}, 任务id:{}, 没有开通守护者无法领取守护者奖励!", player.getPlayerId(), receiveTaskRewardsRq.getCode(), taskBasicModel.getTaskId());
                return ErrorCodeEnum.CLIENT_PRAMS_ERROR;
            }
        }
        return ErrorCodeEnum.SUCCESS;
    }

    /**
     * 领取活跃度奖励(推送ReceiveActivityRewardsRs协议、更新背包列表UpdateItemListRs协议)
     *
     * @param player                   玩家信息
     * @param receiveActivityRewardsRq 请求协议
     */
    @Override
    public void receiveActivityRewards(Player player, ReceiveActivityRewardsRq receiveActivityRewardsRq)
    {
        if (null == receiveActivityRewardsRq || 0 == receiveActivityRewardsRq.getId())
        {
            logger.error("玩家id:{}, 协议号:{}, 领取活跃度奖励时参数为空!", player.getPlayerId(), receiveActivityRewardsRq.getCode());
            CmdUtil.sendErrorMsg(player.getSession(), receiveActivityRewardsRq.getCode(), ErrorCodeEnum.CLIENT_PRAMS_ERROR);
            return;
        }
        TaskActiveModel taskActiveModel = taskActiveDao.getConfigByKey(receiveActivityRewardsRq.getId());
        if (null == taskActiveModel)
        {
            logger.error("玩家id:{}, 活跃度宝箱自增id:{}, 获取配置表数据为空!", player.getPlayerId(), receiveActivityRewardsRq.getId());
            CmdUtil.sendErrorMsg(player.getSession(), receiveActivityRewardsRq.getCode(), ErrorCodeEnum.CONFIG_ERROR);
            return;
        }
        PlayerInfoAttachedModel playerInfoAttachedModel = this.getPlayerInfoAttached(player);
        if (null == playerInfoAttachedModel)
        {
            logger.error("玩家id:{}, 查询玩家附属信息表数据为空!", player.getPlayerId());
            CmdUtil.sendErrorMsg(player.getSession(), receiveActivityRewardsRq.getCode(), ErrorCodeEnum.DATA_ERROR);
            return;
        }
        if (StringUtils.isNotBlank(playerInfoAttachedModel.getActivityRewardList()))
        {
            //存储task_active表领取过奖励的id列表
            List<Integer> rewardList = JSONArray.parseArray(playerInfoAttachedModel.getActivityRewardList(), Integer.class);
            //判断是否已经领取过奖励
            if (rewardList.stream().anyMatch(e -> e.equals(taskActiveModel.getId())))
            {
                logger.error("玩家id:{}, 活跃度宝箱自增id:{}, 已领取过活跃度奖励!", player.getPlayerId(), receiveActivityRewardsRq.getId());
                CmdUtil.sendErrorMsg(player.getSession(), receiveActivityRewardsRq.getCode(), ErrorCodeEnum.ALREADY_RECEIVE_ACTIVITY_REWARDS);
                return;
            }
        }
        int active = 0;
        //日常
        if (taskActiveModel.getTaskType() == TaskTypeEnum.DAILY.getType())
        {
            active = playerInfoAttachedModel.getDailyActivity();
        }
        //周常
        else
        {
            active = playerInfoAttachedModel.getWeekActivity();
        }
        //判断活跃号是否满足领取奖励
        if (active < taskActiveModel.getActive())
        {
            logger.error("玩家id:{}, 当前活跃度不足以领取奖励!", player.getPlayerId());
            CmdUtil.sendErrorMsg(player.getSession(), receiveActivityRewardsRq.getCode(), ErrorCodeEnum.NOT_RECEIVE_ACTIVITY_REWARDS);
            return;
        }
        List<ItemJoinModel> itemJoinModels = new ArrayList<>();//存储更新道具
        for (List<Integer> list : taskActiveModel.getReward())
        {
            ItemJoinModel itemJoinModel = new ItemJoinModel();
            itemJoinModel.setItemType(list.get(0));
            itemJoinModel.setItemId(list.get(1));
            itemJoinModel.setItemNum(list.get(2));
            itemJoinModels.add(itemJoinModel);
        }
        ItemResponse itemResponse = itemService.batchUpdateItemPush(player, itemJoinModels, ItemOperateEnum.ACTIVITY_REWARD, ItemPromp.BULLET_FRAME);
        if (itemResponse.getCodeEnum().equals(ErrorCodeEnum.SUCCESS))
        {
            List<Integer> activityRewardList = new ArrayList<>();
            //更新活跃度奖励列表
            if (null == playerInfoAttachedModel.getActivityRewardList())
            {
                activityRewardList.add(taskActiveModel.getId());
                playerInfoAttachedModel.setActivityRewardList(JSONArray.toJSONString(activityRewardList));
            }
            else
            {
                JSONArray jsonArray = JSONArray.parseArray(playerInfoAttachedModel.getActivityRewardList());
                jsonArray.add(taskActiveModel.getId());
                playerInfoAttachedModel.setActivityRewardList(jsonArray.toJSONString());
            }
            player.cache().setPlayerInfoAttachedModel(playerInfoAttachedModel);
            playerInfoAttachedDao.queueUpdate(playerInfoAttachedModel);
            CmdUtil.sendMsg(player, new ReceiveActivityRewardsRs(1));
        }
        logger.debug("玩家id:{}, 请求协议:{}, 执行领取活跃度奖励操作完毕!", player.getPlayerId(), receiveActivityRewardsRq.toString());
    }

    /**
     * 开通守护者奖励
     *
     * @param player                玩家信息
     * @param openProtectorRewardRq 请求协议
     */
    @Override
    public void openProtector(Player player, OpenProtectorRewardRq openProtectorRewardRq)
    {
        PlayerInfoAttachedModel playerInfoAttachedModel = this.getPlayerInfoAttached(player);
        if (null == playerInfoAttachedModel)
        {
            logger.error("玩家id:{}, 查询玩家附属信息表数据为空!", player.getPlayerId());
            CmdUtil.sendErrorMsg(player.getSession(), openProtectorRewardRq.getCode(), ErrorCodeEnum.DATA_ERROR);
            return;
        }
        ConfigModel itemConfig = configDao.getConfigByKey(ConfigEnum.GUARDIANCONSUME.getConfigType());
        if (null == itemConfig || null == itemConfig.getValue())
        {
            logger.error("玩家id:{}, 从config基础配置表查询\"开通守护者奖励需要消耗的道具\"属性为空!", player.getPlayerId());
            CmdUtil.sendErrorMsg(player.getSession(), openProtectorRewardRq.getCode(), ErrorCodeEnum.CONFIG_ERROR);
            return;
        }
        ConfigModel durationConfig = configDao.getConfigByKey(ConfigEnum.GUARDIANTIME.getConfigType());
        if (null == durationConfig)
        {
            logger.error("玩家id:{}, 从config基础配置表查询\"守护者奖励持续时长\"属性为空!", player.getPlayerId());
            CmdUtil.sendErrorMsg(player.getSession(), openProtectorRewardRq.getCode(), ErrorCodeEnum.CONFIG_ERROR);
            return;
        }
        //解析消耗的道具
        ItemJoinModel itemJoinModel = new ItemJoinModel();
        try
        {
            String[] itemArr = itemConfig.getValue().split(CommonConsts.STR_COLON);
            itemJoinModel.setItemType(Integer.valueOf(itemArr[0]));
            itemJoinModel.setItemId(Integer.valueOf(itemArr[1]));
            itemJoinModel.setItemNum(-Integer.parseInt(itemArr[2]));
        }
        catch (NumberFormatException e)
        {
            logger.error("玩家id:{}, 解析\"开通守护者奖励需要消耗的道具\"属性异常!", player.getPlayerId());
            CmdUtil.sendErrorMsg(player.getSession(), openProtectorRewardRq.getCode(), ErrorCodeEnum.CONFIG_ERROR);
            return;
        }
        ItemResponse itemResponse = itemService.updateItemPush(player, itemJoinModel, ItemOperateEnum.OPEN_PROTECTOR, ItemPromp.GENERIC);
        if (itemResponse.getCodeEnum().equals(ErrorCodeEnum.SUCCESS))
        {
            LocalDateTime currentDateTime = LocalDateTime.now();//当前时间
            long minutesRemaining = 0;//剩余分钟数
            //如果没有开通守护者奖励则把状态置为开通, 激活时间设置为当前开通时间
            if (playerInfoAttachedModel.getIsProtector() == 0)
            {
                playerInfoAttachedModel.setIsProtector(1);//开通守护者奖励
                LocalDateTime activeDateTime = currentDateTime.withSecond(0);//把秒设置为0
                //守护者奖励激活时间
                playerInfoAttachedModel.setProtectorActiveTime(DateUtils.conversionDate(currentDateTime));
                //守护者奖励截止时间
                LocalDateTime lastLocalDateTime = activeDateTime.plusSeconds(Long.parseLong(durationConfig.getValue()));
                playerInfoAttachedModel.setProtectorLastTime(DateUtils.conversionDate(lastLocalDateTime));
                //计算时间差
                Duration duration = Duration.between(activeDateTime, lastLocalDateTime);
                minutesRemaining = duration.toMinutes();
            }
            //如果当前已经开通了守护者奖励则延长截止时间
            else
            {
                LocalDateTime lastLocalDateTime = DateUtils.conversionLocalDateTime(playerInfoAttachedModel.getProtectorLastTime()).plusSeconds(Long.parseLong(durationConfig.getValue()));
                playerInfoAttachedModel.setProtectorLastTime(DateUtils.conversionDate(lastLocalDateTime));
                //计算时间差
                Duration duration = Duration.between(currentDateTime, lastLocalDateTime);
                minutesRemaining = duration.toMinutes();
            }
            //组装协议
            OpenProtectorRewardRs openProtectorRewardRs = new OpenProtectorRewardRs();
            openProtectorRewardRs.setIsProtector(playerInfoAttachedModel.getIsProtector());
            openProtectorRewardRs.setProtectorRemainingTime(minutesRemaining);//设置守护者奖励剩余时间, 单位:分
            player.cache().setPlayerInfoAttachedModel(playerInfoAttachedModel);
            playerInfoAttachedDao.queueUpdate(playerInfoAttachedModel);
            CmdUtil.sendMsg(player, openProtectorRewardRs);
            logger.debug("玩家id:{}, 请求协议:{}, 执行开通守护者奖励操作完毕, 返回协议: {}", player.getPlayerId(), openProtectorRewardRq.toString(), openProtectorRewardRs.toString());
        }
    }

    /**
     * 守护者奖励过期
     *
     * @param player          玩家信息
     * @param rewardExpiredRq 请求协议
     */
    @Override
    public void protectorRewardExpired(Player player, ProtectorRewardExpiredRq rewardExpiredRq)
    {
        ProtectorRewardExpiredRs protectorRewardExpiredRs = new ProtectorRewardExpiredRs(false);
        PlayerInfoAttachedModel playerInfoAttachedModel = this.getPlayerInfoAttached(player);
        if (null == playerInfoAttachedModel)
        {
            logger.error("玩家id:{}, 查询玩家附属信息表数据为空!", player.getPlayerId());
            CmdUtil.sendErrorMsg(player.getSession(), rewardExpiredRq.getCode(), ErrorCodeEnum.DATA_ERROR);
            return;
        }
        //如果是已开通的状态下判断有没有过期
        if (playerInfoAttachedModel.getIsProtector() == 1)
        {
            LocalDateTime currentDateTime = LocalDateTime.now();//当前时间
            LocalDateTime lastLocalDateTime = DateUtils.conversionLocalDateTime(playerInfoAttachedModel.getProtectorLastTime());
            if (lastLocalDateTime.isBefore(currentDateTime))
            {
                playerInfoAttachedModel.setIsProtector(0);
                playerInfoAttachedModel.setProtectorActiveTime(null);
                playerInfoAttachedModel.setProtectorLastTime(null);
                player.cache().setPlayerInfoAttachedModel(playerInfoAttachedModel);
                playerInfoAttachedDao.queueUpdate(playerInfoAttachedModel);
                protectorRewardExpiredRs.setStatus(true);
            }
        }
        CmdUtil.sendMsg(player, protectorRewardExpiredRs);
        logger.debug("玩家id:{}, 协议号:{}, 更新守护者奖励状态完毕, 返回协议:{}", player.getPlayerId(), rewardExpiredRq.getCode(), protectorRewardExpiredRs.toString());
    }

    /**
     * 组装任务协议
     *
     * @param playerTaskModel 玩家任务信息
     * @return 任务协议
     */
    @Override
    public TaskRs taskRs(PlayerTaskModel playerTaskModel)
    {
        TaskRs taskRs = new TaskRs();
        taskRs.setTaskId(playerTaskModel.getTaskId());
        taskRs.setTaskType(playerTaskModel.getTaskType());
        taskRs.setTaskStatus(playerTaskModel.getTaskStatus());
        taskRs.setTaskSchedule(playerTaskModel.getTaskSchedule());
        taskRs.setTaskRewardStatus(playerTaskModel.getTaskRewardStatus());
        taskRs.setProtectorRewardStatus(playerTaskModel.getProtectorRewardStatus());
        return taskRs;
    }

    /**
     * 获取玩家附属信息
     * @param player 玩家信息
     * @return PlayerInfoAttachedModel
     */
    @Override
    public PlayerInfoAttachedModel getPlayerInfoAttached(Player player)
    {
        PlayerInfoAttachedModel playerInfoAttachedModel = player.cache().getPlayerInfoAttachedModel();
        if (null == playerInfoAttachedModel)
        {
            playerInfoAttachedModel = playerInfoAttachedDao.queryByPrimaykey(player.getPlayerId());
            if (null == playerInfoAttachedModel)
            {
                playerInfoAttachedModel = new PlayerInfoAttachedModel();
                playerInfoAttachedModel.setPlayerId(player.getPlayerId());
                playerInfoAttachedModel.setDailyActivity(0);
                playerInfoAttachedModel.setWeekActivity(0);
                playerInfoAttachedModel.setIsProtector(0);
                playerInfoAttachedDao.queueInsert(playerInfoAttachedModel);
            }
            player.cache().setPlayerInfoAttachedModel(playerInfoAttachedModel);
        }
        return playerInfoAttachedModel;
    }
}