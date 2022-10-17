package com.dykj.rpg.net.handler;

import com.dykj.rpg.net.annotation.Cmd;
import com.dykj.rpg.util.PackageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
public abstract class AbstractHandlerManager<T extends IHandler> implements IHandlerManager {

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

    private void initClazz() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            this.clazz = (Class<T>) actualTypeArguments[0];
        } else {
            this.clazz = (Class) genericSuperclass;
        }
    }

    private Class<T> clazz;

    /**
     * T.class
     */

    @Override
    public void initHandler() {
        initClazz();
        int cmd = 0;
        try {
            List<String> list = PackageUtil.getClassName(packagePath, true);
            List<Class<?>> classes = new ArrayList<>();
            for (String str : list) {
                Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass(str);
                classes.add(clazz);
            }
            for (Class<?> cl : classes) {
                if (!clazz.isAssignableFrom(cl)) {
                    continue;
                }
                Cmd cmdAnnotation = cl.getAnnotation(Cmd.class);
                if (cmdAnnotation == null) {
                    logger.error("handler has no  Annotation class {}", clazz);
                    System.exit(-1);
                }
                if (handlers.keySet().contains(cmdAnnotation.id())) {
                    logger.error(cl + " cmd is ready exist cmd =" + cmdAnnotation.id());
                    System.exit(-1);
                }
                cmd = cmdAnnotation.id();
                T t = (T) cl.newInstance();
                handlers.put(cmdAnnotation.id(), t);
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
