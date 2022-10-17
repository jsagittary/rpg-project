package com.dykj.rpg.protocol.item;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class ItemLockRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1210;
	//根据type取对应id
	private long id;
	//1-装备唯一id 2-道具id
	private int type;
	//1-已解锁 2-已上锁
	private int isLock;
	public ItemLockRq(){
	}
	public ItemLockRq(long id,int type,int isLock){
		this.id = id;
		this.type = type;
		this.isLock = isLock;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.id = 0;
		this.type = 0;
		this.isLock = 0;
	}
	public short getCode(){
		return this.code;
	}
	public long getId(){
		return this.id;
	}
	public void setId(long _id){
		this.id = _id;
	}
	public int getType(){
		return this.type;
	}
	public void setType(int _type){
		this.type = _type;
	}
	public int getIsLock(){
		return this.isLock;
	}
	public void setIsLock(int _isLock){
		this.isLock = _isLock;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		id = (long)dch.bytesToParam("Long");
		type = (int)dch.bytesToParam("Integer");
		isLock = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(id)){
			return false;
		}
		if(!ech.paramToBytes(type)){
			return false;
		}
		if(!ech.paramToBytes(isLock)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "ItemLockRq [code=" + code +",id=" + id +",type=" + type +",isLock=" + isLock +"]";
	}
}
