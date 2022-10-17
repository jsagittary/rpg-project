package com.dykj.rpg.util.reflex;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @Author: jyb
 * @Date: 2020/10/28 14:27
 * @Description:
 */
public class ReflexUtil {

    /**
     * 拿到某个类泛型的类型
     * Class<K,V>
     *·
     * @param clazz
     * @param index
     * @return k或者v的Class index 0 为K  1 为V,如果没有返回当前的class
     */
    public static Class getClass(Class clazz, int index) {
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            return (Class) actualTypeArguments[index];
        }
        return clazz;
    }


    /**
     * 通过字段拿值
     * @param o
     * @param name
     * @return
     */
    public static Object getValueByName(Object o, String name) {
        Object v = null;
        Class cls = o.getClass();
        Field[] fs = cls.getDeclaredFields();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            f.setAccessible(true);
            try {
                if (fs[i].getName().toUpperCase().equals(name.toUpperCase())) {
                    v = f.get(o);
                    break;
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return v;

    }

}
