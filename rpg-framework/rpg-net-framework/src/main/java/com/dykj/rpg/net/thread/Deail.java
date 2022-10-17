package com.dykj.rpg.net.thread;

import java.io.Serializable;

public class Deail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 玩家id */
	private int playerId;

	/** 玩家副本id */
	private int copyId = 0;

	/** 是否在跨服中 */
	private boolean crossFlag = false;

	public Deail(int playerId) {
		super();
		this.playerId = playerId;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getCopyId() {
		return copyId;
	}

	public void setCopyId(int copyId) {
		this.copyId = copyId;
	}

	public boolean isCrossFlag() {
		return crossFlag;
	}

	public void setCrossFlag(boolean crossFlag) {
		this.crossFlag = crossFlag;
	}

}
