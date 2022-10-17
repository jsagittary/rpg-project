package com.dykj.rpg.game.module.equip.logic;

import com.dykj.rpg.common.attribute.Attribute;

/**
 * @author jyb
 * @date 2020/12/11 11:44
 * @Description
 */
public class EntryAttribute extends Attribute {


    //影响的是拿一行数据，对应eq_entry_effect 里面的type
    private int effectId;

    /**
     *修正类型 DateFixConstant
     */
    private int pram;

    public EntryAttribute(int id, int value, int increase, int reduction) {
        super(id, value, increase, reduction);
    }


    public int getEffectId() {
        return effectId;
    }

    public void setEffectId(int effectId) {
        this.effectId = effectId;
    }

    public int getPram() {
        return pram;
    }

    public void setPram(int pram) {
        this.pram = pram;
    }
}