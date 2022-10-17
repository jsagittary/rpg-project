package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class BattleAiInfo extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9042;
	//技巧ID
	private int aiId;
	//cd时间(单位ms)
	private int cdTime;
	public BattleAiInfo(){
	}
	public BattleAiInfo(int aiId,int cdTime){
		this.aiId = aiId;
		this.cdTime = cdTime;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.aiId = 0;
		this.cdTime = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getAiId(){
		return this.aiId;
	}
	public void setAiId(int _aiId){
		this.aiId = _aiId;
	}
	public int getCdTime(){
		return this.cdTime;
	}
	public void setCdTime(int _cdTime){
		this.cdTime = _cdTime;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		aiId = (int)dch.bytesToParam("Integer");
		cdTime = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(aiId)){
			return false;
		}
		if(!ech.paramToBytes(cdTime)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "BattleAiInfo [code=" + code +",aiId=" + aiId +",cdTime=" + cdTime +"]";
	}
}
