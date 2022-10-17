package com.dykj.rpg.common.data.model;

import com.dykj.rpg.db.annotation.Column;
import com.dykj.rpg.db.annotation.PrimaryKey;
import com.dykj.rpg.db.data.BaseModel;

import java.util.Date;

/**
 * @Author: lyc
 * @Date: 2021/3/4
 * @Description: 玩家任务类
 */
public class PlayerTaskModel extends BaseModel {
	/**
	 * 玩家id
	 */
	@Column(primaryKey = PrimaryKey.GENERAL)
	private int playerId;

	/**
	 * 任务id
	 */
	@Column(primaryKey = PrimaryKey.GENERAL)
	private int taskId;

	/**
	 * 任务类型(1=主线 2=日常 3=周常
	 */
	private int taskType;

	/**
	 * 任务状态(1-未完成 2-已完成)
	 */
	private int taskStatus;

	/**
	 * 任务进度
	 */
	private int taskSchedule;

	/**
	 * 任务激活时间
	 */
	private Date taskActivationTime;

	/**
	 * 任务奖励领取状态(0-未领取, 1-已领取)
	 */
	private int taskRewardStatus;

	/**
	 * 守护者奖励领取状态(0-未领取, 1-已领取)
	 */
	private int protectorRewardStatus;

	/**
	 * 任务最后一次刷新时间
	 */
	private Date lastRefreshTime;

	/**
	 * 触发状态(0-未触发, 1-已触发)
	 */
	private int triggerStatus;

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public int getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(int taskStatus) {
		this.taskStatus = taskStatus;
	}

	public int getTaskSchedule() {
		return taskSchedule;
	}

	public void setTaskSchedule(int taskSchedule) {
		this.taskSchedule = taskSchedule;
	}

	public Date getTaskActivationTime() {
		return taskActivationTime;
	}

	public void setTaskActivationTime(Date taskActivationTime) {
		this.taskActivationTime = taskActivationTime;
	}

	public int getTaskRewardStatus() {
		return taskRewardStatus;
	}

	public void setTaskRewardStatus(int taskRewardStatus) {
		this.taskRewardStatus = taskRewardStatus;
	}

	public int getProtectorRewardStatus() {
		return protectorRewardStatus;
	}

	public void setProtectorRewardStatus(int protectorRewardStatus) {
		this.protectorRewardStatus = protectorRewardStatus;
	}

	public Date getLastRefreshTime() {
		return lastRefreshTime;
	}

	public void setLastRefreshTime(Date lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}

	public int getTriggerStatus() {
		return triggerStatus;
	}

	public void setTriggerStatus(int triggerStatus) {
		this.triggerStatus = triggerStatus;
	}

	public long primaryKey() {
		return playerId;
	}

	public Long primary2Key() {
		return (long) taskId;
	}

	@Override
	public String toString() {
		return "PlayerTaskModel{" + "playerId=" + playerId + ", taskId=" + taskId + ", taskType=" + taskType
				+ ", taskStatus=" + taskStatus + ", taskSchedule=" + taskSchedule + ", taskActivationTime="
				+ taskActivationTime + ", taskRewardStatus=" + taskRewardStatus + ", protectorRewardStatus="
				+ protectorRewardStatus + ", lastRefreshTime=" + lastRefreshTime + ", triggerStatus=" + triggerStatus
				+ '}';
	}
}
