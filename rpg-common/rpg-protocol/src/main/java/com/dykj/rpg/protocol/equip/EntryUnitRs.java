package com.dykj.rpg.protocol.equip;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class EntryUnitRs extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1303;
	//1 属性,2技能
	private int type;
	// 属性id,技能id
	private int id;
	//为属性的子类型  为技能的时候去查 skill_attr_basic影响的详细信息（确定影响的是哪个字段）
	private int typeId;
	//1值 2 增比  3 减比
	private int pram;
	// value   0,显示这个为entryUnitRs.length-1(去)
	private int value;
	//值域id,0的话表示没有值域,客户端依据这个id 去value_rang 找值域以及value缩放倍数
	private int entryValueRangeId;
	public EntryUnitRs(){
	}
	public EntryUnitRs(int type,int id,int typeId,int pram,int value,int entryValueRangeId){
		this.type = type;
		this.id = id;
		this.typeId = typeId;
		this.pram = pram;
		this.value = value;
		this.entryValueRangeId = entryValueRangeId;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.type = 0;
		this.id = 0;
		this.typeId = 0;
		this.pram = 0;
		this.value = 0;
		this.entryValueRangeId = 0;
	}
	public short getCode(){
		return this.code;
	}
	public int getType(){
		return this.type;
	}
	public void setType(int _type){
		this.type = _type;
	}
	public int getId(){
		return this.id;
	}
	public void setId(int _id){
		this.id = _id;
	}
	public int getTypeId(){
		return this.typeId;
	}
	public void setTypeId(int _typeId){
		this.typeId = _typeId;
	}
	public int getPram(){
		return this.pram;
	}
	public void setPram(int _pram){
		this.pram = _pram;
	}
	public int getValue(){
		return this.value;
	}
	public void setValue(int _value){
		this.value = _value;
	}
	public int getEntryValueRangeId(){
		return this.entryValueRangeId;
	}
	public void setEntryValueRangeId(int _entryValueRangeId){
		this.entryValueRangeId = _entryValueRangeId;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		type = (int)dch.bytesToParam("Integer");
		id = (int)dch.bytesToParam("Integer");
		typeId = (int)dch.bytesToParam("Integer");
		pram = (int)dch.bytesToParam("Integer");
		value = (int)dch.bytesToParam("Integer");
		entryValueRangeId = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(type)){
			return false;
		}
		if(!ech.paramToBytes(id)){
			return false;
		}
		if(!ech.paramToBytes(typeId)){
			return false;
		}
		if(!ech.paramToBytes(pram)){
			return false;
		}
		if(!ech.paramToBytes(value)){
			return false;
		}
		if(!ech.paramToBytes(entryValueRangeId)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "EntryUnitRs [code=" + code +",type=" + type +",id=" + id +",typeId=" + typeId +",pram=" + pram +",value=" + value +",entryValueRangeId=" + entryValueRangeId +"]";
	}
}
