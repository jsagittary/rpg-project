package com.dykj.rpg.common.data.model;

import com.dykj.rpg.db.annotation.Column;
import com.dykj.rpg.db.annotation.PrimaryKey;
import com.dykj.rpg.db.data.BaseModel;
import com.dykj.rpg.util.JsonUtil;

import java.util.Date;
import java.util.List;

/**
 * @author CaoBing
 * @date 2021年4月20日
 * @Description:
 */
public class PlayerMailModel extends BaseModel {
	/**
	 * 玩家ID
	 */
	private long playerId;
	/**
	 * 实例ID
	 */
	@Column(primaryKey = PrimaryKey.GENERAL)
	private long instanceId;
	/**
	 * 邮件ID
	 */
	private int mailId;
	/**
	 * 标题参数
	 */
	private String titleParam;
	/**
	 * 内容参数
	 */
	private String contentParam;
	/**
	 * 附件(奖励物品)
	 */
	private String awards;
	/**
	 * 是否有奖励(0:没有附件 1.有附件)
	 */
	private int isAward;
	/**
	 * 是否已读(0:未读取 1.已读取)
	 */
	private int isRead;
	/**
	 * 是否已领奖(0:未领取取 1.已领取)
	 */
	private int isReceive;
	/**
	 * 创建时间
	 */
	private Date createTime;

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(long instanceId) {
		this.instanceId = instanceId;
	}

	public int getMailId() {
		return mailId;
	}

	public void setMailId(int mailId) {
		this.mailId = mailId;
	}

	public String getTitleParam() {
		return titleParam;
	}

	public void setTitleParam(String titleParam) {
		this.titleParam = titleParam;
	}

	public String getContentParam() {
		return contentParam;
	}

	public void setContentParam(String contentParam) {
		this.contentParam = contentParam;
	}

	public String getAwards() {
		return awards;
	}

	public void setAwards(String awards) {
		this.awards = awards;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public List<String> getTitleParamLists() {
		List<String> titleParmaList = null;
		if (titleParam != null && !titleParam.equals("")) {
			try {
				titleParmaList = JsonUtil.toList(titleParam, String.class);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return titleParmaList;
	}

	public List<String> getContentParamLists() {
		List<String> contentParamList = null;
		if (contentParam != null && !contentParam.equals("")) {
			try {
				contentParamList = JsonUtil.toList(contentParam, String.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return contentParamList;
	}

	public int getIsAward() {
		return isAward;
	}

	public void setIsAward(int isAward) {
		this.isAward = isAward;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	public int getIsReceive() {
		return isReceive;
	}

	public void setIsReceive(int isReceive) {
		this.isReceive = isReceive;
	}

	@Override
	public String toString() {
		return "PlayerMailModel [playerId=" + playerId + ", instanceId=" + instanceId + ", mailId=" + mailId
				+ ", titleParam=" + titleParam + ", contentParam=" + contentParam + ", awards=" + awards
				+ ", createTime=" + createTime + "]";
	}
}
