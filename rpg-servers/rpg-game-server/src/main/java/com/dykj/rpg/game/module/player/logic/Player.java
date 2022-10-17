package com.dykj.rpg.game.module.player.logic;

import com.dykj.rpg.common.consts.CommonConsts;
import com.dykj.rpg.common.data.model.PlayerInfoModel;
import com.dykj.rpg.common.module.player.logic.AbstractPlayer;
import com.dykj.rpg.common.module.uc.model.AccountInfoModel;
import com.dykj.rpg.game.module.cache.PlayerCache;
import com.dykj.rpg.game.module.event.cache.CacheEventManager;
import com.dykj.rpg.game.module.event.login.LoginEventManager;
import com.dykj.rpg.game.module.event.login.LogoutEventManager;
import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.util.spring.BeanFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author jyb
 * @date 2020/7/31 11:36
 * @description
 */
public class Player extends AbstractPlayer {
    /**
     * 账号信息
     */
    private AccountInfoModel accountInfoModel;

    /**
     * 备选的角色
     */
    private Map<Integer, PlayerCache> caches = new ConcurrentHashMap<>();


    public Player(int playerId) {
        this.refreshTime = System.currentTimeMillis();
        this.playerId = playerId;
    }

    public Player() {
        this.refreshTime = System.currentTimeMillis();
    }

    @Override
    public int getHolderId() {
        return playerId;
    }

    @Override
    public void offline() {
        setRefreshTime();
        BeanFactory.getBean(LogoutEventManager.class).doEvents(this);
    }


    @Override
    public void online(ISession session) {
        setRefreshTime();
        super.session = session;
        session.setSessionHolder(this);
        this.accountInfoModel = (AccountInfoModel) session.getAttribute(CommonConsts.ACCOUNT_KYE);
        //BeanFactory.getBean(LoginEventManager.class).doEvents(this);
         BeanFactory.getBean(CacheEventManager.class).doEvents(this);
    }

    /**
     * 是否过时
     *
     * @return
     */
    public boolean isTimeOut() {
        if (System.currentTimeMillis() - refreshTime > CommonConsts.PLAYER_CACHE_INVALID) {
            return true;
        }
        return false;
    }


    public AccountInfoModel getAccount() {
        return accountInfoModel;
    }


    public void setAccountInfoModel(AccountInfoModel accountInfoModel) {
        this.accountInfoModel = accountInfoModel;
    }

    public AccountInfoModel getAccountInfoModel() {
        return accountInfoModel;
    }

    public List<PlayerInfoModel> getAllInfos() {
        List<PlayerInfoModel> playerInfoModels = new ArrayList<>();
        for (PlayerCache playerCache : caches.values()) {
            playerInfoModels.add(playerCache.getPlayerInfoModel());
        }
        return playerInfoModels;
    }

    public Map<Integer, PlayerCache> getCaches() {
        return caches;
    }


    public PlayerCache cache() {
        return caches.get(playerId);
    }

    public PlayerCache getPlayerCache(int playerId) {
        return caches.get(playerId);
    }

    public void clearPlayerCache() {
        caches.clear();
    }

    public PlayerInfoModel getInfo() {
        return cache().getPlayerInfoModel();
    }
}
