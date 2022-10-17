package com.dykj.rpg.common.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 * @author CaoBing
 * @date 2021年4月13日
 * @Description:
 */
public abstract class BaseManager {
	abstract public void load() throws Exception;
	
	public ApplicationContext app;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.app = applicationContext;
    }
	
}
