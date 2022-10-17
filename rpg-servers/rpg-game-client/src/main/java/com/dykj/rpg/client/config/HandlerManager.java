package com.dykj.rpg.client.config;

import com.dykj.rpg.client.consts.InitOrder;
import com.dykj.rpg.client.core.Cmd;
import com.dykj.rpg.client.handler.IHandler;
import com.dykj.rpg.util.PackageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: jyb
 * @Date: 2019/1/8 17:31
 * @Description:
 */
@Configuration
@Component
@Order(value = InitOrder.HANDlER)
public class HandlerManager {



    private Logger logger = LoggerFactory.getLogger(getClass());


    /**
     * 指令集合
     */
    private Map<Short, IHandler> handlers = new ConcurrentHashMap<>();


    @Value("${handler.package}")
    private String handlerPackage;

    @PostConstruct
    public void run() throws Exception {
        int cmd = 0;
        try {
            List<String> list = PackageUtil.getClassName(handlerPackage, true);
            List<Class<?>> classes = new ArrayList<Class<?>>();
            for (String str : list) {
                Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass(str);
                classes.add(clazz);
            }
            for (Class<?> cl : classes) {
                if (!IHandler.class.isAssignableFrom(cl)) {
                    continue;
                }
                Cmd cmdAnnotation = cl.getAnnotation(Cmd.class);
                if (cmdAnnotation == null) {
                    logger.error( "handler has no  Annotation class {}" ,cl);
                    System.exit(-1);
                }
                if (handlers.keySet().contains(cmdAnnotation.id())) {
                    logger.error(cl + " cmd is ready exist cmd =" + cmdAnnotation.id());
                    System.exit(-1);
                }
                cmd = cmdAnnotation.id();
                IHandler t = (IHandler) cl.newInstance();
                handlers.put(cmdAnnotation.id(), t);
            }
        } catch (Exception e) {
            logger.error("HandlerManager initHandler error :" + e + "  ,cmd = " + cmd);
            System.exit(-1);
        }
    }

    public IHandler getHander(short cmd) {
        return handlers.get(cmd);
    }
}
