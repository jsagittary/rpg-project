package com.dykj.rpg.common.attribute;

import java.util.List;

/**
 * @author jyb
 * @date 2020/12/2 16:05
 * @Description
 */
public class AttributeUnit {
    /**
     *  1 属性
     */
    private int id;

    /**
     * 属性的子类型，没有子类型的为 0
     */
    private int type;

    /**
     * 1 值 2 增比  3 减比
     */
    private int pram;

    /**
     * 属性值
     */
    private int value;

    public AttributeUnit(int id, int type, int pram, int value) {
        this.id = id;
        this.type = type;
        this.pram = pram;
        this.value = value;
    }


    public AttributeUnit(List<Integer> attributeInfo) {
        this.id = attributeInfo.get(0);
        this.type = attributeInfo.get(1);
        this.pram = attributeInfo.get(2);
        this.value = attributeInfo.get(3);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPram() {
        return pram;
    }

    public void setPram(int pram) {
        this.pram = pram;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }


    public String key() {
        return new StringBuffer().append(id).append(":").append(type).append(":").append(pram).toString();
    }
}