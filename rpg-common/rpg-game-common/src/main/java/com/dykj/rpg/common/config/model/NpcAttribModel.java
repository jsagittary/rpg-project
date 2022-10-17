package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

import java.util.List;

/**
 * @Description NPC属性
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/02/04
*/
public class NpcAttribModel extends BaseConfig<Integer>
{
    private Integer npcAttribId;//编号
    private Integer growClass;//属性职业
    private List<Integer> npcLv;//等级区间
    private List<String> mainAttrib;//主要属性
    private List<String> generalAttrib;//一般属性

    public Integer getNpcAttribId()
    {
        return this.npcAttribId;
    }

    public void setNpcAttribId(Integer npcAttribId)
    {
        this.npcAttribId = npcAttribId;
    }

    public Integer getGrowClass()
    {
        return this.growClass;
    }

    public void setGrowClass(Integer growClass)
    {
        this.growClass = growClass;
    }

    public List<Integer> getNpcLv()
    {
        return this.npcLv;
    }

    public void setNpcLv(List<Integer> npcLv)
    {
        this.npcLv = npcLv;
    }

    public List<String> getMainAttrib()
    {
        return this.mainAttrib;
    }

    public void setMainAttrib(List<String> mainAttrib)
    {
        this.mainAttrib = mainAttrib;
    }

    public List<String> getGeneralAttrib()
    {
        return this.generalAttrib;
    }

    public void setGeneralAttrib(List<String> generalAttrib)
    {
        this.generalAttrib = generalAttrib;
    }

    @Override
    public Integer getKey()
    {
        return this.npcAttribId;
    }
}