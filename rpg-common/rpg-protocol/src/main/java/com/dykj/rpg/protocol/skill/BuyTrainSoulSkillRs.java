package com.dykj.rpg.protocol.skill;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class BuyTrainSoulSkillRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1420;
	//灵魂之影技能培养的成功时间
	private long soulChangeTime;
	public BuyTrainSoulSkillRs(){
	}
	public BuyTrainSoulSkillRs(long soulChangeTime){
		this.soulChangeTime = soulChangeTime;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.soulChangeTime = 0;
	}
	public short getCode(){
		return this.code;
	}
	public long getSoulChangeTime(){
		return this.soulChangeTime;
	}
	public void setSoulChangeTime(long _soulChangeTime){
		this.soulChangeTime = _soulChangeTime;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		soulChangeTime = (long)dch.bytesToParam("Long");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(soulChangeTime)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "BuyTrainSoulSkillRs [code=" + code +",soulChangeTime=" + soulChangeTime +"]";
	}
}
