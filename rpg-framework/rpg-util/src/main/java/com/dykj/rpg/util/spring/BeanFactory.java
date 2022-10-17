package com.dykj.rpg.util.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Author: jyb
 * @Date: 2018/12/21 17:46
 * @Description: Spring Context提供器，为了方便在应用程序中使用ClassPathXmlApplicationContext定义
 */
@Component
public class BeanFactory  {


    private static ApplicationContext applicationContext;

    public  static  void setApplicationContext(ApplicationContext applicationContext1) throws BeansException {
        applicationContext = applicationContext1;
    }


    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> t) {
        return applicationContext.getBean(t);
    }



}
