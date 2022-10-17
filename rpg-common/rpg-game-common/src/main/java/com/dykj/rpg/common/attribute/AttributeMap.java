package com.dykj.rpg.common.attribute;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jyb
 * @date 2020/11/9 20:14
 * @Description 属性集合
 * <p>
 *  外层key 为属性key
 *  内层key 为属性的元素类型或者技能资源类型，类型为0 表示没有子类
 */
public class AttributeMap {
    /**
     * 属性集合
     */
    private Map<Integer, Map<Integer, Integer>> values = new HashMap<>();


    public Map<Integer, Map<Integer, Integer>> getValues() {
        return values;
    }

    public void setValues(Map<Integer, Map<Integer, Integer>> values) {
        this.values = values;
    }
}