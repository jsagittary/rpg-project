package com.dykj.rpg.protocol.mission;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class GetHandUpAwardRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1506;
	//开始挂机的时间
	private long handUpTime;
	public GetHandUpAwardRs(){
	}
	public GetHandUpAwardRs(long handUpTime){
		this.handUpTime = handUpTime;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.handUpTime = 0;
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
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		handUpTime = (long)dch.bytesToParam("Long");
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
		return true;
	}
	public String toString() {
		return "GetHandUpAwardRs [code=" + code +",handUpTime=" + handUpTime +"]";
	}
}
