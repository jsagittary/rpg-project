package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

/**
 * @Description 关卡通关条件
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/03/10
*/
public class MisConditionModel extends BaseConfig<Integer>
{
    private Integer conditionType;//通关成功条件类型

    public Integer getConditionType()
    {
        return this.conditionType;
    }

    public void setConditionType(Integer conditionType)
    {
        this.conditionType = conditionType;
    }

    @Override
    public Integer getKey()
    {
        return this.conditionType;
    }
}