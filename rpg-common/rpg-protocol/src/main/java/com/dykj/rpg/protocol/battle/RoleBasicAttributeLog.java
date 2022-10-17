package com.dykj.rpg.protocol.battle;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class RoleBasicAttributeLog extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 9034;
	//属性Id
	private short attributeId;
	//属性类型
	private byte attributeType;
	//率值*10000,其他为正常值
	private int num;
	public RoleBasicAttributeLog(){
	}
	public RoleBasicAttributeLog(short attributeId,byte attributeType,int num){
		this.attributeId = attributeId;
		this.attributeType = attributeType;
		this.num = num;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.attributeId = 0;
		this.attributeType = 0;
		this.num = 0;
	}
	public short getCode(){
		return this.code;
	}
	public short getAttributeId(){
		return this.attributeId;
	}
	public void setAttributeId(short _attributeId){
		this.attributeId = _attributeId;
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
		attributeId = (short)dch.bytesToParam("Short");
		attributeType = (byte)dch.bytesToParam("Byte");
		num = (int)dch.bytesToParam("Integer");
		return true;
	}
	public boolean encode(BitArray bitArray){
		ech.init(bitArray);
		if(!ech.paramToBytes(code)){
			return false;
		}
		if(!ech.paramToBytes(attributeId)){
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
		return "RoleBasicAttributeLog [code=" + code +",attributeId=" + attributeId +",attributeType=" + attributeType +",num=" + num +"]";
	}
}
