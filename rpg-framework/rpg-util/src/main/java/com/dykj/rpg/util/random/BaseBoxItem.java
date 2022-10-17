package com.dykj.rpg.util.random;

public  class BaseBoxItem {

	/** 标识ID(道具ID或装备ID或自定义ID)*/
	private int markId;
	/** 扩展参数*/
	private int pram;
	
	/** 对象值*/
	private int value;
	
	/** （获得、消耗）几率*/
	private int probability;
	

	public BaseBoxItem(int markId, int probability) {
		super();
		this.markId = markId;
		this.probability = probability;
	}

	public BaseBoxItem(int markId, int value, int probability) {
		this.markId = markId;
		this.value = value;
		this.probability = probability;
	}

	public BaseBoxItem(int markId, int pram, int value, int probability) {
		this.markId = markId;
		this.pram = pram;
		this.value = value;
		this.probability = probability;
	}

	public BaseBoxItem() {

	}

	public int getMarkId() {
		return markId;
	}

	public void setMarkId(int markId) {
		this.markId = markId;
	}
	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getProbability() {
		return probability;
	}

	public void setProbability(int probability) {
		this.probability = probability;
	}

	public int getPram() {
		return pram;
	}

	public void setPram(int pram) {
		this.pram = pram;
	}
}
