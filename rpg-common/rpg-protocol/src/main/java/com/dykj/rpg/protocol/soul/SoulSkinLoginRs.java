package com.dykj.rpg.protocol.soul;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class SoulSkinLoginRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 2207;
	//所有解锁的灵魂之影信息
	private List<com.dykj.rpg.protocol.soul.SoulSkinRs> soulSkins;
	public SoulSkinLoginRs(){
		this.soulSkins = new ArrayList<>();
	}
	public SoulSkinLoginRs(List<com.dykj.rpg.protocol.soul.SoulSkinRs> soulSkins){
		this.soulSkins = soulSkins;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		for(com.dykj.rpg.protocol.soul.SoulSkinRs value : soulSkins){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.soulSkins.clear();
	}
	public short getCode(){
		return this.code;
	}
	public List<com.dykj.rpg.protocol.soul.SoulSkinRs> getSoulSkins(){
		return this.soulSkins;
	}
	public void setSoulSkins(List<com.dykj.rpg.protocol.soul.SoulSkinRs> _soulSkins){
		this.soulSkins = _soulSkins;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		soulSkins = (List<com.dykj.rpg.protocol.soul.SoulSkinRs>)dch.bytesToParam("List<com.dykj.rpg.protocol.soul.SoulSkinRs>");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(soulSkins)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "SoulSkinLoginRs [code=" + code +",soulSkins=" + soulSkins +"]";
	}
}
