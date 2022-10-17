package com.dykj.rpg.net.kafka.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface KafkaCmdListener {
	
	/**
	 * id号
	 * @return
	 */
	short cmd();

}
