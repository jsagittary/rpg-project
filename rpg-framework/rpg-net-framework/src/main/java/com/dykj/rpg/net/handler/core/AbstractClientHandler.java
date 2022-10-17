package com.dykj.rpg.net.handler.core;

import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.net.protocol.BitArray;
import com.dykj.rpg.net.protocol.Protocol;
import com.dykj.rpg.net.protocol.ProtocolPool;
import com.dykj.rpg.net.protocol.Serializer;
import com.dykj.rpg.net.thread.CmdThreadEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @Author: jyb
 * @Date: 2020/9/9 13:41
 * @Description:
 */
public abstract class AbstractClientHandler<T extends Protocol> implements ClientHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private void initClazz() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            this.clazz = (Class<T>) actualTypeArguments[0];
        } else {
            this.clazz = (Class<T>) genericSuperclass;
        }
        try {
            Protocol protocol = (Protocol) clazz.newInstance();
            code = protocol.getCode();
        } catch (Exception e) {

        }

    }

    public AbstractClientHandler() {
        initClazz();
    }

    /**
     * T 的类型
     */
    protected Class clazz;


    private short code;


    @Override
    public void handler(byte[] bytes, ISession session)  {
        long now = System.currentTimeMillis();
        try {
            Protocol t = Serializer.deserialize(bytes, clazz);
            doHandler((T) t, session);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("NetWork {}  doHandler error {} ", getClass().getSimpleName(), e);
        } finally {
            long costTime = System.currentTimeMillis() - now;
            if (costTime > 100) {
                logger.error("NetWork {} doHandler cost time  error  time = {} ",getClass().getSimpleName(), costTime);
            }
        }
    }


    protected abstract void doHandler(T t, ISession session);

    @Override
    public CmdThreadEnum getThread() {
        return CmdThreadEnum.MAIN;
    }

    @Override
    public Class getClazz() {
        return clazz;
    }


    protected void sendMsg(ISession session, Protocol protocol) {
        session.write(protocol);
        ProtocolPool.getInstance().restoreProtocol(protocol);
    }


    @Override
    public short getCode() {
        return code;
    }
}
