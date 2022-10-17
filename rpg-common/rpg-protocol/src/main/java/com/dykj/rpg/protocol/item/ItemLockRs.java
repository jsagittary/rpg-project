package com.dykj.rpg.protocol.item;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class ItemLockRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1211;
	//根据type取对应id
	private long id;
	//1-装备唯一id 2-道具id
	private int type;
	//true-成功, false-失败
	private boolean status;
	public ItemLockRs(){
	}
	public ItemLockRs(long id,int type,boolean status){
		this.id = id;
		this.type = type;
		this.status = status;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.id = 0;
		this.type = 0;
		this.status = false;
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
	public boolean getStatus(){
		return this.status;
	}
	public void setStatus(boolean _status){
		this.status = _status;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		id = (long)dch.bytesToParam("Long");
		type = (int)dch.bytesToParam("Integer");
		status = (boolean)dch.bytesToParam("Boolean");
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
		if(!ech.paramToBytes(status)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "ItemLockRs [code=" + code +",id=" + id +",type=" + type +",status=" + status +"]";
	}
}
