package com.dykj.rpg.game.module.player.service;

import com.dykj.rpg.db.queue.DbQueueManager;
import com.dykj.rpg.game.module.player.logic.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class PlayerCacheService {

    @Resource
    private PlayerService playerService;


    /**
     * 所有登录缓存未失效的玩家
     */
    private Map<Integer, Player> players = new ConcurrentHashMap<>();

    /**
     * 所有在线玩家
     */
    private Map<Integer, Player> onlinePlayers = new ConcurrentHashMap<>();


    /**
     * 最短刷新数据时间
     */
    private int MIN_DBQUEUE_FLUSH_TIME_SPACE = 1000 * 60 * 5;

    /**
     * 最后一次刷新数据的时间
     */
    private long lastDBQueueFlushTime = System.currentTimeMillis();
    private Logger logger = LoggerFactory.getLogger(getClass());


    public void addPlayer(Player player) {
        players.put(player.getPlayerId(), player);
    }

    /**
     * 玩家集合锁
     */
    private Lock lock = new ReentrantLock();

    /**
     * 查找某个玩家，先从缓存找 找不到 从数据库加载（慎用）
     *
     * @param playerId
     * @return
     */
    public Player get(int playerId) {
        return get(playerId, true);
    }


    /**
     * 查找某个玩家，先从缓存找 找不到 从数据库加载（慎用）
     *
     * @param playerId
     * @return
     */
    public Player getCache(int playerId) {
        return get(playerId, false);
    }

    /***
     * 查找某个玩家
     * @param playerId
     * @return
     */
    public Player get(int playerId, boolean autoCreate) {
//        logger.info("get onlinePlayers size {} ",onlinePlayers.size());
//        logger.info("get offlinePlayers size {} ",players.size());
        Player exist;
        try {
            lock.lock();
            exist = onlinePlayers.get(playerId);
            if (exist != null) {
                exist.setRefreshTime();
                return exist;
            }
        } finally {
            lock.unlock();
        }

        exist = players.get(playerId);

        if (exist != null) {
            exist.setRefreshTime();
            return exist;
        }
        if (!autoCreate) {
            return null;
        }
        return playerService.createPlayer(playerId);
    }

    /**
     * 玩家上线
     *
     * @param player
     */
    public void online(Player player) {
        players.remove(player.getPlayerId());
        try {
            lock.lock();
            onlinePlayers.put(player.getPlayerId(), player);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 玩家离线
     *
     * @param player
     */
    public void offline(Player player) {
        try {
            lock.lock();
            onlinePlayers.remove(player.getPlayerId());
        } finally {
            lock.unlock();
        }

        players.put(player.getPlayerId(), player);
        save();

    }

    public void remove(int playerId) {
        try {
            lock.lock();
            onlinePlayers.remove(playerId);
        } finally {
            lock.unlock();
        }
        players.remove(playerId);
    }

    public Map<Integer, Player> getPlayers() {
        return players;
    }

    public Map<Integer, Player> getOnlinePlayers() {
        return onlinePlayers;
    }

    private Player getPlayer(int accountKey, int serverId, Map<Integer, Player> onlinePlayers) {
        Iterator<Map.Entry<Integer, Player>> it = onlinePlayers.entrySet().iterator();
        while (it.hasNext()) {
            Player player = it.next().getValue();
            if (player.cache().getPlayerInfoModel().getAccountKey() == accountKey && player.cache().getPlayerInfoModel().getServerId() == serverId) {
                return player;
            }
        }
        return null;
    }

    /**
     * 通过账号和serverId获取玩家
     *
     * @param accountKey
     * @param serverId
     * @return
     */
    public Player getPlayerByAccount(int accountKey, int serverId) {
//        logger.info("get onlinePlayers getPlayerByAccount size {} ",onlinePlayers.size());
//        logger.info("get offlinePlayers getPlayerByAccount size {} ",players.size());
        Player exist;
        try {
            lock.lock();
            exist = getPlayer(accountKey, serverId, onlinePlayers);
            if (exist != null) {
                return exist;
            }
        } finally {
            lock.unlock();
        }

        exist = getPlayer(accountKey, serverId, players);
        if (exist != null) {
            return exist;
        }
        return null;
    }

    /**
     * 刷新缓存数据到数据库（玩家离线触发）
     */
    private void save() {
        long timeSpace = System.currentTimeMillis() - lastDBQueueFlushTime;
        if (timeSpace > MIN_DBQUEUE_FLUSH_TIME_SPACE) {
            try {
                DbQueueManager.getInstance().flush();
            } catch (Throwable t) {
                t.printStackTrace();
            }
            lastDBQueueFlushTime = System.currentTimeMillis();
        }
    }
}
