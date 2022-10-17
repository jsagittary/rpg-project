package com.dykj.rpg.protocol.common;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class HeartBeatRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 102;
	//系统时间
	private long systemTime;
	public HeartBeatRs(){
	}
	public HeartBeatRs(long systemTime){
		this.systemTime = systemTime;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.systemTime = 0;
	}
	public short getCode(){
		return this.code;
	}
	public long getSystemTime(){
		return this.systemTime;
	}
	public void setSystemTime(long _systemTime){
		this.systemTime = _systemTime;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		systemTime = (long)dch.bytesToParam("Long");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(systemTime)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "HeartBeatRs [code=" + code +",systemTime=" + systemTime +"]";
	}
}
