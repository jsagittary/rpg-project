package com.dykj.rpg.protocol.gameBattle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class StartBattleRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1601;
	//战斗类型 1为通关类，2为BOSS类，等等
	private int battleType;
	public StartBattleRq(){
	}
	public StartBattleRq(int battleType){
		this.battleType = battleType;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.battleType = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getBattleType(){
		return this.battleType;
	}
	public void setBattleType(int _battleType){
		this.battleType = _battleType;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		battleType = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(battleType)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "StartBattleRq [code=" + code +",battleType=" + battleType +"]";
	}
}
