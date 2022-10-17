package com.dykj.rpg.common.data.model;

import com.dykj.rpg.db.annotation.Column;
import com.dykj.rpg.db.annotation.PrimaryKey;
import com.dykj.rpg.db.data.BaseModel;

import java.util.Date;

/**
 * @author jyb
 * @date 2020/12/21 14:03
 * @Description
 */
public class PlayerMissionModel extends BaseModel {
    /**
     * 玩家id
     */
    @Column(primaryKey = PrimaryKey.GENERAL)
    private int playerId;

    /**
     * 关卡id
     */
    private int missionId;

    /**
     * 上一次战斗获胜的时间，用来计算通过的cd
     */
    private Date lastBattleTime;

    /**
     * 开始挂机的时间,来计算下一次挂机的奖励
     */
    private Date handUpTime;

    /**
     * 快速挂机的次数
     */
    private int quickHangUpNum;

    /**
     * 快速挂机的开始时间，根据这个字段重置次数
     */
    private Date quickHangUpTime;

    /**
     * 挂机的奖励
     */
    private String hangUpAward;

    /**
     * 上一次结算奖励的时间
     */
    private Date lastSettleAwardTime;



    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getMissionId() {
        return missionId;
    }

    public void setMissionId(int missionId) {
        this.missionId = missionId;
    }

    public Date getLastBattleTime() {
        return lastBattleTime;
    }

    public void setLastBattleTime(Date lastBattleTime) {
        this.lastBattleTime = lastBattleTime;
    }

    public Date getHandUpTime() {
        return handUpTime;
    }

    public void setHandUpTime(Date handUpTime) {
        this.handUpTime = handUpTime;
    }

    public int getQuickHangUpNum() {
        return quickHangUpNum;
    }

    public void setQuickHangUpNum(int quickHangUpNum) {
        this.quickHangUpNum = quickHangUpNum;
    }

    public Date getQuickHangUpTime() {
        return quickHangUpTime;
    }

    public void setQuickHangUpTime(Date quickHangUpTime) {
        this.quickHangUpTime = quickHangUpTime;
    }

    public String getHangUpAward() {
        return hangUpAward;
    }

    public void setHangUpAward(String hangUpAward) {
        this.hangUpAward = hangUpAward;
    }

    public Date getLastSettleAwardTime() {
        return lastSettleAwardTime;
    }

    public void setLastSettleAwardTime(Date lastSettleAwardTime) {
        this.lastSettleAwardTime = lastSettleAwardTime;
    }
}