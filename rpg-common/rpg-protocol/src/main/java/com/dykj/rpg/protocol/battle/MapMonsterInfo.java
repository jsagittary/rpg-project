package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class MapMonsterInfo extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9016;
	//怪物ID
	private int monsterId;
	//怪物数量
	private int monsterNum;
	public MapMonsterInfo(){
	}
	public MapMonsterInfo(int monsterId,int monsterNum){
		this.monsterId = monsterId;
		this.monsterNum = monsterNum;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.monsterId = 0;
		this.monsterNum = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getMonsterId(){
		return this.monsterId;
	}
	public void setMonsterId(int _monsterId){
		this.monsterId = _monsterId;
	}
	public int getMonsterNum(){
		return this.monsterNum;
	}
	public void setMonsterNum(int _monsterNum){
		this.monsterNum = _monsterNum;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		monsterId = (int)dch.bytesToParam("Integer");
		monsterNum = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(monsterId)){
			return false;
		}
		if(!ech.paramToBytes(monsterNum)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "MapMonsterInfo [code=" + code +",monsterId=" + monsterId +",monsterNum=" + monsterNum +"]";
	}
}
