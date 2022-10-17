package com.dykj.rpg.common.data.model;

import com.dykj.rpg.db.annotation.Column;
import com.dykj.rpg.db.annotation.PrimaryKey;
import com.dykj.rpg.db.data.BaseModel;

import java.util.Date;

/**
 * @author jyb
 * @date 2020/11/9 16:37
 * @Description
 * 技能表
 */
public class PlayerSkillModel  extends BaseModel
{
	/**
	 * 玩家id
	 */
	@Column(primaryKey = PrimaryKey.GENERAL)
	private int playerId;

	/**
	 * 技能id
	 */
	@Column(primaryKey = PrimaryKey.GENERAL)
	private  int skillId;

	/**
	 * 技能等级
	 */
	private  int skillLevel;

	/***
	 * 技能经验
	 */
	private int skillExp ;

	/**
	 * 位置 0表示未使用 1~n为技能槽
	 */
	private int position;

	/**
	 * 1-普通技能, 2-魂技
	 */
	private int skillType;

	/**
	 * 魂技培养转化起始时间
	 */
	private Date soulChangeTime;

	/**
	 * 灵魂之影转化时的面板的位置
	 */
	private  int soulChangePos;

	/**
	 * 技能星级
	 */
	private int skillStarLevel;

	public int getPlayerId()
	{
		return playerId;
	}

	public void setPlayerId(int playerId)
	{
		this.playerId = playerId;
	}

	public int getSkillId()
	{
		return skillId;
	}

	public void setSkillId(int skillId)
	{
		this.skillId = skillId;
	}

	public int getSkillLevel()
	{
		return skillLevel;
	}

	public void setSkillLevel(int skillLevel)
	{
		this.skillLevel = skillLevel;
	}

	public int getSkillExp()
	{
		return skillExp;
	}

	public void setSkillExp(int skillExp)
	{
		this.skillExp = skillExp;
	}

	public int getPosition()
	{
		return position;
	}

	public void setPosition(int position)
	{
		this.position = position;
	}

	public int getSkillType()
	{
		return skillType;
	}

	public void setSkillType(int skillType)
	{
		this.skillType = skillType;
	}

	public Date getSoulChangeTime() {
		return soulChangeTime;
	}

	public void setSoulChangeTime(Date soulChangeTime) {
		this.soulChangeTime = soulChangeTime;
	}

	public int getSkillStarLevel()
	{
		return skillStarLevel;
	}

	public void setSkillStarLevel(int skillStarLevel)
	{
		this.skillStarLevel = skillStarLevel;
	}

	public int getSoulChangePos() {
		return soulChangePos;
	}

	public void setSoulChangePos(int soulChangePos) {
		this.soulChangePos = soulChangePos;
	}
}