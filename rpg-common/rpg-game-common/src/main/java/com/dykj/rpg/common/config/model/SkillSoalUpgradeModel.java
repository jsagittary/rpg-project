package com.dykj.rpg.common.config.model;

import com.dykj.rpg.db.dao.BaseConfig;

/**
 * @Description VIP相关信息
 * @Author
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/03/02
*/
public class SkillSoalUpgradeModel extends BaseConfig<Integer>
{
    private Integer id;//id
    private Integer posIdx;//开放栏位编号
    private String textPos;//文本内容
    private Integer vip;//vip开启Vip条件

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getPosIdx()
    {
        return this.posIdx;
    }

    public void setPosIdx(Integer posIdx)
    {
        this.posIdx = posIdx;
    }

    public String getTextPos()
    {
        return this.textPos;
    }

    public void setTextPos(String textPos)
    {
        this.textPos = textPos;
    }

    public Integer getVip()
    {
        return this.vip;
    }

    public void setVip(Integer vip)
    {
        this.vip = vip;
    }

    @Override
    public Integer getKey()
    {
        return this.id;
    }
}