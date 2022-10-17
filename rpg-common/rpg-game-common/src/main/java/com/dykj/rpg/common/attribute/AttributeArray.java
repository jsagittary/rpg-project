package com.dykj.rpg.common.attribute;

import java.util.Map;

/**
 * @author jyb
 * @date 2020/11/9 20:14
 * @Description 属性集合
 * <p>
 * index+1 为属性编号
 * attributeId(策划填写) -1 为value的index
 */
public class AttributeArray {
    /**
     * 属性集合
     */
    private int[] values;

    /**
     * 战斗力
     */
    private int power;


    public AttributeArray(int length) {
        this.values = new int[length];
    }


    public void add(int index, int value) {
        values[index] = values[index] + value;
    }

    public void addAttribute(int attributeId, int value) {
        add(attributeId - 1, value);
    }

    public int[] values() {
        return values;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void add(Map<Integer, Integer> attrs) {
        for (Map.Entry<Integer, Integer> entry : attrs.entrySet()) {
            add(entry.getKey() - 1, entry.getValue());
        }
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        int index = 0;
        for (int value : values) {
            index++;
            if (value == 0) {
                continue;
            }
            buffer.append(index).append("_").append(value);
            if (index < values.length - 1) {
                buffer.append(";");
            }
        }
        buffer.append("]");
        return buffer.toString();
    }
}