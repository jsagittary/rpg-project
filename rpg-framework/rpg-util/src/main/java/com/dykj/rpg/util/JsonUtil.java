/**
 *
 */
package com.dykj.rpg.util;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author wkdai
 *
 */
public class JsonUtil {
    /**
     * 对象实例转换成Json字符串
     *
     * @param instance
     * @return
     */
    public static final String toJsonString(Object instance) {

        String jsonText;
        try {
            jsonText = JSONObject.toJSONString(instance);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return jsonText;
    }

    /**
     * json字符串转换成对象实例
     *
     * @param jsonText
     * @param clazz
     * @return
     */
    public static final <T> T toInstance(String jsonText, Class<T> clazz) {
        T instance;
        try {
            instance = JSONObject.parseObject(jsonText, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return instance;
    }

    /**
     * 转换为List
     * @param jsonText
     * @param elementClass
     * @return
     */
    public static final <T> List<T> toList(String jsonText, Class<T> elementClass) {

        List<T> instance;
        try {
            instance = JSONObject.parseArray(jsonText, elementClass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return instance;
    }
}
