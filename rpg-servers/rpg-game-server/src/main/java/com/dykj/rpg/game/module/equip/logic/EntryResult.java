package com.dykj.rpg.game.module.equip.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jyb
 * @date 2020/12/11 15:34
 * @Description
 */
public class EntryResult {
    /**
     * DateFixConstant  枚举
     */
    private int type;
    /**
     * 技能id
     */
    private int skillId; //
    /**
     * 1为skill_character_basic
     * 2为skill_character_carrier
     * 3为skill_character_effect
     * 4为skill_character_state" 的id
     */
    private int effectId;
    /**
     * 属性id
     */
    private int id;

    /**
     * 值修正
     */
    private int value;


    /**
     * 增比修正
     */
    private int increase;


    /**
     * 减比修正
     */
    private int reduction;




    private List<Integer> chuCiJiHuoList =new ArrayList<>();

    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getEffectId() {
        return effectId;
    }

    public void setEffectId(int effectId) {
        this.effectId = effectId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getIncrease() {
        return increase;
    }

    public void setIncrease(int increase) {
        this.increase = increase;
    }

    public int getReduction() {
        return reduction;
    }

    public void setReduction(int reduction) {
        this.reduction = reduction;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EntryResult) {
            EntryResult entryResult = (EntryResult) obj;
            return type == entryResult.type && skillId == entryResult.skillId && effectId == entryResult.effectId && id == entryResult.id;
        }
        return false;
    }

    public EntryResult() {
        super();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    public List<Integer> getChuCiJiHuoList() {
        return chuCiJiHuoList;
    }

    public void setChuCiJiHuoList(List<Integer> chuCiJiHuoList) {
        this.chuCiJiHuoList = chuCiJiHuoList;
    }
}