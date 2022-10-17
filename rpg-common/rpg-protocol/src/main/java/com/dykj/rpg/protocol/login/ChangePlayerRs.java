package com.dykj.rpg.protocol.login;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class ChangePlayerRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1112;
	//装备列表
	private List<com.dykj.rpg.protocol.item.ItemRs> equips;
	public ChangePlayerRs(){
		this.equips = new ArrayList<>();
	}
	public ChangePlayerRs(List<com.dykj.rpg.protocol.item.ItemRs> equips){
		this.equips = equips;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		for(com.dykj.rpg.protocol.item.ItemRs value : equips){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.equips.clear();
	}
	public short getCode(){
		return this.code;
	}
	public List<com.dykj.rpg.protocol.item.ItemRs> getEquips(){
		return this.equips;
	}
	public void setEquips(List<com.dykj.rpg.protocol.item.ItemRs> _equips){
		this.equips = _equips;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		equips = (List<com.dykj.rpg.protocol.item.ItemRs>)dch.bytesToParam("List<com.dykj.rpg.protocol.item.ItemRs>");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(equips)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "ChangePlayerRs [code=" + code +",equips=" + equips +"]";
	}
}
