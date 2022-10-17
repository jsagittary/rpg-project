package com.dykj.rpg.client.game.player.logic;

import com.dykj.rpg.common.data.model.PlayerInfoModel;
import com.dykj.rpg.common.module.player.logic.AbstractPlayer;

/**
 * @Author: jyb
 * @Date: 2019/1/8 16:47
 * @Description:
 */
public class ClientPlayer extends AbstractPlayer {
    public ClientPlayer(int playerId) {
        super(playerId);
    }

    @Override
    public int getPlayerId() {
        return 0;
    }

    @Override
    public void offline() {

    }

    @Override
    public int getHolderId() {
        return 0;
    }
}
