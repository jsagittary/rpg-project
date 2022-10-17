package com.dykj.rpg.protocol.soul;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class BuySoulSkinRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 2202;
	// 灵魂之影皮肤id
	private int soulId;
	public BuySoulSkinRq(){
	}
	public BuySoulSkinRq(int soulId){
		this.soulId = soulId;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.soulId = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getSoulId(){
		return this.soulId;
	}
	public void setSoulId(int _soulId){
		this.soulId = _soulId;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		soulId = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(soulId)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "BuySoulSkinRq [code=" + code +",soulId=" + soulId +"]";
	}
}
