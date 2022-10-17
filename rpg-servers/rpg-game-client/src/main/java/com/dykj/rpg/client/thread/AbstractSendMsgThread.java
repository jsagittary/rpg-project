package com.dykj.rpg.client.thread;

import com.dykj.rpg.net.core.ISession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: jyb
 * @Date: 2019/1/8 11:27
 * @Description:
 */
public abstract class AbstractSendMsgThread< T,V extends ISession> implements Runnable {


    /**
     *session集合
     */
    protected final  Map<Number,V>  sessions = new ConcurrentHashMap<>();

    public  abstract void execute(T msg) ;

    /**
     * 添加客户端
     * @param v
     */
    public void  addSession(V v){
          sessions.put(v.getId(),v);
    }

    public void removeSession(Number id){
        sessions.remove(id);
    }

    public Map<Number, V> getSessions() {
        return sessions;
    }
}
