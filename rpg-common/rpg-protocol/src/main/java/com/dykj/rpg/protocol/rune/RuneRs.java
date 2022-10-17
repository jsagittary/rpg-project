package com.dykj.rpg.protocol.rune;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class RuneRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 2301;
	//符文id
	private int runeId;
	//符文装配栏位
	private int runePos;
	public RuneRs(){
	}
	public RuneRs(int runeId,int runePos){
		this.runeId = runeId;
		this.runePos = runePos;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.runeId = 0;
		this.runePos = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getRuneId(){
		return this.runeId;
	}
	public void setRuneId(int _runeId){
		this.runeId = _runeId;
	}
	public int getRunePos(){
		return this.runePos;
	}
	public void setRunePos(int _runePos){
		this.runePos = _runePos;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		runeId = (int)dch.bytesToParam("Integer");
		runePos = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(runeId)){
			return false;
		}
		if(!ech.paramToBytes(runePos)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "RuneRs [code=" + code +",runeId=" + runeId +",runePos=" + runePos +"]";
	}
}
