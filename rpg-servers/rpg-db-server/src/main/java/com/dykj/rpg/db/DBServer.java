package com.dykj.rpg.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * @Author: jyb
 * @Date: 2020/9/24 14:07
 * @Description:
 */
public class DBServer {
    private static Logger logger = LoggerFactory.getLogger(DBServer.class);

    public static void main(String[] args) throws Exception {
        long now = System.currentTimeMillis();
        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        logger.info("DBServer START SUCCESS %% STARTTIME=" + (System.currentTimeMillis() - now));
        logger.info("DBServer START COMPLETE");
        logger.info("LINUX LOG SUCCESS");
    }
}
