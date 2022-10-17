package com.dykj.rpg.common.data.model;

import com.dykj.rpg.db.annotation.Column;
import com.dykj.rpg.db.annotation.PrimaryKey;
import com.dykj.rpg.db.data.BaseModel;

/**
 * @author caobing
 * @date 2021/04/07 18:53
 * @Description
 */
public class PlayerAiModel extends BaseModel {
    /**
     * 玩家ID
     */
    @Column(primaryKey = PrimaryKey.GENERAL)
    private int playerId;

    /**
     * 技巧ID
     */
    @Column(primaryKey = PrimaryKey.GENERAL)
    private int aiId;

    /**
     * 技巧的位置 0表示未使用
     */
    private int position;

    private int aiConditionParam;
    
    private int aiActionParam;

    public PlayerAiModel() {
        super();
    }

    public PlayerAiModel(int playerId, int aiId, int position, int aiConditionParam, int aiActionParam) {
		super();
		this.playerId = playerId;
		this.aiId = aiId;
		this.position = position;
		this.aiConditionParam = aiConditionParam;
		this.aiActionParam = aiActionParam;
	}


	public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getAiId() {
		return aiId;
	}

	public void setAiId(int aiId) {
		this.aiId = aiId;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getaiConditionParam() {
		return aiConditionParam;
	}

	public void setaiConditionParam(int aiConditionParam) {
		this.aiConditionParam = aiConditionParam;
	}

	public int getAiActionParam() {
		return aiActionParam;
	}

	public void setAiActionParam(int aiActionParam) {
		this.aiActionParam = aiActionParam;
	}

	@Override
	public String toString() {
		return "PlayerAiModel [playerId=" + playerId + ", aiId=" + aiId + ", position=" + position
				+ ", aiConditionParam=" + aiConditionParam + ", aiActionParam=" + aiActionParam + "]";
	}
}
