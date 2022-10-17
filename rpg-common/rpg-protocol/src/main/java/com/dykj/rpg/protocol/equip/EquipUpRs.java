package com.dykj.rpg.protocol.equip;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class EquipUpRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1305;
	//穿上的装备信息
	private com.dykj.rpg.protocol.item.ItemRs upItem;
	//卸下的装备信息
	private com.dykj.rpg.protocol.item.ItemRs downItem;
	public EquipUpRs(){
	}
	public EquipUpRs(com.dykj.rpg.protocol.item.ItemRs upItem,com.dykj.rpg.protocol.item.ItemRs downItem){
		this.upItem = upItem;
		this.downItem = downItem;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		ProtocolPool.getInstance().restoreProtocol(this.upItem);
		this.upItem = null;
		ProtocolPool.getInstance().restoreProtocol(this.downItem);
		this.downItem = null;
	}
	public short getCode(){
		return this.code;
	}
	public com.dykj.rpg.protocol.item.ItemRs getUpItem(){
		return this.upItem;
	}
	public void setUpItem(com.dykj.rpg.protocol.item.ItemRs _upItem){
		this.upItem = _upItem;
	}
	public com.dykj.rpg.protocol.item.ItemRs getDownItem(){
		return this.downItem;
	}
	public void setDownItem(com.dykj.rpg.protocol.item.ItemRs _downItem){
		this.downItem = _downItem;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		upItem = (com.dykj.rpg.protocol.item.ItemRs)dch.bytesToParam("com.dykj.rpg.protocol.item.ItemRs");
		downItem = (com.dykj.rpg.protocol.item.ItemRs)dch.bytesToParam("com.dykj.rpg.protocol.item.ItemRs");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(upItem)){
			return false;
		}
		if(!ech.paramToBytes(downItem)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "EquipUpRs [code=" + code +",upItem=" + upItem +",downItem=" + downItem +"]";
	}
}
