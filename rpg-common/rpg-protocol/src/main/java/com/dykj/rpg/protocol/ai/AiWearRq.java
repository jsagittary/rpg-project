package com.dykj.rpg.protocol.ai;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class AiWearRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1902;
	//雕纹id
	private int itemId;
	//穿戴的位置
	private int pos;
	public AiWearRq(){
	}
	public AiWearRq(int itemId,int pos){
		this.itemId = itemId;
		this.pos = pos;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.itemId = 0;
		this.pos = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getItemId(){
		return this.itemId;
	}
	public void setItemId(int _itemId){
		this.itemId = _itemId;
	}
	public int getPos(){
		return this.pos;
	}
	public void setPos(int _pos){
		this.pos = _pos;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		itemId = (int)dch.bytesToParam("Integer");
		pos = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(itemId)){
			return false;
		}
		if(!ech.paramToBytes(pos)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "AiWearRq [code=" + code +",itemId=" + itemId +",pos=" + pos +"]";
	}
}
