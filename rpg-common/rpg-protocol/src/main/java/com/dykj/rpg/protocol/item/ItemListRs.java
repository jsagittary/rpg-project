package com.dykj.rpg.protocol.item;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class ItemListRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1202;
	//背包列表
	private List<com.dykj.rpg.protocol.item.ItemRs> itemArr;
	public ItemListRs(){
		this.itemArr = new ArrayList<>();
	}
	public ItemListRs(List<com.dykj.rpg.protocol.item.ItemRs> itemArr){
		this.itemArr = itemArr;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		for(com.dykj.rpg.protocol.item.ItemRs value : itemArr){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.itemArr.clear();
	}
	public short getCode(){
		return this.code;
	}
	public List<com.dykj.rpg.protocol.item.ItemRs> getItemArr(){
		return this.itemArr;
	}
	public void setItemArr(List<com.dykj.rpg.protocol.item.ItemRs> _itemArr){
		this.itemArr = _itemArr;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		itemArr = (List<com.dykj.rpg.protocol.item.ItemRs>)dch.bytesToParam("List<com.dykj.rpg.protocol.item.ItemRs>");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(itemArr)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "ItemListRs [code=" + code +",itemArr=" + itemArr +"]";
	}
}
