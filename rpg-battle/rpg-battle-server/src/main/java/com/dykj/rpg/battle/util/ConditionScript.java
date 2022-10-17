package com.dykj.rpg.battle.util;

import cn.hutool.extra.expression.engine.jexl.JexlEngine;

import java.util.HashMap;
import java.util.Map;

public class ConditionScript {

    /**
     * 对象
     */
    /**
     * 技能拥有者
     */
    public final static String host = "host";
    /**
     * 技能击中的目标对象
     */
    public final static String target = "target";

    /**
     * 最大值
     */
    public final static String max = "max";

    /**
     * 当前值
     */
    public final static String cur = "cur";

    /**
     * 属性
     */
    public final static String hp = "hp";


    public static Object parseScript(String script, Map<String,Object> map){
        JexlEngine jexl=new JexlEngine();
        return jexl.eval(script,map);
    }

    public static void main(String[] args) {
        String script = "target.hp==1 && target.hp!=2";
        Map<String,Object> map = new HashMap<>();
        map.put("target.hp",1);
        Boolean bool = (Boolean) parseScript(script,map);
        System.out.println(bool);
    }

}
