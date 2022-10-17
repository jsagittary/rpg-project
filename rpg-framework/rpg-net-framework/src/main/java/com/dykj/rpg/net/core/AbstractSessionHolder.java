package com.dykj.rpg.net.core;

/**
 * @Author: jyb
 * @Date: 2018/12/22 15:02
 * @Description: session 拥有者
 */
public abstract class AbstractSessionHolder  implements ISessionHolder {

    protected ISession session;

    @Override
    public ISession getSession() {
        return session;
    }
}
