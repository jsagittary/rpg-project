package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class ExitBattleRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9004;
	public ExitBattleRq(){
	}
	public void release(){
		this.dch.release();
		this.ech.release();
	}
	public short getCode(){
		return this.code;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "ExitBattleRq [code=" + code +"]";
	}
}
