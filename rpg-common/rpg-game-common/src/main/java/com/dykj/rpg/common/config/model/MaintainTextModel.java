package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

/**
 * @Description 服务器维护信息
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/15
*/
public class MaintainTextModel extends BaseConfig<Integer>
{
    private Integer textId;//名字编号
    private String textContent;//名字列表

    public Integer getTextId()
    {
        return this.textId;
    }

    public void setTextId(Integer textId)
    {
        this.textId = textId;
    }

    public String getTextContent()
    {
        return this.textContent;
    }

    public void setTextContent(String textContent)
    {
        this.textContent = textContent;
    }

    @Override
    public Integer getKey()
    {
        return this.textId;
    }
}