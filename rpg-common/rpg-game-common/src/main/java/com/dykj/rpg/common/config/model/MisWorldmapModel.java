package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

/**
 * @Description 关卡章节
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/02/19
*/
public class MisWorldmapModel extends BaseConfig<Integer>
{
    private Integer id;//序号
    private Integer theme;//主题

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getTheme()
    {
        return this.theme;
    }

    public void setTheme(Integer theme)
    {
        this.theme = theme;
    }

    @Override
    public Integer getKey()
    {
        return this.id;
    }
}