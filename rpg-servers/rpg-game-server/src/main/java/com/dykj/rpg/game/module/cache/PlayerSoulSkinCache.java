package com.dykj.rpg.game.module.cache;

import com.dykj.rpg.common.data.model.PlayerSoulSkinModel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jyb
 * @date 2021/4/26 11:41
 * @Description 玩家
 */
public class PlayerSoulSkinCache {

    /**
     * 灵魂之影皮肤集合
     */
    private Map<Integer, PlayerSoulSkinModel> playerSoulSkinCacheMap = new ConcurrentHashMap<>();


    /**
     * 获得正在穿戴的skin
     *
     * @return
     */
    public PlayerSoulSkinModel getUseSoulSkin() {
        for (PlayerSoulSkinModel p : playerSoulSkinCacheMap.values()) {
            if (p.getUseSoulSkin() == 1) {
                return p;
            }
        }
        return null;
    }

    public void put(PlayerSoulSkinModel playerSoulSkinModel) {
        playerSoulSkinCacheMap.put(playerSoulSkinModel.getSoulSkinId(), playerSoulSkinModel);
    }

    public PlayerSoulSkinModel get(int soulSkinId) {
        return playerSoulSkinCacheMap.get(soulSkinId);
    }

    public Map<Integer, PlayerSoulSkinModel> getPlayerSoulSkinCacheMap() {
        return playerSoulSkinCacheMap;
    }
}