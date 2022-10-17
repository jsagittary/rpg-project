package com.dykj.rpg.protocol.game2battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class AttributeInfo extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 8901;
	//属性ID
	private short id;
	//属性类型
	private byte attributeType;
	//属性数值
	private int num;
	public AttributeInfo(){
	}
	public AttributeInfo(short id,byte attributeType,int num){
		this.id = id;
		this.attributeType = attributeType;
		this.num = num;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.id = 0;
		this.attributeType = 0;
		this.num = 0;
	}
	public short getCode(){
		return this.code;
	}
	public short getId(){
		return this.id;
	}
	public void setId(short _id){
		this.id = _id;
	}
	public byte getAttributeType(){
		return this.attributeType;
	}
	public void setAttributeType(byte _attributeType){
		this.attributeType = _attributeType;
	}
	public int getNum(){
		return this.num;
	}
	public void setNum(int _num){
		this.num = _num;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		id = (short)dch.bytesToParam("Short");
		attributeType = (byte)dch.bytesToParam("Byte");
		num = (int)dch.bytesToParam("Integer");
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
		if(!ech.paramToBytes(attributeType)){
			return false;
		}
		if(!ech.paramToBytes(num)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "AttributeInfo [code=" + code +",id=" + id +",attributeType=" + attributeType +",num=" + num +"]";
	}
}
