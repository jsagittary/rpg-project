package com.dykj.rpg.db.annotation;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Column {
    /**
     * 主键
     *
     * @return 0非主键  1 表示自增主键 2 普通主键
     */
    PrimaryKey primaryKey() default PrimaryKey.NO_KEY;
    /**
     * 字段名，数据库的字段名
     *
     * @return
     */
    String column() default "";
}
