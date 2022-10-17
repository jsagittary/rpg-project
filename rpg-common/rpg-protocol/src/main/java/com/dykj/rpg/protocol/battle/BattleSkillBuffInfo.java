package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class BattleSkillBuffInfo extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9036;
	//buff的ID
	private int stateId;
	//buff在战斗中的唯一ID
	private int guid;
	//叠加层数
	private int superpositionNum;
	//剩余的持续时间
	private int durationTime;
	//1= 出生，2=消失
	private byte state;
	public BattleSkillBuffInfo(){
	}
	public BattleSkillBuffInfo(int stateId,int guid,int superpositionNum,int durationTime,byte state){
		this.stateId = stateId;
		this.guid = guid;
		this.superpositionNum = superpositionNum;
		this.durationTime = durationTime;
		this.state = state;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.stateId = 0;
		this.guid = 0;
		this.superpositionNum = 0;
		this.durationTime = 0;
		this.state = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getStateId(){
		return this.stateId;
	}
	public void setStateId(int _stateId){
		this.stateId = _stateId;
	}
	public int getGuid(){
		return this.guid;
	}
	public void setGuid(int _guid){
		this.guid = _guid;
	}
	public int getSuperpositionNum(){
		return this.superpositionNum;
	}
	public void setSuperpositionNum(int _superpositionNum){
		this.superpositionNum = _superpositionNum;
	}
	public int getDurationTime(){
		return this.durationTime;
	}
	public void setDurationTime(int _durationTime){
		this.durationTime = _durationTime;
	}
	public byte getState(){
		return this.state;
	}
	public void setState(byte _state){
		this.state = _state;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		stateId = (int)dch.bytesToParam("Integer");
		guid = (int)dch.bytesToParam("Integer");
		superpositionNum = (int)dch.bytesToParam("Integer");
		durationTime = (int)dch.bytesToParam("Integer");
		state = (byte)dch.bytesToParam("Byte");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(stateId)){
			return false;
		}
		if(!ech.paramToBytes(guid)){
			return false;
		}
		if(!ech.paramToBytes(superpositionNum)){
			return false;
		}
		if(!ech.paramToBytes(durationTime)){
			return false;
		}
		if(!ech.paramToBytes(state)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "BattleSkillBuffInfo [code=" + code +",stateId=" + stateId +",guid=" + guid +",superpositionNum=" + superpositionNum +",durationTime=" + durationTime +",state=" + state +"]";
	}
}
