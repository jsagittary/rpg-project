package com.dykj.rpg.game.module.battle.logic;

import java.util.List;

/**
 * @author jyb
 * @date 2020/12/9 10:00
 * @Description
 */
public abstract class AbstractBattle implements IBattle {
    /**
     * 战斗类型
     */
    private byte battleType;
    /**
     * 战斗id(游戏服生成)
     */
    private int gameBattleId;
    /**
     * 战斗服ip
     */
    private String ip;

    /**
     * 战斗服端口
     */
    private int port;

    /**
     * 参与这场战斗的人
     */
    private List<Integer> playerIds;

    /**
     * 具体的关卡配置id
     */
    private int configId;

    /**
     * 战斗创建时间
     */
    private long createTime;


    public AbstractBattle(byte battleType) {
        this.battleType = battleType;
    }

    public AbstractBattle(byte battleType, int gameBattleId, String ip, int port, List<Integer> playerIds, int configId) {
        this.battleType = battleType;
        this.gameBattleId = gameBattleId;
        this.ip = ip;
        this.port = port;
        this.playerIds = playerIds;
        this.configId = configId;
    }

    public AbstractBattle() {
    }

    public byte getBattleType() {
        return battleType;
    }

    public void setBattleType(byte battleType) {
        this.battleType = battleType;
    }

    public int getGameBattleId() {
        return gameBattleId;
    }

    public void setGameBattleId(int gameBattleId) {
        this.gameBattleId = gameBattleId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<Integer> getPlayerIds() {
        return playerIds;
    }

    public void setPlayerIds(List<Integer> playerIds) {
        this.playerIds = playerIds;
    }

    public int getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}