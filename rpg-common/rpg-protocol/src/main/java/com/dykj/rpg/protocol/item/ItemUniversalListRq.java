package com.dykj.rpg.protocol.item;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class ItemUniversalListRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1205;
	private List<com.dykj.rpg.protocol.item.ItemUniversalRq> itemUniversalArr;
	//操作类型   1001-GM 1002-出售 1003-兑换 1004-分解 1005-使用 1006-丢弃
	private int operation;
	public ItemUniversalListRq(){
		this.itemUniversalArr = new ArrayList<>();
	}
	public ItemUniversalListRq(List<com.dykj.rpg.protocol.item.ItemUniversalRq> itemUniversalArr,int operation){
		this.itemUniversalArr = itemUniversalArr;
		this.operation = operation;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		for(com.dykj.rpg.protocol.item.ItemUniversalRq value : itemUniversalArr){
			ProtocolPool.getInstance().restoreProtocol(value);
		}
		this.itemUniversalArr.clear();
		this.operation = 0;
	}
	public short getCode(){
		return this.code;
	}
	public List<com.dykj.rpg.protocol.item.ItemUniversalRq> getItemUniversalArr(){
		return this.itemUniversalArr;
	}
	public void setItemUniversalArr(List<com.dykj.rpg.protocol.item.ItemUniversalRq> _itemUniversalArr){
		this.itemUniversalArr = _itemUniversalArr;
	}
	public int getOperation(){
		return this.operation;
	}
	public void setOperation(int _operation){
		this.operation = _operation;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		itemUniversalArr = (List<com.dykj.rpg.protocol.item.ItemUniversalRq>)dch.bytesToParam("List<com.dykj.rpg.protocol.item.ItemUniversalRq>");
		operation = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(itemUniversalArr)){
			return false;
		}
		if(!ech.paramToBytes(operation)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "ItemUniversalListRq [code=" + code +",itemUniversalArr=" + itemUniversalArr +",operation=" + operation +"]";
	}
}
