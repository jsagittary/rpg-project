package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class CurrentTotalLootInfo extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9008;
	private List<com.dykj.rpg.protocol.item.ItemRs> lootInfos;
	public CurrentTotalLootInfo(){
		this.lootInfos = new ArrayList<>();
	}
	public CurrentTotalLootInfo(List<com.dykj.rpg.protocol.item.ItemRs> lootInfos){
		this.lootInfos = lootInfos;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		for(com.dykj.rpg.protocol.item.ItemRs value : lootInfos){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.lootInfos.clear();
	}
	public short getCode(){
		return this.code;
	}
	public List<com.dykj.rpg.protocol.item.ItemRs> getLootInfos(){
		return this.lootInfos;
	}
	public void setLootInfos(List<com.dykj.rpg.protocol.item.ItemRs> _lootInfos){
		this.lootInfos = _lootInfos;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		lootInfos = (List<com.dykj.rpg.protocol.item.ItemRs>)dch.bytesToParam("List<com.dykj.rpg.protocol.item.ItemRs>");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(lootInfos)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "CurrentTotalLootInfo [code=" + code +",lootInfos=" + lootInfos +"]";
	}
}
