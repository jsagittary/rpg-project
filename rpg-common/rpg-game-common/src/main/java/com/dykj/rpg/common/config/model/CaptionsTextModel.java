package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

/**
 * @Description 文字信息
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/02/04
*/
public class CaptionsTextModel extends BaseConfig<Integer>
{
    private Integer textId;//名字编号
    private Integer textType;//显示方式

    public Integer getTextId()
    {
        return this.textId;
    }

    public void setTextId(Integer textId)
    {
        this.textId = textId;
    }

    public Integer getTextType()
    {
        return this.textType;
    }

    public void setTextType(Integer textType)
    {
        this.textType = textType;
    }

    @Override
    public Integer getKey()
    {
        return this.textId;
    }
}