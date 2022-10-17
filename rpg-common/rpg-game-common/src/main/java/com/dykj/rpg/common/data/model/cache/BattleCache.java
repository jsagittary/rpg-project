package com.dykj.rpg.common.data.model.cache;

import com.dykj.rpg.common.cache.ICachePrimaryKey;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jyb
 * @date 2020/12/25 13:52
 * @Description
 */
public class BattleCache implements ICachePrimaryKey {
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
    private List<Integer> playerIds =new ArrayList<>();

    /**
     * 具体的关卡配置id
     */
    private int configId;

    /**
     * 战斗创建时间
     */
    private long createTime;


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

    @Override
    public long cachePrimaryKey() {
        return gameBattleId;
    }

    @Override
    public void primaryKey(long key) {
        this.gameBattleId = (int) key;
    }

    @Override
    public String toString() {
        return "BattleCache{" +
                "battleType=" + battleType +
                ", gameBattleId=" + gameBattleId +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", playerIds=" + playerIds +
                ", configId=" + configId +
                ", createTime=" + createTime +
                '}';
    }
}