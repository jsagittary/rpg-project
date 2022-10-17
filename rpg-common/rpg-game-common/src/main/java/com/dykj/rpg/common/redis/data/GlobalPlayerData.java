package com.dykj.rpg.common.redis.data;

import com.dykj.rpg.common.data.model.GlobalPlayerModel;
import com.dykj.rpg.common.data.model.PlayerInfoModel;
import com.dykj.rpg.redis.cache.base.ICacheMapPrimaryKey;

/**
 * @Author: jyb
 * @Date: 2020/10/9 14:52
 * @Description:
 */
public class GlobalPlayerData extends GlobalPlayerModel implements ICacheMapPrimaryKey {
    /**
     * 是否在线
     */
    private boolean isOnline;


    @Override
    public Object primaryKey() {
        return getPlayerId();
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public GlobalPlayerData(PlayerInfoModel playerInfoModel) {
        super.setName(playerInfoModel.getName());
        super.setPlayerId(playerInfoModel.getPlayerId());
        super.setServerId(playerInfoModel.getServerId());
        super.setAccountKey(playerInfoModel.getAccountKey());

    }

    public GlobalPlayerData(GlobalPlayerModel globalPlayerModel) {
        super.setName(globalPlayerModel.getName());
        super.setPlayerId(globalPlayerModel.getPlayerId());
        super.setServerId(globalPlayerModel.getServerId());
        super.setAccountKey(globalPlayerModel.getAccountKey());
    }



    public GlobalPlayerData() {
    }

}
