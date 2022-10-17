package com.dykj.rpg.common.redis.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;

/**
 * @author jyb
 * @date 2019/6/25 15:10
 */
@Component
public class ScriptConfigCheck {

    private transient Logger logger = LoggerFactory.getLogger("redisScript");

    @Autowired
    private ScriptConfig scriptConfig;

    @PostConstruct
    public void check() {
        boolean wrong = false;
        Method[] methods = scriptConfig.getClass().getDeclaredMethods();
        if (null != methods) {
            logger.info("============================ redis script begin ============================");
            for (Method m : methods) {
                try {
                    if (null != m.getReturnType() && m.getReturnType() == RedisScript.class) {
                        RedisScript s = (RedisScript) m.invoke(scriptConfig);
                        logger.info("redis script method : {}, sha1 : {}", m.getName(), s.getSha1());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    wrong = true;
                }
            }
            logger.info("============================ redis script end ============================");
        }
        if (wrong) {
            System.exit(-1);
        }
    }
}
