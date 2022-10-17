package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

/**
 * @Description 随机名字
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/02/04
*/
public class RandomNameTextModel extends BaseConfig<Integer>
{
    private Integer id;//名字编号
    private String nameListFirst;//名字列表
    private String nameListSecond;//名字列表
    private String nameListThird;//名字列表

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getNameListFirst()
    {
        return this.nameListFirst;
    }

    public void setNameListFirst(String nameListFirst)
    {
        this.nameListFirst = nameListFirst;
    }

    public String getNameListSecond()
    {
        return this.nameListSecond;
    }

    public void setNameListSecond(String nameListSecond)
    {
        this.nameListSecond = nameListSecond;
    }

    public String getNameListThird()
    {
        return this.nameListThird;
    }

    public void setNameListThird(String nameListThird)
    {
        this.nameListThird = nameListThird;
    }

    @Override
    public Integer getKey()
    {
        return this.id;
    }
}