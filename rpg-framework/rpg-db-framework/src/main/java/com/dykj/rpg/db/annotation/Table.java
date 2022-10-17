package com.dykj.rpg.db.annotation;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE})
public @interface Table {
    /**
     * 表名
     * @return
     */
    String tableName();



}
