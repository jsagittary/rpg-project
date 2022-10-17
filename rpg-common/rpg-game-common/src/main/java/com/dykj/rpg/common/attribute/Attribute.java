package com.dykj.rpg.common.attribute;

/**
 * @author jyb
 * @date 2020/12/11 11:34
 * @Description
 */
public class Attribute {

    /**
     * 属性id
     */
    private int id;

    /**
     * 值修正
     */
    private  int value;


    /**
     * 增比修正
     */
    private int increase;


    /**
     * 减比修正
     */
    private int reduction;

    public Attribute(int id, int value, int increase, int reduction) {
        this.id = id;
        this.value = value;
        this.increase = increase;
        this.reduction = reduction;
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
}