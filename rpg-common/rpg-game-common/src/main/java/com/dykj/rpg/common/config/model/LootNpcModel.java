package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

import java.util.List;

/**
 * @Description NPC掉落
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/02/04
*/
public class LootNpcModel extends BaseConfig<Integer>
{
    private Integer id;//编号
    private Integer npcId;//NPC编号
    private List<Integer> npcLv;//NPC等级段
    private List<List<Integer>> droplist;//掉落组编号 和 概率

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getNpcId()
    {
        return this.npcId;
    }

    public void setNpcId(Integer npcId)
    {
        this.npcId = npcId;
    }

    public List<Integer> getNpcLv()
    {
        return this.npcLv;
    }

    public void setNpcLv(List<Integer> npcLv)
    {
        this.npcLv = npcLv;
    }

    public List<List<Integer>> getDroplist()
    {
        return this.droplist;
    }

    public void setDroplist(List<List<Integer>> droplist)
    {
        this.droplist = droplist;
    }

    @Override
    public Integer getKey()
    {
        return this.id;
    }
}