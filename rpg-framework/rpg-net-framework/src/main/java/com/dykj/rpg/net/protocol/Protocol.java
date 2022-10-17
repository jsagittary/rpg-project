package com.dykj.rpg.net.protocol;

public abstract class Protocol {// 协议接口

	public boolean using = false;

	public int idx = 0;

	public abstract boolean encode(BitArray bitArray);
	
	public abstract boolean decode(BitArray bitArray);

	public abstract short getCode();

	public abstract void release();
}
