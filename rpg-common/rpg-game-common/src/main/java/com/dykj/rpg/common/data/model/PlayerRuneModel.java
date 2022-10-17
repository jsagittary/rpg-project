package com.dykj.rpg.common.data.model;

import com.dykj.rpg.db.annotation.Column;
import com.dykj.rpg.db.annotation.PrimaryKey;
import com.dykj.rpg.db.data.BaseModel;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Author: lyc
 * @Date: 2021/5/20
 * @Description: 玩家符文
 */
public class PlayerRuneModel extends BaseModel
{
	/**
	 * 玩家id
	 */
	@Column(primaryKey = PrimaryKey.GENERAL)
	private Integer playerId;

	/**
	 * 符文id
	 */
	@Column(primaryKey = PrimaryKey.GENERAL)
	private Integer runeId;

	/**
	 * 技能id
	 */
	private Integer skillId;

	/**
	 * 符文位置
	 */
	private Integer runePos;

	public Integer getPlayerId()
	{
		return playerId;
	}

	public void setPlayerId(Integer playerId)
	{
		this.playerId = playerId;
	}

	public Integer getRuneId()
	{
		return runeId;
	}

	public void setRuneId(Integer runeId)
	{
		this.runeId = runeId;
	}

	public Integer getSkillId()
	{
		return skillId;
	}

	public void setSkillId(Integer skillId)
	{
		this.skillId = skillId;
	}

	public Integer getRunePos()
	{
		return runePos;
	}

	public void setRunePos(Integer runePos)
	{
		this.runePos = runePos;
	}

	public long primaryKey() {
		return playerId;
	}

	public Long primary2Key() {
		return runeId.longValue();
	}

	@Override
	public String toString()
	{
		return "PlayerRuneModel{" + "playerId=" + playerId + ", runeId=" + runeId + ", skillId=" + skillId + ", runePos=" + runePos + '}';
	}
}
