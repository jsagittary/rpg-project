package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class BattleSkillSourceInfo extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9032;
	//技能资源类型
	private int sourceType;
	//技能资源值
	private int sourceNum;
	public BattleSkillSourceInfo(){
	}
	public BattleSkillSourceInfo(int sourceType,int sourceNum){
		this.sourceType = sourceType;
		this.sourceNum = sourceNum;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.sourceType = 0;
		this.sourceNum = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getSourceType(){
		return this.sourceType;
	}
	public void setSourceType(int _sourceType){
		this.sourceType = _sourceType;
	}
	public int getSourceNum(){
		return this.sourceNum;
	}
	public void setSourceNum(int _sourceNum){
		this.sourceNum = _sourceNum;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		sourceType = (int)dch.bytesToParam("Integer");
		sourceNum = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(sourceType)){
			return false;
		}
		if(!ech.paramToBytes(sourceNum)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "BattleSkillSourceInfo [code=" + code +",sourceType=" + sourceType +",sourceNum=" + sourceNum +"]";
	}
}
