package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

import java.util.List;

/**
 * @Description ai_basic
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/05/08
 */
public class AiCharacterBasicModel extends BaseConfig<Integer>
{
	private Integer aiId;//技巧ID
	private Integer aiCd;//技巧CD时间
	private Integer aiTrigger;//技巧触发类型
	private Integer aiCondition;//技巧条件类型
	private List<Integer> aiConditionParams;//技巧条件类型参数
	private List<Integer> aiConditionRange;//条件自定义技巧范围
	private Integer aiAction;//技巧行为类型
	private List<Integer> aiActionParams;//技巧行为类型参数
	private List<Integer> aiActionRange;//行为自定义技巧范围
	private String aiDiyDesc;//自定义描述

	public Integer getAiId()
	{
		return this.aiId;
	}

	public void setAiId(Integer aiId)
	{
		this.aiId = aiId;
	}

	public Integer getAiCd()
	{
		return this.aiCd;
	}

	public void setAiCd(Integer aiCd)
	{
		this.aiCd = aiCd;
	}

	public Integer getAiTrigger()
	{
		return this.aiTrigger;
	}

	public void setAiTrigger(Integer aiTrigger)
	{
		this.aiTrigger = aiTrigger;
	}

	public Integer getAiCondition()
	{
		return this.aiCondition;
	}

	public void setAiCondition(Integer aiCondition)
	{
		this.aiCondition = aiCondition;
	}

	public List<Integer> getAiConditionParams()
	{
		return this.aiConditionParams;
	}

	public void setAiConditionParams(List<Integer> aiConditionParams)
	{
		this.aiConditionParams = aiConditionParams;
	}

	public List<Integer> getAiConditionRange()
	{
		return this.aiConditionRange;
	}

	public void setAiConditionRange(List<Integer> aiConditionRange)
	{
		this.aiConditionRange = aiConditionRange;
	}

	public Integer getAiAction()
	{
		return this.aiAction;
	}

	public void setAiAction(Integer aiAction)
	{
		this.aiAction = aiAction;
	}

	public List<Integer> getAiActionParams()
	{
		return this.aiActionParams;
	}

	public void setAiActionParams(List<Integer> aiActionParams)
	{
		this.aiActionParams = aiActionParams;
	}

	public List<Integer> getAiActionRange()
	{
		return this.aiActionRange;
	}

	public void setAiActionRange(List<Integer> aiActionRange)
	{
		this.aiActionRange = aiActionRange;
	}

	public String getAiDiyDesc()
	{
		return this.aiDiyDesc;
	}

	public void setAiDiyDesc(String aiDiyDesc)
	{
		this.aiDiyDesc = aiDiyDesc;
	}

	@Override
	public Integer getKey()
	{
		return this.aiId;
	}
}