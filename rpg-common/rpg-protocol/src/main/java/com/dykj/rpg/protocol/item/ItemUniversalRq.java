package com.dykj.rpg.protocol.item;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class ItemUniversalRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1204;
	//服务器唯一id
	private long instId;
	//道具id
	private int itemId;
	//道具数量
	private int itemNum;
	public ItemUniversalRq(){
	}
	public ItemUniversalRq(long instId,int itemId,int itemNum){
		this.instId = instId;
		this.itemId = itemId;
		this.itemNum = itemNum;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.instId = 0;
		this.itemId = 0;
		this.itemNum = 0;
	}
	public short getCode(){
		return this.code;
	}
	public long getInstId(){
		return this.instId;
	}
	public void setInstId(long _instId){
		this.instId = _instId;
	}
	public int getItemId(){
		return this.itemId;
	}
	public void setItemId(int _itemId){
		this.itemId = _itemId;
	}
	public int getItemNum(){
		return this.itemNum;
	}
	public void setItemNum(int _itemNum){
		this.itemNum = _itemNum;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		instId = (long)dch.bytesToParam("Long");
		itemId = (int)dch.bytesToParam("Integer");
		itemNum = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(instId)){
			return false;
		}
		if(!ech.paramToBytes(itemId)){
			return false;
		}
		if(!ech.paramToBytes(itemNum)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "ItemUniversalRq [code=" + code +",instId=" + instId +",itemId=" + itemId +",itemNum=" + itemNum +"]";
	}
}
