package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

import java.util.List;

/**
 * @Description 按钮
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/04/02
*/
public class CardButtonModel extends BaseConfig<Integer>
{
    private Integer cardButtonId;//按钮
    private List<List<Integer>> buttonConsume;//消耗道具ID：数量
    private String refreshTime;//刷新时间
    private Integer times;//抽取次数
    private List<List<Integer>> droplist;//道具集合
    private List<List<Integer>> correctConditions;//修正条件
    private List<List<Integer>> correctResult;//修正结果

    public Integer getCardButtonId()
    {
        return this.cardButtonId;
    }

    public void setCardButtonId(Integer cardButtonId)
    {
        this.cardButtonId = cardButtonId;
    }

    public List<List<Integer>> getButtonConsume()
    {
        return this.buttonConsume;
    }

    public void setButtonConsume(List<List<Integer>> buttonConsume)
    {
        this.buttonConsume = buttonConsume;
    }

    public String getRefreshTime()
    {
        return this.refreshTime;
    }

    public void setRefreshTime(String refreshTime)
    {
        this.refreshTime = refreshTime;
    }

    public Integer getTimes()
    {
        return this.times;
    }

    public void setTimes(Integer times)
    {
        this.times = times;
    }

    public List<List<Integer>> getDroplist()
    {
        return this.droplist;
    }

    public void setDroplist(List<List<Integer>> droplist)
    {
        this.droplist = droplist;
    }

    public List<List<Integer>> getCorrectConditions()
    {
        return this.correctConditions;
    }

    public void setCorrectConditions(List<List<Integer>> correctConditions)
    {
        this.correctConditions = correctConditions;
    }

    public List<List<Integer>> getCorrectResult()
    {
        return this.correctResult;
    }

    public void setCorrectResult(List<List<Integer>> correctResult)
    {
        this.correctResult = correctResult;
    }

    @Override
    public Integer getKey()
    {
        return this.cardButtonId;
    }
}