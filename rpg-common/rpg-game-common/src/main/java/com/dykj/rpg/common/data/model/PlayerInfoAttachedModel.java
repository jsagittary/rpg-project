package com.dykj.rpg.common.data.model;

import com.dykj.rpg.db.annotation.Column;
import com.dykj.rpg.db.annotation.PrimaryKey;
import com.dykj.rpg.db.data.BaseModel;

import java.util.Date;

/**
 * @Author: lyc
 * @Date: 2021/3/4
 * @Description: 玩家数据附属类
 */
public class PlayerInfoAttachedModel extends BaseModel {
	/**
	 * 玩家id
	 */
	@Column(primaryKey = PrimaryKey.GENERAL)
	private int playerId;

	/**
	 * 日活跃度
	 */
	private int dailyActivity;

	/**
	 * 周活跃度
	 */
	private int weekActivity;

	/**
	 * 活跃度奖励列表
	 */
	private String activityRewardList;

	/**
	 * 是否已开通守护者(0-否 1-是)
	 */
	private int isProtector;

	/**
	 * 守护者奖励激活时间
	 */
	private Date protectorActiveTime;

	/**
	 * 守护者奖励截止时间
	 */
	private Date protectorLastTime;

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getDailyActivity() {
		return dailyActivity;
	}

	public void setDailyActivity(int dailyActivity) {
		this.dailyActivity = dailyActivity;
	}

	public int getWeekActivity() {
		return weekActivity;
	}

	public void setWeekActivity(int weekActivity) {
		this.weekActivity = weekActivity;
	}

	public String getActivityRewardList() {
		return activityRewardList;
	}

	public void setActivityRewardList(String activityRewardList) {
		this.activityRewardList = activityRewardList;
	}

	public int getIsProtector() {
		return isProtector;
	}

	public void setIsProtector(int isProtector) {
		this.isProtector = isProtector;
	}

	public Date getProtectorActiveTime() {
		return protectorActiveTime;
	}

	public void setProtectorActiveTime(Date protectorActiveTime) {
		this.protectorActiveTime = protectorActiveTime;
	}

	public Date getProtectorLastTime() {
		return protectorLastTime;
	}

	public void setProtectorLastTime(Date protectorLastTime) {
		this.protectorLastTime = protectorLastTime;
	}

	public long cachePrimaryKey() {
		return playerId;
	}

	@Override
	public String toString() {
		return "PlayerInfoAttachedModel{" + "playerId=" + playerId + ", dailyActivity=" + dailyActivity
				+ ", weekActivity=" + weekActivity + ", activityRewardList='" + activityRewardList + '\''
				+ ", isProtector=" + isProtector + ", protectorActiveTime=" + protectorActiveTime
				+ ", protectorLastTime=" + protectorLastTime + '}';
	}
}
