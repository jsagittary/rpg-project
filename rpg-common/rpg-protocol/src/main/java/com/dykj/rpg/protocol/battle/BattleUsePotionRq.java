package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class BattleUsePotionRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9013;
	//使用的药水ID
	private int potionId;
	public BattleUsePotionRq(){
	}
	public BattleUsePotionRq(int potionId){
		this.potionId = potionId;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.potionId = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getPotionId(){
		return this.potionId;
	}
	public void setPotionId(int _potionId){
		this.potionId = _potionId;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		potionId = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(potionId)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "BattleUsePotionRq [code=" + code +",potionId=" + potionId +"]";
	}
}
