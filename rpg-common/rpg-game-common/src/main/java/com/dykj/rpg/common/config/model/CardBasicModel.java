package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

import java.util.List;

/**
 * @Description 卡池
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/04/02
*/
public class CardBasicModel extends BaseConfig<Integer>
{
    private Integer cardId;//ID
    private String cardName;//卡池显示名
    private List<List<Integer>> cardConditions;//开放条件
    private List<Integer> cardButtonId;//按钮ID

    public Integer getCardId()
    {
        return this.cardId;
    }

    public void setCardId(Integer cardId)
    {
        this.cardId = cardId;
    }

    public String getCardName()
    {
        return this.cardName;
    }

    public void setCardName(String cardName)
    {
        this.cardName = cardName;
    }

    public List<List<Integer>> getCardConditions()
    {
        return this.cardConditions;
    }

    public void setCardConditions(List<List<Integer>> cardConditions)
    {
        this.cardConditions = cardConditions;
    }

    public List<Integer> getCardButtonId()
    {
        return this.cardButtonId;
    }

    public void setCardButtonId(List<Integer> cardButtonId)
    {
        this.cardButtonId = cardButtonId;
    }

    @Override
    public Integer getKey()
    {
        return this.cardId;
    }
}