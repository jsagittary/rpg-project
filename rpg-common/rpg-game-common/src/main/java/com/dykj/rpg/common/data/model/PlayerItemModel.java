package com.dykj.rpg.common.data.model;

import com.dykj.rpg.db.annotation.Column;
import com.dykj.rpg.db.annotation.PrimaryKey;
import com.dykj.rpg.db.data.BaseModel;

import java.util.Date;

/**
 * @Author: jyb
 * @Date: 2020/9/29 14:42
 * @Description:
 */
public class PlayerItemModel extends BaseModel
{
	/**
	 * 玩家id
	 */
	@Column(primaryKey = PrimaryKey.GENERAL)
	private int playerId;

	/**
	 * 道具id
	 */
	@Column(primaryKey = PrimaryKey.GENERAL)
	private int itemId;

	/**
	 * 道具类型
	 */
	private int itemType;

	/**
	 * 道具子类型
	 */
	private int itemTypeDetail;

	/**
	 * 物品数量
	 */
	private int itemNum;

	/**
	 * 道具时限类型(1-永久 2-临时)
	 */
	private int liveType;

	/**
	 * 道具开始持续时间
	 */
	private Date liveTime;

	/**
	 * 道具上锁(1-不可上锁 2-可上锁)
	 */
	private int itemLock;

	/**
	 * 道具每日获取数量
	 */
	private int itemDailyNum;

	/**
	 * 道具获取时间
	 */
	private Date itemGetTime;

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getItemType() {
		return itemType;
	}

	public void setItemType(int itemType) {
		this.itemType = itemType;
	}

	public int getItemTypeDetail() {
		return itemTypeDetail;
	}

	public void setItemTypeDetail(int itemTypeDetail) {
		this.itemTypeDetail = itemTypeDetail;
	}

	public int getItemNum() {
		return itemNum;
	}

	public void setItemNum(int itemNum) {
		this.itemNum = itemNum;
	}

	public int getLiveType() {
		return liveType;
	}

	public void setLiveType(int liveType) {
		this.liveType = liveType;
	}

	public Date getLiveTime() {
		return liveTime;
	}

	public void setLiveTime(Date liveTime) {
		this.liveTime = liveTime;
	}

	public int getItemLock() {
		return itemLock;
	}

	public void setItemLock(int itemLock) {
		this.itemLock = itemLock;
	}

	public int getItemDailyNum() {
		return itemDailyNum;
	}

	public void setItemDailyNum(int itemDailyNum) {
		this.itemDailyNum = itemDailyNum;
	}

	public Date getItemGetTime() {
		return itemGetTime;
	}

	public void setItemGetTime(Date itemGetTime) {
		this.itemGetTime = itemGetTime;
	}

	public long primaryKey() {
		return playerId;
	}

	public Long primary2Key() {
		return (long) itemId;
	}

	public PlayerItemModel(int playerId) {
		this.playerId = playerId;
	}

	public PlayerItemModel() {
	}

	public PlayerItemModel copy()
	{
		PlayerItemModel playerItemModel = new PlayerItemModel();
		playerItemModel.setPlayerId(playerId);
		playerItemModel.setItemId(itemId);
		playerItemModel.setItemType(itemType);
		playerItemModel.setItemTypeDetail(itemTypeDetail);
		playerItemModel.setItemNum(itemNum);
		playerItemModel.setLiveType(liveType);
		playerItemModel.setLiveTime(liveTime);
		playerItemModel.setItemLock(itemLock);
		playerItemModel.setItemDailyNum(itemDailyNum);
		playerItemModel.setItemGetTime(itemGetTime);
		return playerItemModel;
	}

	@Override
	public String toString() {
		return "PlayerItemModel{" + "playerId=" + playerId + ", itemId=" + itemId + ", itemNum=" + itemNum + '}';
	}
}
