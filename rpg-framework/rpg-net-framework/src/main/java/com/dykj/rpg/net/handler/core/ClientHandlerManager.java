package com.dykj.rpg.net.handler.core;

import com.dykj.rpg.net.handler.IHandlerManager;
import com.dykj.rpg.net.protocol.Protocol;
import com.dykj.rpg.util.PackageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: jyb
 * @Date: 2018/12/24 18:40
 * @Description:
 */
@Service
public class ClientHandlerManager<T extends ClientHandler> implements IHandlerManager {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Handler的包路径
     */
    private String packagePath;


    /**
     * 指令集合
     */
    private Map<Short, T> handlers = new ConcurrentHashMap<>();

    @Override
    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    /**
     * T.class
     */

    @Override
    public void initHandler() {
        int cmd = 0;
        try {
            List<String> list = PackageUtil.getClassName(packagePath, true);
            List<Class<?>> classes = new ArrayList<>();
            for (String str : list) {
                Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass(str);
                classes.add(clazz);
            }
            for (Class<?> cl : classes) {
                if (!ClientHandler.class.isAssignableFrom(cl)) {
                    continue;
                }
                T t = (T) cl.newInstance();
                Protocol protocol = (Protocol) t.getClazz().newInstance();
                if (handlers.keySet().contains(protocol.getCode())) {
                    logger.error(cl + " cmd is ready exist cmd =" + protocol.getCode());
                    System.exit(-1);
                }
                logger.info("load handler success !!! code = "+protocol.getCode()+"  handlerName = "+t.getClazz().getName());
                handlers.put(protocol.getCode(), t);
            }
        } catch (Exception e) {
            logger.error("HandlerManager initHandler error :" + e + "  ,cmd = " + cmd);
            System.exit(-1);
        }
    }

    @Override
    public T getHandler(short cmd) {
        return handlers.get(cmd);
    }
}
