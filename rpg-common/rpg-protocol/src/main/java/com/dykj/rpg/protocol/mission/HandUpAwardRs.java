package com.dykj.rpg.protocol.mission;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class HandUpAwardRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1504;
	//开始挂机的时间
	private long handUpTime;
	//挂机奖励
	private List<com.dykj.rpg.protocol.item.ItemRs> awards;
	public HandUpAwardRs(){
		this.awards = new ArrayList<>();
	}
	public HandUpAwardRs(long handUpTime,List<com.dykj.rpg.protocol.item.ItemRs> awards){
		this.handUpTime = handUpTime;
		this.awards = awards;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.handUpTime = 0;
		for(com.dykj.rpg.protocol.item.ItemRs value : awards){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.awards.clear();
	}
	public short getCode(){
		return this.code;
	}
	public long getHandUpTime(){
		return this.handUpTime;
	}
	public void setHandUpTime(long _handUpTime){
		this.handUpTime = _handUpTime;
	}
	public List<com.dykj.rpg.protocol.item.ItemRs> getAwards(){
		return this.awards;
	}
	public void setAwards(List<com.dykj.rpg.protocol.item.ItemRs> _awards){
		this.awards = _awards;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		handUpTime = (long)dch.bytesToParam("Long");
		awards = (List<com.dykj.rpg.protocol.item.ItemRs>)dch.bytesToParam("List<com.dykj.rpg.protocol.item.ItemRs>");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(handUpTime)){
			return false;
		}
		if(!ech.paramToBytes(awards)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "HandUpAwardRs [code=" + code +",handUpTime=" + handUpTime +",awards=" + awards +"]";
	}
}
