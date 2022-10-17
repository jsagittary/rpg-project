package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class BattleNpcRoundWarning extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9039;
	//怪物出生轮次
	private int npcRound;
	public BattleNpcRoundWarning(){
	}
	public BattleNpcRoundWarning(int npcRound){
		this.npcRound = npcRound;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.npcRound = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getNpcRound(){
		return this.npcRound;
	}
	public void setNpcRound(int _npcRound){
		this.npcRound = _npcRound;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		npcRound = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(npcRound)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "BattleNpcRoundWarning [code=" + code +",npcRound=" + npcRound +"]";
	}
}
