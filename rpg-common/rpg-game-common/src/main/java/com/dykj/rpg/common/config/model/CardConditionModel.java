package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

import java.util.List;

/**
 * @Description 条件表
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/04/02
*/
public class CardConditionModel extends BaseConfig<Integer>
{
    private Integer correctConditionId;//条件id
    private Integer cardPool;//卡池判定
    private List<List<Integer>> conditionType;//条件类型
    private Integer conditionParentType;//判定大类
    private List<Integer> conditionSubclass;//判定子类

    public Integer getCorrectConditionId()
    {
        return this.correctConditionId;
    }

    public void setCorrectConditionId(Integer correctConditionId)
    {
        this.correctConditionId = correctConditionId;
    }

    public Integer getCardPool()
    {
        return this.cardPool;
    }

    public void setCardPool(Integer cardPool)
    {
        this.cardPool = cardPool;
    }

    public List<List<Integer>> getConditionType()
    {
        return this.conditionType;
    }

    public void setConditionType(List<List<Integer>> conditionType)
    {
        this.conditionType = conditionType;
    }

    public Integer getConditionParentType()
    {
        return this.conditionParentType;
    }

    public void setConditionParentType(Integer conditionParentType)
    {
        this.conditionParentType = conditionParentType;
    }

    public List<Integer> getConditionSubclass()
    {
        return this.conditionSubclass;
    }

    public void setConditionSubclass(List<Integer> conditionSubclass)
    {
        this.conditionSubclass = conditionSubclass;
    }

    @Override
    public Integer getKey()
    {
        return this.correctConditionId;
    }
}