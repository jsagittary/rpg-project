package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

import java.util.List;

/**
 * @Description 掉落组
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/02/04
*/
public class LootDropteamModel extends BaseConfig<Integer>
{
    private Integer id;//编号
    private Integer dropteamId;//掉落组编号
    private List<Integer> dropteamCharacter;//激活职业
    private Integer dropType;//掉落类型
    private List<Integer> dropteamNumber;//掉落组数量
    private List<List<Integer>> dropThing;//掉落组内容

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getDropteamId()
    {
        return this.dropteamId;
    }

    public void setDropteamId(Integer dropteamId)
    {
        this.dropteamId = dropteamId;
    }

    public List<Integer> getDropteamCharacter()
    {
        return this.dropteamCharacter;
    }

    public void setDropteamCharacter(List<Integer> dropteamCharacter)
    {
        this.dropteamCharacter = dropteamCharacter;
    }

    public Integer getDropType()
    {
        return this.dropType;
    }

    public void setDropType(Integer dropType)
    {
        this.dropType = dropType;
    }

    public List<Integer> getDropteamNumber()
    {
        return this.dropteamNumber;
    }

    public void setDropteamNumber(List<Integer> dropteamNumber)
    {
        this.dropteamNumber = dropteamNumber;
    }

    public List<List<Integer>> getDropThing()
    {
        return this.dropThing;
    }

    public void setDropThing(List<List<Integer>> dropThing)
    {
        this.dropThing = dropThing;
    }

    @Override
    public Integer getKey()
    {
        return this.id;
    }
}