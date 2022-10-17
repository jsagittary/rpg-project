package com.dykj.rpg.game.module.event.cache;

import com.dykj.rpg.game.module.event.core.AbstractEvent;
import com.dykj.rpg.game.module.player.logic.Player;
import com.dykj.rpg.util.reflex.ReflexUtil;

/**
 * @author jyb
 * @date 2021/5/8 15:03
 * @Description
 */
public abstract class CacheEvent<T> extends AbstractEvent {

    private Class<T> t;


    public CacheEvent() {
        this.t = ReflexUtil.getClass(getClass(), 0);
    }

    @Override
    public void doEvent(Object... prams) throws Exception {
        Player player = (Player) prams[0];
        if (createCache(player)) {
            if (isOpen(player)) {
                loadFromDb(player);
            }
        }
        if (isOpen(player)) {
            refreshCache(player);
        }
        send(player);
    }

    /**
     * 创建cache
     *
     * @param player
     * @return 是否创建进行了创建
     */
    public boolean createCache(Player player) {
        if (player.cache().getCache(t) == null) {
            player.cache().setCache(t);
            return true;
        }
        return false;
    }

    /**
     * 此方法制作数据库操作逻辑
     * 缓存中有数据的时候该方法不执行
     * 功能未开启该方法不执行
     *
     * @param player
     */
    public abstract void loadFromDb(Player player);

    /**
     * 刷新缓存，比如时间重置，以及默认初始化默认的cache
     * 数据库查出来需要刷新缓存的逻辑
     * 功能未开启该方法不执行
     *
     * @param player
     */
    public abstract void refreshCache(Player player);

    /**
     * 事件内部去指定是否初始化，有些功能在登录时候需要查询数据库，但是因为等级低没有开放，所以不用查询数据库
     * 这里主要去实现功能是否开启
     *
     * @return
     */
    public boolean isOpen(Player player) {
        return true;
    }

    /**
     * 通知给客户端
     */
    public void send(Player player) {

    }
}