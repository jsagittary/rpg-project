package com.dykj.rpg.game.module.attribute.logic;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jyb
 * @date 2021/1/4 16:32
 * @Description
 */
public class AttributeCache {

    /**
     * 属性集合
     */
    private Map<String, Integer> attributes = new HashMap<>();

    /**
     * 战斗力d
     */
    private int power;

    public Map<String, Integer> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Integer> attributes) {
        this.attributes = attributes;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    /**
     * 获得某个属性的值
     * @param attributeId
     * @param subType
     * @return
     */
    public int getAttributeValue(int attributeId, int subType) {
        Integer value = attributes.get(attributeId + ":" + subType);
        if (value == null) {
            return 0;
        }
        return value;
    }
}