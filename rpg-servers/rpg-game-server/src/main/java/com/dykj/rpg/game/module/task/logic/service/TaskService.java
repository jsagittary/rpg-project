package com.dykj.rpg.game.module.task.logic.service;

import com.dykj.rpg.common.data.model.PlayerInfoAttachedModel;
import com.dykj.rpg.common.data.model.PlayerTaskModel;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.game.module.task.consts.TaskOpenConditionEnum;
import com.dykj.rpg.game.module.task.logic.response.TaskConditions;
import com.dykj.rpg.game.module.task.logic.response.TaskResponse;
import com.dykj.rpg.protocol.task.*;

import java.util.List;

/**
 * @Description 任务系统接口
 * @Author liuyc
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/4
 */
public interface TaskService
{

    /**
     * 获得指定的任务信息
     * @param player 玩家信息
     * @param taskId 任务id
     * @return 任务信息
     */
    public PlayerTaskModel getTaskfo(Player player, Integer taskId);

    /**
     * 根据任务类型、触发类型为1(已触发状态)的任务列表
     * @param taskType 任务类型数组
     * @return 任务列表
     */
    public List<PlayerTaskModel> getPlayerTaskList(Player player, Integer... taskType);

    /**
     * 打开界面加载任务数据
     * @param player 玩家信息
     */
    public void loadTaskData(Player player, TaskListRq taskListRq);

    /**
     * 任务触发(0-无条件开放  1-等级 2-关卡进度 3-前置任务ID)(入库及生成TaskTriggerListRs协议并推送)
     * @param player 玩家信息
     * @param taskOpenConditionEnum 任务触发类型
     */
    public TaskResponse triggerTaskPush(Player player, TaskOpenConditionEnum... taskOpenConditionEnum);

    /**
     * 执行任务进度刷新(入库及生成UpdateTaskListRs协议)
     * @param player 玩家信息
     * @param conditionsModels 参数
     */
    public void taskFinishRealize(Player player, TaskConditions... conditionsModels);

    /**
     * 任务进度刷新(入库及生成UpdateTaskListRs协议)
     * @param player 玩家信息
     * @param conditionsModelList 任务完成条件参数集
     */
    public TaskResponse taskFinishRealize(Player player, List<TaskConditions> conditionsModelList);

    /**
     * 领取任务奖励(推送ReceiveTaskRewardsRs协议、更新背包列表UpdateItemListRs协议, 如果奖励包含活跃度则更新活跃度推送UpdateActivityRs协议)
     * @param player 玩家信息
     * @param receiveTaskRewardsRq 请求协议
     */
    public void receiveTaskRewards(Player player, ReceiveTaskRewardsRq receiveTaskRewardsRq);

    /**
     * 领取活跃度奖励(推送ReceiveActivityRewardsRs协议、更新背包列表UpdateItemListRs协议)
     * @param player 玩家信息
     * @param receiveActivityRewardsRq 请求协议
     */
    public void receiveActivityRewards(Player player, ReceiveActivityRewardsRq receiveActivityRewardsRq);

    /**
     * 开通守护者奖励
     * @param player 玩家信息
     * @param openProtectorRewardRq 请求协议
     */
    public void openProtector(Player player, OpenProtectorRewardRq openProtectorRewardRq);

    /**
     * 守护者奖励过期
     * @param player 玩家信息
     * @param rewardExpiredRq 请求协议
     */
    public void protectorRewardExpired(Player player, ProtectorRewardExpiredRq rewardExpiredRq);

    /**
     * 组装任务协议
     * @param playerTaskModel 玩家任务信息
     * @return 任务协议
     */
    public TaskRs taskRs(PlayerTaskModel playerTaskModel);

    /**
     * 获取玩家附属信息
     * @param player 玩家信息
     * @return PlayerInfoAttachedModel
     */
    public PlayerInfoAttachedModel getPlayerInfoAttached(Player player);
}
