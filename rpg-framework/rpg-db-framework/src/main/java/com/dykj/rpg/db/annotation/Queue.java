package com.dykj.rpg.db.annotation;



import com.dykj.rpg.db.dao.AbstractBaseDao;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Queue {

    Class<? extends AbstractBaseDao> dao();

}
