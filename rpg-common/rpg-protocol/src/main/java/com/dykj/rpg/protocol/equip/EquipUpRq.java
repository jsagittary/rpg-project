package com.dykj.rpg.protocol.equip;
import com.dykj.rpg.net.protocol.*;
import java.util.*;

public class EquipUpRq extends Protocol{
	private DecodeClassProHandler dch = new DecodeClassProHandler();
	private EncodeClassProHandler ech = new EncodeClassProHandler();
	public static short code = 1304;
	//装备的实例id
	private long instId;
	//穿戴的位置
	private int equipPos;
	public EquipUpRq(){
	}
	public EquipUpRq(long instId,int equipPos){
		this.instId = instId;
		this.equipPos = equipPos;
	}
	public void release(){
		this.dch.release();
		this.ech.release();
		this.instId = 0;
		this.equipPos = 0;
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
	public int getEquipPos(){
		return this.equipPos;
	}
	public void setEquipPos(int _equipPos){
		this.equipPos = _equipPos;
	}
	public boolean decode(BitArray bitArray){
		dch.init(bitArray);
		code = (short)dch.bytesToParam("Short");
		instId = (long)dch.bytesToParam("Long");
		equipPos = (int)dch.bytesToParam("Integer");
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
		if(!ech.paramToBytes(equipPos)){
			return false;
		}
		return true;
	}
	public String toString() {
		return "EquipUpRq [code=" + code +",instId=" + instId +",equipPos=" + equipPos +"]";
	}
}
