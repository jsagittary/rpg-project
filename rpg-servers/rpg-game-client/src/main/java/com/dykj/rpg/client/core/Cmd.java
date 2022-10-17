package com.dykj.rpg.client.core;

import java.lang.annotation.*;

/**
 * @Author: jyb
 * @Date: 2018/12/24 18:30
 * @Description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Cmd {
    /**
     * 协议号
     * @return
     */
    short id();
}
