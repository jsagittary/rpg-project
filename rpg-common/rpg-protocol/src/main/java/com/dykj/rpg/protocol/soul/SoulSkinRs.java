package com.dykj.rpg.protocol.soul;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class SoulSkinRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 2206;
	// 灵魂之影皮肤id
	private int soulId;
	//0 未穿戴，1穿戴
	private int use;
	public SoulSkinRs(){
	}
	public SoulSkinRs(int soulId,int use){
		this.soulId = soulId;
		this.use = use;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.soulId = 0;
		this.use = 0;
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
	public int getUse(){
		return this.use;
	}
	public void setUse(int _use){
		this.use = _use;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		soulId = (int)dch.bytesToParam("Integer");
		use = (int)dch.bytesToParam("Integer");
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
		if(!ech.paramToBytes(use)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "SoulSkinRs [code=" + code +",soulId=" + soulId +",use=" + use +"]";
	}
}
