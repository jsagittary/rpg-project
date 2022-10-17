package com.dykj.rpg.protocol.task;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class OpenProtectorRewardRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1712;
	//是否已开通守护者奖励(0-否 1-是)
	private int isProtector;
	//守护者奖励剩余时长(单位:分)
	private long protectorRemainingTime;
	public OpenProtectorRewardRs(){
	}
	public OpenProtectorRewardRs(int isProtector,long protectorRemainingTime){
		this.isProtector = isProtector;
		this.protectorRemainingTime = protectorRemainingTime;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.isProtector = 0;
		this.protectorRemainingTime = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getIsProtector(){
		return this.isProtector;
	}
	public void setIsProtector(int _isProtector){
		this.isProtector = _isProtector;
	}
	public long getProtectorRemainingTime(){
		return this.protectorRemainingTime;
	}
	public void setProtectorRemainingTime(long _protectorRemainingTime){
		this.protectorRemainingTime = _protectorRemainingTime;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		isProtector = (int)dch.bytesToParam("Integer");
		protectorRemainingTime = (long)dch.bytesToParam("Long");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(isProtector)){
			return false;
		}
		if(!ech.paramToBytes(protectorRemainingTime)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "OpenProtectorRewardRs [code=" + code +",isProtector=" + isProtector +",protectorRemainingTime=" + protectorRemainingTime +"]";
	}
}
