package com.dykj.rpg.game.module.battle.logic;

import java.util.List;

/**
 * @author jyb
 * @date 2020/12/25 11:43
 * @Description
 */
public class Battle  extends  AbstractBattle{
    public Battle(byte battleType) {
        super(battleType);
    }

    public Battle(byte battleType, int gameBattleId, String ip, int port, List<Integer> playerIds, int configId) {
        super(battleType, gameBattleId, ip, port, playerIds, configId);
    }

    public Battle() {
    }

    @Override
    public int getBattleId() {
        return 0;
    }

    @Override
    public void startBattle() {

    }

    @Override
    public void endBattle() {

    }

    @Override
    public boolean destroy() {
        return false;
    }
}